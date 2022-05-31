package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemDamageListener implements Listener {
    @EventHandler
    public void itemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (ItemManager.ToolItem.isExcavator(item) || ItemManager.ToolItem.isHammer(item)) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            Damageable damageable = (Damageable) item.getItemMeta();
            int maxDurability = item.getType().getMaxDurability();
            int durability = maxDurability - damageable.getDamage();
            if (durability == 1) {
                if (dataContainer.get(new NamespacedKey(SpigotTools.getPlugin(), "isBroken"), PersistentDataType.BYTE) == 0) {
                    dataContainer.set(new NamespacedKey(SpigotTools.getPlugin(), "isBroken"), PersistentDataType.BYTE, (byte)1);
                    item.setItemMeta(meta);
                    ItemManager.ToolItem.updateLore(item);
                    player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                }
                event.setCancelled(true);
            }
        }
    }
}
