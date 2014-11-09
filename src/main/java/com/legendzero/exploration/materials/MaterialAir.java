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

import com.legendzero.exploration.util.material.Material;
import com.legendzero.exploration.util.Materials;
import javax.vecmath.Color4f;

/**
 *
 * @author CrypticStorm
 */
public class MaterialAir extends Material {

    static {
        Materials.addMaterial(new MaterialAir());
    }

    public MaterialAir() {
        super(new Color4f(1f, 1f, 1f, 1f));
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getName() {
        return "Air";
    }

    @Override
    public String getTextureFile() {
        return "air";
    }
}
