/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.equestria.minecraft.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.checkers.BlockChecker;

public class PlayerMoveListener
implements Listener {
    public static final Logger log = Logger.getLogger("PlayerMoveListener");
    public static int replaceWebHeight;
    public static List<Material> materialsList;
    public static Material targetMaterial;
    private CommonAbilities plugin;

    public PlayerMoveListener(CommonAbilities plugin) {
        this.plugin = plugin;
        this.initProperties();
    }

    private void initProperties() {
        Material blockMaterial;
        replaceWebHeight = this.plugin.getConfigItemId("replaceWebHeight", 90);
        String blockNamesString = this.plugin.getConfigItem("replacedBlocksNames");
        boolean showAllMaterials = this.plugin.getBoolConfigItem("showAllMaterials");
        if (showAllMaterials) {
            for (Material mat : Material.values()) {
                log.info(mat.name());
            }
        }
        if (!StringUtils.isEmpty((String)blockNamesString)) {
            String[] blockNames;
            for (String blockName : blockNames = blockNamesString.split(", ")) {
                Material blockMaterial2 = Material.getMaterial((String)blockName);
                if (blockMaterial2 == null) continue;
                materialsList.add(blockMaterial2);
            }
        }
        String targetMaterialName = this.plugin.getConfigItem("targetBlockName");
        if (!(StringUtils.isEmpty((String)blockNamesString) || (blockMaterial = Material.getMaterial((String)targetMaterialName)) == null)) {
            targetMaterial = blockMaterial;
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (event.getPlayer().hasPermission("replaceBlocksPermission")) {
            BlockChecker.getInstance(this.plugin).checkEvent((Event)event, (JavaPlugin)this.plugin);
        }
    }

    static {
        materialsList = new ArrayList<Material>();
        targetMaterial = Material.WEB;
    }
}

