package dev.spruce.abyss.graphics.render;

import dev.spruce.abyss.game.entity.Entity;
import dev.spruce.abyss.game.terrain.Terrain;
import dev.spruce.abyss.graphics.model.TexturedModel;
import dev.spruce.abyss.graphics.shader.TerrainShader;
import dev.spruce.abyss.graphics.texture.Texture;
import dev.spruce.abyss.utils.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.begin();
        shader.uploadProjectionMatrix(projectionMatrix);
        shader.end();
    }

    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains){
            prepareTerrain(terrain);
            prepareTerrainMatrix(terrain);
            glDrawElements(GL_TRIANGLES, terrain.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindTerrain();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        Texture texture = terrain.getTexture();
        int vao = terrain.getModel().getVao();
        int textureID = texture.getTextureID();

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        shader.uploadShineValues(texture.getShineDamper(), texture.getReflectivity());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    private void unbindTerrain() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void prepareTerrainMatrix(Terrain terrain) {
        Matrix4f transformMatrix = MathUtil.createTransformMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(0, 0, 0), 1f
        );
        shader.uploadTransformMatrix(transformMatrix);
    }
}
