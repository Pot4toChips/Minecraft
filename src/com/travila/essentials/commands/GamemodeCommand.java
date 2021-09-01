package com.travila.essentials.commands;

import com.travila.essentials.Main;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    private Main plugin;
    private Player p;

    private GameMode[] gameModes = {
            GameMode.SURVIVAL,
            GameMode.CREATIVE,
            GameMode.ADVENTURE,
            GameMode.SPECTATOR,
    };

    public GamemodeCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("gm").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only for Players!");
            return false;
        }

        p = (Player) sender;

        if (args.length > 0){
            for (int i = 0; i < gameModes.length; i++){
                if (args[0].equalsIgnoreCase(String.valueOf(i))) {
                    p.setGameMode(gameModes[i]);
                    return true;
                }
            }
            p.sendMessage("§cWrong arguments! Try: /essentials help");
            return true;
        }
        else {
            p.sendMessage("§6Version: §f1.0");
        }

        return false;
    }
}
