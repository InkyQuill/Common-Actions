package net.inkyquill.equestria.ca.settings;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerSettings {

    public List<EntityType> Monsters;
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
        Monsters = new ArrayList<EntityType>();
    }

    public List<String> getMonstersList() {
        List<String> result = new ArrayList<String>();
        for (EntityType en : Monsters) {
            result.add(en.getName());
        }
        return result;
    }

    public void setMonstersList(List<String> lst) {
        List<EntityType> result = new ArrayList<EntityType>();
        for (String s : lst) {
            try {
                result.add(EntityType.valueOf(s.trim().toUpperCase()));
            } catch (Exception e) {
                CASettings.L.warning("Can't resolve monster " + s);
            }
        }
        Monsters = result;
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
