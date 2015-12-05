package org.equestria.minecraft.common.checkers;

import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.checkers.EventChecker;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

//TODO: rewrite Monster Checker

public class MonsterChecker
implements EventChecker {
    public static final Logger log;
    private static final String MOBS_CONFIG = "/Mobs.properties";
    private static MonsterChecker instance;

    static {
        log = Logger.getLogger("MonsterChecker");
    }

    private Properties mobsProps = new Properties();
    private CommonAbilities plugin;

    private MonsterChecker(CommonAbilities plugin) {
        this.plugin = plugin;
        this.initProperties();
    }

    public static MonsterChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new MonsterChecker(plugin);
        }
        return instance;
    }

    private void initProperties() {
        try {
            File mobsConf = new File(this.plugin.getDataFolder() + MOBS_CONFIG);
            if (mobsConf.exists()) {
                this.mobsProps.load(new FileInputStream(mobsConf));
            }
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load mobs config", ex);
        }
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
            File file = new File(this.plugin.getDataFolder() + MOBS_CONFIG);
            this.mobsProps.store(new FileOutputStream(file), "Monsters");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + MOBS_CONFIG, ex);
        }
    }

    private boolean isMonsterRestricted(Player player, Entity target) {
        List<String> mobsList = this.getMobsTypesAsList(player);
        for (String mobName : mobsList) {
            if (!target.getType().equals(EntityType.fromName(mobName))) continue;
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
        return target instanceof Player && this.isMonsterRestricted((Player) target, entity);
    }

    private List<String> getMobsTypesAsList(Player player) {
        Properties properties = this.mobsProps;
        synchronized (properties) {
            ArrayList<String> mobsList = new ArrayList<String>();
            String restrictedMobsString = (String)this.mobsProps.get(player.getName());
            if (restrictedMobsString != null) {
                String[] mobsNames = StringUtils.split(restrictedMobsString, ", ");
                mobsList.addAll(Arrays.asList(mobsNames));
            }
            return mobsList;
        }
    }

    private String getMobsAsString(List<String> mobsList) {
        Object[] mobsNames = mobsList.toArray(new String[mobsList.size()]);
        return StringUtils.join(mobsNames, ", ");
    }
}

