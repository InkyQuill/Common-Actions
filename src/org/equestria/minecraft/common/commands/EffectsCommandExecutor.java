
package org.equestria.minecraft.common.commands;

import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.equestria.minecraft.common.effects.Effect;

import java.util.logging.Logger;

public class EffectsCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("EffectsCommandExecutor");
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private CommonAbilities plugin;

    public EffectsCommandExecutor(CommonAbilities commonAbilities) {
        this.plugin = commonAbilities;
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append(GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[Effects] ");
        message.append(GREEN_COLOR);
        message.append("/effects add PlayerName Effect Amplifier ");
        message.append(YELLOW_COLOR);
        message.append("add this effect to given player.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[Effects] ");
        message.append(GREEN_COLOR);
        message.append("/effects remove PlayerName Effect ");
        message.append(YELLOW_COLOR);
        message.append("remove Effect from the given Player.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[Effects] ");
        message.append(GREEN_COLOR);
        message.append("/effects show PlayerName");
        message.append(YELLOW_COLOR);
        message.append("show effects for given Player.");
        sender.sendMessage(message.toString());
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
                    PotionEffectType.getByName(effect);
                    effectStrenght = Integer.parseInt(as[3]);
                }
                catch (NumberFormatException ex) {
                    this.handleErrorCommand((Player) commandsender, "Effect strength is not numeric");
                }
                catch (Exception e) {
                    this.handleErrorCommand((Player)commandsender, "Effect is wrong");
                }
                Effect effects = EffectsChecker.getInstance(this.plugin).getEffects(name);
                if (effects == null) {
                    effects = new Effect();
                }
                effects.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), 250, effectStrenght));
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
        message.append(GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(GREEN_COLOR);
        message.append(effect);
        message.append(" added for player ");
        message.append(target);
        player.sendMessage(message.toString());
    }

    private void sendEffectRemovedMessage(Player player, String effect, String targetName) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append(GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(GREEN_COLOR);
        message.append(effect + " removed for player ");
        message.append(targetName);
        player.sendMessage(message.toString());
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append(GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("- To view help, do /effects ");
        message.append(GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private void handleErrorCommand(Player player, String error) {
        StringBuilder message = new StringBuilder();
        message.append("[Effects] ");
        message.append(YELLOW_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append(error);
        player.sendMessage(message.toString());
    }

    private void showCommand(CommandSender sender, String name) {
        Effect effects = EffectsChecker.getInstance(this.plugin).getEffects(name);
        if (effects != null) {
            StringBuilder message = new StringBuilder();
            message.append("[Effects] ");
            message.append(GREEN_COLOR);
            sender.sendMessage(message.toString());
            message = new StringBuilder();
            message.append(YELLOW_COLOR);
            message.append("Effects - ");
            message.append(GREEN_COLOR);
            for (PotionEffect effect : effects.getPotionEffects()) {
                message.append(effect.getType().getName());
                message.append(" ");
            }
            sender.sendMessage(message.toString());
        } else {
            StringBuilder message = new StringBuilder();
            message.append("[Effects] ");
            message.append(GREEN_COLOR);
            message.append("No effects for " + name);
            sender.sendMessage(message.toString());
        }
    }

    private void sendPermissionErrorMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append(RED_COLOR);
        message.append("You don't have permissions to this!");
        player.sendMessage(message.toString());
    }
}

