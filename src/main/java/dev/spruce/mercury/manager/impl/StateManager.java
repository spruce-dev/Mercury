package dev.spruce.mercury.manager.impl;

import dev.spruce.mercury.manager.AbstractManager;
import dev.spruce.mercury.state.AbstractState;

public class StateManager extends AbstractManager {

    private AbstractState currentState;

    public StateManager(AbstractState currentState) {
        this.currentState = currentState;
    }

    @Override
    public void init() {
        this.currentState.init();
    }

    @Override
    public void run(float delta) {
        if (this.currentState == null)
            return;
        this.currentState.run(delta);
    }

    @Override
    public void free() {
        this.currentState.free();
    }

    public AbstractState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(AbstractState currentState) {
        this.currentState.free();
        this.currentState = currentState;
        currentState.init();
    }
}
