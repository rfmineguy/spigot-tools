package me.rfmineguy.spigot_hammers.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MinerBackpackItem extends ItemStack implements InventoryHolder {
    Inventory inventory;

    public MinerBackpackItem(int itemMeta) {
        super(Material.SHULKER_SHELL);
        ItemMeta meta = getItemMeta();
        meta.setCustomModelData(itemMeta);
        setItemMeta(meta);

        inventory = Bukkit.createInventory(this, 18);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
