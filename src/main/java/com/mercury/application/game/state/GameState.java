package com.mercury.application.game.state;

import com.mercury.application.game.world.World;
import dev.spruce.mercury.state.AbstractState;

public class GameState extends AbstractState {

    private World world;

    public GameState() {
        super("Single-player.");
    }

    @Override
    public void init() {
        this.world = new World(2, 2);
        this.world.init();
        addManagers(world.getEntityManager());
    }

    @Override
    public void run(float delta) {
        this.world.update(delta);
    }

    @Override
    public void free() {
        this.world.free();
    }
}
