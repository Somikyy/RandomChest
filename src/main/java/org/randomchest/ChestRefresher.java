package org.randomchest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ChestRefresher extends BukkitRunnable {
    private final RandomChest plugin;

    public ChestRefresher(RandomChest plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ConfigurationSection chestSection = plugin.getConfigManager().getConfig().getConfigurationSection("general.Chests");
        if (chestSection != null) {
            for (String chestName : chestSection.getKeys(false)) {
                boolean refresh = plugin.getConfigManager().getConfig().getBoolean("general.Chests." + chestName + ".chest-refresh");
                if (refresh) {
                    int refreshTime = plugin.getConfigManager().getConfig().getInt("general.Chests." + chestName + ".chest-refresh-time");
                    // Convert refresh time from minutes to seconds
                    refreshTime *= 60;
                    // If the chest should be refreshed now
                    if ((System.currentTimeMillis() / 1000) % refreshTime == 0) {
                        // Refresh the chest
                        refreshChest(chestName);
                    }
                }
            }
        }
    }

    void refreshChest(String chestName) {
        String worldName = plugin.getConfigManager().getConfig().getString("general.Chests." + chestName + ".World");
        List<String> locations = plugin.getConfigManager().getConfig().getStringList("general.Chests." + chestName + ".Coordinated");

        for (String location : locations) {
            String[] coords = location.split(", ");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int z = Integer.parseInt(coords[2]);

            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location chestLocation = new Location(world, x, y, z);
                BlockState blockState = world.getBlockAt(chestLocation).getState();
                if (blockState instanceof Chest) {
                    Chest chest = (Chest) blockState;
                    chest.getInventory().clear();  // Clear the chest

                    // Refill the chest
                    ConfigurationSection itemsSection = plugin.getConfigManager().getConfig().getConfigurationSection("general.Chests." + chestName + ".Items");
                    if (itemsSection != null) {
                        for (String itemName : itemsSection.getKeys(false)) {
                            Material material = Material.valueOf(plugin.getConfigManager().getConfig().getString("general.Chests." + chestName + ".Items." + itemName + ".Material"));
                            int minAmount = plugin.getConfigManager().getConfig().getInt("general.Chests." + chestName + ".Items." + itemName + ".Amount.min");
                            int maxAmount = plugin.getConfigManager().getConfig().getInt("general.Chests." + chestName + ".Items." + itemName + ".Amount.max");
                            int amount = (int) (Math.random() * (maxAmount - minAmount + 1)) + minAmount;  // Random amount between min and max

                            ItemStack item = new ItemStack(material, amount);

                            // Add the item to a random slot in the chest
                            int slot = (int) (Math.random() * chest.getInventory().getSize());
                            chest.getInventory().setItem(slot, item);

                        }
                    }
                } else {
                    createChest(chestLocation);
                }
            }
        }
    }

    private void createChest(Location location) {
        location.getBlock().setType(Material.CHEST);
    }
}