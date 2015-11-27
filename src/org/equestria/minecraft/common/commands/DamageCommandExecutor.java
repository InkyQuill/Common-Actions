/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.equestria.minecraft.common.commands;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.damage.DamageController;

public class DamageCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("DamageCommandExecutor");
    private CommonAbilities plugin;
    public static final String DAMAGE_COMMAND = "damageController";
    private static final String DAMAGE_PREFIX = "[damageController] ";
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private static final String HELP_COMMAND = "help";
    private static final String SET_RESPAWN_MODE = "setRespawn";
    private static final String VIEW_RESPAWN_MODE = "viewRespawn";
    private static final String TELEPORT_MODE = "teleportToRespawn";

    public DamageCommandExecutor(CommonAbilities commonAbilities) {
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
            DamageCommandExecutor.helpCommand(commandsender);
            return true;
        }
        if ("setRespawn".equals(as[0])) {
            this.handleSetRespCommand((Player)commandsender, as);
            return true;
        }
        if ("viewRespawn".equals(as[0])) {
            return this.handleViewRespCommand(commandsender);
        }
        if ("teleportToRespawn".equals(as[0])) {
            return this.handleTeleportCommand((Player)commandsender);
        }
        this.handleNoArgsCommand((Player)commandsender);
        return false;
    }

    private boolean handleSetRespCommand(Player commandsender, String[] as) {
        if (as.length != 2) {
            DamageCommandExecutor.helpCommand((CommandSender)commandsender);
            return true;
        }
        String respawnLocation = as[1];
        DamageController.getInstance(this.plugin).setRespawnLocation(respawnLocation, commandsender.getWorld(), commandsender.getLocation());
        this.sendRespawnSetMessage(commandsender, respawnLocation);
        return true;
    }

    private boolean handleViewRespCommand(CommandSender commandsender) {
        Location location = DamageController.getInstance(this.plugin).getRespawnLocation();
        if (location != null) {
            String result = String.format("%s:%s:%s", location.getX(), location.getY(), location.getZ());
            this.sendRespMessage((Player)commandsender, result);
        }
        return true;
    }

    private boolean handleTeleportCommand(Player commandsender) {
        Location location = DamageController.getInstance(this.plugin).getRespawnLocation();
        if (location != null) {
            commandsender.teleport(location);
            this.sendTeleportMessage(commandsender);
        }
        return true;
    }

    private void sendRespawnSetMessage(Player player, String respCoords) {
        StringBuilder message = new StringBuilder();
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append("Respawn location changed to ");
        message.append(respCoords);
        player.sendMessage(message.toString());
    }

    private void sendRespMessage(Player player, String location) {
        StringBuilder message = new StringBuilder();
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append("Current respawn location -  ");
        message.append(location);
        player.sendMessage(message.toString());
    }

    private void sendTeleportMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)GREEN_COLOR);
        message.append("You has been teleported to respawn location.");
        player.sendMessage(message.toString());
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("- To view help, do /damageController ");
        message.append((Object)GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        message.append("/damageController setRespawn \u0445:y:z ");
        message.append((Object)YELLOW_COLOR);
        message.append("set respawn point to xyz coordinates.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        message.append("/damageController viewRespawn ");
        message.append((Object)YELLOW_COLOR);
        message.append("prints current respawn coordinates.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append((Object)YELLOW_COLOR);
        message.append("[damageController] ");
        message.append((Object)GREEN_COLOR);
        message.append("/damageController teleportToRespawn ");
        message.append((Object)YELLOW_COLOR);
        message.append("teleports you to respawn point.");
        sender.sendMessage(message.toString());
    }

    private void sendPermissionErrorMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append((Object)RED_COLOR);
        message.append("You don't have permissions to this!");
        player.sendMessage(message.toString());
    }
}

