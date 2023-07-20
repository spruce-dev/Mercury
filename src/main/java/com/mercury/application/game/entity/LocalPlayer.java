package com.mercury.application.game.entity;

import com.mercury.application.game.components.UserInputController;
import dev.spruce.mercury.entity.Entity;
import dev.spruce.mercury.graphics.model.TexturedModel;
import org.joml.Vector3f;

public class LocalPlayer extends Entity {

    public LocalPlayer(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        addComponent(new UserInputController(this));
    }

    @Override
    protected void getAllComponents() {}
}
