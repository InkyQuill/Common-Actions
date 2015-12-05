
package net.inkyquill.equestria.ca.checkers;

import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.runnable.EffectUpdater;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.Effect;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class EffectsChecker
        implements EventChecker {
    private static EffectsChecker instance;
    private CommonAbilities plugin;

    private EffectsChecker(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    public static EffectsChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new EffectsChecker(plugin);
        }
        return instance;
    }

    public static void RemoveAll(Player p) {
        PlayerSettings sett = CASettings.getPlayerSettings(p);
        sett.Effects.clear();
    }

    public static void Remove(Player p, PotionEffectType eff) {
        PlayerSettings sett = CASettings.getPlayerSettings(p);
        if (sett.Effects.containsKey(eff)) {
            sett.Effects.remove(eff);
        }
    }

    public static void Add(Player p, PotionEffectType eff, int amp) {
        PlayerSettings sett = CASettings.getPlayerSettings(p);
        sett.Effects.put(eff, amp);

        EffectUpdater repeater = new EffectUpdater(p, getInstance(CASettings.plugin));
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CASettings.plugin, repeater, 0);
    }

    public void addDeathEffects(Player player) {

        PlayerSettings ps = CASettings.getPlayerSettings(player);

        if (CASettings.DeathEffectsEnabled)
            for (Effect eff : CASettings.DeathEffects) {
                ps.Effects.put(eff.Type, eff.Amplifier);
            }

        EffectUpdater repeater = new EffectUpdater(player, this);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, repeater, 180);
    }

    @Override
    public <T extends Event> boolean checkEvent(T event, JavaPlugin plugin) {
        return false;
    }

    @Override
    public void callEvent(Player player) {

        PlayerSettings p = CASettings.getPlayerSettings(player);

        Map<PotionEffectType, Integer> effects = p.Effects;
        if (effects != null && effects.size() > 0) {

            for (PotionEffectType eff : p.Effects.keySet()) {
                player.addPotionEffect(new PotionEffect(eff, 200, effects.get(eff)), true);
            }

            EffectUpdater repeater = new EffectUpdater(player, this);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, repeater, 180);
        }
    }
}

