/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockDamageEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package org.equestria.minecraft.common.checkers;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.block.BlockListener;
import org.equestria.minecraft.common.block.BlockReverter;
import org.equestria.minecraft.common.block.PlayerMoveListener;
import org.equestria.minecraft.common.service.BlockService;
import org.equestria.minecraft.common.service.impl.BlockServiceImpl;

public class BlockChecker
implements EventChecker {
    private static BlockChecker instance;
    public static final String REPLACE_BLOCKS_PERMISSION = "replaceBlocksPermission";
    public static final String REPLACE_BLOCK_HEIGHT = "replaceBlockHeight";
    public static final String REPLACED_BLOCKS_NAMES = "replacedBlocksNames";
    public static final String TARGET_BLOCK_NAME = "targetBlockName";
    public static final String REPLACE_WEB_HEIGHT = "replaceWebHeight";
    public static final String SHOW_ALL_MATERIALS = "showAllMaterials";
    public static final String PLACE_CHECK_ENABLED = "blockPlaceCheckEnabled";
    public static final String BREAK_CHECK_ENABLED = "blockBreakCheckEnabled";
    public static final String USE_CHECK_ENABLED = "blockUseCheckEnabled";
    public static final String DROP_CHECK_ENABLED = "blockDropChanceCheckEnabled";
    public static final String REPLACE_BLOCK_ENABLED = "replaceBlocksEnabled";
    private static final Logger log;
    private BlockService blockService;
    private CommonAbilities plugin;
    private boolean debugMode = false;

    private BlockChecker(CommonAbilities plugin) {
        this.plugin = plugin;
        this.setBlockService(new BlockServiceImpl(plugin));
    }

    public static BlockChecker getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new BlockChecker(plugin);
        }
        return instance;
    }

    public boolean checkEvent(Event event, JavaPlugin plugin) {
        if (event instanceof PlayerMoveEvent && this.needReplaceCheck()) {
            return this.checkEvent(((PlayerMoveEvent)event).getPlayer(), plugin);
        }
        if (event instanceof PlayerJoinEvent && this.needReplaceCheck()) {
            PlayerJoinEvent playerEvent = (PlayerJoinEvent)event;
            this.checkEvent(playerEvent.getPlayer(), plugin);
        } else {
            if (event instanceof BlockBreakEvent) {
                return this.checkEvent((BlockBreakEvent)event, plugin);
            }
            if (event instanceof BlockDamageEvent) {
                return this.checkEvent((BlockDamageEvent)event, plugin);
            }
            if (event instanceof BlockPlaceEvent) {
                return this.checkEvent((BlockPlaceEvent)event, plugin);
            }
            if (event instanceof PlayerInteractEvent) {
                return this.checkEvent((PlayerInteractEvent)event, plugin);
            }
            if (event instanceof EntityDamageByEntityEvent) {
                return this.checkEvent((EntityDamageByEntityEvent)event, plugin);
            }
        }
        return false;
    }

    private boolean checkEvent(BlockBreakEvent event, JavaPlugin plugin) {
        Player player = event.getPlayer();
        Material type = event.getBlock().getType();
        String id = StringUtils.join((Object[])new Object[]{type.getId(), Byte.valueOf(event.getBlock().getData())}, (String)":");
        if (this.debugMode) {
            player.sendMessage((Object)ChatColor.YELLOW + "Id - " + id);
        }
        String allId = StringUtils.join((Object[])new Object[]{type.getId(), "*"}, (String)":");
        ArrayList<String> dropsIds = new ArrayList<String>();
        if (event.getBlock() != null && event.getBlock().getDrops() != null) {
            for (ItemStack stack : event.getBlock().getDrops()) {
                String stackId = StringUtils.join((Object[])new Object[]{stack.getType().getId(), stack.getDurability()}, (String)":");
                String stackAllId = StringUtils.join((Object[])new Object[]{stack.getType().getId(), "*"}, (String)":");
                dropsIds.add(stackId);
                dropsIds.add(stackAllId);
            }
            boolean isDropRestricted = this.getBlockService().isDropRestricted(dropsIds, id);
            boolean isAllDropRestricted = this.getBlockService().isDropRestricted(dropsIds, allId);
            if (isDropRestricted || isAllDropRestricted) {
                event.getBlock().setTypeId(Material.AIR.getId());
                player.sendMessage((Object)ChatColor.YELLOW + "No drop.");
                return false;
            }
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return true;
        }
        Location loc = player.getLocation();
        int y = (int)loc.getY();
        if (y >= BlockListener.replaceWebHeight && event.getBlock().getType() == BlockListener.targetMaterial && this.needReplaceCheck()) {
            return false;
        }
        boolean hasPermission = this.getBlockService().hasBreakPermissionForBlock(id, player);
        boolean hasAllPermission = this.getBlockService().hasBreakPermissionForBlock(allId, player);
        if (!(hasAllPermission || hasPermission || !this.needBreakCheck())) {
            player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to do with " + id);
            return false;
        }
        boolean isDropAvailable = this.getBlockService().isDropAvailable(id, player);
        boolean isAllDropAvailable = this.getBlockService().isDropAvailable(allId, player);
        if (!(isDropAvailable || isAllDropAvailable || !this.needDropCheck())) {
            event.getBlock().setTypeId(Material.AIR.getId());
            player.sendMessage((Object)ChatColor.YELLOW + "Empty. Try again.");
            return false;
        }
        return true;
    }

    private boolean checkEvent(BlockPlaceEvent blockPlaceEvent, JavaPlugin plugin) {
        Player player = blockPlaceEvent.getPlayer();
        if (player == null) {
            return true;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return true;
        }
        Material type = blockPlaceEvent.getBlock().getType();
        String id = StringUtils.join((Object[])new Object[]{type.getId(), blockPlaceEvent.getItemInHand().getDurability()}, (String)":");
        if (this.debugMode) {
            player.sendMessage((Object)ChatColor.YELLOW + "Id - " + id);
        }
        String allId = StringUtils.join((Object[])new Object[]{type.getId(), "*"}, (String)":");
        boolean hasPermission = this.getBlockService().hasPlacePermissionForBlock(id, player);
        boolean hasAllPermission = this.getBlockService().hasPlacePermissionForBlock(allId, player);
        if (!(hasAllPermission || hasPermission || !this.needPlaceCheck())) {
            player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to place " + id);
            return false;
        }
        return true;
    }

    private boolean checkEvent(BlockDamageEvent damageEvent, JavaPlugin plugin) {
        Player player = damageEvent.getPlayer();
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return true;
        }
        Material type = damageEvent.getBlock().getType();
        String id = StringUtils.join((Object[])new Object[]{type.getId(), Byte.valueOf(damageEvent.getBlock().getData())}, (String)":");
        String allId = StringUtils.join((Object[])new Object[]{type.getId(), "*"}, (String)":");
        if (this.debugMode) {
            player.sendMessage((Object)ChatColor.YELLOW + "Id - " + id);
        }
        boolean hasPermission = this.getBlockService().hasBreakPermissionForBlock(id, player);
        boolean hasAllPermission = this.getBlockService().hasBreakPermissionForBlock(allId, player);
        if (!(hasPermission || hasAllPermission || !this.needBreakCheck())) {
            player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to work with " + id);
            return false;
        }
        return true;
    }

    private boolean checkEvent(PlayerInteractEvent interactEvent, JavaPlugin plugin) {
        Player player = interactEvent.getPlayer();
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return true;
        }
        Action action = interactEvent.getAction();
        if (action.equals((Object)Action.RIGHT_CLICK_BLOCK)) {
            Material clickedType = interactEvent.getClickedBlock().getType();
            if (clickedType.equals((Object)Material.AIR)) {
                return true;
            }
            String id = StringUtils.join((Object[])new Object[]{clickedType.getId(), Byte.valueOf(interactEvent.getClickedBlock().getData())}, (String)":");
            String allId = StringUtils.join((Object[])new Object[]{clickedType.getId(), "*"}, (String)":");
            if (this.debugMode) {
                player.sendMessage((Object)ChatColor.YELLOW + "Id - " + id);
            }
            boolean hasUsePermission = this.getBlockService().hasUsePermissionForBlock(id, player);
            boolean hasAllUsePermission = this.getBlockService().hasUsePermissionForBlock(allId, player);
            if (!(hasUsePermission || hasAllUsePermission || !this.needUseCheck())) {
                player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to use " + id);
                return false;
            }
            Material type = interactEvent.getMaterial();
            if (type.equals((Object)Material.AIR)) {
                return true;
            }
            if (interactEvent.getItem() != null && interactEvent.getItem().getData() != null) {
                String stackId = StringUtils.join((Object[])new Object[]{type.getId(), interactEvent.getItem().getDurability()}, (String)":");
                if (this.debugMode) {
                    player.sendMessage((Object)ChatColor.YELLOW + "stackId - " + stackId);
                }
                String allStackId = StringUtils.join((Object[])new Object[]{type.getId(), "*"}, (String)":");
                boolean hasPermission = this.getBlockService().hasPlacePermissionForBlock(stackId, player);
                boolean hasAllPermission = this.getBlockService().hasPlacePermissionForBlock(allStackId, player);
                if (!(hasPermission || hasAllPermission || !this.needPlaceCheck())) {
                    player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to place " + stackId);
                    return false;
                }
            }
        } else if (action.equals((Object)Action.LEFT_CLICK_BLOCK) || action.equals((Object)Action.LEFT_CLICK_AIR) || action.equals((Object)Action.PHYSICAL)) {
            if (interactEvent.getItem() == null) {
                return true;
            }
            String id = StringUtils.join((Object[])new Object[]{interactEvent.getItem().getType().getId(), interactEvent.getItem().getDurability()}, (String)":");
            if (this.debugMode) {
                player.sendMessage((Object)ChatColor.YELLOW + "id - " + id);
            }
            String allId = StringUtils.join((Object[])new Object[]{interactEvent.getItem().getType().getId(), "*"}, (String)":");
            boolean hasUsePermission = this.getBlockService().hasUsePermissionForBlock(id, player);
            boolean hasAllUsePermission = this.getBlockService().hasUsePermissionForBlock(allId, player);
            if (!(hasUsePermission || hasAllUsePermission || !this.needUseCheck())) {
                player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to use " + id);
                return false;
            }
        }
        return true;
    }

    private boolean checkEvent(EntityDamageByEntityEvent damageEvent, JavaPlugin plugin) {
        if (damageEvent.getDamager() instanceof Player) {
            Player player = (Player)damageEvent.getDamager();
            if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
                return true;
            }
            if (player.getItemInHand() == null || player.getItemInHand().getType().equals((Object)Material.AIR)) {
                return true;
            }
            String id = StringUtils.join((Object[])new Object[]{player.getItemInHand().getType().getId(), player.getItemInHand().getDurability()}, (String)":");
            if (this.debugMode) {
                player.sendMessage((Object)ChatColor.YELLOW + "id - " + id);
            }
            String allId = StringUtils.join((Object[])new Object[]{player.getItemInHand().getType().getId(), "*"}, (String)":");
            boolean hasUsePermission = this.getBlockService().hasUsePermissionForBlock(id, player);
            boolean hasAllUsePermission = this.getBlockService().hasUsePermissionForBlock(allId, player);
            if (!(hasUsePermission || hasAllUsePermission || !this.needUseCheck())) {
                player.sendMessage((Object)ChatColor.YELLOW + "You do not know how to fight with " + id);
                return false;
            }
        }
        return true;
    }

    private boolean checkEvent(Player player, JavaPlugin plugin) {
        Location loc = player.getLocation();
        World world = player.getWorld();
        int x = (int)loc.getX();
        int y = (int)loc.getY();
        int z = (int)loc.getZ();
        if (y >= PlayerMoveListener.replaceWebHeight) {
            for (int i = x - 1; i < x + 2; ++i) {
                for (int j = y - 2; j < y + 3; ++j) {
                    for (int k = z - 1; k < z + 2; ++k) {
                        Location blockLocation = new Location(world, (double)i, (double)j, (double)k);
                        Block block = world.getBlockAt(blockLocation);
                        if (!PlayerMoveListener.materialsList.contains((Object)block.getType())) continue;
                        BlockReverter runner = new BlockReverter(player, block, block.getType(), block.getData(), plugin);
                        block.setType(PlayerMoveListener.targetMaterial);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, (Runnable)runner);
                    }
                }
            }
        }
        return false;
    }

    public boolean checkBlock(Player player, Location location) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        int x = (int)playerLocation.getX();
        int y = (int)playerLocation.getY();
        int z = (int)playerLocation.getZ();
        for (int i = x - 1; i < x + 2; ++i) {
            for (int j = y - 2; j < y + 3; ++j) {
                for (int k = z - 1; k < z + 2; ++k) {
                    Location blockLocation = new Location(world, (double)i, (double)j, (double)k);
                    if (!blockLocation.equals((Object)location)) continue;
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void callEvent(Player player) {
        PlayerMoveEvent moveEvent = new PlayerMoveEvent(player, player.getLocation(), player.getLocation());
        Bukkit.getServer().getPluginManager().callEvent((Event)moveEvent);
    }

    private boolean needPlaceCheck() {
        return this.plugin.getBoolConfigItem("blockPlaceCheckEnabled");
    }

    private boolean needBreakCheck() {
        return this.plugin.getBoolConfigItem("blockBreakCheckEnabled");
    }

    private boolean needUseCheck() {
        return this.plugin.getBoolConfigItem("blockUseCheckEnabled");
    }

    private boolean needDropCheck() {
        return this.plugin.getBoolConfigItem("blockDropChanceCheckEnabled");
    }

    private boolean needReplaceCheck() {
        return this.plugin.getBoolConfigItem("replaceBlocksEnabled");
    }

    public BlockService getBlockService() {
        return this.blockService;
    }

    public void setBlockService(BlockService blockService) {
        this.blockService = blockService;
    }

    public boolean isDebugMode() {
        return this.debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    static {
        log = Logger.getLogger("BlockChecker");
    }
}

