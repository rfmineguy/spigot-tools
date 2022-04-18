package me.rfmineguy.spigot_hammers.event_listeners;

import me.rfmineguy.spigot_hammers.SpigotTools;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SpigotTools.LOGGER.info("Work on hosting this resource pack");
        event.getPlayer().sendMessage();
        event.setJoinMessage(ChatColor.UNDERLINE + "SpigotTools :\n" +
                ChatColor.AQUA + "   This server has the Spigot Tools plugin installed. " +
                "It makes use of a custom resource pack to improve the playing experience." +
                "A link to download it can be found here : <currently pending curseforge review>");

        //event.getPlayer().setResourcePack("https://drive.google.com/file/d/1-9Se32IkWBhDzrwYI-CGcdkwU11WGr6R/view?usp=sharing"); //direct download to resource pack
    }
}