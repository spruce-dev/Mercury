package dev.spruce.abyss;

import dev.spruce.abyss.game.entity.Entity;
import dev.spruce.abyss.game.entity.Light;
import dev.spruce.abyss.game.entity.player.LocalPlayer;
import dev.spruce.abyss.game.terrain.Terrain;
import dev.spruce.abyss.graphics.render.MasterRenderer;
import dev.spruce.abyss.graphics.camera.GameCamera;
import dev.spruce.abyss.graphics.model.Model;
import dev.spruce.abyss.graphics.model.ModelLoader;
import dev.spruce.abyss.graphics.model.ObjectFileLoader;
import dev.spruce.abyss.graphics.model.TexturedModel;
import dev.spruce.abyss.graphics.shader.StaticShader;
import dev.spruce.abyss.graphics.texture.Texture;
import dev.spruce.abyss.graphics.texture.TextureLoader;
import dev.spruce.abyss.input.KeyboardInput;
import dev.spruce.abyss.input.MouseListener;
import dev.spruce.abyss.utils.Time;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static Window instance;

    private int width, height;
    private String title;

    private long glfwWindow;

    private static long lastFrameTime;
    private static float deltaTime;

    private Light lightSource;
    public GameCamera camera;
    private MasterRenderer renderer;

    private Terrain terrain;
    private Terrain terrain2;

    private LocalPlayer thePlayer;

    private Window(){
        this.width = 1600;
        this.height = 900;
        this.title = "Abyss Game Engine";
    }

    private void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialize GLFW!");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::scrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyboardInput::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(GLFW_TRUE);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        //Test terrain
        terrain = new Terrain(0, 0, TextureLoader.get().loadTexture("assets/res/terrain_grass.jpg"));
        terrain2 = new Terrain(0, 1, TextureLoader.get().loadTexture("assets/res/terrain_grass.jpg"));

        lightSource = new Light(new Vector3f(3000f, 2000f, 2000f), new Vector3f(1, 1, 1));

        TexturedModel playerModel = new TexturedModel(ObjectFileLoader.loadFromObjectFile("assets/res/player.obj"),
                TextureLoader.get().loadTexture("assets/res/no_texture.png"));
        thePlayer = new LocalPlayer(playerModel, new Vector3f(-10, 10, -10), new Vector3f(0, 0, 0), 1f);

        camera = new GameCamera();
        camera.setPosition(new Vector3f(0, 10, 0));

        renderer = new MasterRenderer();
    }

    private void runLoop(){
        lastFrameTime = Time.getTime();
        while (!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            thePlayer.update(getDeltaSeconds());

            renderer.processEntity(thePlayer);

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);

            renderer.render(lightSource, camera);

            glfwSwapBuffers(glfwWindow);

            long currentFrameTime = Time.getTime();
            deltaTime = currentFrameTime - lastFrameTime;
            lastFrameTime = Time.getTime();
        }
    }

    private void destroy(){
        thePlayer.stopComponents();
        renderer.free();
        ModelLoader.get().free();
        TextureLoader.get().free();
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void run(){
        System.out.println("Running LWJGL Version: " + Version.getVersion());
        init();
        runLoop();
        destroy();
    }

    public static float getDeltaSeconds() {
        return (float) (deltaTime / 1000f);
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public static Window get(){
        if (instance == null){
            instance = new Window();
        }
        return instance;
    }
}
