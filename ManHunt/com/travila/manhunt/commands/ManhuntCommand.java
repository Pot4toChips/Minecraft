package com.travila.manhunt.commands;

import com.travila.manhunt.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ManhuntCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Player p;
    private boolean isActive;
    private int taskID;

    public ManhuntCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("manhunt").setExecutor(this);
        plugin.getCommand("manhunt").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only for Players!");
            return false;
        }

        p = (Player) sender;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("target")) {
                if(args.length > 1) {
                    TargetCMD(args[1]);
                }
                else {
                    p.sendMessage("§cYou need to select a player. Try: /manhunt target [Player]");
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("stop")){
                StopCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("help")){
                HelpCMD();
                return true;
            }
            else {
                p.sendMessage("§cWrong arguments! Try: /manhunt help");
            }
        }
        else {
            p.sendMessage("§6Version: §f1.0");
        }

        return false;
    }

    private void HelpCMD() {
        p.sendMessage("§6/manhunt target: §fShow a players position with compass");
        p.sendMessage("§6/manhunt stop: §fStop tracking the player");
    }

    private void StopCMD() {
        if (isActive){
            Bukkit.getScheduler().cancelTask(taskID);
            p.sendMessage("§aManhunt has been stopped.");
            isActive = false;
        }
        else {
            p.sendMessage("§cManhunt is not active. Try: /manhunt target [Player]");
        }
    }

    private void TargetCMD(String name) {
        Player target = null;
        try{
            target = Bukkit.getPlayer(name);
        }
        catch (Exception ex){
            throw ex;
        }

        if (target == null){
            p.sendMessage("§cThere is no player with the name: §f" + name);
            return;
        }
        else {
            p.sendMessage("§aYou are tracking: §f" + name);
        }
        final Player finalTarget = target;

        if (!isActive){
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (Player t : Bukkit.getOnlinePlayers()){
                        t.setCompassTarget(finalTarget.getLocation());
                    }
                }
            }, 0, 16L);
            isActive = true;
        }
        else {
            p.sendMessage("§cManhunt is already active. Try: /manhunt stop");
        }
    }

    ArrayList<String> arguments = new ArrayList<String>();
    ArrayList<String> onlinePlayers = new ArrayList<String>();
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()){
            arguments.add("target");
            arguments.add("stop");
            arguments.add("help");
        }
        if (onlinePlayers.isEmpty()){
            for (Player t : Bukkit.getOnlinePlayers()){
                onlinePlayers.add(t.getName());
            }
        }

        ArrayList<String> result = new ArrayList<String>();
        if (args.length == 1){
            for (String a : arguments){
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        else if(args.length > 1 && args[0].equalsIgnoreCase("target")){
            for (String a : onlinePlayers){
                if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }
}