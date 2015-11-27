package net.inkyquill.equestria.ca.handlers;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

class RealisticChatListener implements Listener { 

    private Random random;
    private static final Enchantment EFFICIENCY;
    private List<Player> disabledGeneralChatPlayersList = new ArrayList<Player>();
    private List<Player> generalChatPlayersList = new ArrayList<Player>();

    static {
        EFFICIENCY = Enchantment.DIG_SPEED;
    }
	
    public RealisticChatListener() {
        random = new Random();       


    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent playerChatEvent) {

    }



}