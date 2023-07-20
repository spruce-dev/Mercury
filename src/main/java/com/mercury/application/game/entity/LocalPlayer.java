package com.mercury.application.game.entity;

import com.mercury.application.game.components.UserInputController;
import com.mercury.application.game.terrain.Terrain;
import dev.spruce.mercury.entity.Entity;
import dev.spruce.mercury.graphics.model.TexturedModel;
import org.joml.Vector3f;

public class LocalPlayer extends Entity {

    private Terrain currentTerrain;
    private UserInputController playerController;

    public LocalPlayer(Terrain currentTerrain, TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        this.currentTerrain = currentTerrain;
        addComponent(playerController = new UserInputController(this, currentTerrain));
    }

    @Override
    protected void getAllComponents() {}

    @Override
    public void update(float delta) {
        super.update(delta);
        this.playerController.setCurrentTerrain(this.currentTerrain);
    }

    public void setCurrentTerrain(Terrain currentTerrain) {
        this.currentTerrain = currentTerrain;
    }
}
