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
package com.legendzero.exploration.render;

import com.legendzero.exploration.Exploration;
import com.legendzero.exploration.api.entity.IPlayer;
import com.legendzero.exploration.util.AABB;
import com.legendzero.exploration.util.Location;

import javax.vecmath.Point2d;
import javax.vecmath.Tuple2d;

import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author CrypticStorm
 */
public enum RenderState {

    GAME {
        @Override
        public void renderSetup(Exploration game) {
            IPlayer player = game.getPlayer();
            double width = player.getViewSize();
            double height = width * Display.getHeight() / (double) Display.getWidth();
            Location loc = player.getLocation();
            this.setCoords(new Point2d(loc.getX() - width / 2, loc.getY() - height / 2), new Point2d(loc.getX() + width / 2, loc.getY() + height / 2));
            AABB aabb = this.getAABB();
            glOrtho(aabb.getLeft(), aabb.getRight(), aabb.getBottom(), aabb.getTop(), 1, -1);

        }

        @Override
        public void renderBackground(Exploration game) {
            game.getPlayer().getLocation().getWorld().render(game);
        }

        @Override
        public void renderForeground(Exploration game) {
            glPushMatrix();
            game.getPlayer().renderGUI(game);
            glPopMatrix();
        }
    };

    private AABB aabb;

    public void render(Exploration game) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        this.renderSetup(game);

        glMatrixMode(GL_MODELVIEW);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        this.renderBackground(game);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        this.renderForeground(game);
    }

    public void setCoords(Tuple2d min, Tuple2d max) {
        this.aabb = new AABB(min, max);
    }

    public AABB getAABB() {
        return this.aabb;
    }

    public abstract void renderSetup(Exploration game);

    public abstract void renderBackground(Exploration game);

    public abstract void renderForeground(Exploration game);
}
