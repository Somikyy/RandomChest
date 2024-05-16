package org.randomchest;

import org.bukkit.plugin.java.JavaPlugin;

public final class RandomChest extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("randomchest").setExecutor(new RDCommandExecutor());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
