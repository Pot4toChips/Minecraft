package com.travila.achievementhunt.commands;

import com.travila.achievementhunt.Main;
import com.travila.achievementhunt.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class AHuntCommand implements CommandExecutor {

    private Main plugin;
    private Player p;
    private Thread startGameThread;

    public AHuntCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("achievementhunt").setExecutor(this);
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
            else if (args[0].equalsIgnoreCase("join")) {
                JoinCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("leave")) {
                LeaveCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("list")){
                ListCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("settings")){
                SettingsCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("start")){
                StartCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("end")){
                EndCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("playertask")){
                PlayertaskCMD();
                return true;
            }
            else {
                p.sendMessage("§cWrong arguments! Try: /ahunt help");
                return true;
            }
        }
        else {
            p.sendMessage("§6Version: §f1.0");
        }

        return false;
    }

    private void HelpCMD(){
        p.sendMessage("§6/ahunt join: §fJoin to the lobby");
        p.sendMessage("§6/ahunt leave: §fLeave from the lobby");
        p.sendMessage("§6/ahunt list: §fGet the player list");
        p.sendMessage("§6/ahunt settings: §fGet settings of the game");
        p.sendMessage("§6/ahunt start: §fStart the game");
        p.sendMessage("§6/ahunt end: §fEnd the game");
        p.sendMessage("§6/ahunt playertask: §fShow players task and remaining time");
    }
    private void JoinCMD(){
        boolean flag = false;
        for (int i = 0; i < plugin.game.playersData.size(); i++){
            if (plugin.game.playersData.get(i).player == p){
                flag = true;
            }
        }

        if (flag){
            p.sendMessage("§cYou've already joined to the player list.");
        } else {
            plugin.game.playersData.add(new PlayerData(p));
            p.sendMessage("§aYou've joined to the player list.");
        }
    }
    private void LeaveCMD(){
        PlayerData playerData = null;
        for (int i = 0; i < plugin.game.playersData.size(); i++){
            if (plugin.game.playersData.get(i).player == p){
                playerData = plugin.game.playersData.get(i);
            }
        }

        if (playerData != null){
            plugin.game.playersData.remove(playerData);
            p.sendMessage("§aYou left the player list.");
        } else {
            p.sendMessage("§cYou are not on the player list.");
        }
    }
    private void ListCMD(){
        p.sendMessage("§6Player List: §f" + plugin.game.playersData.size());
        for (int i = 0; i < plugin.game.playersData.size(); i++){
            p.sendMessage(" - " + plugin.game.playersData.get(i).player.getName());
        }
    }
    private void SettingsCMD(){
        p.sendMessage("§6Round count: §f" + plugin.game.rounds);
        p.sendMessage("§6Teleport range: §f" + plugin.game.teleportRange);
        p.sendMessage("§6Times:");
        p.sendMessage(" §7- §6Start: §f" + plugin.game.waitTimes[0] + "s");
        p.sendMessage(" §7- §6Easy: §f" + plugin.game.waitTimes[1] + "s");
        p.sendMessage(" §7- §6Medium: §f" + plugin.game.waitTimes[2] + "s");
        p.sendMessage(" §7- §6Hard: §f" + plugin.game.waitTimes[3] + "s");
        p.sendMessage(" §7- §6Extreme: §f" + plugin.game.waitTimes[4] + "s");
        p.sendMessage(" §7- §6Impossible: §f" + plugin.game.waitTimes[5] + "s");
        p.sendMessage(" §7- §6Endgame: §f" + plugin.game.waitTimes[6] + "s");
    }
    private void StartCMD(){
        if (plugin.game.playersData.size() > 0){
            if (!plugin.isRunning){
                startGameThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 5; i > 0; i--){
                            plugin.game.SayToEverybody("§6The game is starting in: §f" + i);
                            try{
                                Thread.sleep(1000);
                            }
                            catch (Exception ex){
                                System.out.println(ex);
                            }
                        }
                        plugin.StartGame();
                    }
                });
                startGameThread.start();
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                for (int i = 0; i < plugin.game.playersData.size(); i++){
                    plugin.game.playersData.get(i).player.setGameMode(GameMode.SURVIVAL);
                    plugin.game.playersData.get(i).player.getInventory().clear();
                    plugin.game.playersData.get(i).player.setHealth(20);
                    plugin.game.playersData.get(i).player.setFoodLevel(20);
                    plugin.game.playersData.get(i).player.setBedSpawnLocation(p.getLocation(), true);
                    Bukkit.dispatchCommand(console, "advancement revoke " + plugin.game.playersData.get(i).player.getName() + " everything");
                }
                p.getWorld().setTime(0);
                p.getWorld().setStorm(false);
            }
            else {
                p.sendMessage("§cThe game is already running. You can try: /ahunt end");
            }
        } else {
            p.sendMessage("§cThere is no player on the list.");
        }

        int x, z;
        x = p.getLocation().getBlockX();
        z = p.getLocation().getBlockZ();
        for (int i = 0; i < plugin.game.playersData.size(); i++){
            int random = new Random().nextInt(plugin.game.teleportRange * 2) - plugin.game.teleportRange;
            plugin.game.playersData.get(i).player.teleport(new Location(p.getWorld(), x + random, 255, z + random));
            plugin.game.playersData.get(i).player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 10));
        }
    }
    private void EndCMD(){
        if (plugin.isRunning){
            plugin.game.SayToEverybody("§c" + p.getName() + " stopped the game.");
            plugin.EndGame();
        }
        else {
            p.sendMessage("§cThe game is not running. You can try: /ahunt start");
        }
    }
    private void PlayertaskCMD() {
        if (plugin.isRunning){
            p.sendMessage("§6Time remaining: §f" + plugin.game.timeRemains);
            for (int i = 0; i < plugin.game.playersData.size(); i++){
                p.sendMessage(" §7- §6" + plugin.game.playersData.get(i).player.getName() + "'s task is: " + plugin.game.playersData.get(i).achievement.GetName());
            }
        }
        else {
            p.sendMessage("§cThe game is not running. You can try: /ahunt start");
        }
    }
}
