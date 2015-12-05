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
package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.CommonAbilities;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class ItemsListener
implements Listener {
    public static final Logger log = Logger.getLogger("ItemsListener");
    private CommonAbilities plugin;

    public ItemsListener(CommonAbilities plugin) {
        this.plugin = plugin;
    }


    // TODO: 06.12.2015 Write Items Listener
    /*

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item != null) {

            if(CASettings.ItemMessages.containsKey(StringUtils.join(new Object[]{item.getTypeId(), item.getDurability()}, ":");))

            String id = StringUtils.join(new Object[]{item.getTypeId(), item.getDurability()}, ":");
            GameMasterController.getInstance(this.plugin).sendMessage(PlayerItemHeldEvent.class, event.getPlayer(), id);
        }
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        String id = StringUtils.join(new Object[]{item.getTypeId(), item.getDurability()}, ":");
        GameMasterController.getInstance(this.plugin).sendMessage(PlayerPickupItemEvent.class, event.getPlayer(), id);
    }

    */
}

