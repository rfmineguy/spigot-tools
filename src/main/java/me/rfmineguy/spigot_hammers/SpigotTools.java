package me.rfmineguy.spigot_hammers;

import me.rfmineguy.spigot_hammers.commands.PluginCommands;
import me.rfmineguy.spigot_hammers.event_listeners.*;
import me.rfmineguy.spigot_hammers.inventories.ToolModifierInventory;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import me.rfmineguy.spigot_hammers.runnables.BlockOutlineV2;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

public final class SpigotTools extends JavaPlugin {

    private static SpigotTools plugin;
    public static Logger LOGGER;
    public static Random RANDOM = new Random();

    @Override
    public void onEnable() {
        plugin = this;
        LOGGER = plugin.getLogger();
        ItemManager.init();
        ItemManager.initRecipes();

        //these command names must be registered within the plugin.yml file
        getCommand("spigot-tools").setExecutor(new PluginCommands.Executor());
        getCommand("spigot-tools").setTabCompleter(new PluginCommands.Completer());

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ToolModifierInventory.InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemCraftEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        new BlockOutlineV2().runTaskTimer(this, 0,2);
        LOGGER.info("SpigotTools plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SpigotTools getPlugin() {
        return plugin;
    }
}
