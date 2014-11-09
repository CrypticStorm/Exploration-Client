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

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.entity.IEntity;
import com.legendzero.exploration.api.physics.IPhysics;
import com.legendzero.exploration.world.World;
import java.util.Set;

/**
 *
 * @author CrypticStorm
 */
public class WorldUpdatePhysics implements IPhysics {

    private final World world;
    
    public WorldUpdatePhysics(World world) {
        this.world = world;
    }

    @Override
    public void update(IExploration game) {
        Set<IEntity> entities = this.world.getEntities();
        for(IEntity entity : entities) {
            entity.update(game);
            if(!entity.isAlive()) {
                this.world.removeEntity(entity);
            }
        }
    }
}
