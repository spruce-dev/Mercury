package dev.spruce.mercury.manager.impl;

import dev.spruce.mercury.entity.Entity;
import dev.spruce.mercury.manager.AbstractManager;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractEntityManager extends AbstractManager {

    private final CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();

    @Override
    public abstract void init();
    @Override
    public abstract void run(float delta);
    @Override
    public abstract void free();

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    private void removeEntity(Entity entity) {
        this.entities.remove(entity);
        entity.stopComponents();
    }

    public CopyOnWriteArrayList<Entity> getEntities() {
        return entities;
    }
}
