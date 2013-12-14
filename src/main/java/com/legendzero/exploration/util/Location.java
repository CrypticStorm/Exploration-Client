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
package com.legendzero.exploration.util;

import com.legendzero.exploration.world.World;
import javax.vecmath.Tuple2d;

/**
 *
 * @author CrypticStorm
 */
public class Location {

    private World world;
    private double x;
    private double y;

    public Location(World world, double x, double y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    public Location(World world, Tuple2d tuple) {
        this.world = world;
        this.x = tuple.x;
        this.y = tuple.y;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    public Location add(Location other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Location add(Tuple2d tuple) {
        this.x += tuple.x;
        this.y += tuple.y;
        return this;
    }

    public Location add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public double distance(Location other) {
        return Math.sqrt(this.lengthSquared());
    }

    public double distanceSquared(Location other) {
        return (this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y);
    }

    public double distance(Tuple2d other) {
        return Math.sqrt(this.lengthSquared());
    }

    public double distanceSquared(Tuple2d other) {
        return (this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y);
    }
}
