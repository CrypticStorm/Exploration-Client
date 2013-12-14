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

import com.legendzero.exploration.material.Material;
import com.legendzero.exploration.util.Location;

/**
 *
 * @author CrypticStorm
 */
public class Tile {

    private Material type;
    private Location location;

    public Tile(Material type, Location location) {
        this.type = type;
        this.location = location;
    }

    public Material getType() {
        return this.type;
    }
    
    public Location getLocation() {
        return this.location;
    }
}
