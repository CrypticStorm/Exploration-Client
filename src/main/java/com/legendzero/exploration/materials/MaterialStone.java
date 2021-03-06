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

import com.legendzero.exploration.util.Materials;
import javax.vecmath.Color4f;

/**
 *
 * @author CrypticStorm
 */
public class MaterialStone extends Material {

    static {
        Materials.addMaterial(new MaterialStone());
    }

    public MaterialStone() {
        super(new Color4f(0.5f, 0.5f, 0.5f, 1f));
    }

    @Override
    public String getName() {
        return "Stone";
    }

    @Override
    public String getTextureFile() {
        return "stone";
    }

}
