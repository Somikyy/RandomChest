package org.randomchest;

import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RefreshChestCommand implements CommandExecutor {
    private final RandomChest plugin;
    private ChestRefresher chestRefresher;

    public RefreshChestCommand(RandomChest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            String chestName = args[0];
            plugin.getChestRefresher().refreshChest(chestName);
            sender.sendMessage("Chest " + chestName + " refreshed.");
            return true;
        } else {
            sender.sendMessage("Please specify a chest name.");
            return false;
        }
    }
}