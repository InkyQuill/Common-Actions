
package net.inkyquill.equestria.ca;

import net.inkyquill.equestria.ca.commands.*;
import net.inkyquill.equestria.ca.handlers.*;
import net.inkyquill.equestria.ca.runnable.ConfigUpdater;
import net.inkyquill.equestria.ca.runnable.TimeUpdater;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.RCsettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CommonActions
extends JavaPlugin {
    private Logger log = Logger.getLogger("CommonActions");

    public void onEnable() {
        CASettings.plugin = this;
        CASettings.L = getLogger();
        CASettings.L.info("Initializing...");
        CASettings.L.info("Loading configs...");
        CASettings.LoadSettings();
        for(World w: getServer().getWorlds())
        {
            CASettings.GetWorldConfig(w);
        }
        CASettings.chat = new RCsettings();
        CASettings.loadRCConfig();

        //this.getConfig().options().copyDefaults(true);
        //this.saveConfig();
        //this.reloadConfig();

        CASettings.L.info("Starting mobrestrict command handler...");
        this.getCommand("mobrestrict").setExecutor(new MonsterCommand());
        CASettings.L.info("Starting meteo command handler...");
        this.getCommand("meteo").setExecutor(new WeatherCommand());
        CASettings.L.info("Starting gms command handler...");
        this.getCommand("gms").setExecutor(new GMCommand());
        this.getCommand("gmsay").setExecutor(new GMSayCommand());
        CASettings.L.info("Starting gmi command handler...");
        this.getCommand("gmi").setExecutor(new GMItemCommand());
        CASettings.L.info("Starting timemanager command handler...");
        this.getCommand("timemanager").setExecutor(new TimeCommand());
        CASettings.L.info("Starting eff command handler...");
        this.getCommand("eff").setExecutor(new EffectsCommand());
        CASettings.L.info("Starting death command handler...");
        this.getCommand("death").setExecutor(new DECommands());
        this.getCommand("ca").setExecutor(new CACommands());

        CASettings.L.info("Validating permissions...");
        PluginManager manager = getServer().getPluginManager();
        manager.addPermission(CASettings.weather);
        manager.addPermission(CASettings.gm);
        manager.addPermission(CASettings.gmi);
        manager.addPermission(CASettings.time);
        manager.addPermission(CASettings.effects);
        manager.addPermission(CASettings.death);
        manager.addPermission(CASettings.monsters);

        CASettings.L.info("Deploying bugs...");
        manager.registerEvents(new PlayerChatHandler(),this);
        manager.registerEvents(new WorldListener(), this);
        manager.registerEvents(new MonstersListener(), this);
        manager.registerEvents(new ItemsListener(), this);
        manager.registerEvents(new DamageListener(), this);
        manager.registerEvents(new LoginListener(), this);

        if (CASettings.TimeEnabled)
            new TimeUpdater().runTaskLater(this, 5);

        ConfigUpdater repeater = new ConfigUpdater();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, repeater, 600);

        CASettings.L.info("Plugin successfully initialized...");
    }

    @Override
    public void onDisable(){
        PluginManager manager = getServer().getPluginManager();
        manager.removePermission(CASettings.weather);
        manager.removePermission(CASettings.gm);
        manager.removePermission(CASettings.gmi);
        manager.removePermission(CASettings.time);
        manager.removePermission(CASettings.effects);
        manager.removePermission(CASettings.death);
        manager.removePermission(CASettings.monsters);
        try{CASettings.SaveConfigs();}
        catch(Exception e){log.info("Couldn't save configs: " + e.getMessage());}
        CASettings.L.info("Plugin successfully deinitialized...");
    }
}

