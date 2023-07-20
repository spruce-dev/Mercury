package dev.spruce.mercury.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MasterManager extends AbstractManager {

    private final List<AbstractManager> managers = new ArrayList<>();

    public void addManagers(AbstractManager... abstractManagers) {
        this.managers.addAll(Arrays.stream(abstractManagers).collect(Collectors.toList()));
    }

    public void addManager(AbstractManager manager) {
        this.managers.add(manager);
    }

    @Override
    public void init() {
        managers.forEach(AbstractManager::init);
    }

    @Override
    public void run(float delta) {
        managers.forEach(abstractManager -> abstractManager.run(delta));
    }

    @Override
    public void free() {
        managers.forEach(AbstractManager::free);
    }
}
