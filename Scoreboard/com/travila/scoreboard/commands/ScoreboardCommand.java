package com.travila.scoreboard.commands;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.travila.scoreboard.LobbyBoard;
import com.travila.scoreboard.Main;
import com.travila.scoreboard.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardCommand implements CommandExecutor {

    private Main plugin;
    private Player p;
    private int taskID;

    public static Map<UUID, Boolean> boards = new HashMap<UUID, Boolean>();

    public ScoreboardCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("scoreboard").setExecutor(this);
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
            else if (args[0].equalsIgnoreCase("show")) {
                ShowCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("hide")) {
                HideCMD();
                return true;
            }
            else if (args[0].equalsIgnoreCase("debug")) {
                DebugCMD();
                return true;
            }
            else {
                p.sendMessage("§cWrong arguments! Try: /sb help");
                return true;
            }
        }
        else {
            p.sendMessage("§6Version: §f1.0");
        }

        return false;
    }

    private void HelpCMD(){
        p.sendMessage("§6/sb show: §fShow the Scoreboard");
        p.sendMessage("§6/sb hide: §fHide the Scoreboard");
        p.sendMessage("§6/sb debug: §fGive some information about the Scoreboard");
    }

    private void ShowCMD() {
        if (boards.get(p.getUniqueId())){
            p.sendMessage("§cThe scoreboard is already running. Try: /sb hide");
        }
        else{
            boards.put(p.getUniqueId(), true);

            PlayerScoreboard scoreboard = new PlayerScoreboard(plugin, p);
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                LobbyBoard board = new LobbyBoard(p.getUniqueId());
                @Override
                public void run() {
                    if (!board.hasID())
                        board.setID(taskID);
                    scoreboard.CreateScoreboard();
                }
            }, 0, 16L);
        }
    }
    private void HideCMD() {
        if (!boards.get(p.getUniqueId())){
            p.sendMessage("§cThe scoreboard is not running. Try: /sb show");
        }
        else{
            boards.put(p.getUniqueId(), false);

            LobbyBoard board = new LobbyBoard(p.getUniqueId());
            if (board.hasID())
                board.stop();
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
    private void DebugCMD(){
        p.sendMessage("§6Active Scoreboard users: §f" + LobbyBoard.TASKS.size());
        p.sendMessage("§6Scoreboard list length: §f" + boards.size());
    }
}