package me.rfmineguy.spigot_hammers;

import me.rfmineguy.spigot_hammers.commands.ExcavatorCommand;
import me.rfmineguy.spigot_hammers.commands.HammerCommand;
import me.rfmineguy.spigot_hammers.event_listeners.BlockBreakListener;
import me.rfmineguy.spigot_hammers.event_listeners.ItemCraftEvent;
import me.rfmineguy.spigot_hammers.event_listeners.JoinEvent;
import me.rfmineguy.spigot_hammers.item.ItemManager;
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
        getCommand("hammer").setExecutor(new HammerCommand());
        getCommand("excavator").setExecutor(new ExcavatorCommand());

        //getServer().getPluginManager().registerEvents(new ItemHeldSwapEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemCraftEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SpigotTools getPlugin() {
        return plugin;
    }
}
