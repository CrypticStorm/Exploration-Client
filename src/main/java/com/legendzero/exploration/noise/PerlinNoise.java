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

import java.util.Random;
import javax.vecmath.Vector2f;

/**
 *
 * @author CrypticStorm
 */
public class PerlinNoise {

    private final Random random;
    private final int[] permutation;
    private final Vector2f[] gradients;

    public PerlinNoise() {
        this.random = new Random();
        this.permutation = this.calculatePermutation(256);
        this.gradients = this.calculateGradients(256);
    }

    public PerlinNoise(long seed) {
        this.random = new Random(seed);
        this.permutation = this.calculatePermutation(256);
        this.gradients = this.calculateGradients(256);
    }

    public void setSeed(long seed) {
        this.random.setSeed(seed);
    }

    private int[] calculatePermutation(int length) {
        int[] p = new int[length];

        for (int i = 0; i < length; i++) {
            p[i] = i;
            int index = this.random.nextInt(length);

            int t = p[i];
            p[i] = p[index];
            p[index] = t;
        }
        return p;
    }

    private Vector2f[] calculateGradients(int length) {
        Vector2f[] g = new Vector2f[length];

        for (int i = 0; i < length; i++) {
            Vector2f gradient;

            do {
                gradient = new Vector2f(this.random.nextFloat() * 2 - 1, this.random.nextFloat() * 2 - 1);
            } while (gradient.lengthSquared() >= 1);

            gradient.normalize();

            g[i] = gradient;
        }

        return g;
    }

    private float drop(float t) {
        t = Math.abs(t);
        return 1f - t * t * t * (t * (t * 6 - 15) + 10);
    }

    private float drop(float u, float v) {
        return this.drop(u) * this.drop(v);
    }

    public float genNoise(float x, float y) {
        Vector2f cell = new Vector2f((float) Math.floor(x), (float) Math.floor(y));

        float total = 0f;

        Vector2f[] corners = {new Vector2f(0, 0), new Vector2f(0, 1), new Vector2f(1, 0), new Vector2f(1, 1)};

        for (Vector2f corner : corners) {
            Vector2f ij = new Vector2f(cell.x + corner.x, cell.y + corner.y);
            Vector2f uv = new Vector2f(x - ij.x, y - ij.y);

            int index = this.permutation[((int) ij.x) % this.permutation.length];
            index = this.permutation[(index + (int) ij.y) % this.permutation.length];

            Vector2f grad = this.gradients[index % this.gradients.length];

            total += this.drop(uv.x, uv.y) * grad.dot(uv);
        }

        return Math.max(Math.min(total, 1f), -1f);
    }

    public float[][] genNoiseMap(int width, int height, int octaves, float persistence, float amplitude) {
        float[][] data = new float[width][height];

        for (int i = 0; i < data.length; i++) {
            data[i] = new float[height];
        }

        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        float frequency = 0.5f;

        for (int octave = 0; octave < octaves; octave++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    float noise = this.genNoise(x * frequency * 1f / width, y * frequency * 1f / height) * amplitude;
                    data[x][y] += noise;

                    min = Math.min(min, noise);
                    max = Math.max(max, noise);
                }
            }
            frequency *= 2;
            amplitude *= persistence;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                data[x][y] = (data[x][y] - min) / (max - min);
            }
        }

        return data;
    }
}
