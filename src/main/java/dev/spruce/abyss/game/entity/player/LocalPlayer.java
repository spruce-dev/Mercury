package dev.spruce.abyss.game.entity.player;

import dev.spruce.abyss.Window;
import dev.spruce.abyss.game.component.impl.UserInputController;
import dev.spruce.abyss.game.entity.Entity;
import dev.spruce.abyss.graphics.model.TexturedModel;
import dev.spruce.abyss.input.KeyboardInput;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class LocalPlayer extends Entity {

    public LocalPlayer(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        addComponent(new UserInputController(this));
    }

    @Override
    protected void getAllComponents() {}
}
