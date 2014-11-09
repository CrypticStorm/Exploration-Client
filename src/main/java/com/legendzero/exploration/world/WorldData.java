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

import java.util.Random;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

/**
 *
 * @author CrypticStorm
 */
public class WorldData {

    private long seed;
    private String name;
    private int width;
    private int height;
    private Tile[][] tiles;
    private Point2d spawnLoc;
    private Vector2d gravity;
    private Vector2d terminalVelocity;

    public WorldData() {
        this.seed = new Random().nextLong();
        this.name = "";
        this.width = 100;
        this.height = 100;
        this.tiles = new Tile[this.width][this.height];
        this.spawnLoc = new Point2d(this.width / 2, this.height / 2);
        this.gravity = new Vector2d(0.0, -0.1);
        this.terminalVelocity = new Vector2d(1.0, 1.0);
    }

    public long getSeed() {
        return this.seed;
    }

    public WorldData setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public WorldData setName(String name) {
        this.name = name;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public WorldData setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return this.height;
    }

    public WorldData setHeight(int height) {
        this.height = height;
        return this;
    }
    
    public Tile[][] getTiles() {
        return this.tiles;
    }
    
    public WorldData setTiles(Tile[][] tiles) {
        this.tiles = tiles;
        return this;
    }
    
    public Point2d getSpawnLoc() {
        return this.spawnLoc;
    }
    
    public WorldData setSpawnLoc(Point2d spawnLoc) {
        this.spawnLoc = spawnLoc;
        return this;
    }
    
    public Vector2d getGravity() {
        return this.gravity;
    }
    
    public WorldData setGravity(Vector2d gravity) {
        this.gravity = gravity;
        return this;
    }
    
    public Vector2d getTerminalVelocity() {
        return this.terminalVelocity;
    }
    
    public WorldData setTerminalVelocity(Vector2d terminalVelocity) {
        this.terminalVelocity = terminalVelocity;
        return this;
    }
}
