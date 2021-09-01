package com.travila.essentials;

import com.travila.essentials.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    public boolean formatChat;
    public static Map<UUID, String> prefixes;

    @Override
    public void onEnable() {
        prefixes = new HashMap<UUID, String>();
        this.getServer().getPluginManager().registerEvents(this, this);
        formatChat = false;

        new EssentialsCommand(this);
        new GamemodeCommand(this);
        new ChatformatCommand(this);
        new ClearchatCommand(this);
        new EnchantCommand(this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage("§7[§a+§7] §r" + event.getPlayer().getDisplayName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage("§7[§c-§7] §r" + event.getPlayer().getDisplayName());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if (formatChat){
            String msg = ChatColor.translateAlternateColorCodes('&', event.getMessage());
            event.setCancelled(true);

            String prefix = prefixes.containsKey(event.getPlayer().getUniqueId()) ? prefixes.get(event.getPlayer().getUniqueId()) : "";
            SayToEverybody( ChatColor.translateAlternateColorCodes('&', prefix) + event.getPlayer().getDisplayName() + " §7» §r" + msg);
        }
    }

    public void SayToEverybody(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }
}
