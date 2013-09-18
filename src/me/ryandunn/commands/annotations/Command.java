package me.ryandunn.commands.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String[] aliases ();

    String usage () default "N/A";

    String usageFormat () default "";

    String description () default "N/A";
}
