/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.inkyquill.equestria.ca.commands;

import guava10.com.google.common.base.Joiner;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GMCommand
implements CommandExecutor {

    static String PIntro;
    static{ PIntro = ChatColor.GOLD + "[CA/GM] ";}

    private static void helpCommand(CommandSender sender) {
        String message = PIntro +
                ChatColor.WHITE +
                "Usage:";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms set <prefix|color|radius>" +
                ChatColor.WHITE +
                " sets options for gm speech.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms <radius> <color> <prefix>" +
                ChatColor.WHITE +
                " sets options for gm speech in one command.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms colors" +
                ChatColor.WHITE +
                " shows color list";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms <on|off>" +
                ChatColor.WHITE +
                " starts and ends GM chat.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms my" +
                ChatColor.WHITE +
                " shows your current GM Speech settings.";
        sender.sendMessage(message);
    }

    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] as) {

        if(commandsender instanceof ConsoleCommandSender)
        {
            commandsender.sendMessage(PIntro + ChatColor.RED + "Sorry, but you can't use this command from console!");
            return false;
        }
        if (!commandsender.hasPermission(CASettings.gm)) {
            commandsender.sendMessage(PIntro + ChatColor.RED + "Sorry, but you have no permissions for this command!");
            return false;
        }
        if (as == null || as.length == 0) {
            this.handleNoArgsCommand((Player)commandsender);
            return false;
        }
        if ("help".equals(as[0])) {
            helpCommand(commandsender);
            return false;
        }
        if ("colors".equals(as[0])) {
            this.sendColorsListMessage((Player)commandsender);
            return false;
        }
        if ("my".equals(as[0]))
        {
            Player p = (Player)commandsender;
            PlayerSettings ps = CASettings.getPlayerSettings(p);
            p.sendMessage(PIntro + ChatColor.WHITE + "Your current settings are:");
            p.sendMessage(PIntro + ChatColor.AQUA + "Enabled: " +  ChatColor.WHITE + (ps.GM.Enabled?"true":"false"));
            p.sendMessage(PIntro + ChatColor.AQUA + "Color: " + ChatColor.WHITE + ps.GM.Color.name());
            p.sendMessage(PIntro + ChatColor.AQUA + "Prefix: " + ChatColor.WHITE + ps.GM.Prefix);
            p.sendMessage(PIntro + ChatColor.AQUA + "Radius: " + ChatColor.WHITE + ps.GM.Radius);
            return false;
        }
        if ("on".equals(as[0]))
        {
            Player p = (Player)commandsender;
            PlayerSettings ps = CASettings.getPlayerSettings(p);
            ps.GM.Enabled = true;
            p.sendMessage(PIntro + ChatColor.WHITE + "GM chat is now ON");
            return false;
        }
        if ("off".equals(as[0]))
        {
            Player p = (Player)commandsender;
            PlayerSettings ps = CASettings.getPlayerSettings(p);
            ps.GM.Enabled = false;
            p.sendMessage(PIntro + ChatColor.WHITE + "GM chat is now OFF");
            return false;
        }
        if (as.length > 1) {
            return this.handleValidCommand((Player)commandsender, as);
        }
        this.handleNoArgsCommand((Player)commandsender);
        return false;
    }

    private boolean handleValidCommand(Player commandsender, String[] as) {
        PlayerSettings ps = CASettings.getPlayerSettings(commandsender);
        if(as[0].equals("set"))
        {
            if(as[1].equals("color"))
            {
                if(as.length<3)
                {
                    String message = PIntro +
                            ChatColor.WHITE +
                            "You should specify the color. To view color list, use " +
                            ChatColor.AQUA +
                            "/gms colors";
                    commandsender.sendMessage(message);
                }
                else
                {
                    String color = as[2];

                    try{
                    if(color.trim().length() == 1)
                    {
                         ps.GM.Color = ChatColor.getByChar(color.trim().toLowerCase().charAt(0));
                    }
                    else
                    {
                        ps.GM.Color = ChatColor.valueOf(color.trim().toUpperCase());
                    }
                    }
                    catch(Exception e)
                    {
                        commandsender.sendMessage(PIntro + ChatColor.RED + "Wrong color specified.");
                    }
                    commandsender.sendMessage(PIntro + ChatColor.WHITE + "Your color is " + ps.GM.Color + ps.GM.Color.name());
                }
            }
            else if (as[1].equals("radius"))
            {
                if(as.length<3)
                {
                    String message = PIntro +
                            ChatColor.WHITE +
                            "You should specify the radius.";
                    commandsender.sendMessage(message);
                }
                else
                {
                    try {
                        ps.GM.Radius = Integer.parseInt(as[2]);
                        commandsender.sendMessage(PIntro + ChatColor.WHITE + "Your radius is " + ChatColor.BOLD + ps.GM.Prefix);
                    }
                    catch(Exception e)
                    {
                        commandsender.sendMessage(PIntro + ChatColor.RED + "Wrong radius specified.");
                    }
                }
            }
            else if (as[1].equals("prefix"))
            {
                if(as.length<3)
                {
                    String message = PIntro +
                            ChatColor.WHITE +
                            "You should specify the prefix.";
                    commandsender.sendMessage(message);
                }
                else
                {
                    List<String> tmp = new ArrayList<String>();
                    tmp.addAll(Arrays.asList(as).subList(2, as.length));
                    ps.GM.Prefix = Joiner.on(" ").join(tmp);
                    commandsender.sendMessage(PIntro + ChatColor.WHITE + "Your prefix is " + ChatColor.BOLD + ps.GM.Prefix);
                }
            }
            else
            {
                commandsender.sendMessage(PIntro + ChatColor.RED + "Wrong option specified.");
            }
            commandsender.sendMessage(PIntro + ps.GM.Color + ps.GM.Prefix + " " + "Example Text");
        }
        else
        {
            if(as.length>2)
            {
                String rad = as[0];
                String color = as[1];
                List<String> tmp = new ArrayList<String>();
                tmp.addAll(Arrays.asList(as).subList(2, as.length));
                try {
                    ps.GM.Radius = Integer.parseInt(rad);
                }
                catch(Exception e)
                {
                    commandsender.sendMessage(PIntro + ChatColor.RED + "Wrong radius specified.");
                }
                try{
                    if(color.trim().length() == 1)
                    {
                        ps.GM.Color = ChatColor.getByChar(color.trim().toLowerCase().charAt(0));
                    }
                    else
                    {
                        ps.GM.Color = ChatColor.valueOf(color.trim().toUpperCase());
                    }
                }
                catch(Exception e)
                {
                    commandsender.sendMessage(PIntro + ChatColor.RED + "Wrong color specified.");
                }
                ps.GM.Prefix = Joiner.on(" ").join(tmp);

                commandsender.sendMessage(PIntro + ps.GM.Color + ps.GM.Prefix + " " + "Example Text with radius set to " + ps.GM.Radius);
            }
            else
            {
                commandsender.sendMessage(PIntro + ChatColor.RED + "Wrong options specified.");
            }
        }
        return true;
    }

    private void handleNoArgsCommand(Player player) {
        String message = PIntro +
                ChatColor.WHITE +
                "To view help, use " +
                ChatColor.AQUA +
                "/gms help";
        player.sendMessage(message);
    }

    private void sendColorsListMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append(PIntro);
        message.append(ChatColor.WHITE);
        message.append("Available colors - ");
        for (ChatColor color : ChatColor.values()) {
            message.append(color.name());
            message.append("(");
            message.append(color.getChar());
            message.append(") ");
        }
        player.sendMessage(message.toString());
    }
}

