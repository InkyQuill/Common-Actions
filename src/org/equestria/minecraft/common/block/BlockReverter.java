/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package org.equestria.minecraft.common.block;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.BlockChecker;

public class BlockReverter
implements Runnable {
    private Player player;
    private Material material;
    private JavaPlugin plugin;
    private Block block;
    private byte materialData;
    private static final Logger log = Logger.getLogger("BlockReverter");

    public BlockReverter(Player player, Block block, Material material, byte materialData, JavaPlugin plugin) {
        this.player = player;
        this.block = block;
        this.plugin = plugin;
        this.material = material;
        this.materialData = materialData;
    }

    @Override
    public void run() {
        if (BlockChecker.getInstance((CommonAbilities)this.plugin).checkBlock(this.player, this.block.getLocation())) {
            this.block.setType(this.material);
            this.block.setData(this.materialData);
        } else {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)this);
        }
    }
}

