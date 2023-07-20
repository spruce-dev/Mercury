package dev.spruce.mercury.graphics.camera;

import dev.spruce.mercury.utils.MathUtil;
import org.joml.Vector3f;

public abstract class GameCamera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float yaw = 0;
    private float pitch = 20;
    private float roll = 0;

    public abstract void move();

    public void translate(float x, float y, float z) {
        translate(MathUtil.toVector3f(x, y, z));
    }

    public void translate(Vector3f translation) {
        translation.rotateY((float) Math.toRadians(-yaw));
        position.x += translation.x;
        position.y += translation.y;
        position.z += translation.z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
