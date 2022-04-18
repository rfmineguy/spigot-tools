package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        World world = player.getWorld();
        Block block = event.getClickedBlock();
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
                Material material = world.getBlockAt(block.getLocation().add(i, 0, k)).getType();
                if (canMakePathBlock(material)) {
                    SpigotTools.LOGGER.info("Clicked on a block : " + block);
                    block.setType(Material.DIRT_PATH);
                }
            }
        }
    }

    boolean canMakePathBlock(Material material) {
        return material == Material.GRASS_BLOCK ||
                material == Material.DIRT_PATH ||
                material == Material.DIRT ||
                material == Material.PODZOL ||
                material == Material.COARSE_DIRT;
    }
}
