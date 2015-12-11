/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import net.inkyquill.equestria.ca.settings.CASettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener
implements Listener {

    public LoginListener() {

    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        EffectsChecker.getInstance(CASettings.plugin).callEvent(event.getPlayer());
    }
}

