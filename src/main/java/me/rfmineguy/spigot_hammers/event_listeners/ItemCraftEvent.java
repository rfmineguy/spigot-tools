package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemCraftEvent implements Listener {
    @EventHandler
    public static void craftItem(PrepareSmithingEvent event) {
        ItemStack tool = event.getInventory().getItem(0);
        ItemStack modifier = event.getInventory().getItem(1);
        if (tool == null || modifier == null) {
            return;
        }
        ItemStack result = getResult(tool, modifier);

        if (result != null) {
            event.setResult(result);
        }

        List<HumanEntity> humanEntityList = event.getViewers();
        humanEntityList.forEach(humanEntity -> ((Player)humanEntity).updateInventory());
    }

    @EventHandler
    public static void itemSmithed(SmithItemEvent event) {
        SpigotTools.LOGGER.info(event.getCurrentItem().toString());
        event.getViewers().forEach(humanEntity -> ((Player)humanEntity).updateInventory());
    }

    private static ItemStack getResult(ItemStack toolSlot, ItemStack modifierSlot) {
        Material material = toolSlot.getType();
        if (material == Material.IRON_PICKAXE && modifierSlot.isSimilar(ItemManager.ironConversionKit)) {
            return ItemManager.ironHammer;
        }
        else if (material == Material.DIAMOND_PICKAXE && modifierSlot.isSimilar(ItemManager.diamondConversionKit)) {
            return ItemManager.diamondHammer;
        }
        else if (material == Material.NETHERITE_PICKAXE && modifierSlot.isSimilar(ItemManager.netheriteConversionKit)) {
            return ItemManager.netheriteHammer;
        }
        else if (material == Material.IRON_SHOVEL && modifierSlot.isSimilar(ItemManager.ironConversionKit)) {
            return ItemManager.ironExcavator;
        }
        else if (material == Material.DIAMOND_SHOVEL && modifierSlot.isSimilar(ItemManager.diamondConversionKit)) {
            return ItemManager.diamondExcavator;
        }
        else if (material == Material.NETHERITE_SHOVEL && modifierSlot.isSimilar(ItemManager.netheriteConversionKit)) {
            return ItemManager.netheriteExcavator;
        }
        return null;
    }
}
