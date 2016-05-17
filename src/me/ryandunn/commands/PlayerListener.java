package me.ryandunn.commands;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {

    PlayerListener (){
        Bukkit.getPluginManager().registerEvents (this, Main.getInstance());
    }



    @EventHandler
    public void onPlayerCommand (final PlayerCommandPreprocessEvent event){
        final LocalPlayer localPlayer = LocalPlayer.get (event.getPlayer ());
        final String [] messageChunks = pEvent.getMessage ().split (" ");

        // Determine if the command exists and attempt to execute it.
        if (CommandManager.getInstance().hasCommand (messageChunks [0]) &&
                //CommandManager.getInstance ().execute (messageChunks, localPlayer)) {
            pEvent.setCancelled (true);
        }
    }

}
