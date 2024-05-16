package org.randomchest;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class RDCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("randomchest")) {
            // handle the command here
            sender.sendMessage(ChatColor.GREEN + "You executed this command.");
            return true;
        }
        return false;
    }

}
