package dev.spruce.mercury.graphics.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class TextureLoader {

    private static TextureLoader intance;

    private List<Integer> textures = new ArrayList<>();

    public Texture loadTexture(String fileLocation) {
        int textureID = glGenTextures();
        this.textures.add(textureID);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(fileLocation, width, height, channels, 0);

        if (image == null)
            throw new RuntimeException("Failed to load image!");

        int colourValue = 0;
        switch (channels.get(0)){
            case 3 -> colourValue = GL_RGB;
            case 4 -> colourValue = GL_RGBA;
            default -> throw new RuntimeException("Failed to interpret image colour data!");
        }
        glTexImage2D(GL_TEXTURE_2D, 0, colourValue, width.get(0), height.get(0), 0, colourValue, GL_UNSIGNED_BYTE, image);
        stbi_image_free(image);

        GL30.glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -1);

        return new Texture(textureID);
    }

    public void free() {
        textures.forEach(GL30::glDeleteTextures);
    }

    public static TextureLoader get(){
        if (intance == null) {
            intance = new TextureLoader();
        }
        return intance;
    }
}
