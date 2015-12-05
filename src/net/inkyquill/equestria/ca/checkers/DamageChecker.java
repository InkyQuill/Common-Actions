
package net.inkyquill.equestria.ca.checkers;

import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.settings.CASettings;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageChecker {

    private static DamageChecker instance;

    private CommonAbilities plugin;

    private DamageChecker(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    public static synchronized DamageChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new DamageChecker(plugin);
        }
        return instance;
    }

    public boolean processDamage(EntityDamageEvent event) {

        if (CASettings.DeathEffectsEnabled) {
            if (!(event.getEntity() instanceof Player)) {
                Player player = (Player) event.getEntity();

                if (player.getHealth() <= event.getDamage() + 1) {
                    event.setCancelled(true);
                    EffectsChecker.getInstance(this.plugin).addDeathEffects(player);
                    player.setHealth(1);
                    player.teleport(CASettings.DeathTPLocation);
                    return true;
                }
            }
        }
        return false;
    }
}

