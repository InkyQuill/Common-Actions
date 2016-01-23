package net.inkyquill.equestria.ca.commands;

import net.inkyquill.equestria.ca.settings.CASettings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CACommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        commandSender.sendMessage(ChatColor.GOLD + "[CA] " + ChatColor.AQUA + "CommonActions v" + CASettings.Version + " by InkyQuill");
        commandSender.sendMessage(ChatColor.GOLD + "[CA] " + ChatColor.AQUA + "Available commands:");
        commandSender.sendMessage(ChatColor.GOLD + "[CA] " + ChatColor.AQUA + "/effects (/eff), /death, /meteo (/wth), /gmi, /gms, /mobrestrict (/mr), /timemanager (/tm)");

        return false;
    }
}
