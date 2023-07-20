package com.mercury.application.game.terrain;

import dev.spruce.mercury.graphics.model.Model;
import dev.spruce.mercury.graphics.model.ModelLoader;
import dev.spruce.mercury.graphics.texture.Texture;
import dev.spruce.mercury.utils.MathUtil;
import dev.spruce.mercury.utils.Noise;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Terrain {

    public static final float SIZE = 800;
    public static final int VERTEX_COUNT = 64;

    private float x;
    private float z;
    private final int offsetX, offsetZ;
    private Model model;
    private Texture texture;

    private float[][] heightMap;

    public Terrain(float gridX, float gridZ, Texture texture, float[][] map, int offsetX, int offsetZ) {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
        this.texture = texture;
        this.heightMap = map;
        this.model = generateTerrain();
    }

    private Model generateTerrain() {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i = 0; i < VERTEX_COUNT; i++) {
            for(int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = getHeight(j, i, heightMap);
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, heightMap);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for(int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return ModelLoader.get().loadToVAO(vertices, indices, textureCoords, normals);
    }

    private Vector3f calculateNormal(int x, int y, float[][] noiseMap) {
        float heightL = 0;
        float heightR = 0;
        float heightD = 0;
        float heightU = 0;
        if (x > 0) {
            heightL = getHeight(x - 1, y, noiseMap);
            if (x < noiseMap.length - 1) heightR = getHeight(x + 1, y, noiseMap);
        }
        if (y > 0) {
            heightD = getHeight(x, y - 1, noiseMap);
            if (y < noiseMap.length - 1) heightU = getHeight(x, y + 1, noiseMap);
        }
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        return normal.normalize();
    }

    public float getHeightAtPosition(int worldX, int worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;

        float gridSquareSize = SIZE / (heightMap.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heightMap.length - 1 || gridZ >= heightMap.length - 1 || gridX < 0 || gridZ < 0) {
            return 0f;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        return xCoord <= (1 - zCoord) ?
                MathUtil.barryCentric(new Vector3f(0, heightMap[gridX][gridZ], 0), new Vector3f(1,
                        getHeight(gridX + 1, gridZ, heightMap), 0), new Vector3f(0,
                        getHeight(gridX, gridZ + 1, heightMap), 1), new Vector2f(xCoord, zCoord)) :
                MathUtil.barryCentric(new Vector3f(1, getHeight(gridX + 1, gridZ, heightMap), 0), new Vector3f(1,
                        getHeight(gridX + 1, gridZ + 1, heightMap), 1), new Vector3f(0,
                        getHeight(gridX, gridZ + 1, heightMap), 1), new Vector2f(xCoord, zCoord));
    }

    private float getHeight(int x, int y, float[][] noiseMap) {
        if (x == noiseMap.length - 1 || y == noiseMap.length - 1 || x == 0 || y == 0)
            return 1f;
        return (noiseMap[x][y]);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Model getModel() {
        return model;
    }

    public Texture getTexture() {
        return texture;
    }
}
