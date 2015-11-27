/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.permissions.Permission
 */
package org.equestria.minecraft.common.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import net.inkyquill.equestria.ca.CommonAbilities;
import org.equestria.minecraft.common.dao.BlockDao;
import org.equestria.minecraft.common.daoimpl.BlockFileDaoImpl;
import org.equestria.minecraft.common.service.BlockService;

public class BlockServiceImpl
implements BlockService {
    private BlockDao blockDao;

    public BlockServiceImpl(CommonAbilities plugin) {
        this.blockDao = new BlockFileDaoImpl(plugin);
    }

    @Override
    public boolean hasBreakPermissionForBlock(String type, Player player) {
        Set<Permission> permissions = this.blockDao.getBreakPermissionsForBlock(type);
        for (Permission perm : permissions) {
            if (!player.hasPermission(perm)) continue;
            return true;
        }
        return false;
    }

    public BlockDao getBlockDao() {
        return this.blockDao;
    }

    public void setBlockDao(BlockDao blockDao) {
        this.blockDao = blockDao;
    }

    @Override
    public void addBreakBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.addBreakBlockPermission(type, permission);
    }

    @Override
    public void removeBreakBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.removeBreakBlockPermission(type, permission);
    }

    @Override
    public boolean hasPlacePermissionForBlock(String type, Player player) {
        Set<Permission> permissions = this.blockDao.getPlacePermissionsForBlock(type);
        for (Permission perm : permissions) {
            if (!player.hasPermission(perm)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void addPlaceBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.addPlaceBlockPermission(type, permission);
    }

    @Override
    public void removePlaceBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.removePlaceBlockPermission(type, permission);
    }

    @Override
    public boolean isDropAvailable(String type, Player player) {
        Random random = new Random();
        Set<Permission> permissions = this.blockDao.getChancePermissions(type);
        int chance = permissions.isEmpty() ? 100 : 0;
        for (Permission perm : permissions) {
            if (!player.hasPermission(perm)) continue;
            chance+=this.blockDao.getChanceForPermission(perm);
        }
        int rand = random.nextInt(100);
        return chance >= rand;
    }

    @Override
    public boolean isDropRestricted(List<String> typesList, String type) {
        List<String> restrictedResult = this.blockDao.getRestrictedDrop(type);
        if (Collections.disjoint(typesList, restrictedResult)) {
            return false;
        }
        return true;
    }

    @Override
    public void addBlockChancePermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.addBlockChancePermission(type, permission);
    }

    @Override
    public void removeBlockChancePermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.removeBlockChancePermission(type, permission);
    }

    @Override
    public void addChanceForPermission(String name, int chance) {
        Permission permission = new Permission(name);
        this.blockDao.addChanceForPermission(permission, chance);
    }

    @Override
    public void removeChanceForPermission(String name) {
        Permission permission = new Permission(name);
        this.blockDao.removeChanceForPermission(permission);
    }

    @Override
    public void removeUseBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.removeUseBlockPermission(type, permission);
    }

    @Override
    public void addUseBlockPermission(String type, String name) {
        Permission permission = new Permission(name);
        this.blockDao.addUseBlockPermission(type, permission);
    }

    @Override
    public boolean hasUsePermissionForBlock(String type, Player player) {
        Set<Permission> permissions = this.blockDao.getUsePermissionsForBlock(type);
        for (Permission perm : permissions) {
            if (!player.hasPermission(perm)) continue;
            return true;
        }
        return false;
    }
}

