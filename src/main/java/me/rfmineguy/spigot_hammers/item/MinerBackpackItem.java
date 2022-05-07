package me.rfmineguy.spigot_hammers.item;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MinerBackpack extends ItemStack implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }
}
