package net.inkyquill.equestria.ca.commands;

import com.google.common.base.Joiner;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.Effect;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DECommands implements CommandExecutor {

    static String PIntro;

    static {
        PIntro = ChatColor.GOLD + "[CA/Death] ";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!commandSender.hasPermission(CASettings.death)) {
            commandSender.sendMessage(PIntro + ChatColor.RED + "Sorry, you have no permission for this.");
            return false;
        }

        if (args == null || args.length == 0) {
            showHelp(commandSender);
            return true;
        } else if (args[0].toLowerCase().equals("on")) {
            CASettings.DeathEffectsEnabled = true;
            commandSender.sendMessage(PIntro + ChatColor.GREEN + "Death effects enabled!");
            return true;
        } else if (args[0].toLowerCase().equals("off")) {
            CASettings.DeathEffectsEnabled = false;
            commandSender.sendMessage(PIntro + ChatColor.GREEN + "Death effects disabled!");
            return true;
        } else if (args[0].toLowerCase().equals("eff")) {
            commandSender.sendMessage(PIntro + ChatColor.WHITE + "Current death effects: " + Joiner.on(", ").join(CASettings.DeathEffects));
            return true;
        } else if (args[0].toLowerCase().equals("location")) {
            if (commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage(PIntro + ChatColor.WHITE + "Silly console, you can't set death TP location.");
                return true;
            } else {
                Player p = (Player) commandSender;
                CASettings.DeathTPLocation = p.getLocation();
                commandSender.sendMessage(PIntro + ChatColor.WHITE + "Death location has been set to your current location");
                return true;
            }
        } else if (args[0].toLowerCase().equals("times")) {
            if (args.length > 1) {
                Player p = CASettings.plugin.getServer().getPlayer(args[1]);
                if (p != null) {
                    commandSender.sendMessage(PIntro + ChatColor.WHITE + p.getName() + "died " + CASettings.getPlayerSettings(p).DeathTimes + " times.");
                    return true;
                } else {
                    commandSender.sendMessage(PIntro + ChatColor.RED + "Wrong player specified.");
                    return false;
                }
            } else {
                if (commandSender instanceof ConsoleCommandSender) {
                    commandSender.sendMessage(PIntro + ChatColor.WHITE + "Silly console, you can't die.");
                    return true;
                } else {
                    Player p = (Player) commandSender;
                    commandSender.sendMessage(PIntro + ChatColor.WHITE + "You have died " + CASettings.getPlayerSettings(p).DeathTimes + " times.");
                    return true;
                }
            }
        } else if (args[0].toLowerCase().equals("message")) {
            if (args.length > 1) {
                List<String> arr = new ArrayList<String>();
                arr.addAll(Arrays.asList(args).subList(1, args.length));
                CASettings.DeathMessage = Joiner.on(" ").join(arr);
            }
            commandSender.sendMessage(PIntro + ChatColor.WHITE + "Current death message is: " + ChatColor.RED + CASettings.DeathMessage);

            return true;
        } else if (args[0].toLowerCase().equals("help")) {
            showHelp(commandSender);
            return true;
        } else if (args[0].toLowerCase().equals("set")) {
            if (args.length == 1) {
                commandSender.sendMessage(PIntro + ChatColor.RED + "You should give me a set of effect codes and amplifiers in form of CODE:AMP.");
                commandSender.sendMessage(PIntro + ChatColor.RED + "E.g. " + ChatColor.AQUA + "/death set HN:1 CN:2 SL:2");
                commandSender.sendMessage(PIntro + ChatColor.RED + "You can get codes via " + ChatColor.AQUA + "/eff all");
                return true;
            } else {
                CASettings.DeathEffects = new ArrayList<Effect>();
                for (int i = 1; i < args.length; i++) {
                    try {
                        String[] code = args[i].split(":");
                        PotionEffectType p = Effect.EffectsMap.get(code[0].toUpperCase());
                        int Amplifier = Integer.parseInt(code[1]);
                        CASettings.DeathEffects.add(new Effect(p, Amplifier));
                    } catch (Exception e) {
                        //Catch it?
                    }
                }
                commandSender.sendMessage(PIntro + ChatColor.WHITE + "Current death effects: " + Joiner.on(", ").join(CASettings.DeathEffects));
                return true;
            }
        } else {
            showHelp(commandSender);
            return true;
        }
    }

    private void showHelp(CommandSender sender) {

        String message = PIntro +
                ChatColor.WHITE +
                "Usage:";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/death <on|off>" +
                ChatColor.WHITE +
                " enables or disables death effects.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/death eff" +
                ChatColor.WHITE +
                " shows current death effects";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/death location" +
                ChatColor.WHITE +
                " sets TP location to your current location";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/death message [message]" +
                ChatColor.WHITE +
                " shows or sets (if set) death message";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/death times [playername]" +
                ChatColor.WHITE +
                " shows how many times you or (if playername set) another player died.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/death set {set of CODE:Amplifier}" +
                ChatColor.WHITE +
                " sets death effects. for more info type " + ChatColor.AQUA + "/death set" + ChatColor.WHITE + " without arguments.";
        sender.sendMessage(message);
    }
}
