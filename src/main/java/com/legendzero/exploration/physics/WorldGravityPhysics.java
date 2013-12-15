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
package com.legendzero.exploration.physics;

import com.legendzero.exploration.Exploration;
import com.legendzero.exploration.entity.Entity;
import com.legendzero.exploration.world.World;
import java.util.Set;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

/**
 *
 * @author CrypticStorm
 */
public class WorldGravityPhysics extends Physics {

    private final World world;
    private final Vector2d gravity;
    private final Vector2d terminalVelocity;

    public WorldGravityPhysics(World world, Vector2d gravity, Vector2d terminalVelocity) {
        this.world = world;
        this.gravity = gravity;
        this.terminalVelocity = terminalVelocity;
    }

    @Override
    public void update(Exploration game) {
        Set<Entity> entities = this.world.getEntities();
        for (Entity entity : entities) {
            if (!entity.isFlying()) {
                Tuple2d velocity = entity.getVelocity();
                velocity.add(this.gravity);
                if (Math.abs(velocity.x) > Math.abs(this.terminalVelocity.x)) {
                    velocity.x = this.terminalVelocity.x * Math.signum(velocity.x);
                }
                if (Math.abs(velocity.y) > Math.abs(this.terminalVelocity.y)) {
                    velocity.y = this.terminalVelocity.y * Math.signum(velocity.y);
                }
            }
        }
    }
}
