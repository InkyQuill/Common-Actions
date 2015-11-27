/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.inventory.PrepareItemCraftEvent
 *  org.bukkit.inventory.CraftingInventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.checkers;

import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.service.CraftService;
import org.equestria.minecraft.common.service.impl.CraftServiceImpl;

public class CraftChecker
implements EventChecker {
    private static CraftChecker instance;
    private static final Logger log;
    private CraftService craftService;
    private CommonAbilities plugin;
    public static final String CRAFT_CHECK_ENABLED = "craftCheckEnabled";

    private CraftChecker(CommonAbilities plugin) {
        this.plugin = plugin;
        this.setCraftService(new CraftServiceImpl(plugin));
    }

    public static CraftChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new CraftChecker(plugin);
        }
        return instance;
    }

    public boolean checkEvent(Event event, JavaPlugin plugin) {
        if (event instanceof PrepareItemCraftEvent) {
            return this.checkEvent((PrepareItemCraftEvent)event, plugin);
        }
        return false;
    }

    private boolean checkEvent(PrepareItemCraftEvent event, JavaPlugin plugin) {
        Player player = (Player)event.getInventory().getHolder();
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return true;
        }
        Material type = event.getInventory().getResult().getType();
        String id = StringUtils.join((Object[])new Object[]{type.getId(), event.getInventory().getResult().getDurability()}, (String)":");
        String allId = StringUtils.join((Object[])new Object[]{type.getId(), "*"}, (String)":");
        boolean hasPermission = this.getCraftService().hasCraftPermissionForBlock(id, player);
        boolean hasAllCraftPermission = this.getCraftService().hasCraftPermissionForBlock(allId, player);
        if (!(hasPermission || hasAllCraftPermission || !this.needCraftCheck())) {
            player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to craft " + id);
            return false;
        }
        return true;
    }

    @Override
    public void callEvent(Player player) {
    }

    private boolean needCraftCheck() {
        return this.plugin.getBoolConfigItem("craftCheckEnabled");
    }

    public CraftService getCraftService() {
        return this.craftService;
    }

    public void setCraftService(CraftService craftService) {
        this.craftService = craftService;
    }

    static {
        log = Logger.getLogger("BlockChecker");
    }
}

