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

import com.legendzero.exploration.material.Material;

/**
 *
 * @author CrypticStorm
 */
public class ItemStack {

    private Material material;
    private int amount;
    
    public ItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
