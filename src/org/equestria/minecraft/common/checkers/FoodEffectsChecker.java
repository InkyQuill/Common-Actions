/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 */
package org.equestria.minecraft.common.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.food.FoodLevelEntity;
import org.equestria.minecraft.common.properties.FoodProperties;
import org.equestria.minecraft.common.repeaters.PotionEffectRepeater;

public class FoodEffectsChecker
implements EventChecker {
    private static FoodEffectsChecker instance;
    private static final Logger log;
    private static Map<String, FoodLevelEntity> foodEntityMap;
    private CommonAbilities plugin;
    private static final String DISABLE_FLY_LEVELS = "DisableFlyLevels";
    private static final String FOOD_CHECK_ENABLED = "foodEffectsEnabled";

    private FoodEffectsChecker(CommonAbilities plugin) {
        this.plugin = plugin;
        this.transformPropsToEntities();
    }

    public static FoodEffectsChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new FoodEffectsChecker(plugin);
        }
        return instance;
    }

    public boolean checkEvent(Event event, JavaPlugin plugin) {
        if (event instanceof EntityDamageEvent) {
            return this.checkEvent((EntityDamageEvent)event);
        }
        if (event instanceof FoodLevelChangeEvent) {
            return this.checkEvent((FoodLevelChangeEvent)event, plugin);
        }
        return false;
    }

    private boolean checkEvent(EntityDamageEvent event) {
        String foodLevel;
        Player player;
        FoodLevelEntity entity;
        if (event.getCause().equals((Object)EntityDamageEvent.DamageCause.FALL) && event.getEntity() instanceof Player && foodEntityMap.containsKey(foodLevel = Integer.toString((player = (Player)event.getEntity()).getFoodLevel())) && (entity = foodEntityMap.get(foodLevel)) != null && entity.isDisableFly() && this.needFoodCheck()) {
            return true;
        }
        return false;
    }

    private boolean checkEvent(FoodLevelChangeEvent event, JavaPlugin plugin) {
        String foodLevel = Integer.toString(event.getFoodLevel());
        HumanEntity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (foodEntityMap.containsKey(foodLevel) && this.needFoodCheck()) {
                FoodLevelEntity foodEntity = foodEntityMap.get(foodLevel);
                for (PotionEffect effect : foodEntity.getEffects()) {
                    player.addPotionEffect(effect, true);
                }
                if (foodEntity.isDisableFly()) {
                    player.setAllowFlight(false);
                }
                PotionEffectRepeater repeater = new PotionEffectRepeater(player, this);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, (Runnable)repeater, 180);
            }
        }
        return false;
    }

    @Override
    public void callEvent(Player player) {
        FoodLevelChangeEvent foodChange = new FoodLevelChangeEvent((HumanEntity)player, player.getFoodLevel());
        Bukkit.getServer().getPluginManager().callEvent((Event)foodChange);
    }

    private void transformPropsToEntities() {
        String[] disableFlyLevels = StringUtils.split((String)this.plugin.getConfigItem("DisableFlyLevels"), (String)" ");
        List<String> disableFlyLevelsList = Arrays.asList(disableFlyLevels);
        for (FoodProperties property : FoodProperties.values()) {
            String[] effects;
            String levelEffects = this.plugin.getConfigItem(property.getLevel());
            ArrayList<PotionEffect> effectsList = new ArrayList<PotionEffect>();
            if (StringUtils.isEmpty((String)levelEffects)) continue;
            log.info("levelEffects for " + property.getLevel() + " - " + levelEffects);
            for (String effect : effects = StringUtils.split((String)levelEffects, (String)",")) {
                String[] effectProps = StringUtils.split((String)effect, (String)" ");
                if (effectProps.length != 2) {
                    log.info("levelEffects for " + effect + " ignored");
                    continue;
                }
                PotionEffectType type = PotionEffectType.getByName((String)effectProps[0]);
                PotionEffect potionEffect = new PotionEffect(type, 1250, Integer.parseInt(effectProps[1]));
                effectsList.add(potionEffect);
            }
            FoodLevelEntity foodEntity = new FoodLevelEntity(property.getLevel(), effectsList, disableFlyLevelsList.contains(property.getLevel()));
            int level = Integer.parseInt(property.getLevel());
            if (level == 0) {
                foodEntityMap.put(Integer.toString(2 * level + 1), foodEntity);
            } else {
                foodEntityMap.put(Integer.toString(2 * level - 1), foodEntity);
            }
            foodEntityMap.put(Integer.toString(2 * level), foodEntity);
        }
    }

    private boolean needFoodCheck() {
        return this.plugin.getBoolConfigItem("foodEffectsEnabled");
    }

    static {
        log = Logger.getLogger("FoodEffectsChecker");
        foodEntityMap = new HashMap<String, FoodLevelEntity>();
    }
}

