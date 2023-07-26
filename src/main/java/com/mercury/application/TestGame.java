package com.mercury.application;

import com.mercury.application.game.entity.LocalPlayer;
import com.mercury.application.game.managers.CustomEntityManager;
import com.mercury.application.game.state.GameState;
import com.mercury.application.game.world.World;
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
import dev.spruce.mercury.manager.impl.StateManager;
import dev.spruce.mercury.utils.Noise;
import org.joml.Vector3f;

public class TestGame extends Application {

    private static StateManager stateManager;

    public TestGame(ApplicationConfig config) {
        super(config);
        stateManager = new StateManager(new GameState());
    }

    @Override
    public void addManagers() {
        super.initManagers(stateManager.getCurrentState().getManagers());
    }

    @Override
    public void init() {
        stateManager.init();
    }

    @Override
    public void render(float delta) {
        stateManager.run(delta);
    }

    @Override
    public void free() {
        stateManager.free();
    }
}
