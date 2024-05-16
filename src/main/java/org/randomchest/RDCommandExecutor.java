package org.randomchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
                sender.sendMessage(ChatColor.RED + "Usage: /randomchest addchest <chest_name> <world> <x> <y> <z> <chance> <refresh> <refresh_time>");
                return true;
            }
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("addchest")) {
                    if (args.length == 9) {
                        String chestName = args[1];
                        String world = args[2];
                        int x = Integer.parseInt(args[3]);
                        int y = Integer.parseInt(args[4]);
                        int z = Integer.parseInt(args[5]);
                        int chance = Integer.parseInt(args[6]);
                        boolean refresh = Boolean.parseBoolean(args[7]);
                        int refreshTime = Integer.parseInt(args[8]);

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

                        List<ItemStack> items = new ArrayList<>();
                        ConfigurationSection itemsSection = config.getConfigurationSection("general.Chests." + chestName + ".Items");
                        for (String key : itemsSection.getKeys(false)) {
                            String itemType = config.getString("general.Chests." + chestName + ".Items." + key + ".item-type");
                            String material = config.getString("general.Chests." + chestName + ".Items." + key + ".Material");

                            System.out.println("Creating item with itemType: " + itemType + ", material: " + material);

                            int minAmount = config.getInt("general.Chests." + chestName + ".Items." + key + ".Amount.min");
                            int maxAmount = config.getInt("general.Chests." + chestName + ".Items." + key + ".Amount.max");

                            List<String> coordinates = config.getStringList("general.Chests." + chestName + ".Coordinated");
                            coordinates.add(x + ", " + y + ", " + z);
                            config.set("general.Chests." + chestName + ".Coordinated", coordinates);
                            plugin.getConfigManager().saveConfig();

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

                        Collections.shuffle(items);

                        chestCreator.createChest(world, x, y, z, items.toArray(new ItemStack[0]), chestType);

                        sender.sendMessage(ChatColor.GREEN + "Chest destination added!");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /randomchest addchest <chest_name> <world> <x> <y> <z> <chance> <refresh> <refresh_time>");
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}
