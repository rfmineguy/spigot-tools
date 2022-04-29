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
import java.util.List;

public class HelperFunctions {

    private static final double X_FACE_OFFSET = 0.5;
    private static final double Y_FACE_OFFSET = 0.5;
    private static final double Z_FACE_OFFSET = 0.5;

    /**
     * getTargetBlockFace(Player)
     * @apiNote Calculates the blockface the player is looking at given a player
     * @param player The player of whome you are trying to get the blockface they are looking at
     * @return The blockface this player was looking at or null
     */
    public static BlockFace getTargetBlockFace(Player player, int maxDistance) {
        Location location = player.getEyeLocation();
        RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(location, location.getDirection(), maxDistance, FluidCollisionMode.NEVER);
        return (rayTraceResult != null) ? rayTraceResult.getHitBlockFace() : null;
    }
    public static BlockFace getTargetBlockFace(Player player) {
        return getTargetBlockFace(player, 5);
    }


    /**
     * getTargetBlock(Player)
     *
     * @apiNote      : Calculates the current block a player is looking at
     * @param player : The player of whome you are trying to get the block their looking at
     * @return Block : The block that the player specified is looking at or null
     */
    public static Block getTargetBlock(Player player, int maxDistance) {
        Location location = player.getEyeLocation();
        RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(location, location.getDirection(), maxDistance, FluidCollisionMode.NEVER);
        return (rayTraceResult != null) ? rayTraceResult.getHitBlock() : null;
    }
    public static Block getTargetBlock(Player player) {
        return getTargetBlock(player, 5);
    }

    /**
        getOffsets(BlockFace)

        @apiNote : Calculates what the x, y, and z offset values need to be to break the right 3x3 area
        @param blockFace : A single block face (most commonly retrieved via HelperFunctions#getTargetBlockFace(Player))
        @return     : A list of offset values [xOff, yOff, zOff]
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

    /**
     * getFaceOffsets(BlockFace)
     * @apiNote Calculates the offsets for particle spawning given a blockface
     * @param blockFace The blockface the offsets should be calculated for
     * @return A list of three offset values [xOff, yOff, zOff]
     */
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

    public static boolean isBreakable(Block block) {
        Material material = block.getType();
        return material != Material.WATER && material != Material.LAVA && material != Material.AIR;
    }
}
