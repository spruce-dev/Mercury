package com.mercury.application.game.world;

import com.mercury.application.game.entity.LocalPlayer;
import com.mercury.application.game.managers.CustomEntityManager;
import com.mercury.application.game.terrain.Terrain;
import com.mercury.application.game.terrain.TerrainGenerator;
import dev.spruce.mercury.application.Application;
import dev.spruce.mercury.entity.Light;
import dev.spruce.mercury.graphics.camera.ThirdPersonCamera;
import dev.spruce.mercury.graphics.model.ObjectFileLoader;
import dev.spruce.mercury.graphics.model.TexturedModel;
import dev.spruce.mercury.graphics.render.MasterRenderer;
import dev.spruce.mercury.graphics.texture.Texture;
import dev.spruce.mercury.graphics.texture.TextureLoader;
import dev.spruce.mercury.utils.Noise;
import org.joml.Vector3f;

public class World {

    private final int gridWidth;
    private final int gridHeight;

    private CustomEntityManager entityManager;
    private MasterRenderer renderer;

    private Light lightSource;
    public ThirdPersonCamera camera;

    private Terrain[][] terrains;

    private LocalPlayer thePlayer;

    public World(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    public void init() {
        Noise noise = new Noise(3, 5f, 2f, 5748932);

        TerrainGenerator generator = new TerrainGenerator(2, 2, noise);
        this.terrains = generator.generateTerrain();

        lightSource = new Light(new Vector3f(3000f, 2000f, 2000f), new Vector3f(1, 1, 1));

        Texture playerTexture = TextureLoader.get().loadTexture("assets/res/no_texture.png");
        TexturedModel playerModel = new TexturedModel(ObjectFileLoader.loadFromObjectFile("assets/res/player.obj"),
                playerTexture);
        thePlayer = new LocalPlayer(terrains[0][0], playerModel, new Vector3f(10, 10, 10), new Vector3f(0, 0, 0), 1f);

        camera = new ThirdPersonCamera(thePlayer);
        //camera.setPosition(new Vector3f(0, 10, 0));

        this.renderer = new MasterRenderer();
        this.entityManager = new CustomEntityManager(renderer);
        this.entityManager.addEntity(thePlayer);
    }

    public void update(float delta) {
        int gridX = (int) (thePlayer.getPosition().x / Terrain.SIZE);
        int gridZ = (int) (thePlayer.getPosition().z / Terrain.SIZE);
        this.thePlayer.setCurrentTerrain(this.terrains[gridX][gridZ]);

        camera.move();
        for (int z = 0; z < gridHeight; z++) {
            for (int x = 0; x < gridWidth; x++) {
                renderer.processTerrain(this.terrains[x][z]);
            }
        }
        renderer.render(lightSource, camera);
    }

    public void free() {
        renderer.free();
    }

    public CustomEntityManager getEntityManager() {
        return entityManager;
    }
}
