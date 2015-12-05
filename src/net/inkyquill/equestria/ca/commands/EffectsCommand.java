package net.inkyquill.equestria.ca.commands;

import com.google.common.base.Joiner;
import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.Effect;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class EffectsCommand implements CommandExecutor {
    static String PIntro;

    static {
        PIntro = ChatColor.GOLD + "[CA/Effects] ";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] param) {

        if (!commandSender.hasPermission(CASettings.effects)) {
            commandSender.sendMessage(PIntro + ChatColor.RED + "Sorry, you have no permission for this.");
            return false;
        }

        if (param == null || param.length == 0) {
            showHelp(commandSender);
            return true;
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
                commandSender.sendMessage(PIntro + ChatColor.WHITE + "Your effects: " + ChatColor.AQUA + Joiner.on(", ").join(CASettings.getPlayerSettings(p).getEffects()));
                return true;
            } else if (param[0].toLowerCase().equals("help")) {
                showHelp(commandSender);
                return true;
            } else {
                showHelp(commandSender);
                return true;
            }
        } else if (param.length == 3) {
            if (param[0].toLowerCase().equals("remove")) {
                Player p;
                if (param[1].toLowerCase().equals("me")) {
                    p = (Player) commandSender;
                } else {
                    p = CASettings.plugin.getServer().getPlayer(param[1]);
                    if (p == null) {
                        commandSender.sendMessage(PIntro + ChatColor.RED + " Player " + param[1] + " was not found.");
                        return false;
                    }
                }

                if (param[2].toUpperCase().equals("ALL")) {
                    EffectsChecker.RemoveAll(p);
                    commandSender.sendMessage(PIntro + ChatColor.GREEN + "All effects from " + param[1] + " were removed.");
                    p.sendMessage(PIntro + ChatColor.WHITE + "You no longer have lasting effects");
                    return true;
                } else if (Effect.EffectsMap.containsKey(param[2].toUpperCase())) {
                    PotionEffectType eff = Effect.EffectsMap.get(param[2].toUpperCase());
                    EffectsChecker.Remove(p, eff);
                    commandSender.sendMessage(PIntro + ChatColor.GREEN + "Effect " + eff.getName() + " was removed from " + param[1] + ".");
                    p.sendMessage(PIntro + ChatColor.WHITE + "You no longer have " + eff.getName());
                    return true;
                } else {
                    try {
                        PotionEffectType eff = PotionEffectType.getByName(param[2].toUpperCase());
                        EffectsChecker.Remove(p, eff);
                        commandSender.sendMessage(PIntro + ChatColor.GREEN + "Effect " + eff.getName() + " was removed from " + param[1] + ".");
                        p.sendMessage(PIntro + ChatColor.WHITE + "You no longer have " + eff.getName());
                    } catch (Exception e) {
                        commandSender.sendMessage(PIntro + ChatColor.RED + "Wrong effect name: " + param[2]);
                        return false;
                    }
                }
            } else {
                showHelp(commandSender);
                return true;
            }
        } else if (param.length == 4) {
            if (param[0].toLowerCase().equals("add")) {
                Player p;
                if (param[1].toLowerCase().equals("me")) {
                    p = (Player) commandSender;
                } else {
                    p = CASettings.plugin.getServer().getPlayer(param[1]);
                    if (p == null) {
                        commandSender.sendMessage(PIntro + ChatColor.RED + " Player " + param[1] + " was not found.");
                        return false;
                    }
                }

                int amp = Integer.parseInt(param[3]);

                if (Effect.EffectsMap.containsKey(param[2].toUpperCase())) {
                    PotionEffectType eff = Effect.EffectsMap.get(param[2].toUpperCase());
                    EffectsChecker.Add(p, eff, amp);
                    commandSender.sendMessage(PIntro + ChatColor.GREEN + "Effect " + eff.getName() + " was added to " + param[1] + ".");
                    p.sendMessage(PIntro + ChatColor.WHITE + "You now have " + eff.getName());
                    return true;
                } else {
                    try {
                        PotionEffectType eff = PotionEffectType.getByName(param[2].toUpperCase());
                        EffectsChecker.Add(p, eff, amp);
                        commandSender.sendMessage(PIntro + ChatColor.GREEN + "Effect " + eff.getName() + " was added from " + param[1] + ".");
                        p.sendMessage(PIntro + ChatColor.WHITE + "You now have " + eff.getName());
                    } catch (Exception e) {
                        commandSender.sendMessage(PIntro + ChatColor.RED + "Wrong effect name: " + param[2]);
                        return false;
                    }
                }
            } else {
                showHelp(commandSender);
                return true;
            }
        } else {
            showHelp(commandSender);
            return true;
        }
        return false;
    }

    private void showHelp(CommandSender sender) {

        String message = PIntro +
                ChatColor.WHITE +
                "Usage:";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/eff add <me|playername> <effectcode|effectname> <amplifier>" +
                ChatColor.WHITE +
                " adds an effect to player.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/eff remove <me|playername> <all|effectcode|effectname>" +
                ChatColor.WHITE +
                " removes an effect from player";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/eff all" +
                ChatColor.WHITE +
                " shows effect list with shortcodes.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/eff my" +
                ChatColor.WHITE +
                " lists your effects";
        sender.sendMessage(message);
    }
}
