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
package com.legendzero.exploration.tiles;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.item.IMaterial;
import com.legendzero.exploration.api.render.IMesh;
import com.legendzero.exploration.api.tiles.ITile;
import com.legendzero.exploration.render.Mesh;
import com.legendzero.exploration.util.AABB;
import com.legendzero.exploration.util.Direction;
import com.legendzero.exploration.util.Location;
import org.lwjgl.BufferUtils;

import javax.vecmath.Color4f;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author CrypticStorm
 */
public class Tile implements ITile {

    private boolean[] adjacencies;
    private IMaterial type;
    private Location location;
    private IMesh mesh;

    public Tile(IMaterial type, Location location) {
        this.mesh = new Mesh(4, GL_QUADS);
        this.setType(type);
        this.setLocation(location);
        this.adjacencies = new boolean[Direction.values().length];
    }

    public IMaterial getType() {
        return this.type;
    }

    public void setType(IMaterial type) {
        this.type = type;

        this.mesh.setColorBuffer(IMesh.getStaticFloatBuffer(this.type.getColor()), 4);
    }

    public ITile getAdjacent(Direction dir) {
        return this.location.getWorld().getTile((int) this.location.getX() + dir.getDX(), (int) this.location.getY() + dir.getDY());
    }

    public boolean getAdjacency(Direction dir) {
        return this.adjacencies[dir.ordinal()];
    }

    public void setAdjacency(Direction dir, boolean value) {
        this.adjacencies[dir.ordinal()] = value;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location.copy();
        this.location.setX(Math.round(location.getX()));
        this.location.setY(Math.round(location.getY()));

        DoubleBuffer vertexBuffer = BufferUtils.createDoubleBuffer(12);
        vertexBuffer.put(this.location.getX()).put(this.location.getY()).put(0);
        vertexBuffer.put(this.location.getX() + 1).put(this.location.getY()).put(0);
        vertexBuffer.put(this.location.getX() + 1).put(this.location.getY() + 1).put(0);
        vertexBuffer.put(this.location.getX()).put(this.location.getY() + 1).put(0);
        vertexBuffer.flip();

        this.mesh.setVertexBuffer(vertexBuffer, 3);
    }

    @Override
    public void render(IExploration game) {
        this.mesh.render(game);
    }
}
