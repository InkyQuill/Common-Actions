package net.inkyquill.equestria.ca.settings;

/**
 * Created by obruchnikov_pa on 24.11.2015.
 */
public class WorldSettings {
    public WorldSettings()
    {
        weather = WeatherType.normal;
        time = new TimeSettings();
    }
    public WeatherType weather;

    public TimeSettings time;
}
