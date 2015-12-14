
package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener
implements Listener {

    public LoginListener() {

    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        EffectsChecker.getInstance().callEvent(event.getPlayer());
    }
}

