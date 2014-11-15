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
package com.legendzero.exploration.control.controllers;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.entity.IPlayer;
import com.legendzero.exploration.api.item.IMaterial;
import com.legendzero.exploration.api.tiles.ITile;
import com.legendzero.exploration.control.PlayerController;
import com.legendzero.exploration.util.AABB;
import com.legendzero.exploration.util.Materials;

import javax.vecmath.Point2d;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author CrypticStorm
 */
public class BlockController extends PlayerController {

    private final double maxReachDistance;

    public BlockController(IPlayer player) {
        super(player);
        this.maxReachDistance = 4.0;
    }

    @Override
    public void update(IExploration game) {
        IMaterial material = this.updateMaterial();
        this.interact(game, material);

    }

    private IMaterial updateMaterial() {
        if (Mouse.isButtonDown(0)) {
            return Materials.getMaterial("Air");
        } else if (Mouse.isButtonDown(1)) {
            return Materials.getMaterial("Stone");
        }
        return null;
    }

    private void interact(IExploration game, IMaterial material) {
        if (material != null) {
            int x = Mouse.getX();
            int y = Mouse.getY();

            int oldRangeX = Display.getWidth();
            int oldRangeY = Display.getHeight();

            AABB aabb = game.getVisibleAABB();
            double newRangeX = aabb.getRight() - aabb.getLeft();
            double newRangeY = aabb.getTop() - aabb.getBottom();

            double wx = (x * newRangeX) / oldRangeX + aabb.getLeft();
            double wy = (y * newRangeY) / oldRangeY + aabb.getBottom();

            if (this.player.getLocation().distanceSquared(new Point2d(wx, wy - this.player.getHeight() / 2)) < this.maxReachDistance * this.maxReachDistance) {
                int ix = (int) wx;
                int iy = (int) wy;
                ITile tile = this.player.getLocation().getWorld().getTile(ix, iy);
                if (!new AABB(this.player).collidesStrict(new AABB(tile))) {
                    tile.setType(material);
                }
            }
        }

    }

}
