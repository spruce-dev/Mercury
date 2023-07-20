package dev.spruce.mercury.graphics.camera;

import com.mercury.application.game.entity.LocalPlayer;
import dev.spruce.mercury.input.MouseListener;
import org.lwjgl.glfw.GLFW;

public class ThirdPersonCamera extends GameCamera {

    private LocalPlayer player;

    private float zoom = 12f;
    private float rotationRoundPlayer = 0f;

    public ThirdPersonCamera(LocalPlayer player) {
        this.player = player;
    }

    @Override
    public void move() {
        handleInput();
        translatePosition(calculateHorizontalDistance(), calculateVerticalDistance());
        setYaw(180 - (player.getRotation().y + rotationRoundPlayer));
    }

    private void handleInput() {
        float zoomLevel = (float) (MouseListener.get().getScrollY() * 0.8f);
        this.zoom -= zoomLevel;

        if (MouseListener.get().isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
            float pitchChange = (float) (MouseListener.get().getPosDeltaY() * 0.1f);
            setPitch(getPitch() + pitchChange);
            float rotationChange = (float) (MouseListener.get().getPosDeltaX() * 0.3f);
            this.rotationRoundPlayer -= rotationChange;
        }
    }

    private float calculateHorizontalDistance() {
        return (float) (zoom * Math.cos(Math.toRadians(getPitch())));
    }

    private float calculateVerticalDistance() {
        return (float) (zoom * Math.sin(Math.toRadians(getPitch())));
    }

    private void translatePosition(float horizontal, float vertical) {
        float theta = player.getRotation().y + rotationRoundPlayer;
        float offsetX = (float) (horizontal * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontal * Math.cos(Math.toRadians(theta)));
        getPosition().x = player.getPosition().x - offsetX;
        getPosition().z = player.getPosition().z - offsetZ;
        getPosition().y = player.getPosition().y + vertical;
    }
}
