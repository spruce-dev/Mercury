package dev.spruce.abyss.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;

    private double scrollX, scrollY;
    private double posX, posY, lastX, lastY;
    private boolean mouseButtons[] = new boolean[3];
    private boolean dragging;

    private MouseListener(){
        this.scrollX = 0d;
        this.scrollY = 0f;
        this.posX = 0d;
        this.posY = 0d;
        this.lastX = 0d;
        this.lastY = 0d;
    }

    public static void mousePositionCallback(long window, double posX, double posY){
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = posX;
        get().posY = posY;
        get().dragging = get().mouseButtons[0] || get().mouseButtons[1] || get().mouseButtons[1];
    }

    public static void mouseButtonCallback(long window, int button, int action, int modifiers){
        if (button > get().mouseButtons.length)
            return;

        if (action == GLFW_PRESS) {
            get().mouseButtons[button] = true;
        } else if (action == GLFW_RELEASE) {
            get().mouseButtons[button] = false;
            get().dragging = false;
        }
    }

    public static void scrollCallback(long window, double offsetX, double offsetY){
        get().scrollX = offsetX;
        get().scrollY = offsetY;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    public boolean isButtonPressed(int button){
        if (button > mouseButtons.length)
            return false;
        return mouseButtons[button];
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public double getPosDeltaX(){
        return posX - lastX;
    }

    public double getPosDeltaY(){
        return posY - lastY;
    }

    public static MouseListener get(){
        if (instance == null){
            instance = new MouseListener();
        }
        return instance;
    }
}
