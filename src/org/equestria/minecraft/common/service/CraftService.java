/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package org.equestria.minecraft.common.service;

import org.bukkit.entity.Player;

public interface CraftService {
    public boolean hasCraftPermissionForBlock(String var1, Player var2);

    public void addCraftBlockPermission(String var1, String var2);

    public void removeCraftBlockPermission(String var1, String var2);
}

