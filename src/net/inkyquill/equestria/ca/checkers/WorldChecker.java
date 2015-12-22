
package net.inkyquill.equestria.ca.checkers;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.WeatherType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldChecker
implements EventChecker {

    private static WorldChecker instance;

    public static WorldChecker getInstance() {
        if (instance == null) {
            instance = new WorldChecker();
        }
        return instance;
    }

    public boolean checkEvent(Event event) {
        return event instanceof WeatherChangeEvent && this.checkEvent((WeatherChangeEvent) event);
    }

    private boolean checkEvent(WeatherChangeEvent event) {

        World w = event.getWorld();
        WorldSettings ws = CASettings.getWorldSettings(w);

        if(ws.weather == WeatherType.sunny) //sunny
        {
            if(event.toWeatherState()) {
                w.setStorm(false);
                w.setWeatherDuration(300000);
                //log.info("Current weather set to sunny in " + w.getName());
                return true;
            }
            w.setWeatherDuration(300000);
            return false;
        } else if (ws.weather == WeatherType.storm)
        {
            if(!event.toWeatherState()) {
                w.setStorm(true);
                w.setWeatherDuration(300000);
                //log.info("Current weather set to storm in " + w.getName());
            }
            w.setWeatherDuration(300000);
            return false;
        }
        else
        {return false;}
    }

    @Override
    public void callEvent(Player player) {
    }
}

