/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.checkers;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public interface EventChecker {
    <T extends Event> boolean checkEvent(T var1, JavaPlugin var2);

    void callEvent(Player var1);
}

