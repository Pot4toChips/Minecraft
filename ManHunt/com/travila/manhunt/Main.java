package com.travila.manhunt;

import com.travila.manhunt.commands.ManhuntCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    @Override
    public void onEnable() {
        new ManhuntCommand(this);
    }
}
