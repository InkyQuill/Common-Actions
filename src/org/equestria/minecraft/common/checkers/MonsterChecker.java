/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.entity.EntityTargetLivingEntityEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.checkers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;

public class MonsterChecker
implements EventChecker {
    private static MonsterChecker instance;
    public static final Logger log;
    private static final String MOBS_CONFIG = "/Mobs.properties";
    private Properties mobsProps = new Properties();
    private CommonAbilities plugin;

    private MonsterChecker(CommonAbilities plugin) {
        this.plugin = plugin;
        this.initProperties();
    }

    private void initProperties() {
        try {
            File mobsConf = new File(this.plugin.getDataFolder() + "/Mobs.properties");
            if (mobsConf.exists()) {
                this.mobsProps.load(new FileInputStream(mobsConf));
            }
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load mobs config", ex);
        }
    }

    public static MonsterChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new MonsterChecker(plugin);
        }
        return instance;
    }

    public boolean checkEvent(Event event, JavaPlugin plugin) {
        if (event instanceof EntityTargetLivingEntityEvent) {
            return this.checkEvent((EntityTargetLivingEntityEvent)event, plugin);
        }
        if (event instanceof EntityTargetEvent) {
            return this.checkEvent((EntityTargetEvent)event, plugin);
        }
        return false;
    }

    @Override
    public void callEvent(Player player) {
    }

    public void addMonsterToRestrict(Player player, EntityType type) {
        Properties properties = this.mobsProps;
        synchronized (properties) {
            List<String> mobsList = this.getMobsTypesAsList(player);
            if (!mobsList.contains(type.getName())) {
                mobsList.add(type.getName());
            }
            this.mobsProps.put(player.getName(), this.getMobsAsString(mobsList));
            this.saveMonstersToFile();
        }
    }

    public void removeMonsterFromRestrict(Player player, EntityType type) {
        Properties properties = this.mobsProps;
        synchronized (properties) {
            List<String> mobsList = this.getMobsTypesAsList(player);
            if (mobsList.contains(type.getName())) {
                mobsList.remove(type.getName());
            }
            this.mobsProps.put(player.getName(), this.getMobsAsString(mobsList));
            this.saveMonstersToFile();
        }
    }

    private synchronized void saveMonstersToFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + "/Mobs.properties");
            this.mobsProps.store(new FileOutputStream(file), "Monsters");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + "/Mobs.properties", ex);
        }
    }

    private boolean isMonsterRestricted(Player player, Entity target) {
        List<String> mobsList = this.getMobsTypesAsList(player);
        for (String mobName : mobsList) {
            if (!target.getType().equals((Object)EntityType.fromName((String)mobName))) continue;
            return true;
        }
        return false;
    }

    private boolean checkEvent(EntityTargetLivingEntityEvent event, JavaPlugin plugin) {
        Entity entity = event.getEntity();
        LivingEntity target = event.getTarget();
        if (target instanceof Player) {
            return this.isMonsterRestricted((Player)target, entity);
        }
        return false;
    }

    private boolean checkEvent(EntityTargetEvent event, JavaPlugin plugin) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        if (target instanceof Player) {
            return this.isMonsterRestricted((Player)target, entity);
        }
        return false;
    }

    private List<String> getMobsTypesAsList(Player player) {
        Properties properties = this.mobsProps;
        synchronized (properties) {
            ArrayList<String> mobsList = new ArrayList<String>();
            String restrictedMobsString = (String)this.mobsProps.get(player.getName());
            if (restrictedMobsString != null) {
                String[] mobsNames = StringUtils.split((String)restrictedMobsString, (String)", ");
                mobsList.addAll(Arrays.asList(mobsNames));
            }
            return mobsList;
        }
    }

    private String getMobsAsString(List<String> mobsList) {
        Object[] mobsNames = mobsList.toArray(new String[mobsList.size()]);
        return StringUtils.join((Object[])mobsNames, (String)", ");
    }

    static {
        log = Logger.getLogger("MonsterChecker");
    }
}

