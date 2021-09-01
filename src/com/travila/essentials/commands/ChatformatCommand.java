package com.travila.essentials.commands;

import com.travila.essentials.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatformatCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Player p;

    public ChatformatCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("formatchat").setExecutor(this);
        plugin.getCommand("formatchat").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only for Players!");
            return false;
        }

        p = (Player) sender;

        if (args.length > 0){
            if (args[0].equalsIgnoreCase("on")) {
                OnCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("off")) {
                OffCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("setprefix")) {
                SetPrefixCMD(args);
                return true;
            }
            else if (args[0].equalsIgnoreCase("removeprefix")) {
                RemovePrefixCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("debug")) {
                DebugCMD();
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

    private void OnCMD(){
        if (plugin.formatChat){
            p.sendMessage("§cChat formatting is already enabled.");
        }
        else{
            plugin.formatChat = true;
            p.sendMessage("§aEnabling chat formatting.");
        }
    }
    private void OffCMD(){
        if (plugin.formatChat){
            plugin.formatChat = false;
            p.sendMessage("§aDisabling chat formatting.");
        }
        else{
            p.sendMessage("§cChat formatting is already disabled.");
        }
    }
    private void SetPrefixCMD(String[] args){
        if (args.length > 1 && args[1] != null && args[1] != ""){
            String prefix = "";
            for (int i = 1; i < args.length; i++){
                prefix += i != args.length - 1 ? args[i] + " " : args[i];
            }
            Main.prefixes.put(p.getUniqueId(), prefix);
            p.sendMessage("§aYour new prefix is: §r" + ChatColor.translateAlternateColorCodes('&', prefix));
        }
        else{
            p.sendMessage("§cWrong arguments after /formatchat setprefix [prefix] [namecolor].");
        }
    }
    private void RemovePrefixCMD(){
        if (Main.prefixes.containsKey(p.getUniqueId())){
            Main.prefixes.remove(p.getUniqueId());
            p.sendMessage("§aYour prefix has been removed.");
        }
        else {
            p.sendMessage("§cYou don't have a prefix to remove.");
        }
    }
    private void DebugCMD(){
        p.sendMessage("§6Players using prefixes: §f" + Main.prefixes.size());
    }

    ArrayList<String> arguments = new ArrayList<String>();
    ArrayList<String> prefixExamples = new ArrayList<String>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()){
            arguments.add("on");
            arguments.add("off");
            arguments.add("setprefix");
            arguments.add("removeprefix");
            arguments.add("debug");
        }
        if (prefixExamples.isEmpty()){
            prefixExamples.add("&7[&6Dávidka&7] &r");
            prefixExamples.add("&7[&9Krisztiánka&7] &r");
            prefixExamples.add("&7[&5Pindúrpandúr&7] &r");
            prefixExamples.add("&7[&dMátéka&7] &r");
            prefixExamples.add("&7[&cFaszláma&7] &r");
            prefixExamples.add("&7[&aGaren Main&7] &r");
        }

        ArrayList<String> result = new ArrayList<String>();
        if (args.length == 1){
            for (String a : arguments){
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        else if(args.length > 1 && args[0].equalsIgnoreCase("setprefix")){
            for (String a : prefixExamples){
                if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }
}
