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
import com.legendzero.exploration.api.entity.IEntity;
import com.legendzero.exploration.api.world.ITile;
import com.legendzero.exploration.api.world.IWorld;
import com.legendzero.exploration.util.AABB;
import com.legendzero.exploration.util.IntersectData;
import com.legendzero.exploration.util.Location;

import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2d;

/**
 *
 * @author CrypticStorm
 */
public abstract class Entity implements IEntity {

    private final Color4f color;
    private final String name;
    private final double MAX_HEALTH;
    private double health;

    private final double width;
    private final double height;

    private Location location;
    private Vector2d velocity;
    private boolean isOnGround;
    private boolean isFlying;

    public Entity(Color4f color, String name, double hp, double width, double height) {
        this.color = color;
        this.name = name;
        this.MAX_HEALTH = this.health = hp;
        this.width = width;
        this.height = height;
        this.velocity = new Vector2d(0.0, 0.0);
        this.isOnGround = false;
    }

    @Override
    public void update(IExploration game) {
        this.isOnGround = false;
        Vector2d prevVelocity = new Vector2d(this.getVelocity());

        IWorld world = this.getLocation().getWorld();
        IntersectData NO_COLLISION = new IntersectData(new Vector2d(0, 0), 1);

        List<ITile> tiles = new LinkedList<>();

        AABB i_aabb = new AABB(this).expand(this.getVelocity());

        for (int x = (int) Math.floor(i_aabb.getLeft()) - 1; x <= (int) Math.ceil(i_aabb.getRight()) + 1; x++) {
            for (int y = (int) Math.floor(i_aabb.getBottom()) - 1; y <= (int) Math.ceil(i_aabb.getTop()) + 1; y++) {
                ITile tile = world.getTile(x, y);
                if (tile != null && tile.getType().isSolid()) {
                    tiles.add(tile);
                }
            }
        }

        while (true) {
            ITile reactant = null;
            IntersectData reaction = NO_COLLISION;
            i_aabb = new AABB(this);

            for (ITile current : tiles) {
                AABB t_aabb = new AABB(current);
                IntersectData collision = i_aabb.collide(t_aabb, this.getVelocity());
                if (collision.getTime() < reaction.getTime()) {
                    reaction = collision;
                    reactant = current;
                }
            }

            System.out.println(reaction.getNormal());
            if (reactant == null) {
                break;
            } else {
                this.onCollide(reactant, reaction);
            }
        }
        System.out.println();

        this.getLocation().add(this.getVelocity());

        AABB aabb = new AABB(this);
        boolean[] collisions = aabb.bound(new AABB(this.getLocation().getWorld()));
        if (collisions[0]) {
            this.getVelocity().x = 0;
            this.getLocation().setX(aabb.getMin().x + this.getWidth() / 2);
        }
        if (collisions[1]) {
            this.getVelocity().x = 0;
            this.getLocation().setX(aabb.getMin().x + this.getWidth() / 2);
        }
        if (collisions[2]) {
            this.getVelocity().y = 0;
            this.getLocation().setY(aabb.getMin().y);
        }
        if (collisions[3]) {
            this.getVelocity().y = 0;
            this.getLocation().setY(aabb.getMin().y);
        }

        if (prevVelocity.y <= 0 && this.getVelocity().y == 0) {
            this.isOnGround = true;
        }
    }

    public void onCollide(ITile other, IntersectData collision) {
        Vector2d tempVelocity = new Vector2d(this.getVelocity());
        tempVelocity.scale(collision.getTime());
        this.getLocation().add(tempVelocity);

        double remainingTime = 1.0 - collision.getTime();

        double dot = remainingTime *
                (this.getVelocity().x * collision.getNormal().y +
                        this.getVelocity().y * collision.getNormal().x);
        this.getVelocity().set(dot * collision.getNormal().y, dot * collision.getNormal().x);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Vector2d getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector2d velocity) {
        this.velocity = velocity;
    }

    public Color4f getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public double getHealth() {
        return this.health;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double heal(double amount) {
        return Math.min(this.health += amount, this.MAX_HEALTH);
    }

    public double damage(double amount) {
        return this.health -= amount;
    }

    public boolean isDamageable() {
        return true;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public boolean isOnGround() {
        return this.isOnGround;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public void setFlying(boolean isFlying) {
        this.isFlying = isFlying;
    }
}
