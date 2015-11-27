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

import net.inkyquill.equestria.ca.CommonAbilities;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
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
        isCancelled = isCancelled || FoodEffectsChecker.getInstance(this.plugin).checkEvent(event, null);
        isCancelled = isCancelled || DamageController.getInstance(this.plugin).processDamage(event);
        event.setCancelled(isCancelled);
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (EntityType.PLAYER.equals(event.getEntityType())) {
            Boolean DEenabled = this.plugin.getBoolConfigItem("deathEffectsEnabled");
            if (DEenabled)
                EffectsChecker.getInstance(this.plugin).addDeathEffects((Player)event.getEntity());
        }
    }
}

