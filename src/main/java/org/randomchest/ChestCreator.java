package org.randomchest;

import com.sun.java.accessibility.util.internal.CheckboxTranslator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestCreator {
    private Chest chest;
    public void createChest(String worldName, int x, int y, int z, ItemStack[] items, Material chestType) {
        //get the world
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalArgumentException("World " + worldName + " not found!");
        }

        // create a chest at the specified location.
        Location location = new Location(world, x, y, z);
        Block block = world.getBlockAt(location);
        block.setType(chestType);

        // put items in chest
        chest = (Chest) block.getState();
        chest.getInventory().setContents(items);
        for (int i = 0; i < items.length; i++){
            chest.getInventory().setItem(i, items[i]);
        }
    }

    public ItemStack createItem(String itemType, String material, int minAmount, int maxAmount, String itemName, List<String> itemLore, List<String> enchantments, String potionType, List<String> potionEffects) {
        if (material == null) {
            throw new IllegalArgumentException("Material not specified in the configuration for an item");
        }

        Material itemMaterial = Material.valueOf(material);
        int amount = (int) ((Math.random() * (maxAmount - minAmount)) + minAmount);
        ItemStack item = new ItemStack(itemMaterial, amount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        List<String> lore = new ArrayList<>();
        for (String loreLine : itemLore) {
            lore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        }
        meta.setLore(lore);

        if (itemType.equalsIgnoreCase("BLOCK")) {
            for (String enchantment : enchantments) {
                String[] parts = enchantment.split(", ");
                Enchantment ench = Enchantment.getByName(parts[0]);
                int level = Integer.parseInt(parts[1]);
                meta.addEnchant(ench, level, true);
            }
        } else if (itemType.equalsIgnoreCase("POTION")) {
            PotionMeta potionMeta = (PotionMeta) meta;
            for (String effect : potionEffects) {
                String[] parts = effect.split(", ");
                PotionEffectType effectType = PotionEffectType.getByName(parts[0]);
                int amplifier = Integer.parseInt(parts[1]);
                int duration = Integer.parseInt(parts[2]);
                potionMeta.addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
            }
            meta = potionMeta;
        }

        item.setItemMeta(meta);
        return item;
    }

    public void addItemToRandomSlot(ItemStack item) {
        Random random = new Random();
        int slot;
        do {
            slot = random.nextInt(27);
        } while (chest.getInventory().getItem(slot) != null);
        chest.getInventory().setItem(slot, item);
    }
}
