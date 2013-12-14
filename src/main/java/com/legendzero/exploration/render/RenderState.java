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
import com.legendzero.exploration.entity.Player;
import com.legendzero.exploration.util.Location;
import com.legendzero.exploration.world.Tile;
import javax.vecmath.Color4f;
import javax.vecmath.Point2d;
import javax.vecmath.Tuple2d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author CrypticStorm
 */
public enum RenderState {

    GAME {
                @Override
                public void renderForeground(Exploration game) {
                    double width = game.getPlayer().getViewSize();
                    double height = game.getPlayer().getViewSize() * Display.getHeight() / (double) Display.getWidth();
                    Location loc = game.getPlayer().getLocation();
                    this.setCoords(new Point2d(loc.getX() - width / 2, loc.getY() - height / 2), new Point2d(loc.getX() + width / 2, loc.getY() + height / 2));
                    GL11.glOrtho(this.getMinX(), this.getMaxX(), this.getMinY(), this.getMaxY(), 1, -1);
                }

                @Override
                public void renderBackground(Exploration game) {
                    Tile[][] map = game.getWorld().getMap();
                    Player player = game.getPlayer();
                    Location location = game.getPlayer().getLocation();

                    GL11.glBegin(GL11.GL_QUADS);
                    for (int i = Math.max((int) this.getMinX(), 0); i < Math.min(Math.ceil(this.getMaxX()), game.getWorld().getWidth()); i++) {
                        for (int j = Math.max((int) this.getMinY(), 0); j < Math.min(Math.ceil(this.getMaxY()), game.getWorld().getHeight()); j++) {
                            Tile tile = map[i][j];
                            Color4f color = tile.getType().getColor();
                            GL11.glColor4f(color.x, color.y, color.z, color.w);
                            GL11.glVertex2i(i, j);
                            GL11.glVertex2i(i, j + 1);
                            GL11.glVertex2i(i + 1, j + 1);
                            GL11.glVertex2i(i + 1, j);
                        }
                    }

                    GL11.glColor3f(1f, 0f, 0f);
                    GL11.glVertex2d(location.getX() - player.getWidth() / 2, location.getY());
                    GL11.glVertex2d(location.getX() + player.getWidth() / 2, location.getY());
                    GL11.glVertex2d(location.getX() + player.getWidth() / 2, location.getY() + player.getHeight());
                    GL11.glVertex2d(location.getX() - player.getWidth() / 2, location.getY() + player.getHeight());

                    GL11.glEnd();
                }

            };

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public void render(Exploration game) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        this.renderForeground(game);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        this.renderBackground(game);
    }

    public void setCoords(Tuple2d min, Tuple2d max) {
        this.minX = min.x;
        this.maxX = max.x;
        this.minY = min.y;
        this.maxY = max.y;
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public abstract void renderForeground(Exploration game);

    public abstract void renderBackground(Exploration game);
}
