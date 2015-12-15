package net.inkyquill.equestria.ca.commands;

import guava10.com.google.common.base.Joiner;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class GMSayCommand implements CommandExecutor {
    static String PIntro;

    static {
        PIntro = ChatColor.GOLD + "[CA/GM] ";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {


        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(PIntro + ChatColor.RED + "Sorry, but you can't use this command from console!");
            return false;
        }
        if (!sender.hasPermission(CASettings.gm)) {
            sender.sendMessage(PIntro + ChatColor.RED + "Sorry, but you have no permissions for this command!");
            return false;
        }

        Player p = (Player) sender;
        PlayerSettings ps = CASettings.getPlayerSettings(p);
        String Message = Joiner.on(" ").join(strings);

        CASettings.L.info("[GM] " + ps.GM.Prefix + " " + Message);

        String str = String.valueOf(ps.GM.Color) +
                ps.GM.Prefix +
                " " +
                Message;

        for (Player recipient : Bukkit.getOnlinePlayers()) {

            if (p.equals(recipient)) {
                p.sendMessage(str);
                continue;
            }
            if (!p.getWorld().equals(recipient.getWorld()) || (p.getLocation().distance(recipient.getLocation())) > (double) ps.GM.Radius)
                continue;
            recipient.sendMessage(str);
        }
        return true;
    }
}
