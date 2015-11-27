/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.permissions.Permission
 */
package org.equestria.minecraft.common.service.impl;

import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.dao.CraftDao;
import org.equestria.minecraft.common.daoimpl.CraftFileDaoImpl;
import org.equestria.minecraft.common.service.CraftService;

public class CraftServiceImpl
implements CraftService {
    private CraftDao craftDao;

    public CraftServiceImpl(CommonAbilities plugin) {
        this.setCraftDao(new CraftFileDaoImpl(plugin));
    }

    @Override
    public boolean hasCraftPermissionForBlock(String type, Player player) {
        Set<Permission> permissions = this.getCraftDao().getCraftPermissionsForRecipe(type);
        for (Permission perm : permissions) {
            if (!player.hasPermission(perm)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void addCraftBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.craftDao.addCraftPermission(type, permission);
    }

    @Override
    public void removeCraftBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.craftDao.removeCraftPermission(type, permission);
    }

    public CraftDao getCraftDao() {
        return this.craftDao;
    }

    public void setCraftDao(CraftDao craftDao) {
        this.craftDao = craftDao;
    }
}

