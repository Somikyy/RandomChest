package org.randomchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ChestDisplayManager {
    private final RandomChest plugin;

    public ChestDisplayManager(RandomChest plugin) {
        this.plugin = plugin;
    }

    public void displayRefreshTime(String chestName) {
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
                final int refreshTime = plugin.getConfigManager().getConfig().getInt("general.Chests." + chestName + ".chest-refresh-time");

                final AtomicReference<ArmorStand> armorStandRef = new AtomicReference<>();
                for (Entity entity : chestLocation.getChunk().getEntities()) {
                    if (entity instanceof ArmorStand && entity.getLocation().distance(chestLocation) < 1) {
                        entity.remove();  // Remove the ArmorStand
                    }
                }

                    // If no ArmorStand exists, create a new one
                if (armorStandRef.get() == null) {
                    ArmorStand armorStand = (ArmorStand) world.spawnEntity(chestLocation.add(0.5, 0.1, 0.5), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);  // Make the ArmorStand invisible
                    armorStand.setGravity(false);  // Prevent the ArmorStand from falling
                    armorStand.setCustomNameVisible(true);  // Make the custom name visible
                    armorStand.setCanMove(false);  // Prevent the ArmorStand from moving
                    armorStand.setSmall(true); // Make the ArmorStand small
                    armorStandRef.set(armorStand);
                }

                    // Update the display every second
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ArmorStand armorStand = armorStandRef.get();
                            if (!armorStand.isDead()) {
                                long currentTime = System.currentTimeMillis() / 1000;
                                long timeLeft = refreshTime - currentTime % refreshTime;
                                if (timeLeft == 0) {
                                    armorStand.remove();  // Remove the ArmorStand
                                    this.cancel();  // Cancel the timer
                                } else {
                                    armorStand.setCustomName(ChatColor.GREEN + "Refresh in: " + timeLeft);
                                }
                            } else {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0, 20);  // 20 ticks = 1 second
            }
        }
    }
}