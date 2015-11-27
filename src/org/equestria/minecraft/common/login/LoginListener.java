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
package org.equestria.minecraft.common.login;

import net.inkyquill.equestria.ca.CommonAbilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.equestria.minecraft.common.checkers.EffectsChecker;
import org.equestria.minecraft.common.checkers.FoodEffectsChecker;

import java.util.logging.Logger;

public class LoginListener
implements Listener {
    public static final Logger log = Logger.getLogger("LoginListener");
    private CommonAbilities plugin;

    public LoginListener(JavaPlugin plugin) {
        this.plugin = (CommonAbilities)plugin;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FoodEffectsChecker.getInstance(this.plugin).callEvent(player);
        EffectsChecker.getInstance(this.plugin).callEvent(player);
    }
}

