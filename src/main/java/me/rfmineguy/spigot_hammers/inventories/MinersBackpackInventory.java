package me.rfmineguy.spigot_hammers.inventories;

import com.jeff_media.morepersistentdatatypes.DataType;
import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Arrays;

public class MinersBackpackInventory {
    private static class MinersBackpackUtil {

    }

    public static class Holder implements InventoryHolder {
        Inventory inventory;
        ItemStack backpackItem;

        public Holder(ItemStack backpackItem) {
            inventory = Bukkit.createInventory(this, 9, "Miner's backpack");
            inventory.setMaxStackSize(64);

            // Retrieve and set the contents of this inventory
            PersistentDataContainer container = backpackItem.getItemMeta().getPersistentDataContainer();
            ItemStack items[] = container.get(new NamespacedKey(SpigotTools.getPlugin(), "contents"), DataType.ITEM_STACK_ARRAY);
            inventory.setContents(items);

            this.backpackItem = backpackItem;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }

    public static class InvListener implements Listener {
        @EventHandler
        public void interaction(InventoryClickEvent event) {
            InventoryHolder holder = event.getView().getBottomInventory().getHolder();
            InventoryHolder topHolder = event.getView().getTopInventory().getHolder();
            if (holder instanceof Player && topHolder instanceof MinersBackpackInventory) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void close(InventoryCloseEvent event) {
            if (event.getView().getTopInventory().getHolder() instanceof MinersBackpackInventory.Holder) {
                MinersBackpackInventory.Holder holder = (MinersBackpackInventory.Holder) event.getView().getTopInventory().getHolder();
                ItemMeta meta = holder.backpackItem.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                container.set(new NamespacedKey(SpigotTools.getPlugin(), "contents"), DataType.ITEM_STACK_ARRAY, holder.getInventory().getContents());
                holder.backpackItem.setItemMeta(meta);
            }
        }
    }
}
