/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.equestria.minecraft.common.commands;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.BlockChecker;

public class DropChanceCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("DropChanceCommandExecutor");
    private CommonAbilities plugin;
    public static final String DROP_CHANCE_COMMAND = "dropChance";
    private static final String DROP_PREFIX = "[dropChance] ";
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private static final String HELP_COMMAND = "help";
    private static final String ADD_CHANCE_PERMISSION_MODE = "addDropPerm";
    private static final String REMOVE_CHANCE_PERMISSION_MODE = "removeDropPerm";
    private static final String ADD_CHANCE_MODE = "addPermChance";
    private static final String REMOVE_CHANCE_MODE = "removePermChance";
    private static final String SHOW_TYPES_MODE = "showTypes";

    public DropChanceCommandExecutor(CommonAbilities commonAbilities) {
        this.plugin = commonAbilities;
    }

    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] as) {
        if (!commandsender.hasPermission(command.getName())) {
            this.sendPermissionErrorMessage((Player)commandsender);
            return true;
        }
        if (as == null || as.length == 0) {
            this.handleNoArgsCommand((Player)commandsender);
            return false;
        }
        if ("help".equals(as[0])) {
            DropChanceCommandExecutor.helpCommand(commandsender);
            return true;
        }
        if ("showTypes".equals(as[0])) {
            this.sendAllMonstersMessage((Player)commandsender);
            return true;
        }
        if ("addDropPerm".equals(as[0]) || "removeDropPerm".equals(as[0])) {
            return this.handleAddPermCommand(commandsender, as);
        }
        if ("addPermChance".equals(as[0]) || "removePermChance".equals(as[0])) {
            return this.handleAddChanceCommand(commandsender, as);
        }
        this.handleNoArgsCommand((Player)commandsender);
        return false;
    }

    private boolean handleAddPermCommand(CommandSender commandsender, String[] as) {
        if (as.length != 3) {
            DropChanceCommandExecutor.helpCommand(commandsender);
            return true;
        }
        String mode = as[0];
        String blockId = as[1];
        String permissionName = as[2];
        if (mode.equals("addDropPerm")) {
            BlockChecker.getInstance(this.plugin).getBlockService().addBlockChancePermission(blockId, permissionName);
            this.sendPermissionAddedMessage((Player)commandsender, blockId, permissionName);
        } else if (mode.equals("removeDropPerm")) {
            BlockChecker.getInstance(this.plugin).getBlockService().removeBlockChancePermission(blockId, permissionName);
            this.sendPermissionRemovedMessage((Player)commandsender, blockId, permissionName);
        }
        return true;
    }

    private boolean handleAddChanceCommand(CommandSender commandsender, String[] as) {
        String mode = as[0];
        if (mode.equals("addPermChance")) {
            if (as.length != 3) {
                DropChanceCommandExecutor.helpCommand(commandsender);
                return true;
            }
            String permissionName = as[1];
            String chance = as[2];
            try {
                BlockChecker.getInstance(this.plugin).getBlockService().addChanceForPermission(permissionName, Integer.parseInt(chance));
                this.sendChancePermissionAddedMessage((Player)commandsender, chance, permissionName);
            }
            catch (NumberFormatException ex) {
                this.sendInvalidChanceMessage((Player)commandsender);
                return true;
            }
        } else if (mode.equals("removePermChance")) {
            String permissionName = as[1];
            BlockChecker.getInstance(this.plugin).getBlockService().removeChanceForPermission(permissionName);
            this.sendChancePermissionRemovedMessage((Player)commandsender, permissionName);
        } else {
            DropChanceCommandExecutor.helpCommand(commandsender);
        }
        return true;
    }

    private void sendPermissionAddedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(permission);
        message.append(" added to drop permissions for ");
        message.append(blockType);
        player.sendMessage(message.toString());
    }

    private void sendPermissionRemovedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(permission);
        message.append(" removed from drop permissions for ");
        message.append(blockType);
        player.sendMessage(message.toString());
    }

    private void sendChancePermissionAddedMessage(Player player, String chance, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(chance);
        message.append(" added for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendChancePermissionRemovedMessage(Player player, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append("Drop chance removed for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("- To view help, do /dropChance ");
        message.append((Object)GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        message.append("/dropChance addDropPerm BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("add this permission to drop permissions for BlockType.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        message.append("/dropChance removeDropPerm BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("remove this permission fro drop permissions for BlockType.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        message.append("/dropChance addPermChance Permission Chance.");
        message.append((Object)YELLOW_COLOR);
        message.append("add Chance for Permission.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        message.append("/dropChance removePermChance Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("remove drop chance from Permission.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        message.append("/dropChance showTypes ");
        message.append((Object)YELLOW_COLOR);
        message.append("print all available block types.");
        sender.sendMessage(message.toString());
    }

    private void sendPermissionErrorMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append((Object)RED_COLOR);
        message.append("You don't have permissions to this!");
        player.sendMessage(message.toString());
    }

    private void sendAllMonstersMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        message.append("All blocks types:");
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[dropChance] ");
        message.append((Object)GREEN_COLOR);
        for (Material material : Material.values()) {
            message.append(material.name());
            message.append(", ");
        }
        player.sendMessage(message.toString());
    }

    private void sendInvalidBlockTypeMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)YELLOW_COLOR);
        message.append("Wrong block type.");
        player.sendMessage(message.toString());
    }

    private void sendInvalidChanceMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[dropChance] ");
        message.append((Object)YELLOW_COLOR);
        message.append("Chance must be numeric.");
        player.sendMessage(message.toString());
    }
}

