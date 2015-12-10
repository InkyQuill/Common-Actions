package net.inkyquill.equestria.ca.checkers;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.PlayerSettings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MonsterChecker
        implements EventChecker {

    private static MonsterChecker instance;

    private MonsterChecker() {

    }

    public static MonsterChecker getInstance() {
        if (instance == null) {
            instance = new MonsterChecker();
        }
        return instance;
    }

    public boolean checkEvent(Event event, JavaPlugin plugin) {
        if (event instanceof EntityTargetLivingEntityEvent) {
            return this.checkEvent((EntityTargetLivingEntityEvent) event);
        } else if (event instanceof EntityTargetEvent) {
            return this.checkEvent((EntityTargetEvent) event);
        }
        return false;
    }

    @Override
    public void callEvent(Player player) {
    }

    public void addMonsterToRestrict(Player player, EntityType type) {
        PlayerSettings x = CASettings.getPlayerSettings(player);
        if (!x.Monsters.contains(type)) {
            x.Monsters.add(type);
        }
    }

    public void removeMonsterFromRestrict(Player player, EntityType type) {
        PlayerSettings x = CASettings.getPlayerSettings(player);
        if (x.Monsters.contains(type)) {
            x.Monsters.remove(type);
        }
    }

    private boolean isMonsterRestricted(Player player, Entity target) {
        PlayerSettings x = CASettings.getPlayerSettings(player);
        return x.Monsters.contains(target.getType());
    }

    private boolean checkEvent(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        LivingEntity target = event.getTarget();
        return target instanceof Player && this.isMonsterRestricted((Player) target, entity);
    }

    private boolean checkEvent(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        return target instanceof Player && this.isMonsterRestricted((Player) target, entity);
    }
}

