package dev.spruce.mercury.manager;

public abstract class AbstractManager {
    public abstract void init();
    public abstract void run(float delta);
    public abstract void free();
}
