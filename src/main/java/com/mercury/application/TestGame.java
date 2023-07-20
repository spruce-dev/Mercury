package com.mercury.application;

import com.mercury.application.game.entity.LocalPlayer;
import com.mercury.application.game.managers.CustomEntityManager;
import dev.spruce.mercury.application.Application;
import dev.spruce.mercury.application.ApplicationConfig;
import dev.spruce.mercury.entity.Light;
import com.mercury.application.game.terrain.Terrain;
import dev.spruce.mercury.graphics.camera.GameCamera;
import dev.spruce.mercury.graphics.camera.ThirdPersonCamera;
import dev.spruce.mercury.graphics.model.ObjectFileLoader;
import dev.spruce.mercury.graphics.model.TexturedModel;
import dev.spruce.mercury.graphics.render.MasterRenderer;
import dev.spruce.mercury.graphics.texture.Texture;
import dev.spruce.mercury.graphics.texture.TextureLoader;
import org.joml.Vector3f;

public class TestGame extends Application {

    private CustomEntityManager entityManager;
    private MasterRenderer renderer;

    private Light lightSource;
    public ThirdPersonCamera camera;

    private Terrain terrain;
    private Terrain terrain2;
    private Terrain terrain3;
    private Terrain terrain4;

    private LocalPlayer thePlayer;

    public TestGame(ApplicationConfig config) {
        super(config);
    }

    @Override
    public void addManagers() {
        super.initManagers(entityManager);
    }

    @Override
    public void init() {
        Texture terrainTexture = TextureLoader.get().loadTexture("assets/res/terrain_grass.jpg");
        terrain = new Terrain(0, 0, terrainTexture);
        terrain2 = new Terrain(-1, 0, terrainTexture);
        terrain3 = new Terrain(0, -1, terrainTexture);
        terrain4 = new Terrain(-1, -1, terrainTexture);

        lightSource = new Light(new Vector3f(3000f, 2000f, 2000f), new Vector3f(1, 1, 1));

        Texture playerTexture = TextureLoader.get().loadTexture("assets/res/no_texture.png");
        TexturedModel playerModel = new TexturedModel(ObjectFileLoader.loadFromObjectFile("assets/res/player.obj"),
                playerTexture);
        thePlayer = new LocalPlayer(playerModel, new Vector3f(-10, 10, -10), new Vector3f(0, 0, 0), 1f);

        camera = new ThirdPersonCamera(thePlayer);
        //camera.setPosition(new Vector3f(0, 10, 0));

        this.renderer = new MasterRenderer();
        this.entityManager = new CustomEntityManager(renderer);
        this.entityManager.addEntity(thePlayer);
    }

    @Override
    public void render(float delta) {
        camera.move();
        renderer.processTerrain(terrain);
        renderer.processTerrain(terrain2);
        renderer.processTerrain(terrain3);
        renderer.processTerrain(terrain4);
        renderer.render(lightSource, camera);
    }

    @Override
    public void free() {
        renderer.free();
    }
}
