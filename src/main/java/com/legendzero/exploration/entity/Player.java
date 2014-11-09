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
package com.legendzero.exploration.entity;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.control.IController;
import com.legendzero.exploration.api.entity.IPlayer;
import com.legendzero.exploration.control.controllers.BlockController;
import com.legendzero.exploration.control.controllers.MovementController;
import com.legendzero.exploration.control.controllers.OptionController;
import com.legendzero.exploration.control.controllers.ScreenShotController;
import com.legendzero.exploration.control.controllers.ZoomController;
import java.util.HashSet;
import java.util.Set;
import javax.vecmath.Color4f;

/**
 *
 * @author CrypticStorm
 */
public class Player extends Entity implements IPlayer {

    private final Set<IController> controllers;
    private final double defaultViewSize;
    private double viewSize;
    private int money;

    public Player(String name, boolean controlled) {
        super(new Color4f(1.0f, 0.0f, 0.0f, 1.0f), name, 100.0, 0.6, 0.8);
        this.controllers = new HashSet<>();
        this.defaultViewSize = this.viewSize = 100;
        this.money = 0;
        if (controlled) {
            this.addController(new MovementController(this));
            this.addController(new ZoomController(this));
            this.addController(new BlockController(this));
            this.addController(new ScreenShotController(this));
            this.addController(new OptionController(this));
        }
    }

    @Override
    public void update(IExploration game) {
        for (IController controller : this.controllers) {
            controller.update(game);
        }
        super.update(game);
    }

    public final Set<IController> getControllers() {
        return this.controllers;
    }

    public final void addController(IController controller) {
        this.controllers.add(controller);
    }

    public double getViewSize() {
        return this.viewSize;
    }

    public void resetViewSize() {
        this.viewSize = this.defaultViewSize;
    }

    public void setViewSize(double viewSize) {
        this.viewSize = viewSize;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int amount) {
        this.money = amount;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void takeMoney(int amount) {
        this.money -= amount;
        this.money = Math.max(0, money);
    }

}
