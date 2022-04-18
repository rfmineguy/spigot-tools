package me.rfmineguy.spigot_hammers.util;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelperFunctions {

    private static final double X_FACE_OFFSET = 0.5;
    private static final double Y_FACE_OFFSET = 0.5;
    private static final double Z_FACE_OFFSET = 0.5;

    public static BlockFace getTargetBlockFace(Player player) {
        Location location = player.getEyeLocation();
        RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(location, location.getDirection(), 5, FluidCollisionMode.NEVER);
        return (rayTraceResult != null) ? rayTraceResult.getHitBlockFace() : null;
    }
    public static Block getTargetBlock(Player player) {
        Location location = player.getEyeLocation();
        RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(location, location.getDirection(), 6, FluidCollisionMode.NEVER);
        return (rayTraceResult != null) ? rayTraceResult.getHitBlock() : null;
    }

    /**
        getBlockFace(Player)

        @apiNote      : determines which face of the block a player is looking at (South, West, etc.)
        @param player : the player that is looking at a block
        @return       : The blockface being looked at
     */
    public static BlockFace getBlockFace(Player player) {
        Set<Material> transparent = new HashSet<>();
        transparent.add(Material.WATER);
        transparent.add(Material.LAVA);
        Block targetBlock = player.getTargetBlockExact(100, FluidCollisionMode.NEVER);
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(transparent, 100);
        if (lastTwoTargetBlocks.size() != 2) return null;// || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        //targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

    /**
        getOffsets(BlockFace)

        @apiNote : calculates what the x, y, and z offset values need to be to break the right 3x3 area
        @param blockFace : a single block face (most commonly retrieved via HelperFunctions#getBlockFace(Player))
        @return     : a list of offsets [0]->xoff, [1]->yoff, [2]->zoff
     */
    public static List<Integer> getOffsets(BlockFace blockFace) {
        int xoff = 0;
        int yoff = 0;
        int zoff = 0;

        if (blockFace == BlockFace.DOWN || blockFace == BlockFace.UP) {
            xoff = 1;
            zoff = 1;
        }
        if (blockFace == BlockFace.SOUTH || blockFace == BlockFace.NORTH) {
            xoff = 1;
            yoff = 1;
        }
        if (blockFace == BlockFace.WEST || blockFace == BlockFace.EAST) {
            zoff = 1;
            yoff = 1;
        }
        return Arrays.asList(xoff, yoff, zoff);
    }

    public static List<Double> getFaceOffsets(BlockFace blockFace) {
        //-0.5 and 0.5 put the coordinate at the edge of a block
        double xoff = 0.5;
        double yoff = 0.5;
        double zoff = 0.5;
        if (blockFace == BlockFace.DOWN) {
            xoff = X_FACE_OFFSET; //0.5;
            yoff = -Y_FACE_OFFSET; //-0.5;
            zoff = Z_FACE_OFFSET; //0.5;
        }
        if (blockFace == BlockFace.UP) {
            xoff = X_FACE_OFFSET; //0.5;
            yoff = Y_FACE_OFFSET + 1;//1.5;
            zoff = Z_FACE_OFFSET; //0.5;
        }
        if (blockFace == BlockFace.SOUTH) {
            xoff = X_FACE_OFFSET; //0.5;
            yoff = Y_FACE_OFFSET; //0.5;
            zoff = Z_FACE_OFFSET + 1;//1.5;
        }
        if (blockFace == BlockFace.NORTH) {
            xoff = X_FACE_OFFSET; //0.5;
            yoff = Y_FACE_OFFSET; //0.5;
            zoff = -Z_FACE_OFFSET; //-0.5;
        }
        if (blockFace == BlockFace.EAST) {
            xoff = X_FACE_OFFSET + 1;//1.5;
            yoff = Y_FACE_OFFSET; //0.5;
            zoff = Z_FACE_OFFSET; //0.5;
        }
        if (blockFace == BlockFace.WEST) {
            xoff = -X_FACE_OFFSET; //-0.5;
            yoff = Y_FACE_OFFSET; //0.5;
            zoff = Z_FACE_OFFSET; //0.5;
        }
        return Arrays.asList(xoff, yoff, zoff);
    }
    /**
     * isDirtLike(Block)
     * @apiNote     : uses Block#isPreferredTool to determine whether the block is ideally minable by a shovel
     * @param block : the block that we are testing for dirtLike qualities on
     * @return      : whether the block is minable by a netherite shovel (top tier shovel)
     */
    public static boolean isDirtlike(Block block) {
        return block.isPreferredTool(new ItemStack(Material.NETHERITE_SHOVEL));
    }
}
