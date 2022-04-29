package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.item.ItemManager;
import me.rfmineguy.spigot_hammers.util.HelperFunctions;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockBreakListener implements Listener {

    static boolean currentlyBreaking = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        BlockFace blockFace = HelperFunctions.getTargetBlockFace(player);
        Block block = event.getBlock();
        ItemStack currentItem = player.getInventory().getItem(EquipmentSlot.HAND);

        if (blockFace != null && !player.isSneaking() && !block.isPassable()) {
            if (ItemManager.isHammer(currentItem) && !currentlyBreaking) {
                break3x3(player, blockFace, currentItem, block.getX(), block.getY(), block.getZ(), false);
            }
            else if (ItemManager.isExcavator(currentItem) && !currentlyBreaking) {
                break3x3(player, blockFace, currentItem, block.getX(), block.getY(), block.getZ(), true);
            }
        }
    }

    private void break3x3(Player player, BlockFace face, ItemStack tool, int originx, int originy, int originz, boolean mineDirtLike) {
        World world = player.getWorld();

        currentlyBreaking = true;

        List<Integer> offsets = HelperFunctions.getOffsets(face);
        int xoff = offsets.get(0);
        int yoff = offsets.get(1);
        int zoff = offsets.get(2);

        for (int i = -xoff; i <= xoff; i++) {
            for (int j = -yoff; j <= yoff; j++) {
                for (int k = -zoff; k <= zoff; k++) {
                    Block b = world.getBlockAt(originx + i, originy + j, originz + k);
                    boolean isAir = b.getType() == Material.AIR;
                    boolean isLiquid = b.getType() == Material.WATER || b.getType() == Material.LAVA;
                    boolean isPreferredTool = b.isPreferredTool(tool);
                    boolean isBlockDirtlike = HelperFunctions.isDirtlike(b);

                    //Hammer condition
                    if (!isAir && !isLiquid && !mineDirtLike && (isPreferredTool && !isBlockDirtlike)) {
                        player.breakBlock(b);
                    }

                    //Excavator condition
                    else if (!isAir && !isLiquid && mineDirtLike && isBlockDirtlike) {
                        player.breakBlock(b);
                    }
                }
            }
        }
        currentlyBreaking = false;
    }
}
