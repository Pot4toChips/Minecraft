package com.travila.achievementhunt;

import com.travila.achievementhunt.commands.AHuntCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public Game game;
    public boolean isRunning;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        new AHuntCommand(this);
        new TabComplete(this);
        game = new Game(this);
        isRunning = false;
    }

    public void StartGame(){
        game.StartGame();
        isRunning = true;
    }
    public void EndGame(){
        game.EndGame();
        isRunning = false;
    }
}
