
package org.equestria.minecraft.common.commands;

import net.inkyquill.equestria.ca.CommonAbilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.equestria.minecraft.common.checkers.MonsterChecker;

import java.util.logging.Logger;

//// TODO: rewrite monster command

public class MonsterCommandExecutor
implements CommandExecutor {
    public static final Logger log = Logger.getLogger("MonsterCommandExecutor");
    public static final ChatColor YELLOW_COLOR = ChatColor.YELLOW;
    public static final ChatColor GREEN_COLOR = ChatColor.GREEN;
    public static final ChatColor RED_COLOR = ChatColor.RED;
    private CommonAbilities plugin;


    public MonsterCommandExecutor(CommonAbilities commonAbilities) {
        this.plugin = commonAbilities;
    }

    private static void helpCommand(CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        message.append("Help Menu!");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        message.append("/restrictTarget add PlayerName MobType ");
        message.append(YELLOW_COLOR);
        message.append("add this monster type to restricted list for player.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        message.append("/restrictTarget remove PlayerName MobType ");
        message.append(YELLOW_COLOR);
        message.append("remove this monster type from restricted list for player.");
        sender.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        message.append("/restrictTarget showTypes ");
        message.append(YELLOW_COLOR);
        message.append("print all available monster types.");
        sender.sendMessage(message.toString());
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
            MonsterCommandExecutor.helpCommand(commandsender);
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
        String playerName = as[1];
        String mobType = as[2];
        Player player = commandsender.getServer().getPlayer(playerName);
        EntityType type = EntityType.fromName(mobType);
        if (player == null) {
            this.sendInvalidPlayerMessage((Player)commandsender);
            return true;
        }
        if (type == null) {
            this.sendInvalidMonsterTypeMessage((Player)commandsender);
            return true;
        }
        if (mode.equals("add")) {
            MonsterChecker.getInstance(this.plugin).addMonsterToRestrict(player, type);
            this.sendMonsterAddedMessage((Player)commandsender, mobType, playerName);
        } else if (mode.equals("remove")) {
            MonsterChecker.getInstance(this.plugin).removeMonsterFromRestrict(player, type);
            this.sendMonsterRemovedMessage((Player)commandsender, mobType, playerName);
        } else {
            MonsterCommandExecutor.helpCommand(commandsender);
        }
        return true;
    }

    private void sendMonsterAddedMessage(Player player, String monsterType, String playerName) {
        StringBuilder message = new StringBuilder();
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(GREEN_COLOR);
        message.append(monsterType);
        message.append(" restricted to target player ");
        message.append(playerName);
        player.sendMessage(message.toString());
    }

    private void sendMonsterRemovedMessage(Player player, String monsterType, String playerName) {
        StringBuilder message = new StringBuilder();
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(GREEN_COLOR);
        message.append(monsterType);
        message.append(" allowed to target player ");
        message.append(playerName);
        player.sendMessage(message.toString());
    }

    private void handleNoArgsCommand(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("- To view help, do /restrictTarget ");
        message.append(GREEN_COLOR);
        message.append("help");
        player.sendMessage(message.toString());
    }

    private void sendPermissionErrorMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append(RED_COLOR);
        message.append("You don't have permissions to this!");
        player.sendMessage(message.toString());
    }

    private void sendAllMonstersMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        message.append("All monster types:");
        player.sendMessage(message.toString());
        message = new StringBuilder();
        message.append(YELLOW_COLOR);
        message.append("[restrictTarget] ");
        message.append(GREEN_COLOR);
        message.append(EntityType.ZOMBIE.getName());
        message.append(", ");
        message.append(EntityType.CREEPER.getName());
        message.append(", ");
        message.append(EntityType.SKELETON.getName());
        message.append(", ");
        message.append(EntityType.SPIDER.getName());
        message.append(", ");
        message.append(EntityType.GIANT.getName());
        message.append(", ");
        message.append(EntityType.SLIME.getName());
        message.append(", ");
        message.append(EntityType.GHAST.getName());
        message.append(", ");
        message.append(EntityType.PIG_ZOMBIE.getName());
        message.append(", ");
        message.append(EntityType.ENDERMAN.getName());
        message.append(", ");
        message.append(EntityType.CAVE_SPIDER.getName());
        message.append(", ");
        message.append(EntityType.SILVERFISH.getName());
        message.append(", ");
        message.append(EntityType.BLAZE.getName());
        message.append(", ");
        message.append(EntityType.MAGMA_CUBE.getName());
        message.append(", ");
        message.append(EntityType.ENDER_DRAGON.getName());
        message.append(", ");
        message.append(EntityType.SQUID.getName());
        message.append(", ");
        message.append(EntityType.WOLF.getName());
        message.append(", ");
        message.append(EntityType.IRON_GOLEM.getName());
        player.sendMessage(message.toString());
    }

    private void sendInvalidPlayerMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[restrictTarget] ");
        message.append(YELLOW_COLOR);
        message.append("Player does not exist");
        player.sendMessage(message.toString());
    }

    private void sendInvalidMonsterTypeMessage(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("[restrictTarget] ");
        message.append(YELLOW_COLOR);
        message.append("Wrong monster type.");
        player.sendMessage(message.toString());
    }
}

