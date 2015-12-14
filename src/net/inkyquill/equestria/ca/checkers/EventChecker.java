
package net.inkyquill.equestria.ca.checkers;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface EventChecker {
    <T extends Event> boolean checkEvent(T var1);

    void callEvent(Player var1);
}

