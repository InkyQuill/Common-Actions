package net.inkyquill.equestria.ca.settings;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Effect {

    public static Map<String, PotionEffectType> EffectsMap;

    static {
        EffectsMap = new HashMap<String, PotionEffectType>();
        EffectsMap.put("BL", PotionEffectType.BLINDNESS);
        EffectsMap.put("CN", PotionEffectType.CONFUSION);
        EffectsMap.put("DR", PotionEffectType.DAMAGE_RESISTANCE);
        EffectsMap.put("FD", PotionEffectType.FAST_DIGGING);
        EffectsMap.put("FR", PotionEffectType.FIRE_RESISTANCE);
        EffectsMap.put("HR", PotionEffectType.HARM);
        EffectsMap.put("HL", PotionEffectType.HEAL);
        EffectsMap.put("HN", PotionEffectType.HUNGER);
        EffectsMap.put("ID", PotionEffectType.INCREASE_DAMAGE);
        EffectsMap.put("IN", PotionEffectType.INVISIBILITY);
        EffectsMap.put("JM", PotionEffectType.JUMP);
        EffectsMap.put("NV", PotionEffectType.NIGHT_VISION);
        EffectsMap.put("PN", PotionEffectType.POISON);
        EffectsMap.put("RG", PotionEffectType.REGENERATION);
        EffectsMap.put("SL", PotionEffectType.SLOW);
        EffectsMap.put("SD", PotionEffectType.SLOW_DIGGING);
        EffectsMap.put("SP", PotionEffectType.SPEED);
        EffectsMap.put("WK", PotionEffectType.WEAKNESS);
        EffectsMap.put("WB", PotionEffectType.WATER_BREATHING);
        EffectsMap.put("WR", PotionEffectType.WITHER);
    }

    public final PotionEffectType Type;
    public final int Amplifier;

    public Effect(PotionEffect potion) {
        Type = potion.getType();
        Amplifier = potion.getAmplifier();
    }

    public Effect(PotionEffectType type, int amp) {
        Type = type;
        Amplifier = amp;
    }

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
