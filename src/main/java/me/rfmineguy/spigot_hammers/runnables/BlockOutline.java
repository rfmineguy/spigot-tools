package me.rfmineguy.spigot_hammers.runnables;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.util.HelperFunctions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BlockOutline extends BukkitRunnable {
    Player player;

    public BlockOutline() {

    }
    public BlockOutline(Player player) {
        this.player = player;
    }

    //Spawn particle in the center of each block face that will be broken
    @Override
    public void run() {
        if (!this.player.isOnline()) {
            return;
        }
        Block targetBlock = HelperFunctions.getTargetBlock(player);//player.getTargetBlock(null, 100);
        BlockFace face = HelperFunctions.getTargetBlockFace(player);
        if (face == null) {
            SpigotTools.LOGGER.info("Face is null");
            return;
        }
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 127, 255), 0.5F);
        List<Integer> blockOffsets = HelperFunctions.getOffsets(face);
        List<Double> faceOffsets = HelperFunctions.getFaceOffsets(face);
        int xoff = blockOffsets.get(0);
        int yoff = blockOffsets.get(1);
        int zoff = blockOffsets.get(2);
        for (int i = -xoff; i <= xoff; i++) {
            for (int j = -yoff; j <= yoff; j++) {
                for (int k = -zoff; k <= zoff; k++) {
                    Location origin = targetBlock.getLocation().clone();
                    Location thisBlock = origin.add(i, j, k);
                    if (player.getWorld().getBlockAt(thisBlock).getType() != Material.AIR) {
                        player.spawnParticle(Particle.REDSTONE, thisBlock.add(faceOffsets.get(0), faceOffsets.get(1), faceOffsets.get(2)), 5, dustOptions);
                    }
                }
            }
        }
    }
}