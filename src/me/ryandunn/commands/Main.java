package me.ryandunn.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

    private static Main instance;

    @Override
    public void onEnable (){
        instance = this;

        new PlayerListener ();
    }

    public static Main getInstance (){
        return instance;
    }

}
