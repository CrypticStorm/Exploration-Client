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
import com.legendzero.exploration.api.option.OptionToggle;
import com.legendzero.exploration.control.PlayerController;
import com.legendzero.exploration.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author CrypticStorm
 */
public class OptionController extends PlayerController {

    private final Set<Integer> keysPressed;
    private final Map<Integer, OptionToggle> options;

    public OptionController(IPlayer player) {
        super(player);
        this.keysPressed = new HashSet<>();
        this.options = new HashMap<>();
        this.options.put(Keyboard.KEY_F, (game) ->
                player.setFlying(!player.isFlying()));
        this.options.put(Keyboard.KEY_P, (game) ->
                System.out.println("(" + player.getLocation().getX() + ", " + player.getLocation().getY() + ")"));
        this.options.put(Keyboard.KEY_ESCAPE, IExploration::stop);

    }

    @Override
    public void update(IExploration game) {
        for (Integer key : options.keySet()) {
            if (Keyboard.isKeyDown(key) && !this.keysPressed.contains(key)) {
                this.keysPressed.add(key);
                options.get(key).update(game);
            } else if (!Keyboard.isKeyDown(key) && this.keysPressed.contains(key)) {
                this.keysPressed.remove(key);
            }
        }
    }

}
