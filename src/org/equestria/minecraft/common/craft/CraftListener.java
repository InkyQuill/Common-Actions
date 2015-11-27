/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.CraftItemEvent
 *  org.bukkit.event.inventory.PrepareItemCraftEvent
 *  org.bukkit.inventory.CraftingInventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.craft;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.CraftChecker;

public class CraftListener
implements Listener {
    public static final Logger log = Logger.getLogger("CraftListener");
    private CommonAbilities plugin;

    public CraftListener(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraftItemEvent(PrepareItemCraftEvent event) {
        if (!CraftChecker.getInstance(this.plugin).checkEvent(event, this.plugin)) {
            event.getInventory().setResult(null);
        }
    }
}

