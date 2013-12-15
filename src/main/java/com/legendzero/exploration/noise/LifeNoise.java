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

/**
 *
 * @author CrypticStorm
 */
public class LifeNoise extends Noise {

    private final int iterations;

    public LifeNoise() {
        super();
        this.iterations = 5;
    }
    public LifeNoise(int iterations) {
        super();
        this.iterations = iterations;
    }

    public LifeNoise(long seed) {
        super(seed);
        this.iterations = 5;
    }
    public LifeNoise(long seed, int iterations) {
        super(seed);
        this.iterations = iterations;
    }

    public float[][] genNoiseMap(int width, int height) {
        float[][] data = new float[width][height];

        fill(data, width, height, 0.3f);

        for(int i = 0; i < this.iterations; i++) {
            data = iterate(data, width, height, 3, 2);
        }
        return data;
    }

    private void fill(float[][] data, int width, int height, float initialChance) {
        for (int x = 1; x < width - 1; x++) {
            data[x] = new float[height];
            for (int y = 1; y < height - 1; y++) {
                if (this.random.nextDouble() >= initialChance) {
                    data[x][y] = 1f;
                }
            }
        }
    }

    private float[][] iterate(float[][] map, int width, int height, int birth, int death) {
        float[][] newMap = new float[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int neighbors = getNearby(map, x, y, width, height);
                
                if(map[x][y] == 0f) {
                    if(neighbors < death) {
                        newMap[x][y] = 1f;
                    } else {
                        newMap[x][y] = 0f;
                    }
                } else {
                    if(neighbors > birth) {
                        newMap[x][y] = 0f;
                    } else {
                        newMap[x][y] = 1f;
                    }
                }
            }
        }
        
        return newMap;
    }

    private int getNearby(float[][] map, int x, int y, int width, int height) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int nx = x + i;
                int ny = y + j;

                if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
                    count++;
                } else if (map[nx][ny] == 0f) {
                    count++;
                }
            }
        }
        return count;
    }
}
