package net.inkyquill.equestria.ca.settings;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pavel on 29.11.2015.
 */
public class Effect {

    public static Map<String, PotionEffectType> EffectsMap;

    static {
        EffectsMap = new HashMap<String, PotionEffectType>();
        EffectsMap.put("bl", PotionEffectType.BLINDNESS);
        EffectsMap.put("cn", PotionEffectType.CONFUSION);
        EffectsMap.put("dr", PotionEffectType.DAMAGE_RESISTANCE);
        EffectsMap.put("fd", PotionEffectType.FAST_DIGGING);
        EffectsMap.put("fr", PotionEffectType.FIRE_RESISTANCE);
        EffectsMap.put("hr", PotionEffectType.HARM);
        EffectsMap.put("hl", PotionEffectType.HEAL);
        EffectsMap.put("hn", PotionEffectType.HUNGER);
        EffectsMap.put("id", PotionEffectType.INCREASE_DAMAGE);
        EffectsMap.put("in", PotionEffectType.INVISIBILITY);
        EffectsMap.put("jm", PotionEffectType.JUMP);
        EffectsMap.put("nv", PotionEffectType.NIGHT_VISION);
        EffectsMap.put("pn", PotionEffectType.POISON);
        EffectsMap.put("rg", PotionEffectType.REGENERATION);
        EffectsMap.put("sl", PotionEffectType.SLOW);
        EffectsMap.put("sd", PotionEffectType.SLOW_DIGGING);
        EffectsMap.put("sp", PotionEffectType.SPEED);
        EffectsMap.put("wk", PotionEffectType.WEAKNESS);
        EffectsMap.put("wb", PotionEffectType.WATER_BREATHING);
        EffectsMap.put("wr", PotionEffectType.WITHER);
    }

    public final PotionEffectType Type;
    private final int Amplifier;

    public Effect(PotionEffect potion) {
        Type = potion.getType();
        Amplifier = potion.getAmplifier();
    }

    public Effect(PotionEffectType type, int amp) {
        Type = type;
        Amplifier = amp;
    }

    @Nullable
    public static Effect getFromString(String str) {
        if (str.contains(":")) {
            String[] st = str.split(":");
            try {
                PotionEffectType potiontype = PotionEffectType.getByName(st[0]);
                int potionamp = Integer.parseInt(st[1]);
                return new Effect(potiontype, potionamp);
            } catch (Exception e) {
                CASettings.L.severe("Wrong effect specified: " + str);
                return null;
            }
        }
        CASettings.L.severe("Wrong effect specified: " + str);
        return null;
    }

    public static String toString(Effect eff) {
        return eff.Type.getName() + ":" + eff.Amplifier;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    public PotionEffect getPotionEffect() {
        return new PotionEffect(Type, 200, Amplifier);
    }
}
