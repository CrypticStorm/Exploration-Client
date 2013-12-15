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
import java.util.HashSet;
import java.util.Set;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author CrypticStorm
 */
public class MovementController extends Controller {

    private static final double maxX = 0.5;
    private static final double maxY = 0.8;

    private final Set<Integer> keysPressed;

    public MovementController(Player player) {
        super(player);
        this.keysPressed = new HashSet<Integer>();
    }

    @Override
    public void update(Exploration game) {
        this.updateKeys();

        double dx = this.updateHorizontal();
        double dy = this.updateVertical();

        this.player.getVelocity().set(dx, dy);

    }

    private void updateKeys() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                this.keysPressed.add(Keyboard.getEventKey());
            } else {
                this.keysPressed.remove(Keyboard.getEventKey());
            }
        }
    }

    private double updateHorizontal() {
        double dx = this.player.getVelocity().x;

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            dx -= 0.08;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            dx += 0.08;
        }

        if (Math.abs(dx) < 0.01) {
            dx = 0.0;
        } else {
            dx -= 0.04 * Math.signum(dx);
        }
        return Math.min(Math.max(dx, -maxX), maxX);
    }

    private double updateVertical() {
        double dy = this.player.getVelocity().y;

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            dy -= 0.1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W) && this.player.isOnGround()) {
            dy = maxY;
        }

        return Math.min(Math.max(dy, -maxY), maxY);
    }

}
