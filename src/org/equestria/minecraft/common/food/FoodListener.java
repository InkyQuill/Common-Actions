/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.food;

import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.FoodEffectsChecker;

public class FoodListener
implements Listener {
    public static final Logger log = Logger.getLogger("FoodListener");
    private CommonAbilities plugin;

    public FoodListener(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        FoodEffectsChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin);
    }
}

