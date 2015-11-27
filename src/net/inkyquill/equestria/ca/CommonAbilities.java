/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.FileConfigurationOptions
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.inkyquill.equestria.ca;

import net.inkyquill.equestria.ca.commands.CelestialCommand;
import net.inkyquill.equestria.ca.commands.GMCommand;
import net.inkyquill.equestria.ca.commands.WeatherCommand;
import net.inkyquill.equestria.ca.handlers.PlayerChatHandler;
import net.inkyquill.equestria.ca.handlers.WorldListener;
import net.inkyquill.equestria.ca.runnable.TimeUpdater;
import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.RCsettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.equestria.minecraft.common.commands.DamageCommandExecutor;
import org.equestria.minecraft.common.commands.EffectsCommandExecutor;
import org.equestria.minecraft.common.commands.GameMasterCommandExecutor;
import org.equestria.minecraft.common.commands.MonsterCommandExecutor;
import org.equestria.minecraft.common.damage.DamageListener;
import org.equestria.minecraft.common.food.FoodListener;
import org.equestria.minecraft.common.items.ItemsListener;
import org.equestria.minecraft.common.login.LoginListener;
import org.equestria.minecraft.common.monsters.MonstersListener;

import java.util.logging.Logger;

public class CommonAbilities
extends JavaPlugin {
    private Logger log = Logger.getLogger("CommonAbilities");

    public void onEnable() {
        CASettings.plugin = this;
        CASettings.L = getLogger();
        CASettings.L.info("Initializing...");
        CASettings.L.info("Loading configs...");
        for(World w: getServer().getWorlds())
        {
            CASettings.GetWorldConfig(w);
        }
        CASettings.chat = new RCsettings();
        CASettings.loadRCConfig();


        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();

        CASettings.L.info("Growing food...");
        FoodListener foodListener = new FoodListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(foodListener, this);
        CASettings.L.info("Watching new ponies...");
        LoginListener loginListener = new LoginListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(loginListener, this);
        CASettings.L.info("Brawling...");
        DamageListener damageListener = new DamageListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(damageListener, this);
        //CASettings.L.info("Voyeuring...");
        //PlayerMoveListener moveListener = new PlayerMoveListener(this);
        // Bukkit.getServer().getPluginManager().registerEvents(moveListener, this);
        //CASettings.L.info("Sharpening pickaxes...");
        //BlockListener blockListener = new BlockListener(this);
        //Bukkit.getServer().getPluginManager().registerEvents(blockListener, this);
        CASettings.L.info("Spawning monsters...");
        MonstersListener targetListener = new MonstersListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(targetListener, this);
        CASettings.L.info("Generating epic loot...");
        ItemsListener itemListener = new ItemsListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(itemListener, this);
        //CASettings.L.info("Crafting traps...");
        //CraftListener craftListener = new CraftListener(this);
        //Bukkit.getServer().getPluginManager().registerEvents(craftListener, this);
        CASettings.L.info("Awaiting user commands...");
        MonsterCommandExecutor monstersExecutor = new MonsterCommandExecutor(this);
        this.getCommand("restrictTarget").setExecutor(monstersExecutor);
        // BlockPermissionCommandExecutor blocksExecutor = new BlockPermissionCommandExecutor(this);
        // this.getCommand("blockPerm").setExecutor(blocksExecutor);
        // DropChanceCommandExecutor dropChanceExecutor = new DropChanceCommandExecutor(this);
        // this.getCommand("dropChance").setExecutor(dropChanceExecutor);
        // CraftPermissionCommandExecutor craftPermissionExecutor = new CraftPermissionCommandExecutor(this);
        //  this.getCommand("craftPerm").setExecutor(craftPermissionExecutor);
        EffectsCommandExecutor effectsExecutor = new EffectsCommandExecutor(this);
        this.getCommand("effects").setExecutor(effectsExecutor);
        DamageCommandExecutor damageExecutor = new DamageCommandExecutor(this);
        this.getCommand("damageController").setExecutor(damageExecutor);
        GameMasterCommandExecutor masterExecutor = new GameMasterCommandExecutor(this);
        this.getCommand("gmaster").setExecutor(masterExecutor);

        this.getCommand("meteo").setExecutor(new WeatherCommand());
        this.getCommand("gms").setExecutor(new GMCommand());
        this.getCommand("timemanager").setExecutor(new CelestialCommand());

        CASettings.L.info("Validating permissions...");
        PluginManager manager = getServer().getPluginManager();
        manager.addPermission(CASettings.weather);
        manager.addPermission(CASettings.gm);
        manager.addPermission(CASettings.time);
        CASettings.L.info("Deploying bugs...");
        manager.registerEvents(new PlayerChatHandler(),this);
        manager.registerEvents(new WorldListener(this),this);

        new TimeUpdater().runTaskLater(this, 5);
        CASettings.L.info("Plugin successfully initialized...");
    }

    @Override
    public void onDisable(){
        getServer().getPluginManager().removePermission(CASettings.weather);
        getServer().getPluginManager().removePermission(CASettings.gm);
        getServer().getPluginManager().removePermission(CASettings.time);
        try{CASettings.SaveConfigs();}
        catch(Exception e){log.info("Couldn't save configs: " + e.getMessage());}
        CASettings.L.info("Plugin successfully deinitialized...");
    }

    public int getConfigItemId(String s, int i) {
        String s1 = this.getConfig().getString(s);
        if (s1 == null) {
            return i;
        }
        try {
            return Integer.parseInt(s1, 10);
        }
        catch (NumberFormatException numberformatexception) {
            this.log.warning("Bad item id: " + s1 + ", using default: " + i);
            return i;
        }
    }

    public String getConfigItem(String s) {
        return this.getConfig().getString(s);
    }

    public boolean getBoolConfigItem(String s) {
        return this.getConfig().getBoolean(s);
    }
}

