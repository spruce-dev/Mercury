package dev.spruce.mercury.entity;

import dev.spruce.mercury.entity.component.Component;
import dev.spruce.mercury.entity.component.ComponentHandler;
import dev.spruce.mercury.graphics.model.TexturedModel;
import org.joml.Vector3f;

public abstract class Entity {

    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    private ComponentHandler componentHandler;

    public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.componentHandler = new ComponentHandler(this);
        getAllComponents();
    }

    public void addComponent(Component component) {
        this.componentHandler.addComponent(component);
        component.start();
    }

    public void update(float delta) {
        this.componentHandler.run(delta);
    }

    public void stopComponents() {
        this.componentHandler.stop();
    }

    protected abstract void getAllComponents();

    protected Component getComponent(Class<?> classType){
        if (this.componentHandler.hasComponentOfType(classType)){
            return this.componentHandler.getComponentByType(classType);
        }else {
            throw new RuntimeException("Failed to get component of specified type for entity!");
        }
    }

    public void transformPosition(float dx, float dy, float dz) {
        this.position.add(dx, dy, dz);
    }

    public void transformRotation(float rx, float ry, float rz) {
        this.rotation.add(rx, ry, rz);
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
