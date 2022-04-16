package me.rfmineguy.spigot_hammers.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class HelperFunctions {

    /**
        getBlockFace(Player)

        @apiNote : determines which face of the block a player is looking at (South, West, etc.)
        @param player : the player that is looking at a block
        @return      : The blockface being looked at
     */
    public static BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
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
