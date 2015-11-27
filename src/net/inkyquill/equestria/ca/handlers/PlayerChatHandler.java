package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by obruchnikov_pa on 25.11.2015.
 * includes RealisticChat!
 */
public class PlayerChatHandler implements Listener {

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
        }
        else
        {
            String message = event.getMessage();
            if(message.startsWith(getDeviceTag())) {
                return;
            }
            ArrayList arraylist = new ArrayList();
            
            
            CASettings.L.info(message);
            CASettings.L.info(getGlobalPrefix());
            if(isPlayerInGeneralList(p) || message.startsWith(getGlobalPrefix()))  {
                globallyDeliverMessage(p, message.replaceFirst(getGlobalPrefix(), StringUtils.EMPTY));
                event.setCancelled(true);
                return;
            }

            double d1 = plugin.getConfig().getDouble("garbleRangeDivisor", 2D);
            double d = plugin.getConfig().getDouble("speakingRangeMeters", 50D);
            int i = countExclamationMarks(message);
            if (i > 0) {
                d = processYellMessage(p, arraylist, i);
            }
            int j = countParenthesizeNests(message);
            if (j > 0) {
                d = processWhisperMessage(p, arraylist, j);
            }
            if(holdingBullhorn(p)) {
                arraylist.add("send-mega");
            }
            else if (holdingSmartphone(p)) {
                processSmartPhoneMessage(p, arraylist, message);
            }
            arraylist.add((new StringBuilder()).append("r=").append(d).toString());
            CASettings.L.info((new StringBuilder()).append("<").append(player.getName()).append(": ").
                    append(joinList(arraylist, ",")).append("> ").append(message).toString());
            Iterator iterator = event.getRecipients().iterator();
            while (iterator.hasNext()) {
                Player player1 = (Player)iterator.next();
                ArrayList arraylist1 = new ArrayList();
                if (p.equals(player1)) {
                    deliverMessage(player1, p, message, arraylist1);
                    continue;
                }
                if(!p.getWorld().equals(player1.getWorld())) {
                    continue;
                }
                double d2 = p.getLocation().distance(player1.getLocation());
                arraylist1.add((new StringBuilder()).append("d=").append(d2).toString());
                if(hasWalkieTalking(p) && hasWalkieListening(player1)) {
                    processWalkieMessage(p, player1, arraylist1, message);
                    if(!p.getConfig().getBoolean("walkieHearLocally", true)) {
                        continue;
                    }
                }
                double d3 = d;
                if(holdingBullhorn(p)) {
                    processBullhornMessage(p, player1, arraylist1, d3);
                }
                double d6 = getEarTrumpetRange(player1);
                if(d6 != 0.0D) {
                    arraylist1.add((new StringBuilder()).append("ear=").append(d6).toString());
                    d3 += d6;
                }
                arraylist1.add((new StringBuilder()).append("hr=").append(d3).toString());
                if(d2 <= d3) {
                    double d9 = d3 / d1;
                    if(d2 > d9) {
                        double d12 = (d2 - d9) / d3;
                        double d15 = 1.0D - d12;
                        arraylist1.add((new StringBuilder()).append("clarity=").append(d15).toString());
                        deliverMessage(player1, p, garbleMessage(message, d15), arraylist1);
                    } else {
                        deliverMessage(player1, p, message, arraylist1);
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    private double processYellMessage(Player player, List list, int i) {
        double d = plugin.getConfig().getDouble("speakingRangeMeters", 50D);
        i = Math.min(i, plugin.getConfig().getInt("yellMax", 4));
        list.add((new StringBuilder()).append("yell=").append(i).toString());
        int ai[] = {
                1, 2, 4, 20
        };
        double ad[] = {
                10D, 50D, 100D, 500D
        };
        int k = plugin.getConfig().getInt((new StringBuilder()).append("yell.").append(i).append(".hunger").toString(), ai[i - 1]);
        player.setFoodLevel(player.getFoodLevel() - k);
        d += plugin.getConfig().getDouble((new StringBuilder()).append("yell.").append(i).append(".rangeIncrease").toString(), ad[i - 1]);
        return d;
    }

    private double processWhisperMessage(Player player, List list, int j) {
        double d = plugin.getConfig().getDouble("speakingRangeMeters", 50D);
        list.add((new StringBuilder()).append("whisper=").append(j).toString());
        d -= plugin.getConfig().getDouble("whisperRangeDecrease", 40D) * (double)j;
        if(d < 1.0D) {
            d = 1.0D;
        }
        return d;
    }

    private void processSmartPhoneMessage(Player player, List list, String message) {
        list.add("send-phone");
        SmartphoneCall smartphonecall = SmartphoneCall.lookup(player);
        if(smartphonecall != null) {
            smartphonecall.say(player, message);
            if(!plugin.getConfig().getBoolean("smartphoneHearLocally", true)) {
                return;
            }
        } else {
            String s1 = message.replaceAll("[()!]", "");
            Player player2 = player;
            Player player3 = Bukkit.getPlayer(s1);
            if(player3 == null) {
                player.sendMessage((new StringBuilder()).append("Sorry, I don't understand '").append(s1).append("'").toString());
                player.sendMessage("Say a player name to place a call.");
            } else {
                player.sendMessage((new StringBuilder()).append("Ringing ").append(player3.getDisplayName()).append("...").toString());
                if(isSmartphone(player3.getItemInHand())) {
                    new SmartphoneCall(player2, player3);
                } else {
                    player.sendMessage((new StringBuilder()).append(player3.getDisplayName()).append(" did not answer").toString());
                }
            }
        }
        if(!plugin.getConfig().getBoolean("smartphoneWaterproof", false)) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if(block != null && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)) {
                if(smartphonecall != null) {
                    smartphonecall.hangup();
                }
                Item item = player.getWorld().dropItemNaturally(player.getLocation(), player.getItemInHand());
                item.setPickupDelay(plugin.getConfig().getInt("smartphoneWaterPickupDelay", 10000));
                player.setItemInHand(null);
                player.sendMessage("Your smartphone was destroyed by water damage");
            }
        }
    }

    private void processWalkieMessage(Player player, Player player1, List list, String message) {
        double d2 = player.getLocation().distance(player1.getLocation());
        ArrayList arraylist2 = new ArrayList(list);
        double d4 = plugin.getConfig().getDouble("walkieRangeMeters", 2000D);
        double d7 = plugin.getConfig().getDouble("walkieGarbleDivisor", 2D);
        double d10 = d4 / d7;
        arraylist2.add((new StringBuilder()).append("walkie=").append(d4).append("/").append(d10).toString());
        if(d2 < d10) {
            deliverMessage(player, player,
                    (new StringBuilder()).append("walkie").append(getDeviceTag()).append(message).toString(), arraylist2);
        } else  if(d2 < d4) {
            double d13 = 1.0D - (d2 - d10) / d4;
            arraylist2.add((new StringBuilder()).append("wc=").append(d13).toString());
            deliverMessage(player1, player, (new StringBuilder()).append("walkie").append(getDeviceTag()).
                    append(garbleMessage(message, d13)).toString(), arraylist2);
        }
    }

    private void processBullhornMessage(Player player, Player player1, List list, double d3) {

        double d5 = player1.getLocation().getZ() - player.getLocation().getZ();
        double d8 = player1.getLocation().getX() - player.getLocation().getX();
        double d11 = 0.0D;
        if(d5 <= 0.0D && d8 >= 0.0D) {
            d11 = Math.toDegrees(Math.tan(d8 / (-1D * d5))) + 180D;
        }
        if(d5 >= 0.0D && d8 > 0.0D) {
            d11 = Math.toDegrees(Math.tan(d5 / d8)) + 270D;
        }
        if(d5 > 0.0D && d8 <= 0.0D) {
            d11 = Math.toDegrees(Math.tan((-1D * d8) / d5));
        }
        if(d5 <= 0.0D && d8 < 0.0D) {
            d11 = Math.toDegrees(Math.tan((-1D * d5) / (-1D * d8))) + 90D;
        }
        d11 %= 360D;
        double d14 = player.getLocation().getYaw() * 1.0F;
        double d16 = (d14 + 180D) % 360D;
        double d17 = (d11 + 180D) % 360D;
        double d18 = Math.abs(d14 - d11);
        double d19 = Math.abs(d16 = d17);
        list.add((new StringBuilder()).append("mega-actSlop=").append(d11).toString());
        list.add((new StringBuilder()).append("mega-micSlop=").append(d14).toString());
        list.add((new StringBuilder()).append("mega-deltaZ=").append(d5).toString());
        list.add((new StringBuilder()).append("mega-deltaX=").append(d8).toString());
        list.add((new StringBuilder()).append("mega-degree=").append(d18).toString());
        if(d18 < plugin.getConfig().getDouble("bullhornWidthDegrees", 70D) ||
                d19 < plugin.getConfig().getDouble("bullhornWidthDegrees", 70D)) {
            list.add("mega-HEARD");
            d3 *= plugin.getConfig().getDouble("bullhornFactor", 2D);
        }
    }

    private static boolean hasWalkieTalking(Player player) {
        if(!plugin.getConfig().getBoolean("walkieEnable", true)) {
            return false;
        } else {
            ItemStack itemstack = player.getItemInHand();
            return itemstack != null && itemstack.getTypeId() == plugin.walkieItemId;
        }
    }

    private static boolean hasWalkieListening(Player player) {
        if(!plugin.getConfig().getBoolean("walkieEnable", true)) {
            return false;
        }
        ItemStack aitemstack[] = player.getInventory().getContents();
        for(int i = 0; i < 9; i++) {
            ItemStack itemstack = aitemstack[i];
            if(itemstack != null && itemstack.getTypeId() == plugin.walkieItemId)
                return true;
        }
        return false;
    }

    private static boolean holdingBullhorn(Player player) {
        if(!plugin.getConfig().getBoolean("bullhornEnable", true)) {
            return false;
        } else {
            ItemStack itemstack = player.getItemInHand();
            return itemstack != null && itemstack.getTypeId() == plugin.bullhornItemId;
        }
    }

    private double getEarTrumpetRange(Player player) {
        ItemStack itemstack = getEarTrumpet(player);
        if(itemstack == null) {
            return 0.0D;
        }
        int i = itemstack.getEnchantmentLevel(EFFICIENCY);
        if(i > 3) {
            i = 3;
        }
        double ad[] = {
                100D, 150D, 400D
        };
        double d = plugin.getConfig().getDouble((new StringBuilder()).append("earTrumpet.").
                append(i).append(".rangeIncrease").toString(), ad[i - 1]);
        return d;
    }

    private ItemStack getEarTrumpet(Player player) {
        if(!plugin.getConfig().getBoolean("earTrumpetEnable", true)) {
            return null;
        }
        ItemStack itemstack = player.getInventory().getHelmet();
        if(itemstack != null && itemstack.getType() == Material.GOLD_HELMET && itemstack.containsEnchantment(EFFICIENCY)) {
            return itemstack;
        } else {
            return null;
        }
    }

    public static String joinList(ArrayList arraylist, String s) {
        StringBuilder stringbuilder = new StringBuilder();
        String s2;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext(); stringbuilder.append((new StringBuilder()).append(s2).append(s).toString()))
            s2 = (String)iterator.next();

        String s1 = stringbuilder.toString();
        if(s1.length() == 0) {
            return "";
        } else {
            return s1.substring(0, s1.length() - s.length());
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
        for(i = 0; s.length() > 2 && s.startsWith("(") && s.endsWith(")"); i++) {
            s = s.substring(1, s.length() - 1);
        }

        return i;
    }

    private String garbleMessage(String s, double d) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = 0;
        while(i < s.length()) {
            int k = s.codePointAt(i);
            i += Character.charCount(k);
            if(random.nextDouble() < d) {
                stringbuilder.appendCodePoint(k);
            } else
            if(random.nextDouble() < plugin.getConfig().getDouble("garblePartialChance", 0.10000000000000001D)) {
                stringbuilder.append(plugin.dimMessageColor);
                stringbuilder.appendCodePoint(k);
                stringbuilder.append(plugin.messageColor);
            } else {
                stringbuilder.append(' ');
                j++;
            }
        }
        if(j == s.length()) {
            String s1 = plugin.getConfig().getString("garbleAllDroppedMessage", "~~~");
            if(s1 != null) {
                return s1;
            }
        }
        return new String(stringbuilder);
    }

    public static String getDeviceTag() {
        return "\001";
    }

    public void globallyDeliverMessage(Player player, String s) {
        synchronized (disabledGeneralChatPlayersList) {
            ArrayList arraylist = new ArrayList();
            arraylist.add("global");
            Player aplayer[] = Bukkit.getOnlinePlayers();
            int i = aplayer.length;
            for(int j = 0; j < i; j++) {
                Player player1 = aplayer[j];
                if (disabledGeneralChatPlayersList.contains(player1)) {
                    continue;
                }
    			/*try {
					sendMessageToJabber(s);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
                deliverMessage(player1, player, (new StringBuilder()).append("global").append(getDeviceTag()).append(plugin.generalChannelColor)
                        .append(s).toString(), arraylist);
            }
        }
    }
    /*
    private void sendMessageToJabber(String messageText) throws XMPPException {
    	XMPPConnection connection = new XMPPConnection("jabber.ru");
    	connection.connect();
    	//connection.login("iss_blackd", "dytpfgysqgtnhjczy666");    	
    	connection.loginAnonymously();
    	Message message = new Message();
    	message.setTo("iss_blackd@jabber.ru");
    	message.setSubject("Server down");
    	message.setBody(messageText); 
    	connection.sendPacket(message);
    	connection.disconnect();
    }
*/
    public static void deliverMessage(Player player, Player player1, String s, ArrayList arraylist) {
        PlayerChatEvent playerchatevent = createChatEvent(player, player1, s, arraylist);
        callChatEvent(playerchatevent);
    }

    private static void callChatEvent(PlayerChatEvent playerchatevent) {
        playerchatevent.setMessage((new StringBuilder()).append(getDeviceTag()).append(playerchatevent.getMessage()).toString());
        Bukkit.getServer().getPluginManager().callEvent(playerchatevent);
        if(playerchatevent.isCancelled()) {
           CASettings.L.info((new StringBuilder()).append(" ignoring chat event cancelled by other plugin: ").append(playerchatevent).toString());
            return;
        }
        String s = String.format(playerchatevent.getFormat(), new Object[] {
                playerchatevent.getPlayer().getDisplayName(), playerchatevent.getMessage()
        });
        s = s.replace(getDeviceTag(), "");
        Player player;
        for(Iterator iterator = playerchatevent.getRecipients().iterator(); iterator.hasNext(); player.sendMessage(s)) {
            player = (Player)iterator.next();
        }

    }

    private static PlayerChatEvent createChatEvent(Player player, Player player1, String s, ArrayList arraylist) {
        ChatColor chatcolor = player1.equals(player) ? plugin.spokenPlayerColor : plugin.heardPlayerColor;
        String s1 = null;
        int i = s.indexOf(getDeviceTag());
        if(i != -1) {
            s1 = s.substring(0, i);
            s = s.substring(i + 1);
        }
        String s2 = plugin.getConfig().getString("chatLineFormat", "%1$s: %2$s");
        String s3 = s2;
        if(holdingBullhorn(player1)) {
            String s4 = bullhornDirection(player, player1);
            if(s4 != null && !s4.equals("")) {
                s3 = String.format(plugin.getConfig().getString("bullhornChatLineFormat", "%1$s [%3$s]: %2$s"), new Object[] {
                        "%1$s", "%2$s", s4
                });
            }
        }
        if(s1 != null) {
            if(s1.equals("walkie")) {
                s3 = plugin.getConfig().getString("walkieChatLineFormat", "[walkie] %1$s: %2$s");
            } else if(s1.equals("cell")) {
                s3 = plugin.getConfig().getString("smartphoneChatLineFormat", "[cell] %1$s: %2$s");
            } else if(s1.equals("global")) {
                s3 = plugin.getConfig().getString("globalChatLineFormat", "[global] %1$s: %2$s");
            }
        }
        if(s3 == null) {
            s3 = s2;
        }

        s3 = String.format(s3, new Object[] {
                (new StringBuilder()).append(chatcolor).append("%1$s").append(plugin.messageColor).toString(), "%2$s"
        });
        PlayerChatEvent playerchatevent = new PlayerChatEvent(player1, s);
        playerchatevent.getRecipients().clear();
        playerchatevent.getRecipients().add(player);
        playerchatevent.setFormat(s3);
        return playerchatevent;
    }

    private static String bullhornDirection(Player player, Player player1) {
        if(!plugin.getConfig().getBoolean("bullhornEnable", true) || !holdingBullhorn(player1)) {
            return "";
        }
        String s = "";
        double d = player.getLocation().getX();
        double d1 = player.getLocation().getZ();
        double d2 = player1.getLocation().getX();
        double d3 = player1.getLocation().getZ();
        if(d1 > d3) {
            s = (new StringBuilder()).append(s).append("North").toString();
        }
        if(d1 < d3) {
            s = (new StringBuilder()).append(s).append("South").toString();
        }
        if(d > d2) {
            s = (new StringBuilder()).append(s).append("West").toString();
        }
        if(d < d2) {
            s = (new StringBuilder()).append(s).append("East").toString();
        }
        return s;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent playeritemheldevent) {
        Player player = playeritemheldevent.getPlayer();
        int i = playeritemheldevent.getNewSlot();
        ItemStack itemstack = player.getInventory().getItem(i);
        if(!isSmartphone(itemstack)) {
            SmartphoneCall smartphonecall = SmartphoneCall.lookup(player);
            if(smartphonecall != null) {
                smartphonecall.hangup();
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {

        Player player = playerCommandPreprocessEvent.getPlayer();
        if(getGlobalPrefix() == null) {
            return;
        }
        String message = playerCommandPreprocessEvent.getMessage();
        if (message.equals(getGlobalChatOffMessage())) {
            addPlayerToOffGeneralList(player);
            player.sendMessage(plugin.generalMessagesColor + "Leaving general channel");
            playerCommandPreprocessEvent.setCancelled(true);
            return;
        } else if (message.equals(getGlobalChatOnMessage())) {
            removePlayerFromOffGeneralList(player);
            player.sendMessage(plugin.generalMessagesColor + "Entering general channel");
            playerCommandPreprocessEvent.setCancelled(true);
            return;
        } else if (message.equals(getGlobalPrefix())) {
            if (isPlayerInGeneralList(player)) {
                removePlayerFromGeneralList(player);
                player.sendMessage(plugin.generalMessagesColor + "General channel only mode off");
            } else {
                addPlayerToGeneralList(player);
                player.sendMessage(plugin.generalMessagesColor + "General channel only mode on");
            }
            playerCommandPreprocessEvent.setCancelled(true);
            return;
        } else if (message.startsWith(getGlobalPrefix())) {
            String[] splittedMessage = message.split(" ");
            if (splittedMessage.length >= 2) {
                if (splittedMessage[0].equals(getGlobalPrefix())) {
                    globallyDeliverMessage(player, message.replaceFirst(getGlobalPrefix(), StringUtils.EMPTY));
                    playerCommandPreprocessEvent.setCancelled(true);
                    return;
                }
            }
        }
        if(isPlayerInGeneralList(player) && !message.startsWith("/")) {
            globallyDeliverMessage(player, message);
            playerCommandPreprocessEvent.setCancelled(true);
        }
    }

    private void addPlayerToOffGeneralList(Player player) {
        synchronized(disabledGeneralChatPlayersList) {
            disabledGeneralChatPlayersList.add(player);
        }
    }

    private void removePlayerFromOffGeneralList(Player player) {
        synchronized(disabledGeneralChatPlayersList) {
            disabledGeneralChatPlayersList.remove(player);
        }
    }

    private void addPlayerToGeneralList(Player player) {
        synchronized(generalChatPlayersList) {
            generalChatPlayersList.add(player);
        }
    }

    private void removePlayerFromGeneralList(Player player) {
        synchronized(generalChatPlayersList) {
            generalChatPlayersList.remove(player);
        }
    }

    private boolean isPlayerInGeneralList(Player player) {
        synchronized(generalChatPlayersList) {
            return generalChatPlayersList.contains(player);
        }
    }

    private String getGlobalPrefix() {
        return plugin.getConfig().getString("globalPrefix", "/g ");
    }

    private String getGlobalChatOffMessage() {
        return plugin.getConfig().getString("globalChatOff", "/g off");
    }

    private String getGlobalChatOnMessage() {
        return plugin.getConfig().getString("globalChatOn", "/g on");
    }

    private boolean isSmartphone(ItemStack itemstack) {
        if(!plugin.getConfig().getBoolean("smartphoneEnable", true)) {
            return false;
        } else {
            return itemstack != null && itemstack.getTypeId() == plugin.smartphoneItemId;
        }
    }

    private boolean holdingSmartphone(Player player) {
        return isSmartphone(player.getItemInHand());
    }
    
}
