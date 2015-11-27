/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Monster
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.entity.EntityTargetLivingEntityEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.monsters;

import java.util.logging.Logger;

import org.bukkit.entity.Monster;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.MonsterChecker;

public class MonstersListener
implements Listener {
    public static final Logger log = Logger.getLogger("MonstersListener");
    private CommonAbilities plugin;

    public MonstersListener(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTargetLivingEvent(EntityTargetLivingEntityEvent event) {
        event.setCancelled(event.isCancelled() | MonsterChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin));
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event) {
        event.setCancelled(event.isCancelled() | MonsterChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin));
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Monster) || event.getSpawnReason().equals((Object)CreatureSpawnEvent.SpawnReason.NATURAL)) {
            // empty if block
        }
    }
}

