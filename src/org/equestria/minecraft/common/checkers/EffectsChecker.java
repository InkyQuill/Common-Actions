/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.permissions.PermissionAttachmentInfo
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 */
package org.equestria.minecraft.common.checkers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.effects.Effect;
import org.equestria.minecraft.common.repeaters.PotionEffectRepeater;

public class EffectsChecker
implements EventChecker {
    private CommonAbilities plugin;
    private Map<String, Effect> effectsList = new HashMap<String, Effect>();
    private static EffectsChecker instance;
    public static String DEATH_EFFECTS;
    public static String EFFECTS_FILE;
    public static final Logger log;

    private EffectsChecker(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    public void addEffect(String playerName, Effect effect) {
        Map<String, Effect> map = this.effectsList;
        synchronized (map) {
            effect.saveToSimple();
            this.effectsList.put(playerName, effect);
            Player player = this.plugin.getServer().getPlayer(playerName);
            PotionEffectRepeater repeater = new PotionEffectRepeater(player, this);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)repeater, 0);
            this.saveEffects();
        }
    }

    private void saveEffects() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(this.plugin.getDataFolder() + EFFECTS_FILE);
            fos = new FileOutputStream(file, false);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.effectsList);
            oos.close();
            fos.close();
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to save " + EFFECTS_FILE, ex);
        }
        finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            }
            catch (IOException e) {
                log.log(Level.SEVERE, "Unable to save " + EFFECTS_FILE, e);
            }
        }
    }

    private void loadEffects() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            Map savedEffects;
            File file = new File(this.plugin.getDataFolder() + EFFECTS_FILE);
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            this.effectsList = savedEffects = (Map)ois.readObject();
            ois.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            log.log(Level.INFO, "Effects is empty");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + EFFECTS_FILE, ex);
        }
        catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Unable to load " + EFFECTS_FILE, e);
        }
        finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
            catch (IOException e) {
                log.log(Level.SEVERE, "Unable to load " + EFFECTS_FILE, e);
            }
        }
    }

    public void addDeathEffects(Player player) {
        String[] effects;
        String effectsString = this.plugin.getConfigItem(DEATH_EFFECTS);
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        for (PermissionAttachmentInfo info : permissions) {
            String permEffects = this.plugin.getConfigItem(info.getPermission());
            if (permEffects == null || permEffects.length() <= 0) continue;
            effectsString = permEffects;
        }
        for (String effect : effects = StringUtils.split((String)effectsString, (String)",")) {
            String[] effectData = StringUtils.split((String)effect, (String)" ");
            if (effectData.length != 2) continue;
            String effectName = effectData[0];
            String effectPower = effectData[1];
            int effectStrenght = 0;
            try {
                PotionEffectType.getByName((String)effectName);
                effectStrenght = Integer.parseInt(effectPower);
            }
            catch (NumberFormatException ex) {
                log.log(Level.WARNING, "Effect strenght is not numeric");
            }
            catch (Exception e) {
                log.log(Level.WARNING, "Effect is wrong");
            }
            Effect existedEffects = EffectsChecker.getInstance(this.plugin).getEffects(player.getName());
            if (existedEffects == null) {
                existedEffects = new Effect();
            }
            boolean isContain = false;
            for (PotionEffect eff : existedEffects.getPotionEffects()) {
                if (!eff.getType().equals((Object)PotionEffectType.getByName((String)effectName))) continue;
                isContain = true;
            }
            if (isContain) continue;
            existedEffects.addPotionEffect(new PotionEffect(PotionEffectType.getByName((String)effectName), 1250, effectStrenght));
            this.addEffect(player.getName(), existedEffects);
        }
    }

    public void removeEffect(String playerName, String effect) {
        Map<String, Effect> map = this.effectsList;
        synchronized (map) {
            Effect effects = this.effectsList.get(playerName);
            if (effects != null) {
                effects.loadFromSimple();
                effects.removePotionEffect(new PotionEffect(PotionEffectType.getByName((String)effect), 0, 0));
                effects.saveToSimple();
                this.effectsList.put(playerName, effects);
                this.saveEffects();
            }
        }
    }

    public Effect getEffects(String playerName) {
        Map<String, Effect> map = this.effectsList;
        synchronized (map) {
            Effect effect = this.effectsList.get(playerName);
            if (effect != null) {
                effect.loadFromSimple();
            }
            return effect;
        }
    }

    public static EffectsChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new EffectsChecker(plugin);
            instance.loadEffects();
        }
        return instance;
    }

    @Override
    public <T extends Event> boolean checkEvent(T event, JavaPlugin plugin) {
        return false;
    }

    @Override
    public void callEvent(Player player) {
        Effect effects = this.getEffects(player.getName());
        if (effects != null && effects.getPotionEffects().size() > 0) {
            for (PotionEffect effect : effects.getPotionEffects()) {
                player.addPotionEffect(effect, true);
            }
            PotionEffectRepeater repeater = new PotionEffectRepeater(player, this);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)repeater, 180);
        }
    }

    static {
        DEATH_EFFECTS = "DEATH_EFFECTS";
        EFFECTS_FILE = "/effects.ser";
        log = Logger.getLogger("EffectsChecker");
    }
}

