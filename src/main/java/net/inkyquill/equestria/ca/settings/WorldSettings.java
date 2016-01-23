package net.inkyquill.equestria.ca.settings;

public class WorldSettings {
    public WeatherType weather;
    public TimeSettings time;

    public WorldSettings() {
        weather = WeatherType.normal;
        time = new TimeSettings();
    }
}
