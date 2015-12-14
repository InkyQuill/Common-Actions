
package net.inkyquill.equestria.ca.handlers;

import net.inkyquill.equestria.ca.checkers.WorldChecker;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.WeatherType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener
implements Listener {


    public WorldListener() {
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        boolean isCancelled = event.isCancelled();
        World w = event.getWorld();
        WorldSettings ws = CASettings.getWorldSettings(w);
        if(ws.weather != WeatherType.normal) {
            isCancelled = isCancelled || WorldChecker.getInstance().checkEvent(event);
            event.setCancelled(isCancelled);
        }
    }
}

