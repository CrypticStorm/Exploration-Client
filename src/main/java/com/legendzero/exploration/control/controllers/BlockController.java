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

import com.legendzero.exploration.Exploration;
import com.legendzero.exploration.control.Controller;
import com.legendzero.exploration.entity.Player;
import com.legendzero.exploration.material.Material;
import com.legendzero.exploration.material.Materials;
import javax.vecmath.Point2d;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author CrypticStorm
 */
public class BlockController extends Controller {

    private final double maxReachDistance;

    public BlockController(Player player) {
        super(player);
        this.maxReachDistance = 4.0;
    }

    @Override
    public void update(Exploration game) {
        Material material = this.updateMaterial();
        this.interact(game, material);

    }

    private Material updateMaterial() {
        if (Mouse.isButtonDown(0)) {
            return Materials.getMaterial("Air");
        } else if (Mouse.isButtonDown(1)) {
            return Materials.getMaterial("Stone");
        }
        return null;
    }

    private void interact(Exploration game, Material material) {
        if (material != null) {
            int x = Mouse.getX();
            int y = Mouse.getY();

            int oldRangeX = Display.getWidth();
            int oldRangeY = Display.getHeight();
            double newRangeX = game.getRenderState().getMaxX() - game.getRenderState().getMinX();
            double newRangeY = game.getRenderState().getMaxY() - game.getRenderState().getMinY();

            double wx = (x * newRangeX) / oldRangeX + game.getRenderState().getMinX();
            double wy = (y * newRangeY) / oldRangeY + game.getRenderState().getMinY();

            if (this.player.getLocation().distanceSquared(new Point2d(wx, wy - this.player.getHeight() / 2)) < this.maxReachDistance * this.maxReachDistance) {
                int ix = (int) wx;
                int iy = (int) wy;

                this.player.getLocation().getWorld().setTile(ix, iy, material);
            }
        }

    }

}
