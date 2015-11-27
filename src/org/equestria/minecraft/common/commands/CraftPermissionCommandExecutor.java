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

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.CraftChecker;

public class CraftPermissionCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("CraftPermissionCommandExecutor");
    private CommonAbilities plugin;
    public static final String ADD_PERM_COMMAND = "craftPerm";
    private static final String CRAFT_PREFIX = "[CraftPermission] ";
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private static final String HELP_COMMAND = "help";
    private static final String ADD_MODE = "add";
    private static final String REMOVE_MODE = "remove";

    public CraftPermissionCommandExecutor(CommonAbilities commonAbilities) {
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
            CraftPermissionCommandExecutor.helpCommand(commandsender);
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
        String blockType = as[1];
        String permissionName = as[2];
        if (mode.equals("add")) {
            CraftChecker.getInstance(this.plugin).getCraftService().addCraftBlockPermission(blockType, permissionName);
            this.sendCraftPermissionAddedMessage((Player)commandsender, blockType, permissionName);
        } else if (mode.equals("remove")) {
            CraftChecker.getInstance(this.plugin).getCraftService().removeCraftBlockPermission(blockType, permissionName);
            this.sendCraftPermissionRemovedMessage((Player)commandsender, blockType, permissionName);
        } else {
            CraftPermissionCommandExecutor.helpCommand(commandsender);
        }
        return true;
    }

    private void sendCraftPermissionAddedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[CraftPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" allowed to craft for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void sendCraftPermissionRemovedMessage(Player player, String blockType, String permission) {
        StringBuilder message = new StringBuilder();
        message.append("[CraftPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append(blockType);
        message.append(" disallowed to craft for permission ");
        message.append(permission);
        player.sendMessage(message.toString());
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[CraftPermission] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("- To view help, do /craftPerm ");
        message.append((Object)GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[CraftPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[CraftPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/craftPerm add BlockId Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("add this BlockId to allowed to craft list for Permission.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[CraftPermission] ");
        message.append((Object)GREEN_COLOR);
        message.append("/craftPerm remove BlockId Permission ");
        message.append((Object)YELLOW_COLOR);
        message.append("remove this Permission from allowed to craft for BlockId.");
        sender.sendMessage(message.toString());
    }

    private void sendPermissionErrorMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append((Object)RED_COLOR);
        message.append("You don't have permissions to this!");
        player.sendMessage(message.toString());
    }

    private void sendInvalidRecipeTypeMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[CraftPermission] ");
        message.append((Object)YELLOW_COLOR);
        message.append("Wrong recipe name.");
        player.sendMessage(message.toString());
    }
}

