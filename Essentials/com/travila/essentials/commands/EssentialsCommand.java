package com.travila.essentials.commands;

import com.travila.essentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EssentialsCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Player p;

    public EssentialsCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("essentials").setExecutor(this);
        plugin.getCommand("essentials").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only for Players!");
            return false;
        }

        p = (Player) sender;

        if (args.length > 0){
            if (args[0].equalsIgnoreCase("help")) {
                HelpCMD();
                return true;
            }
            else {
                p.sendMessage("§cWrong arguments! Try: /essentials help");
                return true;
            }
        }
        else {
            p.sendMessage("§6Version: §f1.0");
        }

        return false;
    }

    private void HelpCMD(){
        p.sendMessage("§6/gm (0-3): §fChange gamemode");
        p.sendMessage("§6/formatchat (on/off/set-removeprefix): §fChange chat formatting");
        p.sendMessage("§6/clearchat: §fClear the chat");
        p.sendMessage("§6/enchant [name] [level]: §fEnchant to any level");
    }

    ArrayList<String> arguments = new ArrayList<String>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()){
            arguments.add("help");
        }

        ArrayList<String> result = new ArrayList<String>();
        if (args.length == 1){
            for (String a : arguments){
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }

        return null;
    }
}