package dev.spruce.abyss.graphics.model;

import dev.spruce.abyss.graphics.texture.Texture;

public class TexturedModel {

    private Model model;
    private Texture texture;

    public TexturedModel(Model model, Texture texture) {
        this.model = model;
        this.texture = texture;
    }

    public Model getModel() {
        return model;
    }

    public Texture getTexture() {
        return texture;
    }
}
