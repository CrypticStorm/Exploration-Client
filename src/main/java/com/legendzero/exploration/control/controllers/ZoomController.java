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
import org.lwjgl.input.Mouse;

/**
 *
 * @author CrypticStorm
 */
public class ZoomController extends Controller {

    private final double minZoom = 10.0;
    private final double maxZoom = 200.0;
    private final double scrollSpeed = 5.0;

    public ZoomController(Player player) {
        super(player);
    }

    @Override
    public void update(Exploration game) {
        if (Mouse.isButtonDown(2)) {
            this.player.resetViewSize();
        } else {
            int dwheel = Mouse.getDWheel();
            if (dwheel != 0) {
                this.player.setViewSize(Math.min(this.maxZoom, Math.max(this.minZoom, this.player.getViewSize() - this.scrollSpeed * Math.signum(dwheel))));
            }
        }
    }
}
