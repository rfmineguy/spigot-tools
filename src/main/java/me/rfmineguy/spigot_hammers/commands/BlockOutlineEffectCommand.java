package me.rfmineguy.spigot_hammers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BlockOutlineEffect {//} implements CommandExecutor {
    public static class Command implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command");
                return true;
            }
            return false;
        }
    }

    public static class Completer implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            List<String> completions = new ArrayList<>();

            return null;
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase(""))
        return false;
    }
}
