package me.ryandunn.commands.interfaces;

import org.bukkit.entity.Player;

public interface CommandSender {

    // public LocalPlayer getLocalPlayer ();

    public Player getBukkitPlayer ();

    public String getName ();
}
