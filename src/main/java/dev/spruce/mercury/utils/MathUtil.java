package dev.spruce.mercury.utils;

import dev.spruce.mercury.graphics.camera.GameCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MathUtil {

    public static Matrix4f createTransformMatrix(Vector3f translation, Vector3f rotation, float scale) {
        return new Matrix4f()
                .identity()
                .translate(translation)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
    }

    public static Matrix4f createViewMatrix(GameCamera camera){
        return new Matrix4f()
                .identity()
                .rotateX((float) Math.toRadians(camera.getPitch()))
                .rotateY((float) Math.toRadians(camera.getYaw()))
                .translate(new Vector3f(camera.getPosition()).negate());
    }

    public static Vector3f toVector3f(float rx, float ry, float rz) {
        return new Vector3f(rx, ry, rz);
    }
}
