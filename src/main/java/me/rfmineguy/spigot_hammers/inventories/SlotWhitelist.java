package me.rfmineguy.spigot_hammers.inventories;

import org.bukkit.inventory.ItemStack;

public interface SlotWhitelist {
    boolean isValidItem(ItemStack itemStack);
    ItemStack getItemStack();
}
