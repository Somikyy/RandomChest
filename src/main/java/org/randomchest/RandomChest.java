package org.randomchest;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class RandomChest extends JavaPlugin {
    private ConfigManager configManager;
    private ChestRefresher chestRefresher;

    @Override
    public void onEnable() {

        // Plugin startup logic
        this.getCommand("randomchest").setExecutor(new RDCommandExecutor(this));
        this.getCommand("refreshchest").setExecutor(new RefreshChestCommand(this));

        getServer().getPluginManager().registerEvents(new ChestBreakListener(this), this);

        configManager = new ConfigManager(this);

        configManager.loadConfig();

        ConfigurationSection chestSection = configManager.getConfig().getConfigurationSection("general.Chests");
        if (chestSection == null) {
            chestSection = configManager.getConfig().createSection("general.Chests");
        }

        for (String chestName : chestSection.getKeys(false)) {
            String world = configManager.getConfig().getString("general.Chests." + chestName + ".World");
            List<String> locations = configManager.getConfig().getStringList("general.Chests." + chestName + ".Coordinated");
            int chance = configManager.getConfig().getInt("general.Chests." + chestName + ".chest-spawn-chance");
            boolean refresh = configManager.getConfig().getBoolean("general.Chests." + chestName + ".chest-refresh");
            int refreshTime = configManager.getConfig().getInt("general.Chests." + chestName + ".chest-refresh-time");
            System.out.println("Refresh time for chest " + chestName + ": " + refreshTime);

            for (String location : locations) {
                String[] coords = location.split(", ");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);
            }
        }

        chestRefresher = new ChestRefresher(this);
        chestRefresher.runTaskTimer(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPluginConfig() {
        this.reloadConfig();
        configManager.reloadConfig();
    }

    public ConfigManager getConfigManager() {
    return configManager;
    }
    public ChestRefresher getChestRefresher() {
        return chestRefresher;
    }
}
