package com.travila.scoreboard;

import com.travila.scoreboard.commands.ScoreboardCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public int tps = 0;
    private ScoreboardCommand scoreboardCommand;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        scoreboardCommand = new ScoreboardCommand(this);
        new TabComplete(this);

        if (!Bukkit.getOnlinePlayers().isEmpty()){
            for (Player player : Bukkit.getOnlinePlayers()){
                LobbyBoard board = new LobbyBoard(player.getUniqueId());
                if (board.hasID()){
                    board.stop();
                }
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                scoreboardCommand.boards.put(player.getUniqueId(), false);
            }
        }
        CalculateTPS();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        scoreboardCommand.boards.put(event.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        LobbyBoard board = new LobbyBoard(event.getPlayer().getUniqueId());
        if (board.hasID()){
            board.stop();
        }
        scoreboardCommand.boards.remove(event.getPlayer().getUniqueId());
    }

    private int CalculateTPS(){
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            long sec;
            long currentSec;
            int ticks;
            int delay;

            @Override
            public void run()
            {
                sec = (System.currentTimeMillis() / 1000);
                if(currentSec == sec)
                {
                    ticks++;
                }
                else
                {
                    currentSec = sec;
                    tps = (tps == 0 ? ticks : ((tps + ticks) / 2));
                    ticks = 0;
                }
            }
        }, 0, 1);
        return tps;
    }
}
