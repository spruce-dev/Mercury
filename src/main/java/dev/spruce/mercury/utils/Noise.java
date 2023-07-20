package dev.spruce.mercury.utils;

import java.util.Random;

public class Noise {

    private int octaves;
    private float amplitude;
    private float persistence;
    private Random random;
    private int seed;

    public Noise(int octaves, float amplitude, float persistence, int seed) {
        this.octaves = octaves;
        this.amplitude = amplitude;
        this.persistence = persistence;
        this.seed = seed;
        this.random = new Random(seed);
    }

    public float[][] generateNoise(int width, int height) {
        float[][] noise = new float[width][height];

        for (int octave = 0; octave < octaves; octave++) {
            float[][] octaveNoise = new float[width][height];
            float octaveAmplitude = (float) Math.pow(amplitude, octave);

            int samplePeriod = 1 << octave;
            float sampleFrequency = 1.0f / samplePeriod;

            for (int x = 0; x < width; x++) {
                int sampleX0 = (x / samplePeriod) * samplePeriod;
                int sampleX1 = (sampleX0 + samplePeriod) % width;
                float horizontalBlend = (x - sampleX0) * sampleFrequency;

                for (int y = 0; y < height; y++) {
                    int sampleY0 = (y / samplePeriod) * samplePeriod;
                    int sampleY1 = (sampleY0 + samplePeriod) % height;
                    float verticalBlend = (y - sampleY0) * sampleFrequency;

                    float top = interpolate(random.nextFloat(), smoothNoise(sampleX0, sampleY0), smoothNoise(sampleX1, sampleY0), horizontalBlend);
                    float bottom = interpolate(random.nextFloat(), smoothNoise(sampleX0, sampleY1), smoothNoise(sampleX1, sampleY1), horizontalBlend);

                    octaveNoise[x][y] += interpolate(random.nextFloat(), top, bottom, verticalBlend) * octaveAmplitude;
                }
            }

            noise = octaveNoise;
        }

        return noise;
    }

    private float smoothNoise(int x, int y) {
        float corners = (noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, y + 1) + noise(x + 1, y + 1)) / 16.0f;
        float sides = (noise(x - 1, y) + noise(x + 1, y) + noise(x, y - 1) + noise(x, y + 1)) / 8.0f;
        float center = noise(x, y) / 4.0f;
        return corners + sides + center;
    }

    private float interpolate(float a, float x, float y, float blend) {
        float f = (1 - (float) Math.cos(blend * Math.PI)) * 0.5f;
        return x * (1 - f) + y * f;
    }

    private float noise(int x, int y) {
        random.setSeed(x * 49632L + y * 325176L + seed);
        return random.nextFloat() * 2 - 1;
    }
}
