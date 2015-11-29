
package org.equestria.minecraft.common.damage;

import net.inkyquill.equestria.ca.CommonAbilities;
import net.inkyquill.equestria.ca.checkers.EffectsChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.logging.Logger;

public class DamageController {
    private static final Logger log;
    private static DamageController instance;

    static {
        log = Logger.getLogger("DamageController");
    }

    private CommonAbilities plugin;
    private Location respawnLocation;
    private Object synchObj = new Object();

    private DamageController(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    public static synchronized DamageController getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new DamageController(plugin);
        }
        return instance;
    }

    public boolean processDamage(EntityDamageEvent event) {
        Player player;
        if (this.needDeathCheck() && event.getEntity() instanceof Player && (player = (Player)event.getEntity()).getHealth() <= event.getDamage() + 1) {
            event.setCancelled(true);
            EffectsChecker.getInstance(this.plugin).addDeathEffects(player);
            player.setHealth(1);
            Location respawnLocation = this.getRespawnLocation();
            if (respawnLocation == null) {
                String respawnCoordinates = this.getRespawnCoords();
                respawnLocation = this.parseLocation(respawnCoordinates, player.getWorld(), player.getLocation());
            }
            player.teleport(respawnLocation);
            return true;
        }
        return false;
    }

    private Location parseLocation(String location, World world, Location defaultLocation) {
        String[] coords = location.split(":");
        Location respawnLocation = defaultLocation;
        if (coords.length == 3) {
            try {
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);
                respawnLocation = new Location(world, (double)x, (double)y, (double)z);
            }
            catch (NumberFormatException ex) {
                log.info("DamageController: Error while parse respawn coordinates.");
            }
        }
        return respawnLocation;
    }

    private synchronized boolean needDeathCheck() {
        return this.plugin.getBoolConfigItem("deathEffectsEnabled");
    }

    private synchronized String getRespawnCoords() {
        return this.plugin.getConfigItem("respawnCoordinates");
    }

    public Location getRespawnLocation() {
        Object object = this.synchObj;
        synchronized (object) {
            return this.respawnLocation;
        }
    }

    public void setRespawnLocation(Location respawnLocation) {
        Object object = this.synchObj;
        synchronized (object) {
            this.respawnLocation = respawnLocation;
        }
    }

    public void setRespawnLocation(String respawnLocationStr, World world, Location defaultLocation) {
        Object object = this.synchObj;
        synchronized (object) {
            this.respawnLocation = this.parseLocation(respawnLocationStr, world, defaultLocation);
            this.plugin.getConfig().set("respawnCoordinates", respawnLocationStr);
            this.plugin.saveConfig();
        }
    }
}

