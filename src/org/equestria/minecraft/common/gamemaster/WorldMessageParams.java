/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package org.equestria.minecraft.common.gamemaster;

import java.io.Serializable;
import org.bukkit.ChatColor;

public class WorldMessageParams
implements Serializable {
    private ChatColor color;
    private int radius;
    private String worldPrefix;

    public ChatColor getColor() {
        return this.color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getWorldPrefix() {
        return this.worldPrefix;
    }

    public void setWorldPrefix(String worldPrefix) {
        this.worldPrefix = worldPrefix;
    }
}

