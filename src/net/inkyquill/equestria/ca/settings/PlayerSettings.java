package net.inkyquill.equestria.ca.settings;

import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerSettings {

    public GMsettings GM;
    public Map<PotionEffectType, Integer> Effects;

    public int DeathTimes;

    public PlayerSettings()
    {
        GM = new GMsettings();
        GM.Enabled = false ;
        GM.Color = ChatColor.WHITE;
        GM.Radius = 100;
        GM.Prefix = "World";
        Effects = new HashMap<PotionEffectType, Integer>();
        DeathTimes = 0;
    }

    public Set<Effect> getEffectsList() {
        HashSet<Effect> effects = new HashSet<Effect>();
        for (PotionEffectType eff : Effects.keySet()) {
            effects.add(new Effect(eff, Effects.get(eff)));
        }
        return effects;
    }

    public Iterable<?> getEffects() {

        HashSet<String> effects = new HashSet<String>();
        for (PotionEffectType eff : Effects.keySet()) {
            effects.add(eff.getName() + "(" + Effects.get(eff) + ")");
        }
        return effects;
    }
}
