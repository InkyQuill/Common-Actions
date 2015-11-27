/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.material.MaterialData
 */
package org.equestria.minecraft.common.items;

import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.gamemaster.GameMasterController;

public class ItemsListener
implements Listener {
    public static final Logger log = Logger.getLogger("ItemsListener");
    private CommonAbilities plugin;

    public ItemsListener(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item != null) {
            String id = StringUtils.join((Object[])new Object[]{item.getTypeId(), item.getDurability()}, (String)":");
            GameMasterController.getInstance(this.plugin).sendMessage(PlayerItemHeldEvent.class, event.getPlayer(), id);
        }
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        String id = StringUtils.join((Object[])new Object[]{item.getTypeId(), item.getDurability()}, (String)":");
        GameMasterController.getInstance(this.plugin).sendMessage(PlayerPickupItemEvent.class, event.getPlayer(), id);
    }

    private void showDebugItem(ItemStack item, Player caller) {
        caller.sendMessage("Amount " + item.getAmount());
        caller.sendMessage("getDurability " + item.getDurability());
        caller.sendMessage("getTypeId " + item.getTypeId());
        if (item.getData() != null) {
            caller.sendMessage("getData " + item.getData().getData());
        }
    }
}

