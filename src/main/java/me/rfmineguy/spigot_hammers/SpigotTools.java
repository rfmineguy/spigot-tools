package me.rfmineguy.spigot_hammers;

import me.rfmineguy.spigot_hammers.commands.BlockOutlineEffectCommand;
import me.rfmineguy.spigot_hammers.commands.ExcavatorCommand;
import me.rfmineguy.spigot_hammers.commands.HammerCommand;
import me.rfmineguy.spigot_hammers.event_listeners.*;
import me.rfmineguy.spigot_hammers.inventories.ToolModifierInventory;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import me.rfmineguy.spigot_hammers.runnables.BlockOutlineV2;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SpigotTools extends JavaPlugin {

    private static SpigotTools plugin;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        plugin = this;
        LOGGER = plugin.getLogger();
        LOGGER.info("SpigotHammers plugin enabled");
        ItemManager.init();
        ItemManager.initRecipes();

        //these command names must be registered within the plugin.yml file
        getCommand("hammer").setExecutor(new HammerCommand());
        getCommand("excavator").setExecutor(new ExcavatorCommand());
        getCommand("st-effect").setExecutor(new BlockOutlineEffectCommand.Command());
        getCommand("st-effect").setTabCompleter(new BlockOutlineEffectCommand.Completer());


        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ToolModifierInventory.InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemCraftEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        new BlockOutlineV2().runTaskTimer(this, 0,2);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SpigotTools getPlugin() {
        return plugin;
    }
}
