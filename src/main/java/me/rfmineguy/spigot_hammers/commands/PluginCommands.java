package me.rfmineguy.spigot_hammers.commands;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public abstract class PluginCommands {
    public static class Executor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command");
                return true;
            }
            Player player = (Player) sender;
            switch (args[0].toLowerCase()) {
                case "effect": {
                    handleEffectCommand(player, args);
                    break;
                }
                case "give": {
                    handleGiveCommand(player, args);
                    break;
                }
            }
            return false;
        }

        void handleEffectCommand(Player player, String[] args) {
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

        void handleGiveCommand(Player player, String[] args) {
            switch (args[1]) {
                case "hammer": {
                    handleHammerGive(player, args);
                    break;
                }
                case "excavator": {
                    handleExcavatorGive(player, args);
                    break;
                }
                case "upgrade": {
                    handleUpgradeGive(player, args);
                    break;
                }
            }
        }

        void handleHammerGive(Player player, String[] args) {
            switch (args[2]) {
                case "stone": player.getInventory().addItem(ItemManager.stoneHammer);
                    break;
                case "iron": player.getInventory().addItem(ItemManager.ironHammer);
                    break;
                case "diamond": player.getInventory().addItem(ItemManager.diamondHammer);
                    break;
                case "netherite": player.getInventory().addItem(ItemManager.netheriteHammer);
                    break;
                default:
                    player.sendMessage("That's not a hammer type");
            }
        }

        void handleExcavatorGive(Player player, String[] args) {
            switch (args[2]) {
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

        void handleUpgradeGive(Player player, String[] args) {
            ItemStack itemStack = new ItemStack(Material.AIR);
            switch (args[2]) {
                case "efficiency_1": itemStack = ItemManager.speedUpgradeItemLvl1;
                break;
                case "efficiency_2": itemStack = ItemManager.speedUpgradeItemLvl2;
                    break;
                case "efficiency_4": itemStack = ItemManager.speedUpgradeItemLvl3;
                    break;
                case "fortune_1": itemStack = ItemManager.fortuneUpgradeItemLvl1;
                    break;
                case "fortune_3": itemStack = ItemManager.fortuneUpgradeItemLvl2;
                    break;
                case "silk_touch": itemStack = ItemManager.silkTouchUpgradeItem;
                    break;
                default:
            }
            ItemManager.makeUpgradeItemUnique(itemStack);
            player.getInventory().addItem(itemStack);
        }
    }

    public static class Completer implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
            List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                completions.add("effect");
                completions.add("give");
                return completions;
            }
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("effect")) {
                    completions.add("on");
                    completions.add("off");
                    completions.add("query");
                    return completions;
                }
                if (args[0].equalsIgnoreCase("give")) {
                    completions.add("hammer");
                    completions.add("excavator");
                    completions.add("upgrade");
                    return completions;
                }
            }
            else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (args[1].equalsIgnoreCase("hammer") || args[1].equalsIgnoreCase("excavator")) {
                        completions.add("stone");
                        completions.add("iron");
                        completions.add("diamond");
                        completions.add("netherite");
                        return completions;
                    }
                    else if (args[1].equalsIgnoreCase("upgrade")) {
                        completions.add("efficiency_1");
                        completions.add("efficiency_2");
                        completions.add("efficiency_4");
                        completions.add("fortune_1");
                        completions.add("fortune_3");
                        completions.add("silk_touch");
                        return completions;
                    }
                }
            }
            return null;
        }
    }
}