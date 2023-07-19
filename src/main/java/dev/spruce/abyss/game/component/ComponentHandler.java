package dev.spruce.abyss.game.component;

import dev.spruce.abyss.game.entity.Entity;

import java.util.concurrent.CopyOnWriteArrayList;

public class ComponentHandler {

    private CopyOnWriteArrayList<Component> components = new CopyOnWriteArrayList<>();
    private Entity parent;

    public ComponentHandler(Entity parent) {
        this.parent = parent;
    }

    public void start() {
        components.forEach(Component::start);
    }

    public void run(float delta) {
        components.forEach(component -> component.run(delta));
    }

    public void stop() {
        components.forEach(Component::stop);
    }

    public boolean hasComponentOfType(Class<?> classType) {
        for (Component component : components) {
            if (component.getClass().isInstance(classType)){
                return true;
            }
        }
        return false;
    }

    public Component getComponentByType(Class<?> classType) {
        for (Component component : components) {
            if (component.getClass().isInstance(classType)){
                return component;
            }
        }
        return null;
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    public void removeComponent(Component component) {
        this.components.remove(component);
    }

    public Entity getParent() {
        return parent;
    }
}
