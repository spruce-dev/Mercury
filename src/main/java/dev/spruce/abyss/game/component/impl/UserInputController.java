package dev.spruce.abyss.game.component.impl;

import dev.spruce.abyss.Window;
import dev.spruce.abyss.game.component.Component;
import dev.spruce.abyss.game.entity.Entity;
import dev.spruce.abyss.input.KeyboardInput;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class UserInputController extends Component {

    // TEMPORARY CONSTANTS
    // TODO make better way of calculating gravity using physics
    protected static final float BASE_SPEED = 20f;
    protected static final float BASE_ROTATE_SPEED = 160;
    protected static final float GRAVITY = -50;
    protected static final float JUMP_FACTOR = 30;

    // TODO replace with a way of using terrain heights to calculate position
    // TODO currently all terrains are locked to 0 height so this is oki for now :3
    private static final float TERRAIN_HEIGHT = 0;

    private float currentMovementSpeed = 0f;
    private float currentRotationSpeed = 0f;
    private float jumpMotion = 0f;

    private boolean inAir;
    private boolean onGround;

    public UserInputController(Entity parent) {
        super(parent);
    }

    @Override
    public void start() {

    }

    @Override
    public void run(float delta) {
        handleInput();

        this.parent.transformRotation(0, this.currentRotationSpeed * Window.getDeltaSeconds(), 0);
        float distance = this.currentMovementSpeed * Window.getDeltaSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(this.parent.getRotation().y)));
        float dz = (float) (distance * Math.cos(Math.toRadians(this.parent.getRotation().y)));
        this.parent.transformPosition(dx, 0, dz);

        this.jumpMotion += GRAVITY * Window.getDeltaSeconds();
        this.parent.transformPosition(0, this.jumpMotion * Window.getDeltaSeconds(), 0);
        if (this.parent.getPosition().y < TERRAIN_HEIGHT) {
            this.jumpMotion = 0f;
            this.parent.getPosition().y = 0f;
            setAirAndGroundBooleans(true);
        }
    }

    @Override
    public void stop() {

    }

    public void jump() {
        if (!isInAir()) {
            this.jumpMotion = JUMP_FACTOR;
            setAirAndGroundBooleans(false);
        }
    }

    private void handleInput() {
        if (KeyboardInput.get().isKeyDown(GLFW.GLFW_KEY_W)) {
            this.currentMovementSpeed = BASE_SPEED;
        } else if (KeyboardInput.get().isKeyDown(GLFW.GLFW_KEY_S)){
            this.currentMovementSpeed = -BASE_SPEED;
        } else {
            this.currentMovementSpeed = 0f;
        }
        if (KeyboardInput.get().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            this.currentRotationSpeed = BASE_ROTATE_SPEED;
        } else if (KeyboardInput.get().isKeyDown(GLFW.GLFW_KEY_RIGHT)){
            this.currentRotationSpeed = -BASE_ROTATE_SPEED;
        } else {
            this.currentRotationSpeed = 0f;
        }

        if (KeyboardInput.get().isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            jump();
        }
    }

    private void setAirAndGroundBooleans(boolean ground) {
        this.inAir = !ground;
        this.onGround = ground;
    }

    public float getJumpMotion() {
        return jumpMotion;
    }

    public boolean isInAir() {
        return inAir;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
