package me.ryandunn.commands.annotations;

public @interface CommandPermission {

    String permission () default "";
}
