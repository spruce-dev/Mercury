package dev.spruce.mercury.graphics.model;

public class Model {

    private int vao;
    private int vertexCount;

    public Model(int vao, int vertexCount) {
        this.vao = vao;
        this.vertexCount = vertexCount;
    }

    public int getVao() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
