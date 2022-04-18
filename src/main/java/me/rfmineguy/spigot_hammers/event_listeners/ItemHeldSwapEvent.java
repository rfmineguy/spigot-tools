package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import me.rfmineguy.spigot_hammers.runnables.BlockOutline;
import me.rfmineguy.spigot_hammers.util.HelperFunctions;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class ItemHeldSwapEvent implements Listener {

    BukkitTask task;

    @EventHandler
    public void ItemSwap(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int slot = event.getNewSlot();
        ItemStack item = player.getInventory().getItem(slot);
        BlockFace blockFace = HelperFunctions.getTargetBlockFace(player);

        //if the player isn't looking at a block get out, we don't care anymore
        if (blockFace == null) {
            return;
        }

        //Cancel any currently running task
        if (task != null && !task.isCancelled()) {
            Bukkit.getScheduler().cancelTask(task.getTaskId()); //outlineRunnable.cancel();
        }

        //Start a new task for the current item being held
        if (item != null && (ItemManager.isExcavator(item) || ItemManager.isHammer(item))) {
            task = new BlockOutline(player).runTaskTimer(SpigotTools.getPlugin(), 0, 1); //Bukkit.getScheduler().runTaskTimer(SpigotHammers.getPlugin(), outlineRunnable, 0, 20);
        }
    }
}
