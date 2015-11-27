package net.inkyquill.equestria.ca.settings;

import org.bukkit.ChatColor;

/**
 * Created by obruchnikov_pa on 25.11.2015.
 */
public class PlayerSettings {

    public GMsettings GM;

    public PlayerSettings()
    {
        GM = new GMsettings();
        GM.Enabled = false ;
        GM.Color = ChatColor.WHITE;
        GM.Radius = 100;
        GM.Prefix = "World";
    }
}
