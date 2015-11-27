package net.inkyquill.equestria.ca.settings;

import net.inkyquill.equestria.ca.CommonAbilities;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Inky Quill on 23.11.2015.
 * Updated on 27.11.2015
 * Settings for the whole plugin.
 */
public class CASettings
{
    public static final Permission weather;
    public static final Permission gm;
    public static final Permission time;
    public static CommonAbilities plugin;
    public static Logger L;
    public static RCsettings chat;
    static Map<String, WorldSettings> W;
    static Map<String, PlayerSettings> P;

    static {
        weather = new Permission("ca.weather");
        gm = new Permission("ca.gms");
        time = new Permission("ca.time");
        W = new HashMap<String, WorldSettings>();
        P = new HashMap<String, PlayerSettings>();
    }

    public static WorldSettings getWorldSettings(World w)
    {
        return getWorldSettings(w.getName());
    }

    public static WorldSettings getWorldSettings(String wname)
    {
        if(W.containsKey(wname))
            return W.get(wname);
        else
        {
            W.put(wname, new WorldSettings());
            return W.get(wname);
        }
    }

    public static PlayerSettings getPlayerSettings(Player p)
    {
        return getPlayerSettings(p.getName());
    }

    public static PlayerSettings getPlayerSettings(String pname)
    {
        if(P.containsKey(pname))
            return P.get(pname);
        else
        {
            PlayerSettings pp = loadPlayerSettings(pname);
            P.put(pname, pp);
            return P.get(pname);
        }
    }

    private static PlayerSettings loadPlayerSettings(String pname) {
        PlayerSettings p = new PlayerSettings();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players.yml"));
        p.GM = new GMsettings();
        p.GM.Enabled = config.getBoolean(pname + ".gm.enabled");
        try {
            p.GM.Color = ChatColor.valueOf(config.getString(pname + ".gm.color", "WHITE"));
        }
        catch(Exception e){
            p.GM.Color = ChatColor.WHITE;
        }
        p.GM.Prefix = config.getString(pname+".gm.prefix","World");
        p.GM.Radius = config.getInt(pname+".gm.radius",100);
        return p;
    }

    public static void GetWorldConfig(World world) {
        WorldSettings w = new WorldSettings();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "worlds.yml"));
        String Type = config.getString(world.getName()+".weather.type","normal");
        try {
            w.weather = WeatherType.valueOf(Type);
        }
        catch(Exception e)
        {
            w.weather = WeatherType.normal;
            L.severe(e.getMessage());
        }

        Type = config.getString(world.getName()+".time.type","mine");
        try {
            w.time.Type = TimeType.fromString(Type);
        }
        catch(Exception e)
        {
            w.time.Type = TimeType.mine;
            L.info("Couldn't set type: " + e.getMessage());
        }
        double Latitude = config.getDouble(world.getName()+".time.real.latitude",55.75222);
        double Longitude = config.getDouble(world.getName()+".time.real.longitude",37.61556);
        int Offset = config.getInt(world.getName()+".time.real.offset",0);

        w.time.Latitude = Latitude;
        w.time.Longitude = Longitude;
        w.time.Offset = Offset;

        w.time.FixedSunrise = config.getString(world.getName()+".time.fixed.sunrise","06:00");
        w.time.FixedSunset = config.getString(world.getName()+".time.fixed.sunset","18:00");

        w.time.TicksPerCalc = config.getInt(world.getName()+".time.tickspercalc",20);
        L.info("TicksPerCalc: " + w.time.TicksPerCalc);
        w.time.ChaosDurationMin = config.getInt(world.getName()+".time.chaos.minimum",2);
        w.time.ChaosDurationMax = config.getInt(world.getName()+".time.chaos.maximum",15);

        W.put(world.getName(),w);
    }

    public static void SaveConfigs() throws IOException {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "worlds.yml"));
        for(World w: plugin.getServer().getWorlds())
        {
            WorldSettings wc = getWorldSettings(w.getName());
            config.set(w.getName()+".weather.type", wc.weather.name());

            config.set(w.getName()+".time.type", wc.time.Type.getText());
            config.set(w.getName()+".time.real.latitude",wc.time.Latitude);
            config.set(w.getName()+".time.real.longitude",wc.time.Longitude);
            config.set(w.getName()+".time.real.offset",wc.time.Offset);
            config.set(w.getName()+".time.fixed.sunrise",wc.time.FixedSunrise);
            config.set(w.getName()+".time.fixed.sunset",wc.time.FixedSunset);
            config.set(w.getName()+".time.tickspercalc",wc.time.TicksPerCalc);
            config.set(w.getName()+".time.chaos.minimum",wc.time.ChaosDurationMin);
            config.set(w.getName()+".time.chaos.maximum",wc.time.ChaosDurationMax);
        }
        config.save(new File(plugin.getDataFolder(),"worlds.yml"));
        L.info("Saved all worlds!");
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players.yml"));
        for(String p: P.keySet())
        {
            config.set(p+".gm.enabled",P.get(p).GM.Enabled);
            config.set(p+".gm.color",P.get(p).GM.Color.name());
            config.set(p+".gm.prefix",P.get(p).GM.Prefix);
            config.set(p+".gm.radius",P.get(p).GM.Radius);
        }
        config.save(new File(plugin.getDataFolder(),"players.yml"));
        L.info("Saved all players!");
        saveRCConfig();
        L.info("Saved RealChat config.");
    }

    public static void loadRCConfig() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "realisticchat.yml"));

        chat.spokenPlayerColor = getConfigColor(config, "chatSpokenPlayerColor", ChatColor.YELLOW);
        chat.heardPlayerColor = getConfigColor(config, "chatHeardPlayerColor", ChatColor.GREEN);
        chat.messageColor = getConfigColor(config, "chatMessageColor", ChatColor.WHITE);
        chat.dimMessageColor = getConfigColor(config, "chatDimMessageColor", ChatColor.DARK_GRAY);

        chat.garbleRangeDivisor = config.getDouble("garbleRangeDivisor", 2D);
        chat.speakingRangeMeters = config.getDouble("speakingRangeMeters", 50D);
        chat.yellMax = config.getInt("yellMax", 4);
        chat.yellHunger = config.getIntegerList("yellHunger");
        chat.yellDistance = config.getDoubleList("yellDistance");
        int ai[] = {
                1, 2, 4, 20
        };
        double ad[] = {
                10D, 50D, 100D, 500D
        };
        if (chat.yellHunger == null || chat.yellHunger.size() < chat.yellMax || chat.yellDistance == null || chat.yellDistance.size() < chat.yellMax) {
            chat.yellMax = 4;
            chat.yellHunger = new ArrayList<Integer>();
            for (int i : ai) chat.yellHunger.add(i);
            chat.yellDistance = new ArrayList<Double>();
            for (double d : ad) chat.yellDistance.add(d);
        }
        chat.whisperRangeDecrease = config.getDouble("whisperRangeDecrease", 40D);
        chat.garblePartialChance = config.getDouble("garblePartialChance", 0.1D);
        chat.garbleAllDroppedMessage = config.getString("garbleAllDroppedMessage", "~~~");
        chat.chatLineFormat = config.getString("chatLineFormat", "%1$s: %2$s");

    }

    public static void saveRCConfig() throws IOException {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "realisticchat.yml"));
        config.set("chatSpokenPlayerColor", ChatColor.YELLOW.name());
        config.set("chatHeardPlayerColor", ChatColor.GREEN.name());
        config.set("chatMessageColor", ChatColor.WHITE.name());
        config.set("chatDimMessageColor", ChatColor.DARK_GRAY.name());

        config.set("garbleRangeDivisor", chat.garbleRangeDivisor);
        config.set("speakingRangeMeters", chat.speakingRangeMeters);
        config.set("yellMax", chat.yellMax);

        config.set("yellHunger", chat.yellHunger);
        config.set("yellDistance", chat.yellDistance);

        config.set("whisperRangeDecrease", chat.whisperRangeDecrease);
        config.set("garblePartialChance", chat.garblePartialChance);
        config.set("garbleAllDroppedMessage", chat.garbleAllDroppedMessage);
        config.set("chatLineFormat", chat.chatLineFormat);
        config.save(new File(plugin.getDataFolder(), "realisticchat.yml"));
    }


    private static ChatColor getConfigColor(FileConfiguration config, String option, ChatColor yellow) {
        try {
            return ChatColor.valueOf(config.getString(option).toUpperCase());
        } catch (Exception e) {
            return yellow;
        }
    }
}
