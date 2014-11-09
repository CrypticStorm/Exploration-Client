/*
 * Copyright (C) 2013 Legend Zero
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.legendzero.exploration.world;

import com.legendzero.exploration.api.world.ITile;
import com.legendzero.exploration.api.world.IWorld;
import com.legendzero.exploration.util.Location;
import com.legendzero.exploration.util.Materials;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

/**
 *
 * @author CrypticStorm
 */
public class WorldManager {

    private final File folder;

    public WorldManager(File folder) {
        this.folder = folder;
        if(!this.folder.exists()) {
            if(!this.folder.mkdir()) {
                System.err.println("Error creating world directory: " + folder);
            }
        }
    }

    public void saveWorlds(List<IWorld> worlds) {
        for(IWorld world : worlds) {
            this.saveWorld(world);
        }
    }

    public void saveWorld(IWorld world) {
        this.saveWorld(world, new File(this.folder, world.getName() + ".map"));
    }

    public void saveWorld(IWorld world, File file) {
        try {
            if (file.createNewFile()) {
                System.out.println("Saving new world " + world.getName());
            } else {
                System.out.println("Saving over world " + world.getName());
            }
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));

            output.writeLong(world.getSeed());
            output.writeUTF(world.getName());
            output.writeInt(world.getWidth());
            output.writeInt(world.getHeight());

            String[][] tiles = new String[world.getWidth()][world.getHeight()];
            ITile[][] map = world.getMap();
            for (int i = 0; i < world.getWidth(); i++) {
                for (int j = 0; j < world.getHeight(); j++) {
                    tiles[i][j] = map[i][j].getType().getName();
                }
            }

            output.writeObject(tiles);
            output.writeObject(new Point2d(world.getSpawnLocation().getX(), world.getSpawnLocation().getY()));
            output.writeObject(world.getGravity());
            output.writeObject(world.getTerminalVelocity());

            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<IWorld> loadWorlds() {
        return this.loadWorlds(this.folder);
    }

    public List<IWorld> loadWorlds(File directory) {
        List<IWorld> worlds = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".map")) {
                    IWorld world = this.loadWorld(file);
                    if (world != null) {
                        worlds.add(world);
                    }
                }
            }
        } else {
            System.err.println("Error reading files from directory.");
        }
        return worlds;
    }

    public IWorld loadWorld(String name) {
        return this.loadWorld(new File(this.folder, name + ".map"));
    }

    public IWorld loadWorld(File directory, String name) {
        if (name.endsWith(".map")) {
            return this.loadWorld(new File(directory, name + ".map"));
        } else {
            return this.loadWorld(new File(directory, name));
        }
    }

    public IWorld loadWorld(File file) {
        if (!file.exists()) {
            return null;
        } else {
            try {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));

                WorldData data = new WorldData();
                data.setSeed(input.readLong());
                data.setName(input.readUTF());
                data.setWidth(input.readInt());
                data.setHeight(input.readInt());

                String[][] tiles = (String[][]) input.readObject();
                Tile[][] map = new Tile[data.getWidth()][data.getHeight()];
                for (int i = 0; i < data.getWidth(); i++) {
                    for (int j = 0; j < data.getHeight(); j++) {
                        map[i][j] = new Tile(Materials.getMaterial(tiles[i][j]), new Location(null, i, j));
                    }
                }

                data.setTiles(map);
                data.setSpawnLoc((Point2d) input.readObject());
                data.setGravity((Vector2d) input.readObject());
                data.setTerminalVelocity((Vector2d) input.readObject());

                input.close();

                return new World(data);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
