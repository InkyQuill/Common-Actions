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
import org.equestria.minecraft.common.dao.CraftDao;

public class CraftFileDaoImpl
implements CraftDao {
    public static final Logger log = Logger.getLogger("CraftFileDaoImpl");
    private static final String CRAFT_CONFIG = "/CraftPerms.properties";
    private Properties craftProps = new Properties();
    private CommonAbilities plugin;

    public CraftFileDaoImpl(CommonAbilities plugin) {
        this.plugin = plugin;
        this.initProperties();
    }

    private void initProperties() {
        try {
            File craftConf = new File(this.plugin.getDataFolder() + "/CraftPerms.properties");
            if (craftConf.exists()) {
                this.craftProps.load(new FileInputStream(craftConf));
            }
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load craft config", ex);
        }
    }

    @Override
    public void addCraftPermission(String type, Permission permission) {
        Properties properties = this.craftProps;
        synchronized (properties) {
            List<String> permsList = this.getCraftPermissionsAsList(type);
            if (!permsList.contains(permission.getName())) {
                permsList.add(permission.getName());
            }
            this.craftProps.put(type, this.getPermissionsAsString(permsList));
            this.saveCraftsToFile();
        }
    }

    @Override
    public void removeCraftPermission(String type, Permission permission) {
        Properties properties = this.craftProps;
        synchronized (properties) {
            List<String> permsList = this.getCraftPermissionsAsList(type);
            if (permsList.contains(permission.getName())) {
                permsList.remove(permission.getName());
            }
            this.craftProps.put(type, this.getPermissionsAsString(permsList));
            this.saveCraftsToFile();
        }
    }

    @Override
    public Set<Permission> getCraftPermissionsForRecipe(String type) {
        List<String> permsList = this.getCraftPermissionsAsList(type);
        HashSet<Permission> permissions = new HashSet<Permission>();
        for (String permName : permsList) {
            permissions.add(new Permission(permName));
        }
        return permissions;
    }

    private List<String> getCraftPermissionsAsList(String type) {
        Properties properties = this.craftProps;
        synchronized (properties) {
            ArrayList<String> craftsList = new ArrayList<String>();
            String permissionsString = (String)this.craftProps.get(type);
            if (permissionsString != null) {
                String[] permissions = StringUtils.split((String)permissionsString, (String)", ");
                craftsList.addAll(Arrays.asList(permissions));
            }
            return craftsList;
        }
    }

    private String getPermissionsAsString(List<String> permissionsList) {
        Object[] permNames = permissionsList.toArray(new String[permissionsList.size()]);
        return StringUtils.join((Object[])permNames, (String)", ");
    }

    private synchronized void saveCraftsToFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + "/CraftPerms.properties");
            this.craftProps.store(new FileOutputStream(file), "Crafts");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + "/CraftPerms.properties", ex);
        }
    }
}

