/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.permissions.Permission
 */
package org.equestria.minecraft.common.dao;

import java.util.Set;
import org.bukkit.permissions.Permission;

public interface CraftDao {
    public void addCraftPermission(String var1, Permission var2);

    public void removeCraftPermission(String var1, Permission var2);

    public Set<Permission> getCraftPermissionsForRecipe(String var1);
}

