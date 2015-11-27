package net.inkyquill.equestria.ca.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

class SmartphoneCall {
    public SmartphoneCall(Player player, Player player1) {
        new SmartphoneRinger(plugin, player);
        new SmartphoneRinger(plugin, player1);
        members = new HashSet();
        members.add(player);
        members.add(player1);
        ArrayList arraylist = new ArrayList();
        Player player2;
        for(Iterator iterator = members.iterator(); iterator.hasNext(); arraylist.add(player2.getDisplayName())) {
            player2 = (Player)iterator.next();
        }

        Player player3;
        for(Iterator iterator1 = members.iterator(); iterator1.hasNext(); 
        player3.sendMessage((new StringBuilder()).append("Call established: ").
        		append(RealisticChatListener.joinList(arraylist, ", ")).toString())) {
            player3 = (Player)iterator1.next();            
        }
        whenStarted = new Date();
        calls.put(player, this);
        calls.put(player1, this);
    }

    public static SmartphoneCall lookup(Player player) {
        return (SmartphoneCall)calls.get(player);
    }

    public void say(Player player, String s) {
        ArrayList arraylist = new ArrayList();
        arraylist.add("smartphone");
        Iterator iterator = members.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Player player1 = (Player)iterator.next();
            if(player != player1)
                RealisticChatListener.deliverMessage(player1, player, (new StringBuilder()).append("cell").append(RealisticChatListener.getDeviceTag()).append(s).toString(), arraylist);
        } while(true);
    }

    public void hangup() {
        whenEnded = new Date();
        long l = whenEnded.getTime() - whenStarted.getTime();
        long l1 = (l / 1000L) % 60L;
        long l2 = l / 1000L / 60L;
        String s = (new StringBuilder()).append(l2).append(" m ").append(l1).append(" s").toString();
        Player player;
        for(Iterator iterator = members.iterator(); iterator.hasNext(); calls.remove(player)) {
            player = (Player)iterator.next();
            player.sendMessage((new StringBuilder()).append("Hung up call, lasted ").append(s).toString());
        }

    }

    static RealisticChat plugin;
    static ConcurrentHashMap calls = new ConcurrentHashMap();
    HashSet members;
    Date whenStarted;
    Date whenEnded;

}
