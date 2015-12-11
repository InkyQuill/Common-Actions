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

import guava10.com.google.common.base.Joiner;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.ItemData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemsListener
implements Listener {

    public ItemsListener() {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItem(event.getNewSlot());
        if (item != null) {
            String key = Joiner.on(":").join(new Object[]{item.getTypeId(), item.getDurability()});
            ItemData i = CASettings.getItemSettings(key);
            if (i.HoldMessage != null) {
                SendMessage(i.HoldMessage, i.HoldType, i.HoldRadius, p);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPickupItem(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        if (item != null) {
            String key = Joiner.on(":").join(new Object[]{item.getTypeId(), item.getDurability()});
            ItemData i = CASettings.getItemSettings(key);
            if (i.PickupMessage != null) {
                SendMessage(i.PickupMessage, i.PickupType, i.PickupRadius, p);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        ItemStack item = p.getItemInHand();
        if (item != null) {
            String key = Joiner.on(":").join(new Object[]{item.getTypeId(), item.getDurability()});
            ItemData i = CASettings.getItemSettings(key);
            if (i.UseMessage != null) {
                SendMessage(i.UseMessage, i.UseType, i.UseRadius, p);
            }
        }
    }

    private void SendMessage(String useMessage, ItemData.Messagetype useType, int useRadius, Player p) {
        if (useType == ItemData.Messagetype.USER) {
            p.sendMessage(useMessage);
        } else {
            String msg = useMessage.replace("%(p)%", p.getDisplayName());
            msg = msg.replace("%(P)%", p.getDisplayName());
            for (Player recipient : CASettings.plugin.getServer().getOnlinePlayers()) {
                if (p.equals(recipient)) {
                    p.sendMessage(msg);
                    continue;
                }
                if (!p.getWorld().equals(recipient.getWorld()) || (p.getLocation().distance(recipient.getLocation())) > useRadius)
                    continue;
                recipient.sendMessage(msg);
            }
        }
    }
}

