/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package net.inkyquill.equestria.ca.runnable;

import net.inkyquill.equestria.ca.checkers.EventChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EffectUpdater
implements Runnable {

    private Player player;
    private EventChecker checker;

    public EffectUpdater(Player player, EventChecker checker) {
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

