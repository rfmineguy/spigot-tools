package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.item.ItemManager;
import me.rfmineguy.spigot_hammers.item.MinerBackpackItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemPickupEvent implements Listener {
    @EventHandler
    public void pickup(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            // search for a Miner's Backpack in the player's inventory (not very efficient, but functional)
            ItemStack[] contents = player.getInventory().getContents();

            ItemStack offHandStack = player.getInventory().getItemInOffHand();
            if (ItemManager.isBackpack(offHandStack)) {
                MinerBackpackItem backpackItem = (MinerBackpackItem) offHandStack;
                backpackItem.getInventory().addItem(event.getItem().getItemStack());
                event.setCancelled(true);
            }
        }
    }
}
