package com.mercury.application.game.managers;

import dev.spruce.mercury.entity.Entity;
import dev.spruce.mercury.graphics.render.EntityRenderer;
import dev.spruce.mercury.graphics.render.MasterRenderer;
import dev.spruce.mercury.manager.MasterManager;
import dev.spruce.mercury.manager.impl.AbstractEntityManager;

public class CustomEntityManager extends AbstractEntityManager {

    private MasterRenderer renderInstance;

    public CustomEntityManager(MasterRenderer renderInstance) {
        this.renderInstance = renderInstance;
    }

    @Override
    public void init() {

    }

    @Override
    public void run(float delta) {
        for (Entity entity : getEntities()) {
            entity.update(delta);
            renderInstance.processEntity(entity);
        }
    }

    @Override
    public void free() {
        getEntities().forEach(Entity::stopComponents);
    }
}
