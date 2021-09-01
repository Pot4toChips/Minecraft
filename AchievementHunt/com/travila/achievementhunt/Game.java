package com.travila.achievementhunt;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    private Main plugin;

    public Game(Main plugin) {
        this.plugin = plugin;
        playersData = new ArrayList<PlayerData>();
        notFinishedPlayers = new ArrayList<PlayerData>();
        loserPlayers = new ArrayList<PlayerData>();
        ReadConfig();
    }

    public int[] waitTimes;
    public int rounds;
    public int teleportRange;

    public int timeRemains;
    private int status;
    private Thread gameThread;
    public ArrayList<PlayerData> playersData;
    public ArrayList<PlayerData> notFinishedPlayers;
    public ArrayList<PlayerData> loserPlayers;

    private boolean isEnded = true;

    public void StartGame() {
        isEnded = false;
        status = 0;
        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GameLoop();
            }
        });
        gameThread.start();
    }

    public void EndGame() {
        SayToEverybody("§aThe game has been ended.");
        isEnded = true;
        plugin.isRunning = false;

        playersData = new ArrayList<PlayerData>();
        notFinishedPlayers = new ArrayList<PlayerData>();
        loserPlayers = new ArrayList<PlayerData>();
    }

    public void GameLoop() {
        OneLoop(TaskContainer.StartTasks);
        OneLoop(TaskContainer.EasyTasks);
        OneLoop(TaskContainer.MediumTasks);
        OneLoop(TaskContainer.HardTasks);
        OneLoop(TaskContainer.ExtremeTasks);
        OneLoop(TaskContainer.ImpossibleTasks);
        OneLoop(TaskContainer.EndgameTasks);

        String winners = "§6";
        for (int i = 0; i < playersData.size(); i++){
            if (i == playersData.size() - 1){
                winners +=  " and " + playersData.get(i).player.getName();
            } else if (i == playersData.size() - 2){
                winners +=  playersData.get(i).player.getName();
            } else {
                winners +=  playersData.get(i).player.getName() + ", ";
            }
        }
        SayToEverybody("§7=====================================================");
        SayToEverybody(winners + "§f have won. Well played guys!");
        SayToEverybody("§7=====================================================");
        EndGame();
    }
    public void OneLoop(ItemStack[] itemStacks){
        for (int i = 0; i < rounds; i++) {
            if (isEnded){
                gameThread.interrupt();
                break;
            }
            GiveTask(itemStacks);
            for (int q = 0; q <= waitTimes[status]; q++) {
                if (isEnded){
                    gameThread.interrupt();
                    break;
                }
                else{
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    if (isEnded){
                        gameThread.interrupt();
                        break;
                    }
                    CheckForDone();
                    timeRemains = waitTimes[status] - q;
                    for (int j = 0; j < playersData.size(); j++) {
                        playersData.get(j).player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6Time remaining: §f" + timeRemains + "s"));
                    }
                }
            }
            status++;
            CheckForLoser();
            CheckForWinner();
        }
    }

    public void ReadConfig() {
        waitTimes = new int[7];
        waitTimes[0] = plugin.getConfig().getInt("StartTime");
        waitTimes[1] = plugin.getConfig().getInt("EasyTime");
        waitTimes[2] = plugin.getConfig().getInt("MediumTime");
        waitTimes[3] = plugin.getConfig().getInt("HardTime");
        waitTimes[4] = plugin.getConfig().getInt("ExtremeTime");
        waitTimes[5] = plugin.getConfig().getInt("ImpossibleTime");
        waitTimes[6] = plugin.getConfig().getInt("EndgameTime");
        rounds = plugin.getConfig().getInt("Rounds");
        teleportRange = plugin.getConfig().getInt("TeleportRange");
    }

    public void GiveTask(ItemStack[] itemStacks) {
        for (int i = 0; i < playersData.size(); i++) {
            Random r = new Random();
            playersData.get(i).achievement = new Achievement(itemStacks[r.nextInt(itemStacks.length)]);
            playersData.get(i).player.sendMessage("§7===============================");
            playersData.get(i).player.sendMessage("§aYour task is:");
            playersData.get(i).player.sendMessage(" §e- " + playersData.get(i).achievement.GetName());
            playersData.get(i).player.sendMessage("§7===============================");
        }
        notFinishedPlayers = new ArrayList<PlayerData>(playersData);
    }

    public void CheckForDone() {
        for (int i = 0; i < notFinishedPlayers.size(); i++) {
            notFinishedPlayers.get(i).achievement.CheckForItem(notFinishedPlayers.get(i).player);
            if (notFinishedPlayers.get(i).achievement.isDone) {
                SayToEverybody("§6" + notFinishedPlayers.get(i).player.getName() + " §fis done.");
                SayToEverybody("§7 - §6The Achievement was: " + notFinishedPlayers.get(i).achievement.GetName());
                notFinishedPlayers.remove(notFinishedPlayers.get(i));
            }
        }
    }

    public void CheckForLoser() {
        if (notFinishedPlayers.size() > 0) {
            for (int i = 0; i < notFinishedPlayers.size(); i++) {
                SayToEverybody("§c" + notFinishedPlayers.get(i).player.getName() + " is out.");
                SayToEverybody("§7 - §cThe Achievement was: " + notFinishedPlayers.get(i).achievement.GetName());
                loserPlayers.add(notFinishedPlayers.get(i));
                playersData.remove(notFinishedPlayers.get(i));
            }
        }
    }

    public void CheckForWinner() {
        System.out.println(playersData.size());
        if (playersData.size() == 1) {
            SayToEverybody("§7==============================================");
            SayToEverybody("§6" + playersData.get(0).player.getName() + "§f has won! Congratulations to everyone!");
            SayToEverybody("§7==============================================");
            EndGame();
        } else if (playersData.size() == 0) {
            SayToEverybody("§7==============================");
            SayToEverybody("§cEverybody lost, there is no winner!");
            SayToEverybody("§7==============================");
            EndGame();
        }
    }

    public void SayToEverybody(String msg) {
        for (int i = 0; i < playersData.size(); i++) {
            playersData.get(i).player.sendMessage(msg);
        }
        for (int i = 0; i < loserPlayers.size(); i++){
            loserPlayers.get(i).player.sendMessage(msg);
        }
    }
}
