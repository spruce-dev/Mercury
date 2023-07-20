package dev.spruce.mercury;

import dev.spruce.mercury.application.Application;
import dev.spruce.mercury.application.ApplicationConfig;
import dev.spruce.mercury.graphics.model.ModelLoader;
import dev.spruce.mercury.graphics.texture.TextureLoader;
import dev.spruce.mercury.input.KeyboardInput;
import dev.spruce.mercury.input.MouseListener;
import dev.spruce.mercury.utils.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private Application application;

    private static long glfwWindow;

    private static long lastFrameTime;
    private static float deltaTime;

    public Window(Application application){
        this.application = application;
    }

    private void init(){
        ApplicationConfig programConfig = this.application.getApplicationConfig();
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialize GLFW!");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, programConfig.getResizable());
        glfwWindowHint(GLFW_MAXIMIZED, programConfig.getStartMaximized());

        glfwWindow = glfwCreateWindow(programConfig.getWidth(), programConfig.getHeight(), programConfig.getTitle(), NULL, NULL);
        if (glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::scrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyboardInput::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(programConfig.getVsync());

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        application.init();
        application.getMasterManager().init();
    }

    private void runLoop(){
        lastFrameTime = Time.getTime();
        while (!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            application.getMasterManager().run(getDeltaSeconds());
            application.render(getDeltaSeconds());

            glfwSwapBuffers(glfwWindow);

            long currentFrameTime = Time.getTime();
            deltaTime = currentFrameTime - lastFrameTime;
            lastFrameTime = Time.getTime();
        }
    }

    private void destroy(){
        application.getMasterManager().free();
        application.free();
        ModelLoader.get().free();
        TextureLoader.get().free();
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void run(){
        printEngineStartInfo();
        init();
        runLoop();
        destroy();
    }

    private void printEngineStartInfo() {
        String info = String.format("Mercury build %s running on LWJGL version %s!", EngineVersion.BUILD, Version.getVersion());
        System.out.println(info);
    }

    public static float getDeltaSeconds() {
        return (float) (deltaTime / 1000f);
    }

    public static long getGlfwWindow() {
        return glfwWindow;
    }
}
