/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.permissions.Permission
 */
package org.equestria.minecraft.common.dao;

import java.util.List;
import java.util.Set;
import org.bukkit.permissions.Permission;

public interface BlockDao {
    public void addBreakBlockPermission(String var1, Permission var2);

    public void removeBreakBlockPermission(String var1, Permission var2);

    public Set<Permission> getBreakPermissionsForBlock(String var1);

    public void addPlaceBlockPermission(String var1, Permission var2);

    public void removePlaceBlockPermission(String var1, Permission var2);

    public Set<Permission> getPlacePermissionsForBlock(String var1);

    public void addBlockChancePermission(String var1, Permission var2);

    public void removeBlockChancePermission(String var1, Permission var2);

    public void addChanceForPermission(Permission var1, int var2);

    public void removeChanceForPermission(Permission var1);

    public int getChanceForPermission(Permission var1);

    public Set<Permission> getChancePermissions(String var1);

    public List<String> getRestrictedDrop(String var1);

    public void removeUseBlockPermission(String var1, Permission var2);

    public void addUseBlockPermission(String var1, Permission var2);

    public Set<Permission> getUsePermissionsForBlock(String var1);
}

