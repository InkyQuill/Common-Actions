
package net.inkyquill.equestria.ca.commands;

import guava10.com.google.common.base.Joiner;
import net.inkyquill.equestria.ca.checkers.MonsterChecker;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MonsterCommand
implements CommandExecutor {

    public static final EntityType[] MonsterTypes;
    static String PIntro;

    static {
        PIntro = ChatColor.GOLD + "[CA/MobRestrict] ";
        MonsterTypes = new EntityType[]{
                EntityType.ZOMBIE, EntityType.CREEPER, EntityType.SKELETON, EntityType.SPIDER, EntityType.GIANT,
                EntityType.SLIME, EntityType.GHAST, EntityType.PIG_ZOMBIE, EntityType.ENDERMAN, EntityType.CAVE_SPIDER
                , EntityType.SILVERFISH, EntityType.BLAZE, EntityType.MAGMA_CUBE, EntityType.ENDER_DRAGON,
                EntityType.SQUID, EntityType.WOLF, EntityType.IRON_GOLEM
        };
    }

    public MonsterCommand() {

    }

    private static void showHelp(CommandSender sender) {
        sender.sendMessage(PIntro + ChatColor.WHITE + "Usage");
        sender.sendMessage(PIntro + ChatColor.AQUA + "/mobrestrict list" + ChatColor.WHITE + " lists all monsters");
        sender.sendMessage(PIntro + ChatColor.AQUA + "/mobrestrict <player> add|remove <monster>" + ChatColor.WHITE + " adds or removes restricted monster for a player");
        sender.sendMessage(PIntro + ChatColor.AQUA + "/mobrestrict <player> show|clear|all" + ChatColor.WHITE + " shows, clears or adds all monsters to restricted for a player");
        sender.sendMessage(PIntro + ChatColor.AQUA + "/mobrestrict my|clear|all" + ChatColor.WHITE + " shows, clears or adds all monsters to restricted for you");
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] param) {
        if (!sender.hasPermission(CASettings.monsters)) {
            sender.sendMessage(PIntro + ChatColor.RED + "Sorry, you have no permission for this.");
            return false;
        }
        if (param == null || param.length == 0) {
            showHelp(sender);
            return true;
        }
        if ("help".equals(param[0])) {
            showHelp(sender);
            return true;
        }
        if ("list".equals(param[0])) {
            sendAllMonstersMessage(sender);
            return true;
        }
        if ("my".equals(param[0])) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(PIntro + ChatColor.WHITE + "Silly console, you can't be attacked by monsters.");
                return true;
            } else {
                Player p = (Player) sender;
                listRestrictedMonsters(sender, p);
                return true;
            }
        }
        if ("clear".equals(param[0])) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(PIntro + ChatColor.WHITE + "Silly console, you can't be attacked by monsters.");
                return true;
            } else {
                Player p = (Player) sender;
                clearRestrictedMonsters(sender, p);
                return true;
            }
        }
        if ("all".equals(param[0])) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(PIntro + ChatColor.WHITE + "Silly console, you can't be attacked by monsters.");
                return true;
            } else {
                Player p = (Player) sender;
                allRestrictedMonsters(sender, p);
                return true;
            }
        }
        if (param.length > 1) {
            if ("show".equals(param[1])) {
                Player p = CASettings.plugin.getServer().getPlayer(param[0]);
                if (p == null) {
                    sender.sendMessage(PIntro + ChatColor.WHITE + "We do not know this player...");
                    return true;
                } else {
                    listRestrictedMonsters(sender, p);
                    return true;
                }
            } else if ("clear".equals(param[1])) {
                Player p = CASettings.plugin.getServer().getPlayer(param[0]);
                if (p == null) {
                    sender.sendMessage(PIntro + ChatColor.WHITE + "We do not know this player...");
                    return true;
                } else {
                    clearRestrictedMonsters(sender, p);
                    return true;
                }
            } else if ("all".equals(param[1])) {
                Player p = CASettings.plugin.getServer().getPlayer(param[0]);
                if (p == null) {
                    sender.sendMessage(PIntro + ChatColor.WHITE + "We do not know this player...");
                    return true;
                } else {
                    allRestrictedMonsters(sender, p);
                    return true;
                }
            } else if (param.length > 2) {
                Player p = CASettings.plugin.getServer().getPlayer(param[0]);
                if (p == null) {
                    sender.sendMessage(PIntro + ChatColor.WHITE + "We do not know this player.");
                    return true;
                }
                EntityType ent;
                try {
                    ent = EntityType.valueOf(param[2].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong monster name.");
                    return true;
                }

                if ("add".equals(param[1].toLowerCase())) {
                    MonsterChecker.getInstance().addMonsterToRestrict(p, ent);
                    this.sendMonsterAddedMessage(sender, ent, p);
                    return true;
                } else if ("remove".equals(param[1].toLowerCase())) {
                    MonsterChecker.getInstance().removeMonsterFromRestrict(p, ent);
                    this.sendMonsterRemovedMessage(sender, ent, p);
                    return true;
                }

                showHelp(sender);
                return true;
            } else {
                showHelp(sender);
                return true;
            }
        } else

        return false;
    }

    private void allRestrictedMonsters(CommandSender sender, Player p) {
        PlayerSettings ps = CASettings.getPlayerSettings(p);
        ps.Monsters.clear();
        Collections.addAll(ps.Monsters, MonsterTypes);
        sender.sendMessage(PIntro + ChatColor.WHITE + "All restrictions for " + p.getName() + " were cleared.");
    }


    private void clearRestrictedMonsters(CommandSender sender, Player p) {
        PlayerSettings ps = CASettings.getPlayerSettings(p);
        ps.Monsters.clear();
        sender.sendMessage(PIntro + ChatColor.WHITE + "All restrictions for " + p.getName() + " were cleared.");
    }

    private void listRestrictedMonsters(CommandSender sender, Player p) {
        PlayerSettings ps = CASettings.getPlayerSettings(p);
        sender.sendMessage(PIntro + ChatColor.WHITE + "Restricted for " + p.getName() + " are: " + ChatColor.AQUA + Joiner.on(", ").join(ps.Monsters));
    }

    private void sendMonsterAddedMessage(CommandSender player, EntityType monsterType, Player playerName) {
        String message = PIntro +
                ChatColor.WHITE +
                monsterType +
                " restricted to target player " +
                playerName.getName();
        player.sendMessage(message);
    }

    private void sendMonsterRemovedMessage(CommandSender player, EntityType monsterType, Player playerName) {
        String message = PIntro +
                ChatColor.WHITE +
                monsterType +
                " allowed to target player " +
                playerName.getName();
        player.sendMessage(message);
    }

    private void sendAllMonstersMessage(CommandSender player) {
        player.sendMessage(PIntro + ChatColor.WHITE + "All monsters:");
        player.sendMessage(PIntro + ChatColor.AQUA + Joiner.on(", ").join(MonsterTypes));
    }
}

