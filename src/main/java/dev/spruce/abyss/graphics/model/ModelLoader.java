package dev.spruce.abyss.graphics.model;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ModelLoader {

    private static ModelLoader instance;

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    public Model loadToVAO(float[] vertices, int[] indices, float[] texCoords, float[] normals) {
        int VAO = createVAO();
        bindIndicesBuffer(indices);
        storeInAttributeList(0, 3, vertices);
        storeInAttributeList(1, 2, texCoords);
        storeInAttributeList(2, 3, normals);
        unbindVAO();
        return new Model(VAO, indices.length);
    }

    private int createVAO() {
        int VAO = glGenVertexArrays();
        this.vaos.add(VAO);
        glBindVertexArray(VAO);
        return VAO;
    }

    private void unbindVAO() {
        glBindVertexArray(0);
    }

    private void storeInAttributeList(int index, int size, float[] data) {
        int VBO = glGenBuffers();
        this.vbos.add(VBO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        FloatBuffer dataBuffer = getFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int VBO = glGenBuffers();
        this.vbos.add(VBO);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, VBO);
        IntBuffer dataBuffer = getIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
    }

    private FloatBuffer getFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer getIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void free() {
        vaos.forEach(GL30::glDeleteVertexArrays);
        vbos.forEach(GL30::glDeleteBuffers);
    }

    public static ModelLoader get() {
        if (instance == null) {
            instance = new ModelLoader();
        }
        return instance;
    }
}
