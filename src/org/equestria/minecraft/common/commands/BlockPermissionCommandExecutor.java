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

public class BlockPermissionCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("BlockPermissionCommandExecutor");
    private CommonAbilities plugin;
    public static final String ADD_PERM_COMMAND = "blockPerm";
    private static final String RESTRICT_PREFIX = "[blockPermission] ";
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private static final String HELP_COMMAND = "help";
    private static final String ADD_BREAK_MODE = "addBreak";
    private static final String REMOVE_BREAK_MODE = "removeBreak";
    private static final String ADD_PLACE_MODE = "addPlace";
    private static final String REMOVE_PLACE_MODE = "removePlace";
    private static final String ADD_USE_MODE = "addUse";
    private static final String REMOVE_USE_MODE = "removeUse";
    private static final String SHOW_TYPES_MODE = "showTypes";
    private static final String DEBUG_MODE = "debugMode";

    public BlockPermissionCommandExecutor(CommonAbilities commonAbilities) {
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
            BlockPermissionCommandExecutor.helpCommand(commandsender);
            return true;
        }
        if ("debugMode".equals(as[0])) {
            boolean bebugMode;
            BlockChecker.getInstance(this.plugin).setDebugMode(!(bebugMode = BlockChecker.getInstance(this.plugin).isDebugMode()));

            this.sendDebugModeMessage((Player)commandsender, !bebugMode);
            return true;
        }
        if ("showTypes".equals(as[0])) {
            this.sendAllMonstersMessage((Player)commandsender);
            return true;
        }
        if (as.length == 3) {
            return this.handleValidCommand(commandsender, as);
        }
        this.handleNoArgsCommand((Player)commandsender);
        return false;
    }

    private boolean handleValidCommand(CommandSender commandsender, String[] as) {
        String mode = as[0];
        String blockId = as[1];
        String permissionName = as[2];
        if (mode.equals("addBreak")) {
            BlockChecker.getInstance(this.plugin).getBlockService().addBreakBlockPermission(blockId, permissionName);
            this.sendBreakPermissionAddedMessage((Player)commandsender, blockId, permissionName);
        } else if (mode.equals("removeBreak")) {
            BlockChecker.getInstance(this.plugin).getBlockService().removeBreakBlockPermission(blockId, permissionName);
            this.sendBreakPermissionRemovedMessage((Player)commandsender, blockId, permissionName);
        } else if (mode.equals("addPlace")) {
            BlockChecker.getInstance(this.plugin).getBlockService().addPlaceBlockPermission(blockId, permissionName);
            this.sendPlacePermissionAddedMessage((Player)commandsender, blockId, permissionName);
        } else if (mode.equals("removePlace")) {
            BlockChecker.getInstance(this.plugin).getBlockService().removePlaceBlockPermission(blockId, permissionName);
            this.sendPlacePermissionRemovedMessage((Player)commandsender, blockId, permissionName);
        } else if (mode.equals("addUse")) {
            BlockChecker.getInstance(this.plugin).getBlockService().addUseBlockPermission(blockId, permissionName);
            this.sendUsePermissionAddedMessage((Player)commandsender, blockId, permissionName);
        } else if (mode.equals("removeUse")) {
            BlockChecker.getInstance(this.plugin).getBlockService().removeUseBlockPermission(blockId, permissionName);
            this.sendUsePermissionRemovedMessage((Player)commandsender, blockId, permissionName);
        } else {
            BlockPermissionCommandExecutor.helpCommand(commandsender);
        }
        return true;
    }

    private void sendBreakPermissionAddedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" allowed to break for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendBreakPermissionRemovedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" disallowed to break for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendPlacePermissionAddedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" allowed to place for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendPlacePermissionRemovedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" disallowed to place for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendUsePermissionAddedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" allowed to use for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendUsePermissionRemovedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" disallowed to use for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendDebugModeMessage(Player player, boolean mode) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append("debugMode");
        if (mode) {
            message.append(" enabled");
        } else {
            message.append(" disabld");
        }
        player.sendMessage(message.toString());
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("- To view help, do /blockPerm ");
        message.append((Object)GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm addBreak BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("add this BlockType to allowed to break list for Permission.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm removeBreak BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("remove this Permission from allowed to break for BlockType.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm addPlace BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("add this BlockType to allowed to place list for Permission.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm removePlace BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("remove this Permission from allowed to place for BlockType.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm addUse BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("add this BlockType to allowed to use list for Permission.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm removeUse BlockType Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("remove this Permission from allowed to use for BlockType.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm showTypes ");
        message.append((Object)YELLOW_COLOR);
        message.append("print all available block types.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/blockPerm debugMode ");
        message.append((Object)YELLOW_COLOR);
        message.append("enable or disable debug mode.");
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
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("All blocks types:");
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[blockPermission] ");
        message.append((Object)GREEN_COLOR);
        for (Material material : Material.values()) {
            message.append(material.name());
            message.append(", ");
        }
        player.sendMessage(message.toString());
    }

    private void sendInvalidBlockTypeMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[blockPermission] ");
        message.append((Object)YELLOW_COLOR);
        message.append("Wrong block type.");
        player.sendMessage(message.toString());
    }
}

