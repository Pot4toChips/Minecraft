package com.travila.essentials.commands;

import com.travila.essentials.Main;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Player p;

    private String[] enchantNames = {
            "Fire_Protection",
            "Sharpness",
            "Flame",
            "Soul_Speed",
            "Aqua_Affinity",
            "Punch",
            "Loyalty",
            "Depth_Strider",
            "Vanishing_Curse",
            "Unbreaking",
            "Knockback",
            "Luck_of_the_Sea",
            "Binding_Curse",
            "Fortune",
            "Protection",
            "Efficiency",
            "Mending",
            "Frost_Walker",
            "Lure",
            "Looting",
            "Piercing",
            "Blast_Protection",
            "Smite",
            "Multishot",
            "Fire_Aspect",
            "Channeling",
            "Sweeping",
            "Thorns",
            "Bane_of_Arthropods",
            "Respiration",
            "Riptide",
            "Silk_Touch",
            "Quick_Charge",
            "Projectile_Protection",
            "Impaling",
            "Feather_Falling",
            "Power",
            "Infinity",
    };

    public EnchantCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("enchant").setExecutor(this);
        plugin.getCommand("enchant").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only for Players!");
            return false;
        }

        p = (Player) sender;

        if (args.length > 1){
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR)
            {
                if (args[1].matches("[0-9]+") || (args[1].charAt(0) == '-' && args[1].substring(1).matches("[0-9]+"))){
                    for (int i = 0; i < enchantNames.length; i++){
                        if (args[0].equalsIgnoreCase(enchantNames[i])){
                            int level = Integer.parseInt(args[1]);
                            if (level == 0){
                                p.getInventory().getItemInMainHand().removeEnchantment(Enchantment.values()[i]);
                            }
                            else {
                                p.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.values()[i], level);
                            }
                            return true;
                        }
                    }
                    p.sendMessage("§cInvalid enchantment name!");
                }
                else {
                    p.sendMessage("§cWrong arguments! Try: /enchant [name] [level]");
                }
            }
            else {
                p.sendMessage("§cYou don't have an item in your hand");
            }
            return true;
        }
        else if (args.length == 1){
            p.sendMessage("§cWrong arguments! Try: /enchant [name] [level]");
        }
        else {
            p.sendMessage("§6Version: §f1.0");
        }
        return false;
    }

    ArrayList<String> arguments = new ArrayList<String>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()){
            for (int i = 0; i < enchantNames.length; i++){
                arguments.add(enchantNames[i]);
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

        return null;
    }
}