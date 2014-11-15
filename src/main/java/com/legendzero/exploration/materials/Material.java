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
package com.legendzero.exploration.materials;

import javax.vecmath.Color4f;

/**
 *
 * @author CrypticStorm
 */
public abstract class Material implements com.legendzero.exploration.api.item.IMaterial {

    private final Color4f color;

    public Material(Color4f color) {
        this.color = color;
    }

    @Override
    public Color4f getColor() {
        return this.color;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean isLiquid() {
        return false;
    }
}
