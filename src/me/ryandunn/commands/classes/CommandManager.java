package me.ryandunn.commands.classes;

import me.ryandunn.commands.Messaging;
import me.ryandunn.commands.annotations.Command;
import me.ryandunn.commands.annotations.CommandPermission;
import me.ryandunn.commands.interfaces.CommandSender;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandManager {

    /**
     * Instance of the class
     */
    private static CommandManager instance;

    /**
     * The mapping of the commands (including aliases)
     */
    private final Map<String, Method> commands = new HashMap<String, Method>();

    /**
     * Used to store the instances associated with a method
     */
    private final Map <Method, Object> instances = new HashMap <Method, Object> ();

    /**
     * Will be used to store the usage formats for the command so we don't have to
     * waste CPU cycles and compile the regex string every time we use the command since
     * the usage format never changes
     */
    private final Map <Method, Pattern> usageFormats = new HashMap <Method, Pattern> ();

    public CommandManager (){
        instance = this;


    }

    /**
     * Registers a class that contains commands.
     */
    private void register (final Class <?> clazz){
        registerMethods (clazz, getInstance(clazz));
    }

    /**
     * Register methods of a command class
     */
    private void registerMethods (final Class <?> clazz, final Object constructor){
        for (final Method method : clazz.getMethods()){

            if (!method.isAnnotationPresent (Command.class))
                    continue;

            Command command     = method.getAnnotation(Command.class);
            boolean isStatic    = Modifier.isStatic (clazz.getModifiers());

            for (String aliases : command.aliases()){
                commands.put (aliases, method);
            }

            if (command.usageFormat().length() != 0){
                usageFormats.put (method, Pattern.compile (command.usageFormat ()));
            }

            if (!isStatic){
                if (constructor == null)
                    continue;

                instances.put (method, constructor);
            }
        }

        if (clazz.getSuperclass() != null){
            registerMethods (clazz.getSuperclass(), constructor);
        }
    }

    /**
     * @return The constructor of the class containing the commands
     */
    private Object getInstance (final Class<?> clazz){
        try{
            Constructor<?> constructor = clazz.getConstructor ();
            constructor.setAccessible (true);
            return constructor.newInstance ();

        }catch (Exception e){
            System.out.println ("Error initializing command class: " + clazz.getSimpleName() + " : ");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Attempts to execute the command
     */
    public boolean execute (final String [] args, final CommandSender sender, final Object... methodArgs){
        Object [] newMethodArgs         = new Object [methodArgs.length + 2];
        System.arraycopy                (methodArgs, 0, newMethodArgs, 2, methodArgs.length);

        return executeMethod            (args, sender, newMethodArgs);
    }

    /**
     * Attempts to execute the command
     */
    public boolean executeMethod (final String [] args, final CommandSender sender, final Object[] methodArgs){
        final String commandName = args [0];
        final Method commandMethod = commands.get (commandName.toLowerCase());

        if (commandMethod == null){
            return false;
        }

        if (!checkPermission (sender, commandMethod)){
            return false;
        }

        final Command command                   = commandMethod.getAnnotation (Command.class);
        final CommandContext commandContext     = new CommandContext (args);

        if (usageFormats.containsKey (commandMethod)){
            final Pattern usageFormat = usageFormats.get (commandMethod);
            final Matcher usageMatcher = usageFormat.matcher (commandContext.getMessage());

            if (!usageMatcher.matches()){
                Messaging.sendUsage (sender.getBukkitPlayer(), commandName, command.usage ());
                Messaging.sendDescription (sender.getBukkitPlayer(), command.description ());

                return true;
            }

            commandContext.setMatcher (usageMatcher);
        }

        methodArgs [0]          = sender;
        methodArgs [1]          = commandContext;
        Object methodInstance   = instances.get (commandMethod);
        return invokeMethod     (commandMethod, methodInstance, methodArgs);
    }

    /**
     * This method will invoke the command method
     */
    private boolean invokeMethod (final Method commandMethod, final Object commandInstance, final Object [] methodArgs){
        try{
            commandMethod.invoke (commandInstance, methodArgs);

        } catch (Exception ex){
            Messaging.severe ("Failed to execute command: ");
            ex.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * Will check whether the executor has the correct permissions to execute the command.
     */
    private boolean checkPermission (final CommandSender sender, final Method commandMethod){
        if (commandMethod.isAnnotationPresent (CommandPermission.class)) {
            final CommandPermission pCommandPermission = commandMethod.getAnnotation (CommandPermission.class);

            if (!sender.getBukkitPlayer ().hasPermission (pCommandPermission.permission ()))
                return false;
        }

        return true;
    }

    /**
     * @return True if command exists, otherwise false
     */
    public boolean hasCommand (final String command){
        return commands.containsKey (command);
    }

    /**
     * @return The instance of this class for easy access.
     */
    public static CommandManager getInstance (){
        return instance;
    }
}
