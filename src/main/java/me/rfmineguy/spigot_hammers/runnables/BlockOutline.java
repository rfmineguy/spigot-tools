package me.rfmineguy.spigot_hammers.runnables;

import me.rfmineguy.spigot_hammers.util.HelperFunctions;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        Location targetBlockLocation = player.getTargetBlock(null, 100).getLocation();
        BlockFace face = HelperFunctions.getBlockFace(player);
        if (face == null) {
            return;
        }

        player.spawnParticle(Particle.CRIT, targetBlockLocation, 1, 0.5f, 1.05f, 0.5f, 0);

        /*
        List<Integer> offsets = HelperFunctions.getOffsets(face);
        int xoff = offsets.get(0);
        int yoff = offsets.get(1);
        int zoff = offsets.get(2);

        for (int i = -xoff; i <= xoff; i++) {
            for (int j = -yoff; j <= yoff; j++) {
                for (int k = -zoff; k <= zoff; k++) {
                    Block b = world.getBlockAt(targetBlockLocation.getBlockX() + i, targetBlockLocation.getBlockY() + j, targetBlockLocation.getBlockZ() + k);
                }
            }
        }
         */
    }
}