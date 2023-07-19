package dev.spruce.abyss.graphics.shader;

import dev.spruce.abyss.game.entity.Light;
import dev.spruce.abyss.graphics.camera.GameCamera;
import dev.spruce.abyss.utils.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StaticShader extends Shader {

    private int uniformTransformMatrix;
    private int uniformProjectionMatrix;
    private int uniformViewMatrix;

    private int uniformLightPosition;
    private int uniformLightColour;

    private int uniformShineDamper;
    private int uniformReflectivity;

    private int uniformSkyColour;

    public StaticShader(String vertexFileLocation, String fragmentFileLocation) {
        super(vertexFileLocation, fragmentFileLocation);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        this.uniformTransformMatrix = super.getUniformLocation("transformMatrix");
        this.uniformProjectionMatrix = super.getUniformLocation("projectionMatrix");
        this.uniformViewMatrix = super.getUniformLocation("viewMatrix");
        this.uniformLightPosition = super.getUniformLocation("lightPosition");
        this.uniformLightColour = super.getUniformLocation("lightColour");
        this.uniformReflectivity = super.getUniformLocation("reflectivity");
        this.uniformShineDamper = super.getUniformLocation("shineDamper");
        this.uniformSkyColour = super.getUniformLocation("skyColour");
    }

    public void uploadTransformMatrix(Matrix4f matrix) {
        super.uploadMatrix4f(uniformTransformMatrix, matrix);
    }

    public void uploadProjectionMatrix(Matrix4f matrix) {
        super.uploadMatrix4f(uniformProjectionMatrix, matrix);
    }

    public void uploadViewMatrix(GameCamera camera) {
        super.uploadMatrix4f(uniformViewMatrix, MathUtil.createViewMatrix(camera));
    }

    public void uploadLight(Light light) {
        super.uploadVector3f(uniformLightPosition, light.getPosition());
        super.uploadVector3f(uniformLightColour, light.getColour());
    }

    public void uploadShineValues(float shineDamper, float reflectivity) {
        super.uploadFloat(uniformShineDamper, shineDamper);
        super.uploadFloat(uniformReflectivity, reflectivity);
    }

    public void uploadSkyColour(Vector3f colour) {
        super.uploadVector3f(uniformSkyColour, colour);
    }
}
