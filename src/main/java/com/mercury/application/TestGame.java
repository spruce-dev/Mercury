package com.mercury.application;

import com.mercury.application.game.entity.LocalPlayer;
import com.mercury.application.game.managers.CustomEntityManager;
import dev.spruce.mercury.application.Application;
import dev.spruce.mercury.application.ApplicationConfig;
import dev.spruce.mercury.entity.Light;
import com.mercury.application.game.terrain.Terrain;
import dev.spruce.mercury.graphics.camera.GameCamera;
import dev.spruce.mercury.graphics.model.ObjectFileLoader;
import dev.spruce.mercury.graphics.model.TexturedModel;
import dev.spruce.mercury.graphics.render.MasterRenderer;
import dev.spruce.mercury.graphics.texture.TextureLoader;
import org.joml.Vector3f;

public class TestGame extends Application {

    private CustomEntityManager entityManager;
    private MasterRenderer renderer;

    private Light lightSource;
    public GameCamera camera;

    private Terrain terrain;
    private Terrain terrain2;

    private LocalPlayer thePlayer;

    public TestGame(ApplicationConfig config) {
        super(config);
    }

    @Override
    protected void addManagers() {
        super.initManagers(entityManager);
    }

    @Override
    public void init() {
        terrain = new Terrain(0, 0, TextureLoader.get().loadTexture("assets/res/terrain_grass.jpg"));
        terrain2 = new Terrain(0, 1, TextureLoader.get().loadTexture("assets/res/terrain_grass.jpg"));

        lightSource = new Light(new Vector3f(3000f, 2000f, 2000f), new Vector3f(1, 1, 1));

        TexturedModel playerModel = new TexturedModel(ObjectFileLoader.loadFromObjectFile("assets/res/player.obj"),
                TextureLoader.get().loadTexture("assets/res/no_texture.png"));
        thePlayer = new LocalPlayer(playerModel, new Vector3f(-10, 10, -10), new Vector3f(0, 0, 0), 1f);

        camera = new GameCamera();
        camera.setPosition(new Vector3f(0, 10, 0));

        this.renderer = new MasterRenderer();
        this.entityManager = new CustomEntityManager(renderer);
        this.entityManager.addEntity(thePlayer);
    }

    @Override
    public void render(float delta) {
        renderer.processTerrain(terrain);
        renderer.processTerrain(terrain2);
        renderer.render(lightSource, camera);
    }

    @Override
    public void free() {
        renderer.free();
    }
}
