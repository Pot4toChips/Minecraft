package com.travila.essentials.commands;

import com.travila.essentials.Main;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearchatCommand implements CommandExecutor {

    private Main plugin;
    private Player p;

    public ClearchatCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("clearchat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only for Players!");
            return false;
        }

        p = (Player) sender;
        for (int i = 0; i < 100; i++){
            plugin.SayToEverybody("");
        }
        plugin.SayToEverybody("§6" + p.getDisplayName() + " §fhas cleared the chat.");

        return false;
    }
}