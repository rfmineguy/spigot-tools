package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class JoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        byte status = player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SpigotTools.getPlugin(), "effect"), PersistentDataType.BYTE, (byte) 0);
        String strStatus = status == 1 ? "on." : "off.";
        player.sendMessage(ChatColor.ITALIC + "INFO:");
        player.sendMessage(ChatColor.ITALIC + "Your hammer/excavator indicator's status is currently : " + strStatus);
        player.sendMessage(ChatColor.ITALIC + "To change it use " + ChatColor.BLUE + "/st-effect effect [on/off]");

        SpigotTools.LOGGER.info("Work on hosting this resource pack");
        event.setJoinMessage(ChatColor.UNDERLINE + "SpigotTools :\n" +
                ChatColor.AQUA + "   This server has the Spigot Tools plugin installed. " +
                "It makes use of a custom resource pack to improve the playing experience." +
                "A link to download it can be found here : <currently pending curseforge review>");

        //event.getPlayer().setResourcePack("https://drive.google.com/file/d/1-9Se32IkWBhDzrwYI-CGcdkwU11WGr6R/view?usp=sharing"); //direct download to resource pack
    }
}