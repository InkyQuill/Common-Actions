
package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.checkers.DamageChecker;
import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageListener
implements Listener {
    private CommonAbilities plugin;

    public DamageListener(JavaPlugin plugin) {
        this.plugin = (CommonAbilities)plugin;
    }

    @EventHandler
    public void onDamageDone(EntityDamageEvent event) {
        boolean isCancelled = event.isCancelled();
        isCancelled = isCancelled || DamageChecker.getInstance(this.plugin).processDamage(event);
        event.setCancelled(isCancelled);
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {

        if (EntityType.PLAYER.equals(event.getEntityType())) {

            Player p = (Player) event.getEntity();
            PlayerSettings ps = CASettings.getPlayerSettings(p);
            ps.DeathTimes++;
            if (CASettings.DeathEffectsEnabled) {
                EffectsChecker.getInstance(this.plugin).addDeathEffects(p);
            }
        }
    }
}

