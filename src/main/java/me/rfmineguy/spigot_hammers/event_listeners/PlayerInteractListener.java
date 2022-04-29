package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.inventories.ToolModifierInventory;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        //handlePathBlock(event);
        handleInventoryOpening(event);
    }

    void handlePathBlock(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        World world = player.getWorld();
        Block block = event.getClickedBlock();
        SpigotTools.LOGGER.info(block.toString());

        if (ItemManager.isExcavator(player.getInventory().getItemInMainHand())) {
            for (int i = -1; i <= 1; i++) {
                for (int k = -1; k <= 1; k++) {
                    Location origin = block.getLocation().clone();
                    Material material = world.getBlockAt(origin.add(i, 1, k)).getType();
                    if (canMakePathBlock(material)) {
                        block.setType(Material.DIRT_PATH, true);
                    }
                }
            }
        }
    }

    void handleInventoryOpening(PlayerInteractEvent event) {
        SpigotTools.LOGGER.info("Interaction");
        if ((event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) || !event.getPlayer().isSneaking()) {
            return;
        }
        ItemStack itemStack = event.getItem();
        if (itemStack != null && (ItemManager.isHammer(itemStack) || ItemManager.isExcavator(itemStack))) {
            ToolModifierInventory.CustomInventory inventory = new ToolModifierInventory.CustomInventory();
            event.getPlayer().openInventory(inventory.getInventory());
            event.setCancelled(true);
        }
    }

    boolean canMakePathBlock(Material material) {
        return material == Material.GRASS_BLOCK ||
                material == Material.DIRT ||
                material == Material.PODZOL ||
                material == Material.COARSE_DIRT;
    }
}
