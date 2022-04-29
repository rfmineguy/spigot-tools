package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import me.rfmineguy.spigot_hammers.item.ItemManager;
import me.rfmineguy.spigot_hammers.runnables.BlockOutline;
import me.rfmineguy.spigot_hammers.util.HelperFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class ItemHeldSwapEvent implements Listener {

    BukkitTask task;

    /* Spigot events that may be involved in toggling on/off the visual effects
        - [x] PlayerItemHeldEvent  (when the player changes their selected slot)
        - [] PlayerSwapHandItemsEvent (when the player swaps items between left/right hands
        - [] PlayerItemBreakEvent (when the item the player is using breaks)
        - [] PlayerDropItemEvent (when the player drops the currently held item)
        - [] EntityPickupItemEvent (when the player picks an item up)
     */

    @EventHandler
    public void ItemDropEvent(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        int slot = p.getInventory().getHeldItemSlot();
        boolean wasRestarted = tryResetStatus(p, slot);
    }

    // I'm surprised that the EntityPickupItemEvent doesn't contain the slot the item went into.. hmm
    @EventHandler
    public void ItemPickupEvent(EntityPickupItemEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Player))
            return;
        Player p = (Player) e;
        int slot = p.getInventory().getHeldItemSlot();
        boolean wasRestarted = tryResetStatus(p, slot);
    }

    @EventHandler
    public void ItemBreakEvent(PlayerItemBreakEvent event) {

    }

    @EventHandler
    public void ItemSwapHands(PlayerSwapHandItemsEvent event) {

    }

    @EventHandler
    public void ItemSwapHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        boolean wasRestarted = tryResetStatus(player, event.getNewSlot());

        if (wasRestarted) {
            player.sendMessage("Restarted effect");
        }
        /*
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
         */
    }

    /*
        Resets the status of the block effect
         - if the task was successfully restarted, return true
         - otherwise false
     */
    public boolean tryResetStatus(Player player, int slot) {
        if (task != null && !task.isCancelled()) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
        }

        ItemStack itemStack = player.getInventory().getItem(slot);

        if (itemStack != null && (ItemManager.isExcavator(itemStack) || ItemManager.isHammer(itemStack))) {
            task = new BlockOutline(player).runTaskTimer(SpigotTools.getPlugin(), 0, 1);
            return true;
        }
        return false;
    }
}
