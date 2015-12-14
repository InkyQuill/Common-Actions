package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

/**
 * Created by obruchnikov_pa on 25.11.2015.
 * includes RealisticChat!
 */
public class PlayerChatHandler implements Listener {
    private final Random random;

    public PlayerChatHandler() {
        random = new Random();
    }

    public static void deliverMessage(Player to, Player from, String msg) {
        ChatColor col;
        col = from == to ? CASettings.chat.spokenPlayerColor : CASettings.chat.heardPlayerColor;
        to.sendMessage(String.format(CASettings.chat.chatLineFormat, col + from.getDisplayName(), ChatColor.WHITE + msg));
    }

    @EventHandler
    public void OnPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        PlayerSettings ps = CASettings.getPlayerSettings(p);
        if (ps.GM.Enabled) {
            String str = String.valueOf(ps.GM.Color) +
                    ps.GM.Prefix +
                    " " +
                    event.getMessage();

            for (Player recipient : Bukkit.getOnlinePlayers()) {

                if (p.equals(recipient)) {
                    p.sendMessage(str);
                    continue;
                }
                if (!p.getWorld().equals(recipient.getWorld()) || (p.getLocation().distance(recipient.getLocation())) > (double) ps.GM.Radius)
                    continue;
                recipient.sendMessage(str);
            }
            event.setCancelled(true);
        } else {
            String message = event.getMessage();
            CASettings.L.info(p.getDisplayName() + ": " + message);

            double d = CASettings.chat.speakingRangeMeters;

            int i = countExclamationMarks(message);
            if (i > 0) {

                if (i > CASettings.chat.yellMax)
                    i = CASettings.chat.yellMax;

                int k = CASettings.chat.yellHunger.get(i - 1);
                p.setFoodLevel(p.getFoodLevel() - k);
                double u = CASettings.chat.yellDistance.get(i - 1);
                d += u;

                //CASettings.L.info("");
            }

            int j = countParenthesizeNests(message);
            if (j > 0) {
                d -= CASettings.chat.whisperRangeDecrease * j;
                if (d < 1.0D) d = 1.0D;
            }


            //CASettings.L.info((new StringBuilder()).append("<").append(p.getName()).append(": ").append(joinList(arraylist, ",")).append("> ").append(message).toString());


            for (Player to : event.getRecipients()) {
                if (p.equals(to)) {
                    deliverMessage(to, p, message);
                    continue;
                }
                if (!p.getWorld().equals(to.getWorld())) {
                    continue;
                }
                double d2 = p.getLocation().distance(to.getLocation());

                if (d2 <= d * 0.6) {
                    deliverMessage(to, p, message);
                    continue;
                }
                if (d2 > d * 1.5) {
                    continue;
                }
                String msg = message;
                if (d2 > d) {
                    msg = garble(msg, (d2 - d) / (0.5 * d));
                }
                msg = color(msg, (d2 - d * 0.6) / (d * 0.9));

                deliverMessage(to, p, msg);
            }
            event.setCancelled(true);

        }
    }


    private int countExclamationMarks(String s) {
        int i;
        for (i = 0; s.length() > 1 && s.endsWith("!"); i++) {
            s = s.substring(0, s.length() - 1);
        }
        return i;
    }

    private int countParenthesizeNests(String s) {
        int i;
        for (i = 0; s.length() > 2 && s.startsWith("(") && s.endsWith(")"); i++) {
            s = s.substring(1, s.length() - 1);
        }
        return i;
    }

    private String garble(String s, double d) {

        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = 0;
        while (i < s.length()) {
            int k = s.codePointAt(i);
            i += Character.charCount(k);
            if (random.nextDouble() < d) {
                stringbuilder.appendCodePoint(k);
            } else {
                stringbuilder.append(' ');
                j++;
            }
        }
        if (j == s.length()) {
            String s1 = CASettings.chat.garbleAllDroppedMessage;
            if (s1 != null) {
                return s1;
            }
        }
        return new String(stringbuilder);
    }

    private String color(String s, double d) {

        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        ChatColor last = ChatColor.WHITE;
        while (i < s.length()) {
            int k = s.codePointAt(i);
            i += Character.charCount(k);
            double dd = random.nextDouble();
            if (dd < d / 3) {
                if (last != ChatColor.DARK_GRAY) {
                    last = ChatColor.DARK_GRAY;
                    stringbuilder.append(last);
                }
                stringbuilder.appendCodePoint(k);
            } else if (dd < d) {
                if (last != ChatColor.GRAY) {
                    last = ChatColor.GRAY;
                    stringbuilder.append(last);
                }
                stringbuilder.appendCodePoint(k);
            } else {
                if (last != ChatColor.WHITE) {
                    last = ChatColor.WHITE;
                    stringbuilder.append(last);
                }
                stringbuilder.appendCodePoint(k);
            }
        }
        return new String(stringbuilder);
    }
}
