package com.travila.achievementhunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    private Main plugin;
    ArrayList<String> arguments = new ArrayList<String>();

    public TabComplete(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("achievementhunt").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()){
            arguments.add("help");
            arguments.add("join");
            arguments.add("leave");
            arguments.add("list");
            arguments.add("settings");
            arguments.add("start");
            arguments.add("end");
            arguments.add("playertask");
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
