package dev.spruce.mercury.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardInput {

    private static KeyboardInput instance;

    private boolean[] keys = new boolean[356];

    public static void keyCallback(long window, int key, int scancode, int action, int modifiers){
        if (key > get().keys.length)
            return;
        if (action == GLFW_PRESS){
            get().keys[key] = true;
        } else if (action == GLFW_RELEASE){
            get().keys[key] = false;
        }
    }

    public boolean isKeyDown(int key) {
        if (key > keys.length)
            return false;
        return keys[key];
    }

    public static KeyboardInput get(){
        if (instance == null){
            instance = new KeyboardInput();
        }
        return instance;
    }
}
