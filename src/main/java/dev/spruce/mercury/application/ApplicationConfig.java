package dev.spruce.mercury.application;

import dev.spruce.mercury.EngineVersion;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;

public class ApplicationConfig {

    private String title = EngineVersion.getNameVersion();
    private int width = 1280;
    private int height = 720;
    private int resizable = GLFW_TRUE;
    private int startMaximized = GLFW_TRUE;
    private int vsync = GLFW_FALSE;

    public ApplicationConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public ApplicationConfig setDisplaySize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ApplicationConfig setResizable(boolean value) {
        this.resizable = value ? GLFW_TRUE : GLFW_FALSE;
        return this;
    }

    public ApplicationConfig setStartMaximized(boolean value) {
        this.startMaximized = value ? GLFW_TRUE : GLFW_FALSE;
        return this;
    }

    public ApplicationConfig enableVSync() {
        this.vsync = GLFW_TRUE;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getResizable() {
        return resizable;
    }

    public int getStartMaximized() {
        return startMaximized;
    }

    public int getVsync() {
        return vsync;
    }
}
