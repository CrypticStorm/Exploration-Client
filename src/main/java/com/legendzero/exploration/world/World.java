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

import com.legendzero.exploration.Exploration;
import com.legendzero.exploration.entity.Entity;
import com.legendzero.exploration.material.Material;
import com.legendzero.exploration.material.Materials;
import com.legendzero.exploration.noise.LifeNoise;
import com.legendzero.exploration.noise.Noise;
import com.legendzero.exploration.physics.WorldPhysics;
import com.legendzero.exploration.util.Location;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

/**
 *
 * @author CrypticStorm
 */
public class World {

    private final Exploration game;
    private final Tile[][] tiles;
    private final long seed;
    private final int width;
    private final int height;

    private final Set<Entity> entities;
    private final WorldPhysics physics;
    private final Tuple2d gravity;
    private final Tuple2d terminalVelocity;
    private Location spawnLoc;

    public World(Exploration game, int width, int height) {
        this(game, new Random().nextLong(), width, height);
    }

    public World(Exploration game, long seed, int width, int height) {
        this.game = game;
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.spawnLoc = new Location(this, this.width / 2.0, this.height / 2.0);
        this.tiles = generateWorld();
        this.entities = new HashSet<Entity>();
        this.physics = new WorldPhysics(this);
        this.gravity = new Vector2d(0.0, -0.1);
        this.terminalVelocity = new Vector2d(1.0, -1.0);
        this.game.addPhysicsEngine(physics);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Tuple2d getGravity() {
        return this.gravity;
    }

    public Tuple2d getTerminalVelocity() {
        return this.terminalVelocity;
    }

    public Tile[][] getMap() {
        return this.tiles;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            return null;
        } else {
            return this.tiles[x][y];
        }
    }

    public void setTile(int x, int y, Material material) {
        if (x >= 0 && x < this.width && y >= 0 && y < this.height && material != null) {
            this.tiles[x][y] = new Tile(material, new Location(this, x, y));
        }
    }

    public Location getSpawnLocation() {
        return this.spawnLoc;
    }

    public Set<Entity> getEntities() {
        return this.entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void spawnEntity(Entity entity, Location location) {
        entity.setLocation(location);
        this.addEntity(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    private Tile[][] generateWorld() {
        Noise noise = new LifeNoise(this.seed, 6);
        float[][] noiseMap = noise.genNoiseMap(this.width, this.height);
        Tile[][] tileMap = new Tile[this.width][this.height];

        for (int i = 0; i < this.width; i++) {
            tileMap[i] = new Tile[this.height];
        }

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
