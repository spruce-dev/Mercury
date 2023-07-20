package com.mercury.application.game.terrain;

import dev.spruce.mercury.graphics.texture.Texture;
import dev.spruce.mercury.graphics.texture.TextureLoader;
import dev.spruce.mercury.utils.Noise;

public class TerrainGenerator {

    private final int width;
    private final int height;
    private final Noise noiseGenerator;

    public TerrainGenerator(int width, int height, Noise noiseGenerator) {
        this.width = width;
        this.height = height;
        this.noiseGenerator = noiseGenerator;
    }

    public Terrain[][] generateTerrain() {
        Texture terrainTexture = TextureLoader.get().loadTexture("assets/res/terrain_grass.jpg");
        Terrain[][] terrains = new Terrain[this.width][this.height];
        float[][] noise = noiseGenerator.generateNoise(width * Terrain.VERTEX_COUNT + 1, height * Terrain.VERTEX_COUNT + 1);
        for (int z = 0; z < this.height; z++) {
            for (int x = 0; x < this.width; x++) {
                terrains[x][z] = new Terrain(x, z, terrainTexture, getOffsetArray(noise, x * Terrain.VERTEX_COUNT, z * Terrain.VERTEX_COUNT), 0, 0);
            }
        }
        return terrains;
    }

    private float[][] getOffsetArray(float[][] original, int offsetX, int offsetY) {
        float[][] newArray = new float[Terrain.VERTEX_COUNT][Terrain.VERTEX_COUNT];
        for (int z = 0; z < Terrain.VERTEX_COUNT; z++) {
            for (int x = 0; x < Terrain.VERTEX_COUNT; x++) {
                newArray[x][z] = original[offsetX + x][offsetY + z];
            }
        }
        return newArray;
    }
}
