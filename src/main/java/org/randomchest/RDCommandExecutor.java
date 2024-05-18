package org.randomchest;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RDCommandExecutor implements CommandExecutor {
    private final RandomChest plugin;

    public RDCommandExecutor(RandomChest plugin) {
        this.plugin = plugin;
    }

    public void addItemAtSlot(List<ItemStack> items, ItemStack item, int slot) {
        while (items.size() <= slot) {
            items.add(null);
        }
        items.set(slot, item);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("randomchest")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /randomchest addchest <chest_name> <world> <x> <y> <z>");
                return true;
            }
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("addchest")) {
                    if (args.length == 6) {
                        String chestName = args[1];
                        String world = args[2];
                        int x = Integer.parseInt(args[3]);
                        int y = Integer.parseInt(args[4]);
                        int z = Integer.parseInt(args[5]);

                        Block block = Bukkit.getWorld(world).getBlockAt(x, y, z);

                        if (block.getType() == Material.CHEST) {
                            sender.sendMessage(ChatColor.RED + "A chest already exists at this location.");
                            return true;
                        }

                        FileConfiguration config = plugin.getConfigManager().getConfig();
                        String chestTypeString = config.getString("general.Chests." + chestName + ".Chest-type");
                        if (chestTypeString == null) {
                            sender.sendMessage(ChatColor.RED + "Chest type not specified in the configuration for chest " + chestName);
                            return true;
                        }

                        Material chestType;
                        try {
                            chestType = Material.valueOf(chestTypeString);
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid chest type specified in the configuration for chest " + chestName + ": " + chestTypeString);
                            return true;
                        }

                        ChestCreator chestCreator = new ChestCreator();
                        chestCreator.createChest(world, x, y, z, new ItemStack[0], chestType);

                        ConfigurationSection chestsSection = config.getConfigurationSection("general.Chests");
                        for (String chestNameKey : chestsSection.getKeys(false)) {
                            List<String> coordinatesList = config.getStringList("general.Chests." + chestNameKey + ".Coordinated");
                            String newCoordinate = x + ", " + y + ", " + z;
                            if (coordinatesList.contains(newCoordinate)) {
                                sender.sendMessage(ChatColor.RED + "A chest already exists at the specified location in the configuration.");
                                return true;
                            }
                        }

                        List<ItemStack> items = new ArrayList<>();
                        ConfigurationSection itemsSection = config.getConfigurationSection("general.Chests." + chestName + ".Items");
                        for (String key : itemsSection.getKeys(false)) {
                            String itemType = config.getString("general.Chests." + chestName + ".Items." + key + ".item-type");
                            String material = config.getString("general.Chests." + chestName + ".Items." + key + ".Material");

                            int minAmount = config.getInt("general.Chests." + chestName + ".Items." + key + ".Amount.min");
                            int maxAmount = config.getInt("general.Chests." + chestName + ".Items." + key + ".Amount.max");

                            String itemName = config.getString("general.Chests." + chestName + ".Items." + key + ".Item-name");
                            List<String> itemLore = config.getStringList("general.Chests." + chestName + ".Items." + key + ".Item-lore");
                            List<String> enchantments = config.getStringList("general.Chests." + chestName + ".Items." + key + ".Enchantments");
                            String potionType = config.getString("general.Chests." + chestName + ".Items." + key + ".Potion-type");
                            List<String> potionEffects = config.getStringList("general.Chests." + chestName + ".Items." + key + ".Potion-Effects");
                            int itemChance = config.getInt("general.Chests." + chestName + ".Items." + key + ".Item-Chance");

                            Random random = new Random();
                            if (random.nextInt(100) < itemChance) {
                                try {
                                    ItemStack item = chestCreator.createItem(itemType, material, minAmount, maxAmount, itemName, itemLore, enchantments, potionType, potionEffects);
                                    chestCreator.addItemToRandomSlot(item);
                                    int slot = random.nextInt(27);
                                    addItemAtSlot(items, item, slot);
                                } catch (IllegalArgumentException e) {
                                    sender.sendMessage(ChatColor.RED + "Invalid item configuration for chest " + chestName + ": " + e.getMessage());
                                    return true;
                                }
                            }
                        }

                        List<String> coordinates = config.getStringList("general.Chests." + chestName + ".Coordinated");
                        coordinates.add(x + ", " + y + ", " + z);
                        config.set("general.Chests." + chestName + ".Coordinated", coordinates);
                        plugin.getConfigManager().saveConfig();

                        Collections.shuffle(items);

                        chestCreator.createChest(world, x, y, z, items.toArray(new ItemStack[0]), chestType);

                        sender.sendMessage(ChatColor.GREEN + "You created " + chestName + " at " + x + ", " + y + ", " + z);
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /randomchest addchest <chest_name> <world> <x> <y> <z>");
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.getConfigManager().reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}
