package me.rfmineguy.spigot_hammers.commands;

import me.rfmineguy.spigot_hammers.item.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExcavatorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("excavator")) {
            switch (args[0]) {
                case "stone": player.getInventory().addItem(ItemManager.stoneExcavator);
                    break;
                case "iron": player.getInventory().addItem(ItemManager.ironExcavator);
                    break;
                case "diamond": player.getInventory().addItem(ItemManager.diamondExcavator);
                    break;
                case "netherite": player.getInventory().addItem(ItemManager.netheriteExcavator);
                    break;
                default:
                    player.sendMessage("That's not an excavator type");
            }
        }

        return true;
    }
}
