
package net.inkyquill.equestria.ca.checkers;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public interface EventChecker {
    <T extends Event> boolean checkEvent(T var1, JavaPlugin var2);

    void callEvent(Player var1);
}

