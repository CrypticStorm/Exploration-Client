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

import com.legendzero.exploration.Exploration;
import com.legendzero.exploration.util.Location;
import com.legendzero.exploration.world.World;
import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Color4f;
import javax.vecmath.Point2d;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

/**
 *
 * @author CrypticStorm
 */
public abstract class Entity {

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

    public void update(Exploration game) {
        World world = this.location.getWorld();

        double vx = this.velocity.x;
        double vy = this.velocity.y;

        boolean down = vy < 0.0;
        boolean up = vy > 0.0;
        boolean left = vx < 0.0;
        boolean right = vx > 0.0;

        Location newLoc = new Location(world, this.location.getX() + vx, this.location.getY() + vy);

        int ix1 = (int) (this.location.getX() - (this.width / 2) - 1);
        int ix2 = (int) (this.location.getX() + (this.width / 2) - 0.0001) + 1;
        int iy1 = (int) (this.location.getY() - 1);
        int iy2 = (int) (this.location.getY() + this.height - 0.0001) + 1;

        List<Tuple2d> horizontal = new LinkedList<Tuple2d>();
        List<Tuple2d> vertical = new LinkedList<Tuple2d>();
        Tuple2d corner = null;

        if (right) {
            if (up) {
                if (world.getTile(ix2, iy2) == null || world.getTile(ix2, iy2).getType().isSolid()) {
                    corner = new Point2d(ix2, iy2);
                }
            }
            for (int y = iy1 + 1; y < iy2; y++) {
                if (world.getTile(ix2, y) == null || world.getTile(ix2, y).getType().isSolid()) {
                    vertical.add(new Point2d(ix2, y));
                }
            }
        } else if (left) {
            if (down) {
                if (world.getTile(ix1, iy1) == null || world.getTile(ix1, iy1).getType().isSolid()) {
                    corner = new Point2d(ix1, iy1);
                }
            }
            for (int y = iy1 + 1; y < iy2; y++) {
                if (world.getTile(ix1, y) == null || world.getTile(ix1, y).getType().isSolid()) {
                    vertical.add(new Point2d(ix1, y));
                }
            }
        }

        if (up) {
            if (left) {
                if (world.getTile(ix1, iy2) == null || world.getTile(ix1, iy2).getType().isSolid()) {
                    corner = new Point2d(ix1, iy2);
                }
            }
            for (int x = ix1 + 1; x < ix2; x++) {
                if (world.getTile(x, iy2) == null || world.getTile(x, iy2).getType().isSolid()) {
                    horizontal.add(new Point2d(x, iy2));
                }
            }
        } else if (down) {
            if (right) {
                if (world.getTile(ix2, iy1) == null || world.getTile(ix2, iy1).getType().isSolid()) {
                    corner = new Point2d(ix2, iy1);
                }
            }
            for (int x = ix1 + 1; x < ix2; x++) {
                if (world.getTile(x, iy1) == null || world.getTile(x, iy1).getType().isSolid()) {
                    horizontal.add(new Point2d(x, iy1));
                }
            }
        }

        for (Tuple2d tuple : vertical) {
            if (newLoc.getX() + this.width / 2 > tuple.x && newLoc.getX() - this.width / 2 < tuple.x + 1) {
                if (right) {
                    vx = tuple.x - (this.getLocation().getX() + this.width / 2);
                } else if (left) {
                    vx = tuple.x + 1 - (this.getLocation().getX() - this.width / 2);
                }
            }
        }

        for (Tuple2d tuple : horizontal) {
            if (newLoc.getY() + this.height > tuple.y && newLoc.getY() < tuple.y + 1) {
                if (up) {
                    vy = tuple.y - (this.getLocation().getY() + this.height);
                } else if (down) {
                    vy = tuple.y + 1 - this.getLocation().getY();
                }
            }
        }

        if (corner != null && vertical.isEmpty() && horizontal.isEmpty()) {
            if (newLoc.getY() + this.height > corner.y && newLoc.getY() < corner.y + 1 && newLoc.getX() + (this.width / 2) > corner.x && newLoc.getX() - (this.width / 2) < corner.x + 1) {
                if (up) {
                    vy = corner.y - (this.getLocation().getY() + this.height);
                } else if (down) {
                    vy = corner.y + 1 - this.getLocation().getY();
                }
            }
        }

        if (this.velocity.y < 0.0 && vy == 0.0) {
            this.isOnGround = true;
        } else if (vy != 0.0) {
            this.isOnGround = false;
        }

        this.velocity.set(vx, vy);
        this.getLocation().add(this.velocity);

        if (this.getLocation().getX() < this.width / 2) {
            this.getLocation().setX(this.width / 2);
        } else if (this.getLocation().getX() > world.getWidth() - this.width / 2) {
            this.getLocation().setX(world.getWidth() - this.width / 2);
        }
        if (this.getLocation().getY() < 0.0) {
            this.getLocation().setY(0.0);
            this.isOnGround = true;
        } else if (this.getLocation().getY() > world.getHeight() - this.height) {
            this.getLocation().setY(world.getHeight() - this.height);
        }
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
