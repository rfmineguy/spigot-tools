package me.rfmineguy.spigot_hammers.runnables;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import me.rfmineguy.spigot_hammers.util.HelperFunctions;
import me.rfmineguy.spigot_hammers.util.ParticleUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/*
    setMarker(true) on armor stand entity will make it "see through" (non interactable)
 */

public class BlockOutlineV2 extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getServer().getOnlinePlayers().forEach((Player p) -> {
            if (p.getGameMode().equals(GameMode.SPECTATOR) || p.getGameMode().equals(GameMode.ADVENTURE)) {
                return;
            }
            ItemStack itemStack = p.getInventory().getItemInMainHand();

            //otherwise do absolutely nothing and move on to the next player
            byte playerEffectStatus = p.getPersistentDataContainer().getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "effect"), PersistentDataType.BYTE, (byte) 0);
            boolean isHoldingItem = itemStack.getType() != Material.AIR;
            boolean isHammer = ItemManager.isHammer(itemStack);
            boolean isExcavator = ItemManager.isExcavator(itemStack);
            if (playerEffectStatus == (byte)1 && (isHoldingItem && (isExcavator || isHammer))) {
                Block block = HelperFunctions.getTargetBlock(p);
                BlockFace face = HelperFunctions.getTargetBlockFace(p);
                World world = p.getWorld();
                boolean isPreferredTool, passable, breakable, isDirtlike, airInFrontOf;
                if (face != null) {
                    // Highlight one block
                    if (p.isSneaking()) {
                        isPreferredTool = block.isPreferredTool(itemStack);
                        airInFrontOf = world.getBlockAt(block.getLocation().clone().add(face.getDirection())).getType().isAir();
                        passable = block.isPassable();
                        breakable = HelperFunctions.isBreakable(block);
                        isDirtlike = HelperFunctions.isDirtlike(block);
                        if (airInFrontOf && !passable && breakable) {
                            if (isHammer && isPreferredTool && !isDirtlike) {
                                ParticleUtil.drawFace(p, block, face);
                            }
                            else if (isExcavator && isDirtlike) {
                                ParticleUtil.drawFace(p, block, face);
                            }
                        }
                    }

                    //Highlight 3x3 block area
                    else {
                        List<Integer> blockOffsets = HelperFunctions.getOffsets(face);
                        int xoff = blockOffsets.get(0);
                        int yoff = blockOffsets.get(1);
                        int zoff = blockOffsets.get(2);
                        for (int i = -xoff; i <= xoff; i++) {
                            for (int j = -yoff; j <= yoff; j++) {
                                for (int k = -zoff; k <= zoff; k++) {
                                    Location origin = block.getLocation().clone();
                                    Location thisBlock = origin.add(i, j, k);
                                    Block b = world.getBlockAt(thisBlock);
                                    airInFrontOf = world.getBlockAt(thisBlock.clone().add(face.getDirection())).getType().isAir();
                                    isPreferredTool = b.isPreferredTool(itemStack);
                                    passable = b.isPassable();
                                    breakable = HelperFunctions.isBreakable(b);
                                    isDirtlike = HelperFunctions.isDirtlike(b);

                                    if (airInFrontOf && !passable && breakable) {
                                        if (isHammer && isPreferredTool && !isDirtlike) {
                                            ParticleUtil.drawFace(p, thisBlock.getBlock(), face);
                                        }
                                        else if (isExcavator && isDirtlike) {
                                            ParticleUtil.drawFace(p, thisBlock.getBlock(), face);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
