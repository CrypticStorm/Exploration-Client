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

    public LifeNoise(int iterations) {
        super();
        this.iterations = iterations;
    }

    public LifeNoise(long seed, int iterations) {
        super(seed);
        this.iterations = iterations;
    }

    public float[][] genNoiseMap(int width, int height) {
        float[][] data = new float[width][height];

        fill(data, width, height, 0.4f);

        for(int i = 0; i < this.iterations; i++) {
            data = iterate(data, width, height, 4, 3);
        }
        /*for (int x = 0; x < width; x++) {
         data[x][0] = 1f;
         data[x][height - 1] = 1f;
         }

         for (int y = 0; y < height; y++) {
         data[0][y] = 1f;
         data[width - 1][y] = 1f;
         }

         int iters = this.iterations;
         while(--iters > 0) {
         automate(data, width, height, 6, 3, iters);
         }
         automate(data, width, height, 5, 5, 1);*/
        return data;
    }

    private void fill(float[][] data, int width, int height, float initialChance) {
        for (int x = 0; x < width; x++) {
            data[x] = new float[height];
            for (int y = 0; y < height; y++) {
                if (this.random.nextDouble() < initialChance) {
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
                
                if(map[x][y] == 1f) {
                    if(neighbors < death) {
                        newMap[x][y] = 0f;
                    } else {
                        newMap[x][y] = 1f;
                    }
                } else {
                    if(neighbors > birth) {
                        newMap[x][y] = 1f;
                    } else {
                        newMap[x][y] = 0f;
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
                } else if (map[nx][ny] == 1f) {
                    count++;
                }
            }
        }
        return count;
    }
    /*public void automate(float[][] data, int width, int height, int born, int survive, int iterations) {
     float[][] copy = new float[width][height];

     for (int x = 0; x < width; x++) {
     copy[x] = new float[height];
     }

     for (int i = 0; i < this.iterations; i++) {
     for (int x = 0; x < width; x++) {
     for (int y = 0; y < height; y++) {

     boolean alive;
     if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
     alive = true;
     } else {
     float neighbors = 0f;

     neighbors += data[x - 1][y - 1];
     neighbors += data[x - 1][y];
     neighbors += data[x - 1][y + 1];
     neighbors += data[x][y - 1];
     neighbors += data[x][y + 1];
     neighbors += data[x + 1][y - 1];
     neighbors += data[x + 1][y];
     neighbors += data[x + 1][y + 1];

     alive = (data[x][y] == 0f && neighbors >= born) || (data[x][y] == 1f && neighbors >= survive);
     }

     copy[x][y] = alive ? 1f : 0f;
     }
     }
     }
     for (int x = 0; x < width; x++) {
     for (int y = 0; y < height; y++) {
     data[x][y] = copy[x][y];
     }
     }
     }*/
}
