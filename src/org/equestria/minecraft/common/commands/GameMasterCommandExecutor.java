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
package org.equestria.minecraft.common.commands;

import net.inkyquill.equestria.ca.CommonAbilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.equestria.minecraft.common.gamemaster.GameMasterController;

import java.util.logging.Logger;

public class GameMasterCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("GameMasterCommandExecutor");
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private CommonAbilities plugin;

    //TODO: Rework ItemCommands.

    public GameMasterCommandExecutor(CommonAbilities commonAbilities) {
        this.plugin = commonAbilities;
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[World] ");
        message.append(GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /say text ");
        message.append(YELLOW_COLOR);
        message.append("prints given text to all players within defined radius as World message.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /setMsgRadius radius ");
        message.append(YELLOW_COLOR);
        message.append("set Radius for World message.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /setMsgColor color ");
        message.append(YELLOW_COLOR);
        message.append("set Color for World message.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /getColorsList ");
        message.append(YELLOW_COLOR);
        message.append("show list of availible colors and effects.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /setMsgPrefix ");
        message.append(YELLOW_COLOR);
        message.append("set Prefix for World message.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /addItemMsg action(HOLD, PICKUP, USE) message");
        message.append(YELLOW_COLOR);
        message.append("add message for specificItem on action.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /addItemUniqMsg action(HOLD, PICKUP, USE) message");
        message.append(YELLOW_COLOR);
        message.append("add message for unique specificItem on action.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /setItemMsgColor action(HOLD, PICKUP, USE) color");
        message.append(YELLOW_COLOR);
        message.append("set message color for specific item.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /setItemMsgPrefix action(HOLD, PICKUP, USE) prefix");
        message.append(YELLOW_COLOR);
        message.append("set message prefix for specific item.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[gm]");
        message.append(GREEN_COLOR);
        message.append(" /deleteItemMsg action(HOLD, PICKUP, USE)");
        message.append(YELLOW_COLOR);
        message.append("delete message for specific item.");
        sender.sendMessage(message.toString());
    }

    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] as) {
        if (as == null || as.length == 0) {
            this.handleNoArgsCommand((Player)commandsender);
            return false;
        }
        if (!commandsender.hasPermission(command.getName())) {
            this.sendPermissionErrorMessage((Player)commandsender);
            return true;
        }
        if ("help".equals(as[0])) {
            GameMasterCommandExecutor.helpCommand(commandsender);
            return true;
        }
        if ("getColorsList".equals(as[0])) {
            this.sendColorsListMessage((Player)commandsender);
            return true;
        }
        if (as.length > 1) {
            return this.handleValidCommand((Player)commandsender, as);
        }
        this.handleNoArgsCommand((Player)commandsender);
        return false;
    }

    private boolean handleValidCommand(Player commandsender, String[] as) {
        String mode = as[0];
        String space = " ";
        if (mode.equals("say")) {
            if (as.length > 1) {
                StringBuilder phraseBuilder = new StringBuilder();
                for (String part : as) {
                    if (part.equals(mode)) continue;
                    phraseBuilder.append(part);
                    phraseBuilder.append(space);
                }
                GameMasterController.getInstance(this.plugin).sendWorldMessage(phraseBuilder.toString(), commandsender);
            } else {
                this.handleNoArgsCommand(commandsender);
            }
        } else if (mode.equals("setMsgRadius")) {
            if (as.length > 1) {
                String radiusStr = as[1];
                try {
                    int radius = Integer.parseInt(radiusStr);
                    GameMasterController.getInstance(this.plugin).setRadius(radius, commandsender.getName());
                    String message = "[World] " +
                            GREEN_COLOR +
                            "Radius changed to " +
                            radiusStr;
                    commandsender.sendMessage(message);
                }
                catch (NumberFormatException ex) {
                    String message = "[World] " +
                            YELLOW_COLOR +
                            "Enter valid message radius";
                    commandsender.sendMessage(message);
                }
            } else {
                this.handleNoArgsCommand(commandsender);
            }
        } else if (mode.equals("setMsgColor")) {
            if (as.length > 1) {
                String colorStr = as[1];
                if (GameMasterController.getInstance(this.plugin).setColor(colorStr, commandsender.getName())) {
                    this.sendColorsMessage(commandsender, true, colorStr);
                } else {
                    this.sendColorsMessage(commandsender, false, colorStr);
                }
            } else {
                this.handleNoArgsCommand(commandsender);
            }
        } else if (mode.equals("setMsgPrefix")) {
            if (as.length > 1) {
                String prefix = as[1];
                GameMasterController.getInstance(this.plugin).setPrefix(prefix, commandsender.getName());
                this.sendPrefixMessage(commandsender, prefix);
            } else {
                this.handleNoArgsCommand(commandsender);
            }
        } else if (mode.equals("addItemMsg") || mode.equals("addItemUniqMsg")) {
            if (as.length > 2) {
                String action = as[1];
                StringBuilder phraseBuilder = new StringBuilder();
                for (String part : as) {
                    if (part.equals(mode) || part.equals(action)) continue;
                    phraseBuilder.append(part);
                    phraseBuilder.append(space);
                }
                GameMasterController.getInstance(this.plugin).addMessageToItem(action, phraseBuilder.toString(), commandsender, mode.equals("addItemUniqMsg"));
            } else {
                GameMasterCommandExecutor.helpCommand(commandsender);
            }
        } else if (mode.equals("deleteItemMsg")) {
            if (as.length > 1) {
                String action = as[1];
                GameMasterController.getInstance(this.plugin).removeMessageFromItem(action, commandsender);
            } else {
                GameMasterCommandExecutor.helpCommand(commandsender);
            }
        } else if (mode.equals("setItemMsgColor")) {
            if (as.length > 1) {
                String action = as[1];
                String colorStr = as[2];
                if (GameMasterController.getInstance(this.plugin).setItemColor(action, colorStr, commandsender)) {
                    this.sendColorsMessage(commandsender, true, colorStr);
                } else {
                    this.sendColorsMessage(commandsender, false, colorStr);
                }
            } else {
                this.handleNoArgsCommand(commandsender);
            }
        } else if (mode.equals("setItemMsgPrefix")) {
            if (as.length > 1) {
                String action = as[1];
                StringBuilder phraseBuilder = new StringBuilder();
                for (String part : as) {
                    if (part.equals(mode) || part.equals(action)) continue;
                    phraseBuilder.append(part);
                    phraseBuilder.append(space);
                }
                GameMasterController.getInstance(this.plugin).setItemPrefix(action, phraseBuilder.toString(), commandsender);
                this.sendPrefixMessage(commandsender, phraseBuilder.toString());
            } else {
                this.handleNoArgsCommand(commandsender);
            }
        } else {
            GameMasterCommandExecutor.helpCommand(commandsender);
        }
        return true;
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[World] ");
        message.append(GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("- To view help, do /gm ");
        message.append(GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private void sendPermissionErrorMessage(Player player) {
        String message = String.valueOf(RED_COLOR) +
                "You don't have permissions to this!";
        player.sendMessage(message);
    }

    private void sendColorsListMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[World] ");
        message.append(GREEN_COLOR);
        message.append("Available colors - ");
        for (ChatColor color : ChatColor.values()) {
            message.append(color.name());
            message.append(" ");
        }
        player.sendMessage(message.toString());
    }

    private void sendColorsMessage(Player player, boolean correctColor, String color) {
        StringBuilder message = new StringBuilder();
        message.append("[World] ");
        if (correctColor) {
            message.append(GREEN_COLOR);
            message.append("Color changed to ");
            message.append(color);
        } else {
            message.append(YELLOW_COLOR);
            message.append("Enter valid color name");
        }
        player.sendMessage(message.toString());
    }

    private void sendPrefixMessage(Player player, String prefix) {
        String message = "[World] " +
                GREEN_COLOR +
                "Prefix changed to " +
                prefix;
        player.sendMessage(message);
    }
}

