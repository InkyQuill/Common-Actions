/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package org.equestria.minecraft.common.repeaters;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.equestria.minecraft.common.checkers.EventChecker;

public class PotionEffectRepeater
implements Runnable {
    public static final Logger log = Logger.getLogger("PotionEffectRepeater");
    private Player player;
    private EventChecker checker;

    public PotionEffectRepeater(Player player, EventChecker checker) {
        this.player = player;
        this.checker = checker;
    }

    @Override
    public void run() {
        this.player = Bukkit.getServer().getPlayer(this.player.getName());
        if (this.player != null) {
            this.checker.callEvent(this.player);
        }
    }
}

