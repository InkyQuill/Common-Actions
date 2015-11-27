/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.permissions.Permission
 */
package org.equestria.minecraft.common.daoimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.permissions.Permission;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.dao.BlockDao;

public class BlockFileDaoImpl
implements BlockDao {
    public static final Logger log = Logger.getLogger("BlockFileDaoImpl");
    private static final String BREAK_BLOCKS_CONFIG = "/PermBlocks.properties";
    private Properties breakBlocksProps = new Properties();
    private static final String PLACE_BLOCKS_CONFIG = "/PermPlaceBlocks.properties";
    private Properties placeBlocksProps = new Properties();
    private static final String DROP_PERMS_CONFIG = "/DropPerms.properties";
    private Properties dropPermsProps = new Properties();
    private static final String DROP_CHANCE_CONFIG = "/DropChances.properties";
    private Properties dropChanceProps = new Properties();
    private static final String RESTRICTED_DROP_CONFIG = "/RestrictedDrop.properties";
    private Properties restrictedDropProps = new Properties();
    private static final String USE_BLOCKS_CONFIG = "/PermUseBlocks.properties";
    private Properties useBlocksProps = new Properties();
    private CommonAbilities plugin;

    public BlockFileDaoImpl(CommonAbilities plugin) {
        this.plugin = plugin;
        this.initProperties();
    }

    private void initProperties() {
        try {
            File restrictedDrop;
            File placeConf;
            File dropChancesConf;
            File dropPermsConf;
            File useBlocksConf;
            File breakConf = new File(this.plugin.getDataFolder() + "/PermBlocks.properties");
            if (breakConf.exists()) {
                this.breakBlocksProps.load(new FileInputStream(breakConf));
            }
            if ((placeConf = new File(this.plugin.getDataFolder() + "/PermPlaceBlocks.properties")).exists()) {
                this.placeBlocksProps.load(new FileInputStream(placeConf));
            }
            if ((dropPermsConf = new File(this.plugin.getDataFolder() + "/DropPerms.properties")).exists()) {
                this.dropPermsProps.load(new FileInputStream(dropPermsConf));
            }
            if ((dropChancesConf = new File(this.plugin.getDataFolder() + "/DropChances.properties")).exists()) {
                this.dropChanceProps.load(new FileInputStream(dropChancesConf));
            }
            if ((restrictedDrop = new File(this.plugin.getDataFolder() + "/RestrictedDrop.properties")).exists()) {
                this.restrictedDropProps.load(new FileInputStream(restrictedDrop));
            }
            if ((useBlocksConf = new File(this.plugin.getDataFolder() + "/PermUseBlocks.properties")).exists()) {
                this.useBlocksProps.load(new FileInputStream(useBlocksConf));
            }
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load blocks config", ex);
        }
    }

    @Override
    public void addBreakBlockPermission(String type, Permission permission) {
        Properties properties = this.breakBlocksProps;
        synchronized (properties) {
            List<String> permsList = this.getBreakPermissionsAsList(type);
            if (!permsList.contains(permission.getName())) {
                permsList.add(permission.getName());
            }
            this.breakBlocksProps.put(type, this.getPermissionsAsString(permsList));
            this.saveBreakBlocksToFile();
        }
    }

    @Override
    public void removeBreakBlockPermission(String type, Permission permission) {
        Properties properties = this.breakBlocksProps;
        synchronized (properties) {
            List<String> permsList = this.getBreakPermissionsAsList(type);
            if (permsList.contains(permission.getName())) {
                permsList.remove(permission.getName());
            }
            this.breakBlocksProps.put(type, this.getPermissionsAsString(permsList));
            this.saveBreakBlocksToFile();
        }
    }

    @Override
    public Set<Permission> getBreakPermissionsForBlock(String type) {
        List<String> permsList = this.getBreakPermissionsAsList(type);
        HashSet<Permission> permissions = new HashSet<Permission>();
        for (String permName : permsList) {
            permissions.add(new Permission(permName));
        }
        return permissions;
    }

    private List<String> getBreakPermissionsAsList(String type) {
        Properties properties = this.breakBlocksProps;
        synchronized (properties) {
            ArrayList<String> mobsList = new ArrayList<String>();
            String permissionsString = (String)this.breakBlocksProps.get(type);
            if (permissionsString != null) {
                String[] permissions = StringUtils.split((String)permissionsString, (String)", ");
                mobsList.addAll(Arrays.asList(permissions));
            }
            return mobsList;
        }
    }

    private synchronized void saveBreakBlocksToFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + "/PermBlocks.properties");
            this.breakBlocksProps.store(new FileOutputStream(file), "Blocks");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + "/PermBlocks.properties", ex);
        }
    }

    @Override
    public void addPlaceBlockPermission(String type, Permission permission) {
        Properties properties = this.placeBlocksProps;
        synchronized (properties) {
            List<String> permsList = this.getPlacePermissionsAsList(type);
            if (!permsList.contains(permission.getName())) {
                permsList.add(permission.getName());
            }
            this.placeBlocksProps.put(type, this.getPermissionsAsString(permsList));
            this.savePlaceBlocksToFile();
        }
    }

    @Override
    public void removePlaceBlockPermission(String type, Permission permission) {
        Properties properties = this.placeBlocksProps;
        synchronized (properties) {
            List<String> permsList = this.getPlacePermissionsAsList(type);
            if (permsList.contains(permission.getName())) {
                permsList.remove(permission.getName());
            }
            this.placeBlocksProps.put(type, this.getPermissionsAsString(permsList));
            this.savePlaceBlocksToFile();
        }
    }

    @Override
    public Set<Permission> getPlacePermissionsForBlock(String type) {
        List<String> permsList = this.getPlacePermissionsAsList(type);
        HashSet<Permission> permissions = new HashSet<Permission>();
        for (String permName : permsList) {
            permissions.add(new Permission(permName));
        }
        return permissions;
    }

    private List<String> getPlacePermissionsAsList(String type) {
        Properties properties = this.placeBlocksProps;
        synchronized (properties) {
            ArrayList<String> mobsList = new ArrayList<String>();
            String permissionsString = (String)this.placeBlocksProps.get(type);
            if (permissionsString != null) {
                String[] permissions = StringUtils.split((String)permissionsString, (String)", ");
                mobsList.addAll(Arrays.asList(permissions));
            }
            return mobsList;
        }
    }

    private synchronized void savePlaceBlocksToFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + "/PermPlaceBlocks.properties");
            this.placeBlocksProps.store(new FileOutputStream(file), "Blocks");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + "/PermPlaceBlocks.properties", ex);
        }
    }

    @Override
    public void addBlockChancePermission(String type, Permission permission) {
        Properties properties = this.dropPermsProps;
        synchronized (properties) {
            List<String> permsList = this.getBlockChancePermissionsAsList(type);
            if (!permsList.contains(permission.getName())) {
                permsList.add(permission.getName());
            }
            this.dropPermsProps.put(type, this.getPermissionsAsString(permsList));
            this.saveBlockChanceToFile();
        }
    }

    @Override
    public void removeBlockChancePermission(String type, Permission permission) {
        Properties properties = this.dropPermsProps;
        synchronized (properties) {
            List<String> permsList = this.getBlockChancePermissionsAsList(type);
            if (permsList.contains(permission.getName())) {
                permsList.remove(permission.getName());
            }
            this.dropPermsProps.put(type, this.getPermissionsAsString(permsList));
            this.saveBlockChanceToFile();
        }
    }

    private List<String> getBlockChancePermissionsAsList(String type) {
        Properties properties = this.dropPermsProps;
        synchronized (properties) {
            ArrayList<String> permsList = new ArrayList<String>();
            String permissionsString = (String)this.dropPermsProps.get(type);
            if (permissionsString != null) {
                String[] permissions = StringUtils.split((String)permissionsString, (String)", ");
                permsList.addAll(Arrays.asList(permissions));
            }
            return permsList;
        }
    }

    private synchronized void saveBlockChanceToFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + "/DropPerms.properties");
            this.dropPermsProps.store(new FileOutputStream(file), "Blocks");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + "/DropPerms.properties", ex);
        }
    }

    @Override
    public Set<Permission> getChancePermissions(String type) {
        List<String> permsList = this.getBlockChancePermissionsAsList(type);
        HashSet<Permission> permissions = new HashSet<Permission>();
        for (String permName : permsList) {
            permissions.add(new Permission(permName));
        }
        return permissions;
    }

    @Override
    public void addChanceForPermission(Permission permission, int chance) {
        Properties properties = this.dropChanceProps;
        synchronized (properties) {
            try {
                this.dropChanceProps.put(permission.getName(), Integer.toString(chance));
                this.saveChanceToFile();
            }
            catch (NumberFormatException ex) {
                // empty catch block
            }
        }
    }

    @Override
    public void removeChanceForPermission(Permission permission) {
        Properties properties = this.dropChanceProps;
        synchronized (properties) {
            this.dropChanceProps.remove(permission.getName());
            this.saveChanceToFile();
        }
    }

    @Override
    public int getChanceForPermission(Permission permission) {
        Properties properties = this.dropChanceProps;
        synchronized (properties) {
            if (this.dropChanceProps.containsKey(permission.getName())) {
                try {
                    return Integer.parseInt((String)this.dropChanceProps.get(permission.getName()));
                }
                catch (NumberFormatException ex) {
                    return 0;
                }
            }
            return 0;
        }
    }

    @Override
    public List<String> getRestrictedDrop(String type) {
        Properties properties = this.restrictedDropProps;
        synchronized (properties) {
            ArrayList<String> restrictedResultList = new ArrayList<String>();
            if (this.restrictedDropProps.containsKey(type)) {
                String restrictedString = (String)this.restrictedDropProps.get(type);
                if (restrictedString != null) {
                    String[] restrResults = StringUtils.split((String)restrictedString, (String)";");
                    restrictedResultList.addAll(Arrays.asList(restrResults));
                }
                return restrictedResultList;
            }
            return restrictedResultList;
        }
    }

    private synchronized void saveChanceToFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + "/DropChances.properties");
            this.dropChanceProps.store(new FileOutputStream(file), "Blocks");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + "/DropChances.properties", ex);
        }
    }

    private String getPermissionsAsString(List<String> permissionsList) {
        Object[] permNames = permissionsList.toArray(new String[permissionsList.size()]);
        return StringUtils.join((Object[])permNames, (String)", ");
    }

    @Override
    public void addUseBlockPermission(String type, Permission permission) {
        Properties properties = this.useBlocksProps;
        synchronized (properties) {
            List<String> permsList = this.getUsePermissionsAsList(type);
            if (!permsList.contains(permission.getName())) {
                permsList.add(permission.getName());
            }
            this.useBlocksProps.put(type, this.getPermissionsAsString(permsList));
            this.saveUseBlocksToFile();
        }
    }

    @Override
    public void removeUseBlockPermission(String type, Permission permission) {
        Properties properties = this.useBlocksProps;
        synchronized (properties) {
            List<String> permsList = this.getUsePermissionsAsList(type);
            if (permsList.contains(permission.getName())) {
                permsList.remove(permission.getName());
            }
            this.useBlocksProps.put(type, this.getPermissionsAsString(permsList));
            this.saveUseBlocksToFile();
        }
    }

    @Override
    public Set<Permission> getUsePermissionsForBlock(String type) {
        List<String> permsList = this.getUsePermissionsAsList(type);
        HashSet<Permission> permissions = new HashSet<Permission>();
        for (String permName : permsList) {
            permissions.add(new Permission(permName));
        }
        return permissions;
    }

    private List<String> getUsePermissionsAsList(String type) {
        Properties properties = this.useBlocksProps;
        synchronized (properties) {
            ArrayList<String> mobsList = new ArrayList<String>();
            String permissionsString = (String)this.useBlocksProps.get(type);
            if (permissionsString != null) {
                String[] permissions = StringUtils.split((String)permissionsString, (String)", ");
                mobsList.addAll(Arrays.asList(permissions));
            }
            return mobsList;
        }
    }

    private synchronized void saveUseBlocksToFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + "/PermUseBlocks.properties");
            this.useBlocksProps.store(new FileOutputStream(file), "Blocks");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + "/PermUseBlocks.properties", ex);
        }
    }
}

