package com.mercury.application.game.components;

import com.mercury.application.game.terrain.Terrain;
import dev.spruce.mercury.Window;
import dev.spruce.mercury.entity.component.Component;
import dev.spruce.mercury.entity.Entity;
import dev.spruce.mercury.input.KeyboardInput;
import org.lwjgl.glfw.GLFW;

public class UserInputController extends Component {

    // TEMPORARY CONSTANTS
    // TODO make better way of calculating gravity using physics
    protected static final float BASE_SPEED = 60f;
    protected static final float BASE_ROTATE_SPEED = 160;
    protected static final float GRAVITY = -50;
    protected static final float JUMP_FACTOR = 30;

    private float currentMovementSpeed = 0f;
    private float currentRotationSpeed = 0f;
    private float jumpMotion = 0f;

    private boolean inAir;
    private boolean onGround;

    private Terrain currentTerrain;

    public UserInputController(Entity parent, Terrain currentTerrain) {
        super(parent);
        this.currentTerrain = currentTerrain;
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
        float terrainHeightBelow = currentTerrain.getHeightAtPosition((int) parent.getPosition().x, (int) parent.getPosition().z);
        if (this.parent.getPosition().y < terrainHeightBelow) {
            this.jumpMotion = 0f;
            this.parent.getPosition().y = terrainHeightBelow;
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
        if (KeyboardInput.get().isKeyDown(GLFW.GLFW_KEY_A)) {
            this.currentRotationSpeed = BASE_ROTATE_SPEED;
        } else if (KeyboardInput.get().isKeyDown(GLFW.GLFW_KEY_D)){
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

    public Terrain getCurrentTerrain() {
        return currentTerrain;
    }

    public void setCurrentTerrain(Terrain currentTerrain) {
        this.currentTerrain = currentTerrain;
    }
}
