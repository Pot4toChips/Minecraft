package com.travila.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerScoreboard {

    private Player p;
    private Main plugin;


    public PlayerScoreboard(Main plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
    }

    public void CreateScoreboard(){
        String boardName = "&4&lDavid&c&lMinecraftozik";

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("Scoreboard", "dummy", ChatColor.translateAlternateColorCodes('&', boardName));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        String[] lines = {
                "§7§m--------------------------§r",
                "§f§lNearest Player §7»",
                "§c" + FindNearestPlayerName(p),
                "  ",
                "§f§lBiome §7» §c" + FormatBiomeText(p.getLocation().getBlock().getBiome().name()),
                "§f§lTime §7» §c" + CalculateTime(),
                "§f§lLocation §7» §c" + (int)p.getLocation().getX() + " " + (int)p.getLocation().getY() + " " + (int)p.getLocation().getZ(),
                "§f§lOnline §7» §c" + Bukkit.getOnlinePlayers().size(),
                "§f§lTPS §7» §c" + plugin.tps,
                " ",
                "§f§lHealth §7» §c" + (int)Math.ceil(p.getHealth()),
                "§f§lFood §7» §c" + p.getFoodLevel(),
                "§7§m--------------------------",
                "§f§lIP §7» §cDavidMinecraftozik.serv.gs",
        };

        for (int i = 0; i < lines.length; i++){
            String line = lines[i].length() <= 40 ? lines[i] : lines[i].substring(0, 37) + "...";
            Score score = obj.getScore(line);
            score.setScore(lines.length - (i + 1));
        }

        p.setScoreboard(board);
    }
    private String FindNearestPlayerName(Player player){
        Player nearest = null;
        double distance = 0.0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distanceSquared(players.getLocation()) < distance) {
                if (players != player) {
                    distance = player.getLocation().distanceSquared(players.getLocation());
                    nearest = players;

                }
            }
        }
        if (nearest == null){
            return "None";
        }else {
            return nearest.getName() + " (" + (int)Math.sqrt(distance) + ")";
        }
    }
    private String CalculateTime(){
        String timeStr = "";
        long time = p.getWorld().getTime();
        int min = (int) (time / 16.666);
        int hour = 6 + min / 60;
        min -= ((hour - 6) * 60);

        if (hour > 23){
            hour -= 24;
        }

        if (hour < 10){
            timeStr += "0" + hour;
        }
        else {
            timeStr += hour;
        }
        timeStr += ":";
        if (min < 10){
            timeStr += "0" + min;
        }
        else {
            timeStr += min;
        }

        return timeStr;
    }
    private String FormatBiomeText(String biome){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < biome.length(); i++){
            if (i == 0 || biome.charAt(i - 1) == '_'){
                sb.append(biome.charAt(i));
            }
            else {
                if (biome.charAt(i) == '_'){
                    sb.append(' ');
                }
                else{
                    sb.append(String.valueOf(biome.charAt(i)).toLowerCase().charAt(0));
                }
            }
        }

        return sb.toString();
    }
}