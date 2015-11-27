package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by obruchnikov_pa on 25.11.2015.
 * includes RealisticChat!
 */
public class PlayerChatHandler implements Listener {

    private static final String MSG_START = "[/i/]";
    private final Random random;

    public PlayerChatHandler() {
        random = new Random();
    }

    public static void deliverMessage(Player to, Player from, String msg) {
        AsyncPlayerChatEvent playerchatevent = createChatEvent(to, from, msg);
        callChatEvent(playerchatevent);
    }

    private static void callChatEvent(AsyncPlayerChatEvent playerchatevent) {
        playerchatevent.setMessage(MSG_START + playerchatevent.getMessage());
        Bukkit.getServer().getPluginManager().callEvent(playerchatevent);
        if (playerchatevent.isCancelled()) {
            CASettings.L.info(" ignoring chat event cancelled by other plugin: " + playerchatevent);
            return;
        }
        String s = String.format(playerchatevent.getFormat(), playerchatevent.getPlayer().getDisplayName(), playerchatevent.getMessage());
        s = s.replace(MSG_START, "");
        Player player;
        for (Iterator iterator = playerchatevent.getRecipients().iterator(); iterator.hasNext(); player.sendMessage(s)) {
            player = (Player) iterator.next();
        }
    }

    private static AsyncPlayerChatEvent createChatEvent(Player to, Player from, String s) {
        ChatColor chatcolor = from.equals(to) ? CASettings.chat.spokenPlayerColor : CASettings.chat.heardPlayerColor;
        int i = s.indexOf(MSG_START);
        if (i != -1) {
            s = s.substring(i + 1);
        }
        String s2 = CASettings.chat.chatLineFormat;
        String s3 = String.format(s2, String.valueOf(chatcolor) + "%1$s" + CASettings.chat.messageColor, "%2$s");
        AsyncPlayerChatEvent playerchatevent = new AsyncPlayerChatEvent(true, from, s, new HashSet<Player>());
        playerchatevent.getRecipients().clear();
        playerchatevent.getRecipients().add(to);
        playerchatevent.setFormat(s3);
        return playerchatevent;
    }

    @EventHandler
    public void OnPlayerChat(AsyncPlayerChatEvent event)
    {
        Player p = event.getPlayer();
        PlayerSettings ps = CASettings.getPlayerSettings(p);
        if(ps.GM.Enabled) {
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

            if (!message.startsWith(MSG_START)) {

                CASettings.L.info(message);

                double d1 = CASettings.chat.garbleRangeDivisor;
                double d = CASettings.chat.speakingRangeMeters;
                int i = countExclamationMarks(message);
                if (i > 0) {

                    if (i > CASettings.chat.yellMax)
                        i = CASettings.chat.yellMax;

                    int k = CASettings.chat.yellHunger.get(i - 1);
                    p.setFoodLevel(p.getFoodLevel() - k);
                    double u = CASettings.chat.yellDistance.get(i - 1);
                    d += u;
                }

                int j = countParenthesizeNests(message);
                if (j > 0) {
                    d -= CASettings.chat.whisperRangeDecrease;
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

                    if (d2 <= d) {
                        double d9 = d / d1;
                        if (d2 > d9) {
                            double d12 = (d2 - d9) / d;
                            double d15 = 1.0D - d12;
                            deliverMessage(to, p, garbleMessage(message, d15));
                        } else {
                            deliverMessage(to, p, message);
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    private int countExclamationMarks(String s) {
        int i;
        for(i = 0; s.length() > 1 && s.endsWith("!"); i++) {
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

    private String garbleMessage(String s, double d) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = 0;
        while (i < s.length()) {
            int k = s.codePointAt(i);
            i += Character.charCount(k);
            if (random.nextDouble() < d) {
                stringbuilder.appendCodePoint(k);
            } else if (random.nextDouble() < CASettings.chat.garblePartialChance) {
                stringbuilder.append(CASettings.chat.dimMessageColor);
                stringbuilder.appendCodePoint(k);
                stringbuilder.append(CASettings.chat.messageColor);
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
}
