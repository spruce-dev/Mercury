package dev.spruce.abyss.graphics.render;

import dev.spruce.abyss.Window;
import dev.spruce.abyss.game.entity.Entity;
import dev.spruce.abyss.game.entity.Light;
import dev.spruce.abyss.game.terrain.Terrain;
import dev.spruce.abyss.graphics.camera.GameCamera;
import dev.spruce.abyss.graphics.model.TexturedModel;
import dev.spruce.abyss.graphics.shader.StaticShader;
import dev.spruce.abyss.graphics.shader.TerrainShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class MasterRenderer {

    private static final float FOV = 90.0f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Vector3f skyColour = new Vector3f(1.0f, 1.0f, 1.0f);

    //Entity Rendering
    private StaticShader shader;
    private EntityRenderer entityRenderer;

    //Terrain Rendering
    private TerrainShader terrainShader;
    private TerrainRenderer terrainRenderer;

    private Matrix4f projectionMatrix;

    private HashMap<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        createProjectionMatrix();

        shader = new StaticShader("assets/shaders/default.vert", "assets/shaders/default.frag");
        entityRenderer = new EntityRenderer(shader, projectionMatrix);

        terrainShader = new TerrainShader();
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void prepare() {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(skyColour.x, skyColour.y, skyColour.z, 1);
    }

    public void render(Light lightSource, GameCamera camera) {
        prepare();
        shader.begin();
        shader.uploadLight(lightSource);
        shader.uploadViewMatrix(camera);
        shader.uploadSkyColour(skyColour);
        entityRenderer.render(entities);
        shader.end();

        terrainShader.begin();
        terrainShader.uploadLight(lightSource);
        terrainShader.uploadViewMatrix(camera);
        terrainShader.uploadSkyColour(skyColour);
        terrainRenderer.render(terrains);
        terrainShader.end();

        entities.clear();
        terrains.clear();
    }

    public void processTerrain(Terrain terrain) {
        this.terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel model = entity.getModel();
        List<Entity> batch = entities.get(model);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newEntityBatch = new ArrayList<>();
            newEntityBatch.add(entity);
            entities.put(model, newEntityBatch);
        }
    }

    private void createProjectionMatrix() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(Window.get().getGlfwWindow(), w, h);
        float width = w.get(0);
        float height = h.get(0);
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), width / height, NEAR_PLANE, FAR_PLANE);
    }

    public void free() {
        shader.free();
        terrainShader.free();
    }
}
