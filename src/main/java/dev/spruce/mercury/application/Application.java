package dev.spruce.mercury.application;

import dev.spruce.mercury.manager.AbstractManager;
import dev.spruce.mercury.manager.MasterManager;

public abstract class Application {

    private final ApplicationConfig applicationConfig;
    private final MasterManager masterManager;

    public Application(ApplicationConfig config) {
        this.applicationConfig = config;
        this.masterManager = new MasterManager();
        addManagers();
    }

    public abstract void init();
    public abstract void render(float delta);
    public abstract void free();

    protected abstract void addManagers();

    protected void initManagers(AbstractManager... managers) {
        this.masterManager.addManagers(managers);
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public MasterManager getMasterManager() {
        return masterManager;
    }
}
