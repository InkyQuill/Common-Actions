package net.inkyquill.equestria.ca.runnable;

import net.inkyquill.equestria.ca.settings.CASettings;
import org.bukkit.Bukkit;

import java.io.IOException;

/**
 * Created by Pavel on 05.12.2015.
 */
public class ConfigUpdater implements Runnable {
    @Override
    public void run() {
        try {
            CASettings.SaveConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConfigUpdater repeater = new ConfigUpdater();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CASettings.plugin, repeater, 6000);
    }
}
