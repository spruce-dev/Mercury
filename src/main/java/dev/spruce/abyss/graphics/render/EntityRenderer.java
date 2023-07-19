package dev.spruce.abyss.graphics.render;

import dev.spruce.abyss.Window;
import dev.spruce.abyss.game.entity.Entity;
import dev.spruce.abyss.graphics.model.Model;
import dev.spruce.abyss.graphics.model.TexturedModel;
import dev.spruce.abyss.graphics.shader.StaticShader;
import dev.spruce.abyss.graphics.texture.Texture;
import dev.spruce.abyss.utils.MathUtil;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.begin();
        shader.uploadProjectionMatrix(projectionMatrix);
        shader.end();
    }

    public void render(HashMap<TexturedModel, List<Entity>> entityMap) {
        for (TexturedModel model : entityMap.keySet()) {
            prepareModel(model);
            List<Entity> entityBatch = entityMap.get(model);
            for (Entity entity : entityBatch) {
                prepareEntity(entity);
                glDrawElements(GL_TRIANGLES, model.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindModel();
        }
    }

    private void prepareModel(TexturedModel model) {
        Texture texture = model.getTexture();
        int vao = model.getModel().getVao();
        int textureID = texture.getTextureID();

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        shader.uploadShineValues(texture.getShineDamper(), texture.getReflectivity());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    private void unbindModel() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void prepareEntity(Entity entity) {
        Matrix4f transformMatrix = MathUtil.createTransformMatrix(
                entity.getPosition(), entity.getRotation(), entity.getScale()
        );
        shader.uploadTransformMatrix(transformMatrix);
    }
}
