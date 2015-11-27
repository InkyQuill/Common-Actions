/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.equestria.minecraft.common.effects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Effect
implements Serializable {
    private transient List<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
    private List<SimpleEffect> simplePotionEffects = new ArrayList<SimpleEffect>();

    public void addPotionEffect(PotionEffect effect) {
        this.potionEffects.add(effect);
    }

    public void removePotionEffect(PotionEffect removedEffect) {
        for (PotionEffect effect : this.potionEffects) {
            if (!effect.getType().equals((Object)removedEffect.getType())) continue;
            this.potionEffects.remove((Object)effect);
            return;
        }
    }

    public void loadFromSimple() {
        this.potionEffects = new ArrayList<PotionEffect>();
        for (SimpleEffect simpleEffect : this.simplePotionEffects) {
            PotionEffectType type = PotionEffectType.getByName((String)simpleEffect.getEffectName());
            PotionEffect effect = new PotionEffect(type, 1250, simpleEffect.getAmplifier());
            this.addPotionEffect(effect);
        }
    }

    public void saveToSimple() {
        this.simplePotionEffects.clear();
        for (PotionEffect effect : this.potionEffects) {
            SimpleEffect simpleEffect = new SimpleEffect(effect.getType().getName(), effect.getAmplifier());
            this.simplePotionEffects.add(simpleEffect);
        }
    }

    public List<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public void setPotionEffects(List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    private class SimpleEffect
    implements Serializable {
        private String effectName;
        private int amplifier;

        public SimpleEffect(String effectName, int amplifier) {
            this.effectName = effectName;
            this.amplifier = amplifier;
        }

        public String getEffectName() {
            return this.effectName;
        }

        public int getAmplifier() {
            return this.amplifier;
        }
    }

}

