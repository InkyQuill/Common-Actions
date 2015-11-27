/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package org.equestria.minecraft.common.service;

import java.util.List;
import org.bukkit.entity.Player;

public interface BlockService {
    public boolean hasBreakPermissionForBlock(String var1, Player var2);

    public void addBreakBlockPermission(String var1, String var2);

    public void removeBreakBlockPermission(String var1, String var2);

    public boolean hasPlacePermissionForBlock(String var1, Player var2);

    public void addPlaceBlockPermission(String var1, String var2);

    public void removePlaceBlockPermission(String var1, String var2);

    public boolean isDropAvailable(String var1, Player var2);

    public boolean isDropRestricted(List<String> var1, String var2);

    public void addBlockChancePermission(String var1, String var2);

    public void removeBlockChancePermission(String var1, String var2);

    public void addChanceForPermission(String var1, int var2);

    public void removeChanceForPermission(String var1);

    public boolean hasUsePermissionForBlock(String var1, Player var2);

    public void addUseBlockPermission(String var1, String var2);

    public void removeUseBlockPermission(String var1, String var2);
}

