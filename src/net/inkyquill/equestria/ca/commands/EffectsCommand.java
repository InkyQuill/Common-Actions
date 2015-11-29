package net.inkyquill.equestria.ca.commands;

import com.google.common.base.Joiner;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.Effect;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by obruchnikov_pa on 26.11.2015.
 */
public class EffectsCommand implements CommandExecutor {
    static String PIntro;

    static {
        PIntro = ChatColor.GOLD + "[CA/Effects] ";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] param) {

        //Todo: add other command options!

        if (commandSender.hasPermission(CASettings.effects)) {
            commandSender.sendMessage(PIntro + ChatColor.RED + "Sorry, you have no permission for this.");
            return false;
        }

        if (param == null || param.length == 0) {
            this.handleNoArgsCommand((Player) commandsender);
        } else if (param.length == 1) {
            if (param[0].toLowerCase().equals("all")) {
                List<String> arr = new ArrayList<String>();
                for (String key : Effect.EffectsMap.keySet()) {
                    arr.add(Effect.EffectsMap.get(key).getName() + " (" + key + ")");
                }
                commandSender.sendMessage(PIntro + ChatColor.WHITE + "All effects: " + ChatColor.AQUA + Joiner.on(", ").join(arr));
                return true;
            } else if (param[0].toLowerCase().equals("my")) {
                Player p = (Player) commandSender;
                commandSender.sendMessage(PIntro + ChatColor.WHITE + "Your effects: " + ChatColor.AQUA + Joiner.on(", ").join(CASettings.getPlayerSettings(p).Effects));
                return true;
            } else if (param[0].toLowerCase().equals("help")) {
                showHelp(commandSender);
                return true;
            } else {
                showHelp(commandSender);
                return true;
            }
        }


        return false;
    }
}
