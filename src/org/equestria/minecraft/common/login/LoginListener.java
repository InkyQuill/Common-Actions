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

import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.BlockChecker;
import org.equestria.minecraft.common.checkers.EffectsChecker;
import org.equestria.minecraft.common.checkers.FoodEffectsChecker;

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
        BlockChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin);
        EffectsChecker.getInstance(this.plugin).callEvent(player);
    }
}

