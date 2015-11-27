/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.equestria.minecraft.common.commands;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.EffectsChecker;
import org.equestria.minecraft.common.effects.Effect;

public class EffectsCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("EffectsCommandExecutor");
    private CommonAbilities plugin;
    public static final String EFFECTS = "effects";
    private static final String EFFECTS_PREFIX = "[Effects] ";
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private static final String HELP_COMMAND = "help";
    private static final String ADD_MODE = "add";
    private static final String REMOVE_MODE = "remove";
    private static final String SHOW_EFFECTS = "show";

    public EffectsCommandExecutor(CommonAbilities commonAbilities) {
        this.plugin = commonAbilities;
    }

    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] as) {
        if (as == null || as.length == 0) {
            this.handleNoArgsCommand((Player)commandsender);
            return false;
        }
        if ("show".equals(as[0]) && as.length == 2) {
            this.showCommand(commandsender, as[1]);
            return true;
        }
        if (!commandsender.hasPermission(command.getName())) {
            this.sendPermissionErrorMessage((Player)commandsender);
            return true;
        }
        if ("help".equals(as[0])) {
            EffectsCommandExecutor.helpCommand(commandsender);
            return true;
        }
        if (as.length >= 3) {
            return this.handleValidCommand(commandsender, as);
        }
        this.handleNoArgsCommand((Player)commandsender);
        return false;
    }

    private boolean handleValidCommand(CommandSender commandsender, String[] as) {
        String mode = as[0];
        String name = as[1];
        String effect = as[2];
        if (mode.equals("add")) {
            if (as.length == 4) {
                int effectStrenght = 0;
                try {
                    PotionEffectType.getByName((String)effect);
                    effectStrenght = Integer.parseInt(as[3]);
                }
                catch (NumberFormatException ex) {
                    this.handleErrorCommand((Player)commandsender, "Effect strenght is not numeric");
                }
                catch (Exception e) {
                    this.handleErrorCommand((Player)commandsender, "Effect is wrong");
                }
                Effect effects = EffectsChecker.getInstance(this.plugin).getEffects(name);
                if (effects == null) {
                    effects = new Effect();
                }
                effects.addPotionEffect(new PotionEffect(PotionEffectType.getByName((String)effect), 250, effectStrenght));
                EffectsChecker.getInstance(this.plugin).addEffect(name, effects);
                this.sendEffectAddedMessage((Player)commandsender, name, effect);
            } else {
                this.handleNoArgsCommand((Player)commandsender);
            }
        } else if (mode.equals("remove")) {
            EffectsChecker.getInstance(this.plugin).removeEffect(name, effect);
            this.sendEffectRemovedMessage((Player)commandsender, effect, name);
        } else {
            EffectsCommandExecutor.helpCommand(commandsender);
        }
        return true;
    }

    private void sendEffectAddedMessage(Player player, String target, String effect) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(effect);
        message.append(" added for player ");
        message.append(target);
        player.sendMessage(message.toString());
    }

    private void sendEffectRemovedMessage(Player player, String effect, String targetName) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(effect + " removed for player ");
        message.append(targetName);
        player.sendMessage(message.toString());
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("- To view help, do /effects ");
        message.append((Object)GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private void handleErrorCommand(Player player, String error) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append((Object)YELLOW_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append(error);
        player.sendMessage(message.toString());
    }

    private void showCommand(CommandSender sender, String name) {
        Effect effects = EffectsChecker.getInstance(this.plugin).getEffects(name);
        if (effects != null) {
            StringBuilder message = new StringBuilder();
            message.append("[Effects] ");
            message.append((Object)GREEN_COLOR);
            sender.sendMessage(message.toString());
            message = new StringBuilder();
            message.append((Object)YELLOW_COLOR);
            message.append("Effects - ");
            message.append((Object)GREEN_COLOR);
            for (PotionEffect effect : effects.getPotionEffects()) {
                message.append(effect.getType().getName());
                message.append(" ");
            }
            sender.sendMessage(message.toString());
        } else {
            StringBuilder message = new StringBuilder();
            message.append("[Effects] ");
            message.append((Object)GREEN_COLOR);
            message.append("No effects for " + name);
            sender.sendMessage(message.toString());
        }
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append((Object)GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[Effects] ");
        message.append((Object)GREEN_COLOR);
        message.append("/effects add PlayerName Effect Amplifier ");
        message.append((Object)YELLOW_COLOR);
        message.append("add this effect to given player.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[Effects] ");
        message.append((Object)GREEN_COLOR);
        message.append("/effects remove PlayerName Effect ");
        message.append((Object)YELLOW_COLOR);
        message.append("remove Effect from the given Player.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[Effects] ");
        message.append((Object)GREEN_COLOR);
        message.append("/effects show PlayerName");
        message.append((Object)YELLOW_COLOR);
        message.append("show effects for given Player.");
        sender.sendMessage(message.toString());
    }

    private void sendPermissionErrorMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append((Object)RED_COLOR);
        message.append("You don't have permissions to this!");
        player.sendMessage(message.toString());
    }
}

