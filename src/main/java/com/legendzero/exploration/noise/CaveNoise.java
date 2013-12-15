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
package com.legendzero.exploration.noise;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author CrypticStorm
 */
public class CaveNoise extends Noise {

    public CaveNoise() {
        super();
    }

    public CaveNoise(long seed) {
        super(seed);
    }

    @Override
    public float[][] genNoiseMap(int width, int height) {
        float[][] data = new float[width][height];

        for (int i = 0; i < data.length; i++) {
            data[i] = new float[height];
        }

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        int generatedMiners = 0;
        Set<Miner> miners = new HashSet<Miner>();
        miners.add(new Miner(width / 2, height / 2));
        miners.add(new Miner(width / 4, height / 2));
        miners.add(new Miner(width / 2, height / 4));
        miners.add(new Miner(width * 3 / 4, height / 2));
        miners.add(new Miner(width / 2, height * 3 / 4));
        generatedMiners = 5;

        while (generatedMiners < 1000) {
            for (Miner miner : miners.toArray(new Miner[0])) {
                if (miner.x < 0 || miner.x >= width || miner.y < 0 || miner.y >= height) {
                    miners.remove(miner);
                    continue;
                }
                data[miner.x][miner.y] = 1f;

                if (this.random.nextDouble() < 0.1) {
                    int[] movement = directions[this.random.nextInt(directions.length)];
                    miners.add(new Miner(miner.x + movement[0], miner.y + movement[1]));
                    generatedMiners++;
                }
                int[] movement = directions[this.random.nextInt(directions.length)];
                miner.move(movement[0], movement[1]);
            }
        }
        
        return data;
    }

    private class Miner {

        private int x;
        private int y;

        public Miner(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(int x, int y) {
            this.x += x;
            this.y += y;
        }
    }
}
