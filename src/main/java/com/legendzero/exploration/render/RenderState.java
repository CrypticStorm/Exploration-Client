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
import com.legendzero.exploration.api.entity.IEntity;
import com.legendzero.exploration.api.entity.IPlayer;
import com.legendzero.exploration.api.world.ITile;
import com.legendzero.exploration.api.world.IWorld;
import com.legendzero.exploration.util.AABB;
import com.legendzero.exploration.util.Location;
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
                    IPlayer player = game.getPlayer();
                    double width = player.getViewSize();
                    double height = width * Display.getHeight() / (double) Display.getWidth();
                    Location loc = player.getLocation();
                    this.setCoords(new Point2d(loc.getX() - width / 2, loc.getY() - height / 2), new Point2d(loc.getX() + width / 2, loc.getY() + height / 2));
                    AABB aabb = this.getAABB();
                    GL11.glOrtho(aabb.getLeft(), aabb.getRight(), aabb.getBottom(), aabb.getTop(), 1, -1);

                }

                @Override
                public void renderBackground(Exploration game) {
                    //game.getFont().drawString(0, game.getFont().getHeight() * 10, "TESTING", 1.5f, 1.5f);
                    IWorld world = game.getPlayer().getLocation().getWorld();
                    ITile[][] map = world.getMap();

                    AABB aabb = this.getAABB();
                    int xMin = Math.max((int) aabb.getLeft(), 0);
                    int xMax = Math.min((int) Math.ceil(aabb.getRight()), world.getWidth());
                    int yMin = Math.max((int) aabb.getBottom(), 0);
                    int yMax = Math.min((int) Math.ceil(aabb.getTop()), world.getHeight());
                    GL11.glBegin(GL11.GL_QUADS);
                    for (int i = xMin; i < xMax; i++) {
                        for (int j = yMin; j < yMax; j++) {
                            ITile tile = map[i][j];
                            Color4f color = tile.getType().getColor();
                            GL11.glColor4f(color.x, color.y, color.z, color.w);
                            GL11.glVertex2i(i, j);
                            GL11.glVertex2i(i, j + 1);
                            GL11.glVertex2i(i + 1, j + 1);
                            GL11.glVertex2i(i + 1, j);
                        }
                    }

                    for (IEntity entity : world.getEntities()) {
                        Location loc = entity.getLocation();

                        //if (xMin <= loc.getX() && loc.getX() <= xMax && yMin <= loc.getY() && loc.getY() <= yMax) {
                        Color4f color = entity.getColor();

                        GL11.glColor4f(color.x, color.y, color.z, color.w);
                        GL11.glVertex2d(loc.getX() - entity.getWidth() / 2, loc.getY());
                        GL11.glVertex2d(loc.getX() + entity.getWidth() / 2, loc.getY());
                        GL11.glVertex2d(loc.getX() + entity.getWidth() / 2, loc.getY() + entity.getHeight());
                        GL11.glVertex2d(loc.getX() - entity.getWidth() / 2, loc.getY() + entity.getHeight());

                        //}
                    }
                    GL11.glEnd();
                }

            };

    private AABB aabb;

    public void render(Exploration game) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        this.renderForeground(game);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        this.renderBackground(game);
    }

    public void setCoords(Tuple2d min, Tuple2d max) {
        this.aabb = new AABB(min, max);
    }

    public AABB getAABB() {
        return this.aabb;
    }

    public abstract void renderForeground(Exploration game);

    public abstract void renderBackground(Exploration game);
}
