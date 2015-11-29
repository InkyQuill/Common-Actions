
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

import java.util.List;

public class EffectsChecker
        implements EventChecker {
    private static EffectsChecker instance;
    private CommonAbilities plugin;

    //TODO: Add death Effects!

    private EffectsChecker(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    public static EffectsChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new EffectsChecker(plugin);
        }
        return instance;
    }

    public void addEffect(String playerName, Effect effect) {
        Player player = this.plugin.getServer().getPlayer(playerName);
        PlayerSettings p = CASettings.getPlayerSettings(playerName);

        int res = -1;
        for (int i = 0; i < p.Effects.size(); i++) {
            if (p.Effects.get(i).Type == effect.Type) {
                res = i;
                break;
            }
        }
        if (res == -1) {
            p.Effects.add(effect);
        } else {
            p.Effects.set(res, effect);
        }

        EffectUpdater repeater = new EffectUpdater(player, this);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, repeater, 0);
    }

    /*
    public void addDeathEffects(Player player) {
        String effectsString = this.plugin.getConfigItem(DEATH_EFFECTS);
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        for (PermissionAttachmentInfo info : permissions) {
            String permEffects = this.plugin.getConfigItem(info.getPermission());
            if (permEffects == null || permEffects.length() <= 0) continue;
            effectsString = permEffects;
        }
        for (String effect : StringUtils.split(effectsString, ",")) {
            String[] effectData = StringUtils.split(effect, " ");
            if (effectData.length != 2) continue;
            String effectName = effectData[0];
            String effectPower = effectData[1];
            int effectStrenght = 0;
            try {
                PotionEffectType.getByName(effectName);
                effectStrenght = Integer.parseInt(effectPower);
            }
            catch (NumberFormatException ex) {
                log.log(Level.WARNING, "Effect strenght is not numeric");
            }
            catch (Exception e) {
                log.log(Level.WARNING, "Effect is wrong");
            }
            Effect existedEffects = EffectsChecker.getInstance(this.plugin).getEffects(player.getName());
            if (existedEffects == null) {
                existedEffects = new Effect();
            }
            boolean isContain = false;
            for (PotionEffect eff : existedEffects.getPotionEffects()) {
                if (!eff.getType().equals(PotionEffectType.getByName(effectName))) continue;
                isContain = true;
            }
            if (isContain) continue;
            existedEffects.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effectName), 1250, effectStrenght));
            this.addEffect(player.getName(), existedEffects);
        }
    }
*/
    public void removeEffect(String playerName, Effect effect) {


        PlayerSettings p = CASettings.getPlayerSettings(playerName);

        int res = -1;
        for (int i = 0; i < p.Effects.size(); i++) {
            if (p.Effects.get(i).Type == effect.Type) {
                res = i;
                break;
            }
        }
        if (res != -1) {
            p.Effects.remove(res);
        }

    }

    @Override
    public <T extends Event> boolean checkEvent(T event, JavaPlugin plugin) {
        return false;
    }

    @Override
    public void callEvent(Player player) {

        PlayerSettings p = CASettings.getPlayerSettings(player);

        List<Effect> effects = p.Effects;
        if (effects != null && effects.size() > 0) {
            for (Effect effect : effects) {
                player.addPotionEffect(effect.getPotionEffect(), true);
            }
            EffectUpdater repeater = new EffectUpdater(player, this);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, repeater, 180);
        }
    }
}

