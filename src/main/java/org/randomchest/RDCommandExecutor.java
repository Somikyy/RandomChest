package org.randomchest;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RDCommandExecutor implements CommandExecutor {
    private final RandomChest plugin;

    public RDCommandExecutor(RandomChest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("randomchest")) {
            // handle the command here
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /randomchest addchest <chest_name> <world> <x> <y> <z> <chance> <refresh> <refresh_time>");
                return true;
            }
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("addchest")) {
                    // handle create command
                    if(args.length == 9) {
                        // handle the command here

                        // get the configuration values
                        String chestName = args[1];
                        String world = args[2];
                        String coordinates = args[3] + ", " + args[4] + ", " + args[5];
                        int chance = Integer.parseInt(args[6]);
                        boolean refresh = Boolean.parseBoolean(args[7]);
                        int refreshTime = Integer.parseInt(args[8]);

                        // use the configmanager to get and set configuration values
                        FileConfiguration config = plugin.getConfigManager().getConfig();
                        List<String> locations = config.getStringList("general.Chests." + chestName + ".Coordinated");
                        locations.add(coordinates);
                        config.set("general.Chests." + chestName + ".world", world);
                        config.set("general.Chests." + chestName + ".Coordinated", locations);
                        config.set("general.Chests." + chestName + ".chance-spawn-chance", chance);
                        config.set("general.Chests." + chestName + ".chest-refresh", refresh);
                        config.set("general.Chests." + chestName + ".chest-refresh_time", refreshTime);
                        plugin.getConfigManager().saveConfig();

                        sender.sendMessage(ChatColor.GREEN + "Chest destination added!");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /randomchest addchest <chest_name> <world> <x> <y> <z> <chance> <refresh> <refresh_time>");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /randomchest addchest <chest_name> <world> <x> <y> <z> <chance> <refresh> <refresh_time>");
                return true;
            }
        }
        return false;
    }
}
