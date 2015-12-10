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

import net.inkyquill.equestria.ca.CommonActions;
import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class LoginListener
implements Listener {
    public static final Logger log = Logger.getLogger("LoginListener");
    private CommonActions plugin;

    public LoginListener(JavaPlugin plugin) {
        this.plugin = (CommonActions) plugin;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        EffectsChecker.getInstance(this.plugin).callEvent(player);
    }
}

