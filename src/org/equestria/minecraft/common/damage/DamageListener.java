
package org.equestria.minecraft.common.damage;

import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
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

