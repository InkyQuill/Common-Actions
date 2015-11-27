/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.weather.WeatherChangeEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.inkyquill.equestria.ca.checkers;

import java.util.logging.Logger;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.WeatherType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.EventChecker;

public class WorldChecker
implements EventChecker {
    private static WorldChecker instance;
    private static final Logger log;
    private CommonAbilities plugin;

    private WorldChecker(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    public static WorldChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new WorldChecker(plugin);
        }
        return instance;
    }

    public boolean checkEvent(Event event, JavaPlugin plugin) {
        if (event instanceof WeatherChangeEvent) {
            return this.checkEvent((WeatherChangeEvent)event, plugin);
        }
        return false;
    }

    private boolean checkEvent(WeatherChangeEvent event, JavaPlugin plugin) {

        World w = event.getWorld();
        WorldSettings ws = CASettings.getWorldSettings(w);

        if(ws.weather == WeatherType.sunny) //sunny
        {
            if(event.toWeatherState()) {
                w.setStorm(false);
                w.setWeatherDuration(600);
                //log.info("Current weather set to sunny in " + w.getName());
                return true;
            }
            w.setWeatherDuration(600);
            return false;
        }
        else if(ws.weather == WeatherType.rainy)
        {
            if(!event.toWeatherState()) {
                w.setStorm(true);
                w.setWeatherDuration(600);
                //log.info("Current weather set to rainy in " + w.getName());
            }
            w.setWeatherDuration(600);
            return false;
        }
        else
        {return false;}
    }

    @Override
    public void callEvent(Player player) {
    }

    static {
        log = Logger.getLogger("WorldChecker");
    }
}

