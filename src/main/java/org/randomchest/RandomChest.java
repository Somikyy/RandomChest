package org.randomchest;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class RandomChest extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("randomchest").setExecutor(new RDCommandExecutor(this));
        configManager = new ConfigManager(this);

        configManager.loadConfig();

        ConfigurationSection chestSection = configManager.getConfig().getConfigurationSection("general.Chests");
        if (chestSection == null) {
            chestSection = configManager.getConfig().createSection("general.Chests");
        }

        for (String chestName : chestSection.getKeys(false)) {
            String world = configManager.getConfig().getString("chests." + chestName + ".world");
            List<String> locations = configManager.getConfig().getStringList("chests." + chestName + ".Coordinated");
            int chance = configManager.getConfig().getInt("chests." + chestName + ".chest-spawn-chance");
            boolean refresh = configManager.getConfig().getBoolean("chests." + chestName + ".chest-refresh");
            int refreshTime = configManager.getConfig().getInt("chests." + chestName + ".chest-refresh-time");

            for (String location : locations) {
                String[] coords = location.split(", ");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);

                // Use these values to initialize your plugin
                // For example, you can create a new Chest object and add it to a list of chests
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
    return configManager;
    }
}
