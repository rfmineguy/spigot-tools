package me.rfmineguy.spigot_hammers.commands;

import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BlockOutlineEffectCommand {
    public static class Command implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command");
                return true;
            }
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("effect")) {
                NamespacedKey key  = new NamespacedKey(SpigotTools.getPlugin(), "effect");
                switch (args[1]) {
                    case "on": {
                        player.sendMessage("turning on the effect for you");
                        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
                        break;
                    }
                    case "off": {
                        player.sendMessage("turning off the effect for you");
                        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 0);
                        break;
                    }
                    case "query":{
                        byte status = player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "effect"), PersistentDataType.BYTE, (byte) 0);
                        String statusStr = status == 0 ? "off" : "on";
                        player.sendMessage("Your current particle status is " + statusStr);
                    }
                    default:
                }
            }
            return false;
        }
    }

    public static class Completer implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            List<String> completions = new ArrayList<>();
            SpigotTools.LOGGER.info("" + args.length);
            if (args.length == 1) {
                completions.add("effect");
                return completions;
            }
            else if (args.length == 2) {
                completions.add("on");
                completions.add("off");
                completions.add("query");
                return completions;
            }
            return null;
        }
    }
}
