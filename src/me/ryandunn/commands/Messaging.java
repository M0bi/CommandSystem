package me.ryandunn.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Messaging {
    private static void sendMessage (final Player player, String message, final Object... args){

        if (player != null && player.isOnline ()){
            message = ChatColor.translateAlternateColorCodes('&', message);
            player.sendMessage (args.length != 0 ? String.format (message, args) : message);
        }
    }

    public static void sendUsage (final Player player, final Object... args) {
        if (player != null && player.isOnline ()) {
            sendMessage (player, "&4Usage&f: %s %s", args);
        }
    }

    public static void sendDescription (final Player player, final Object... args) {
        if (player != null && player.isOnline ()) {
            sendMessage (player, "&4Description&f: %s", args);
        }
    }

    private static final Logger logger = Logger.getLogger(Main.class.getSimpleName());

    private static void log (final Level level, final String message, Object... args){
        logger.log (level, (args.length != 0 ? String.format (message, args) : message));
    }

    public static void info (final String message, final Object... args){
        log (Level.INFO, message, args);
    }

    public static void severe (final String message, final Object... args){
        log (Level.SEVERE, message, args);
    }
}
