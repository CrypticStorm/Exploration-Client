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
import com.legendzero.exploration.util.Direction;
import com.legendzero.exploration.util.Location;
import com.legendzero.exploration.util.material.Material;

/**
 *
 * @author CrypticStorm
 */
public class Tile implements ITile {

    private boolean[] adjacencies;
    private Material type;
    private Location location;

    public Tile(Material type, Location location) {
        this.type = type;
        this.location = location;
        this.adjacencies = new boolean[Direction.values().length];
    }

    public Material getType() {
        return this.type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public ITile getAdjancent(Direction dir) {
        return this.location.getWorld().getTile((int) this.location.getX() + dir.getDX(), (int) this.location.getY() + dir.getDY());
    }

    public boolean getAdjacency(Direction dir) {
        return this.adjacencies[dir.ordinal()];
    }

    public void setAdjacency(Direction dir, boolean value) {
        this.adjacencies[dir.ordinal()] = value;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location.copy();
        this.location.setX(Math.round(location.getX()));
        this.location.setY(Math.round(location.getY()));
    }
}
