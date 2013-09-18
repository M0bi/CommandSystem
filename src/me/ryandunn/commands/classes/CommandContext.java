package me.ryandunn.commands.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.regex.Matcher;

public class CommandContext {
    /**
     * The command
     */
    private final String m_sCommand;

    /**
     * The message after the command
     */
    private final String m_sMessage;

    /**
     * The arguments after the command
     */
    private final String [] m_aArguments;

    /**
     * The Matcher instance applied on the arguments if usageFormat is given.
     */
    private Matcher m_pMatcher;

    /**
     * The constructor will populate the members of the class
     */
    public CommandContext (final String sArguments) {
        this (sArguments.split (" "));
    }

    /**
     * The constructor will populate the members of the class
     */
    public CommandContext (final String [] aArguments) {
        m_sCommand      = aArguments [0];
        m_sMessage      = (aArguments.length > 1 ? joinString (aArguments, " ", 1) : "");
        m_aArguments    = m_sMessage.split (" ");
    }

    /**
     * @return True if argument matches command name, otherwise false
     */
    public boolean matches (final String sCommand) {
        return m_sCommand.equalsIgnoreCase (sCommand);
    }

    /**
     * @return The command
     */
    public String getCommand () {
        return m_sCommand;
    }

    /**
     * @return The messages
     */
    public String getMessage () {
        return m_sMessage;
    }

    /**
     * @return String out of the arguments array
     */
    public String getString (final int nIndex) {
        if (nIndex < (size () - 1))
            return null;

        return m_aArguments [nIndex];
    }

    /**
     * @return String out of the arguments array in lower casing
     */
    public String getStringLowerCase (final int nIndex) {
        if (nIndex < (size () - 1))
            return null;

        return m_aArguments [nIndex].toLowerCase ();
    }

    /**
     * @return String out of the arguments array
     */
    public String getString (final int nStart, final int nEnd) {
        if (nStart < (size () - 1) || (nEnd != -1 && nEnd < (size () - 1)))
            return null;

        StringBuilder pBuffer = new StringBuilder ();
        for (int iii = nStart; iii < (nEnd == -1 ? size () : nEnd); iii ++) {
            pBuffer.append (m_aArguments [iii]).append (" ");
        }

        pBuffer.setLength (pBuffer.length () - 1);
        return pBuffer.toString ();
    }

    /**
     * @return Player instance of a name of a player within the arguments array
     */
    public Player getPlayer (final int nIndex) {
        if (nIndex < (size () - 1))
            return null;

        return Bukkit.getPlayer (m_aArguments[nIndex]);
    }

    /**
     * Will set the Matcher instance for the command context
     */
    public void setMatcher (final Matcher pMatcher) {
        m_pMatcher = pMatcher;
    }

    /**
     * @return Matcher of a regex pattern on an argument
     */
    public Matcher getMatcher () {
        return m_pMatcher;
    }

    /**
     * @return Group of the usage pattern as a string
     */
    public String getMatcherString (final int nIndex) {
        return m_pMatcher.group (nIndex);
    }

    /**
     * @return Group of the usage pattern as an integer
     */
    public Integer getMatcherInteger (final int nIndex) {
        final String sValue = getMatcherString (nIndex);

        if (isInteger (sValue))
            return null;

        return Integer.parseInt (sValue);
    }

    /**
     * @return Group of the usage pattern as a Bukkit Player
     */
    public Player getMatcherPlayer (final int nIndex) {
        return Bukkit.getPlayer(getMatcherString(nIndex));
    }

    /**
     * @return Integer out of the arguments array
     */
    public Integer getInteger (final int nIndex) {
        if (nIndex < (size () - 1))
            return null;

        if (isInteger(m_aArguments[nIndex]))
            return null;

        return Integer.parseInt (m_aArguments [nIndex]);
    }

    /**
     * @return The size of the arguments list
     */
    public int size () {
        return (getMessage ().length () == 0 ? 0 : m_aArguments.length);
    }

    private boolean isInteger (String value){
        return value.matches ("\\d+");
    }

    private static String joinString (String [] string, String delimiter, int index) {
        if (string.length == 0)
            return "";

        StringBuilder pBuffer = new StringBuilder (string [index]);
        for (int i = index + 1; i < string.length; ++ i) {
            pBuffer.append (delimiter).append (string [i]);
        }

        return pBuffer.toString ();
    }
}
