
package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.checkers.MonsterChecker;
import net.inkyquill.equestria.ca.settings.CASettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class MonstersListener
        implements Listener {

    public MonstersListener() {

    }

    @EventHandler
    public void onEntityTargetLivingEvent(EntityTargetLivingEntityEvent event) {
        event.setCancelled(event.isCancelled() | MonsterChecker.getInstance().checkEvent(event, CASettings.plugin));
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event) {
        event.setCancelled(event.isCancelled() | MonsterChecker.getInstance().checkEvent(event, CASettings.plugin));
    }
}

