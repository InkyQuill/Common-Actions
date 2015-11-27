/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffect
 */
package org.equestria.minecraft.common.food;

import java.util.List;
import org.bukkit.potion.PotionEffect;

public class FoodLevelEntity {
    private String level;
    private List<PotionEffect> effects;
    private boolean disableFly;

    public FoodLevelEntity(String string, List<PotionEffect> effects, boolean disableFly) {
        this.setLevel(string);
        this.setEffects(effects);
        this.setDisableFly(disableFly);
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<PotionEffect> getEffects() {
        return this.effects;
    }

    public void setEffects(List<PotionEffect> effects) {
        this.effects = effects;
    }

    public boolean isDisableFly() {
        return this.disableFly;
    }

    public void setDisableFly(boolean disableFly) {
        this.disableFly = disableFly;
    }
}

