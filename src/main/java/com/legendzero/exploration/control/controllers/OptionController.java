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
import com.legendzero.exploration.option.OptionToggle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author CrypticStorm
 */
public class OptionController extends Controller {

    private final Set<Integer> keysPressed;
    private final Map<Integer, OptionToggle> options;

    public OptionController(Player player) {
        super(player);
        this.keysPressed = new HashSet<Integer>();
        this.options = new HashMap<Integer, OptionToggle>();
        this.options.put(Keyboard.KEY_F, new OptionToggle() {
            public void update(Player player) {
                player.setFlying(!player.isFlying());
            }
        });
    }

    @Override
    public void update(Exploration game) {
        for (Integer key : options.keySet()) {
            if (Keyboard.isKeyDown(key) && !this.keysPressed.contains(key)) {
                this.keysPressed.add(key);
                options.get(key).update(this.player);
            } else if (!Keyboard.isKeyDown(key) && this.keysPressed.contains(key)) {
                this.keysPressed.remove(key);
            }
        }
    }

}
