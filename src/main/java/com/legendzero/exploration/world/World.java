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

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.entity.IEntity;
import com.legendzero.exploration.api.physics.IPhysics;
import com.legendzero.exploration.api.world.ITile;
import com.legendzero.exploration.api.world.IWorld;
import com.legendzero.exploration.noise.LifeNoise;
import com.legendzero.exploration.noise.Noise;
import com.legendzero.exploration.physics.WorldGravityPhysics;
import com.legendzero.exploration.physics.WorldUpdatePhysics;
import com.legendzero.exploration.util.Direction;
import com.legendzero.exploration.util.Location;
import com.legendzero.exploration.util.Materials;
import com.legendzero.exploration.util.material.Material;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.vecmath.Vector2d;

/**
 *
 * @author CrypticStorm
 */
public class World implements IWorld {

    private final ITile[][] tiles;
    private final long seed;
    private final String name;
    private final int width;
    private final int height;

    private final Set<IEntity> entities;
    private final Set<IPhysics> physics;
    private Location spawnLoc;
    private Vector2d gravity;
    private Vector2d terminalVelocity;

    public World(String name, int width, int height, Vector2d gravity, Vector2d terminalVelocity) {
        this(new Random().nextLong(), name, width, height, gravity, terminalVelocity);
    }

    public World(long seed, String name, int width, int height, Vector2d gravity, Vector2d terminalVelocity) {
        this.seed = seed;
        this.name = name;
        this.width = width;
        this.height = height;
        this.spawnLoc = new Location(this, this.width / 2.0, this.height / 2.0);
        this.tiles = this.generateWorld();
        this.gravity = gravity;
        this.terminalVelocity = terminalVelocity;
        this.entities = new HashSet<>();
        this.physics = new HashSet<>();
        this.addDefaultPhysics();
        this.fixTiles();
    }

    public World(WorldData data) {
        this.seed = data.getSeed();
        this.name = data.getName();
        this.width = data.getWidth();
        this.height = data.getHeight();
        this.spawnLoc = new Location(this, data.getSpawnLoc());
        this.tiles = data.getTiles();
        this.gravity = data.getGravity();
        this.terminalVelocity = data.getTerminalVelocity();
        this.entities = new HashSet<>();
        this.physics = new HashSet<>();
        this.addDefaultPhysics();
        this.fixTiles();
    }

    private void addDefaultPhysics() {
        this.physics.add(new WorldUpdatePhysics(this));
        this.physics.add(new WorldGravityPhysics(this, this.gravity, this.terminalVelocity));
    }

    private void fixTiles() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                ITile tile = this.getTile(i, j);
                tile.setLocation(new Location(this, tile.getLocation().getX(), tile.getLocation().getY()));
                if (tile.getType().isSolid()) {
                    for (Direction dir : Direction.values()) {
                        ITile tile2 = tile.getAdjancent(dir);
                        if (tile2 == null) {
                            tile.setAdjacency(dir, true);
                        } else if (!tile2.getType().isSolid()) {
                            tile.setAdjacency(dir, true);
                            tile2.setAdjacency(dir.getOpposite(), true);
                        }
                    }
                }
            }
        }
    }

    public void update(IExploration game) {
        for (IPhysics physic : this.physics) {
            physic.update(game);
        }
    }

    public long getSeed() {
        return this.seed;
    }

    public String getName() {
        return this.name;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ITile[][] getMap() {
        return this.tiles;
    }

    public Vector2d getGravity() {
        return this.gravity;
    }

    public Vector2d getTerminalVelocity() {
        return this.terminalVelocity;
    }

    public ITile getTile(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            return null;
        } else {
            return this.tiles[x][y];
        }
    }

    public void setTile(int x, int y, Material material) {
        if (x >= 0 && x < this.width && y >= 0 && y < this.height && material != null) {
            ITile tile = this.tiles[x][y];
            Material oldMaterial = tile.getType();
            this.tiles[x][y] = new Tile(material, new Location(this, x, y));
            boolean solid = material.isSolid();
            if (oldMaterial.isSolid() != solid) {
                for (Direction dir : Direction.values()) {
                    ITile tile2 = tile.getAdjancent(dir);
                    if (tile2 == null) {
                        tile.setAdjacency(dir, !solid);
                    } else {
                        boolean match = solid == tile2.getType().isSolid();
                        tile.setAdjacency(dir, !match);
                        tile2.setAdjacency(dir.getOpposite(), !match);
                    }
                }
            }
        }
    }

    public boolean isTileSolid(int x, int y) {
        return x < 0 || x >= this.width || y < 0 || y >= this.height || this.tiles[x][y].getType().isSolid();
    }

    public Location getSpawnLocation() {
        return this.spawnLoc.copy();
    }

    public Set<IEntity> getEntities() {
        return this.entities;
    }

    public void addEntity(IEntity entity) {
        this.entities.add(entity);
    }

    public void spawnEntity(IEntity entity, Location location) {
        entity.setLocation(location);
        this.addEntity(entity);
    }

    public void removeEntity(IEntity entity) {
        this.entities.remove(entity);
    }

    public ITile[][] generateWorld() {
        Noise noise = new LifeNoise(this.seed);
        float[][] noiseMap = noise.genNoiseMap(this.width, this.height);
        ITile[][] tileMap = new ITile[this.width][this.height];

        Location target = this.spawnLoc;
        Location best = target;
        double spawnDist = Double.MAX_VALUE;

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                float n = noiseMap[i][j];
                if (n >= 0.5) {
                    Location location = new Location(this, i, j);
                    tileMap[i][j] = new Tile(Materials.getMaterial("Air"), location);
                    if (j == 0 || tileMap[i][j - 1].getType().equals(Materials.getMaterial("Stone"))) {
                        double dist = target.distanceSquared(location);
                        if (dist < spawnDist) {
                            spawnDist = dist;
                            best = location;
                        }

                    }
                } else {
                    tileMap[i][j] = new Tile(Materials.getMaterial("Stone"), new Location(this, i, j));
                }
            }
        }

        this.spawnLoc = best;
        return tileMap;
    }
}
