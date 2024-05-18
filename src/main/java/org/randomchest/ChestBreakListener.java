package org.randomchest;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class ChestBreakListener implements Listener {
    private final RandomChest plugin;

    public ChestBreakListener(RandomChest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // Check if the block is a chest, the player has the required permission, and the player is in creative mode
        if (block.getType() == Material.CHEST && player.hasPermission("randomchest.delete") && player.getGameMode() == GameMode.CREATIVE) {
            Chest chest = (Chest) block.getState();
            Location chestLocation = chest.getLocation();

            ConfigurationSection chestSection = plugin.getConfigManager().getConfig().getConfigurationSection("general.Chests");
            if (chestSection != null) {
                for (String chestName : chestSection.getKeys(false)) {
                    List<String> locations = plugin.getConfigManager().getConfig().getStringList("general.Chests." + chestName + ".Coordinated");

                    // Check if the chest is one of the chests declared in the config
                    for (String location : locations) {
                        String[] coords = location.split(", ");
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        int z = Integer.parseInt(coords[2]);

                        if (chestLocation.getBlockX() == x && chestLocation.getBlockY() == y && chestLocation.getBlockZ() == z) {
                            // Remove the chest from the config
                            locations.remove(location);
                            plugin.getConfigManager().getConfig().set("general.Chests." + chestName + ".Coordinated", locations);
                            plugin.getConfigManager().saveConfig();

                            // Send a message to the player
                            player.sendMessage(ChatColor.RED + "You deleted " + chestName + "  at coordinates " + x + ", " + y + ", " + z);

                            for (Entity entity : chestLocation.getChunk().getEntities()) {
                                if (entity instanceof ArmorStand && entity.getLocation().distance(chestLocation) < 1) {
                                    entity.remove();
                                    break;
                                }
                            }

                            return;

                        }
                    }

                }
            }
        }
    }
}