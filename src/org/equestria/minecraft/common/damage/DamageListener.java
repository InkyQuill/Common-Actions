/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.damage;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.BlockChecker;
import org.equestria.minecraft.common.checkers.EffectsChecker;
import org.equestria.minecraft.common.checkers.FoodEffectsChecker;

public class DamageListener
implements Listener {
    private CommonAbilities plugin;

    public DamageListener(JavaPlugin plugin) {
        this.plugin = (CommonAbilities)plugin;
    }

    @EventHandler
    public void onDamageDone(EntityDamageEvent event) {
        boolean isCancelled = event.isCancelled();
        isCancelled = isCancelled || FoodEffectsChecker.getInstance(this.plugin).checkEvent((Event)event, null);
        isCancelled = isCancelled || DamageController.getInstance(this.plugin).processDamage(event);
        event.setCancelled(isCancelled);
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (EntityType.PLAYER.equals((Object)event.getEntityType())) {
			Boolean DEenabled = this.plugin.getBoolConfigItem("deathEffectsEnabled");
			if(DEenabled)
				EffectsChecker.getInstance(this.plugin).addDeathEffects((Player)event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        boolean isCancelled = event.isCancelled();
        boolean result = BlockChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin);
        isCancelled = isCancelled || !result;
        event.setCancelled(isCancelled);
    }
}

