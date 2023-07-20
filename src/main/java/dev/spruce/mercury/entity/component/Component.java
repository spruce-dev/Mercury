package dev.spruce.mercury.entity.component;

import dev.spruce.mercury.entity.Entity;

public abstract class Component {

    protected Entity parent;

    public Component(Entity parent) {
        this.parent = parent;
    }

    public abstract void start();
    public abstract void run(float delta);
    public abstract void stop();
}
