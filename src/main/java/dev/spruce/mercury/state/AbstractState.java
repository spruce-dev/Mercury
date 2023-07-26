package dev.spruce.mercury.state;

import dev.spruce.mercury.manager.AbstractManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractState {

    private String name;

    private List<AbstractManager> managers = new ArrayList<>();

    public AbstractState(String name) {
        this.name = name;
    }

    public void addManagers(AbstractManager... managers) {
        this.managers.addAll(Arrays.stream(managers).toList());
    }

    public abstract void init();
    public abstract void run(float delta);
    public abstract void free();

    public String getName() {
        return name;
    }

    public List<AbstractManager> getManagers() {
        return managers;
    }
}
