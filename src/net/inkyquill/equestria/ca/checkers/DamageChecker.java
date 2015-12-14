
package net.inkyquill.equestria.ca.checkers;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageChecker {

    private static DamageChecker instance;

    public static synchronized DamageChecker getInstance() {
        if (instance == null) {
            instance = new DamageChecker();
        }
        return instance;
    }

    public boolean processDamage(EntityDamageEvent event) {

        if (CASettings.DeathEffectsEnabled) {
            if ((event.getEntity() instanceof Player)) {
                Player player = (Player) event.getEntity();

                if (player.getHealth() <= event.getDamage() + 1) {
                    event.setCancelled(true);
                    PlayerSettings ps = CASettings.getPlayerSettings(player);
                    ps.DeathTimes++;
                    player.sendMessage(ChatColor.RED + CASettings.DeathMessage);
                    EffectsChecker.getInstance().addDeathEffects(player);
                    player.setHealth(1);
                    player.setFireTicks(0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5, 5));
                    player.teleport(CASettings.DeathTPLocation);
                    return true;
                }
            }
        }
        return false;
    }
}

