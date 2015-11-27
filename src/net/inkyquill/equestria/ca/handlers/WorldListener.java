/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.weather.WeatherChangeEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.WeatherType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.checkers.WorldChecker;

public class WorldListener
implements Listener {
    private CommonAbilities plugin;

    public WorldListener(JavaPlugin plugin) {
        this.plugin = (CommonAbilities)plugin;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        boolean isCancelled = event.isCancelled();
        World w = event.getWorld();
        WorldSettings ws = CASettings.getWorldSettings(w);
        if(ws.weather != WeatherType.normal) {
            isCancelled = isCancelled || WorldChecker.getInstance(this.plugin).checkEvent((Event) event, null);
            event.setCancelled(isCancelled);
        }
    }
}

