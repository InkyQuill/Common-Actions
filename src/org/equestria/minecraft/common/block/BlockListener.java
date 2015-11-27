/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockDamageEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.block;

import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.BlockChecker;
import org.equestria.minecraft.common.gamemaster.GameMasterController;

public class BlockListener
implements Listener {
    public static final Logger log = Logger.getLogger("BlockListener");
    public static int replaceWebHeight;
    public static Material targetMaterial;
    private CommonAbilities plugin;

    public BlockListener(CommonAbilities plugin) {
        this.plugin = plugin;
        this.initProperties();
    }

    private void initProperties() {
        replaceWebHeight = this.plugin.getConfigItemId("replaceWebHeight", 90);
        String targetMaterialName = this.plugin.getConfigItem("targetBlockName");
        Material blockMaterial = Material.getMaterial((String)targetMaterialName);
        if (blockMaterial != null) {
            targetMaterial = blockMaterial;
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        boolean staminaResult;
        event.setCancelled(event.isCancelled() | !BlockChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin));
    }

    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent event) {
        event.setCancelled(event.isCancelled() | !BlockChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin));
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        boolean staminaResult;
        event.setCancelled(event.isCancelled() | !BlockChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin));
    }

    @EventHandler
    public void onBlockInteractEvent(PlayerInteractEvent event) {
        boolean result = BlockChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin);
        if (event.getAction() != null && event.getItem() != null && event.getPlayer() != null && (event.getAction().equals((Object)Action.RIGHT_CLICK_AIR) || event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK))) {
            String id = StringUtils.join((Object[])new Object[]{event.getItem().getTypeId(), event.getItem().getDurability()}, (String)":");
            GameMasterController.getInstance(this.plugin).sendMessage(PlayerInteractEvent.class, event.getPlayer(), id);
        }
        event.setCancelled(event.isCancelled() | !result);
        event.setUseItemInHand(result ? Event.Result.ALLOW : Event.Result.DENY);
    }

    static {
        targetMaterial = Material.WEB;
    }
}

