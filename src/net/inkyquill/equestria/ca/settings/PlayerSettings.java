package net.inkyquill.equestria.ca.settings;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PlayerSettings {

    public GMsettings GM;
    public List<Effect> Effects;

    public PlayerSettings()
    {
        GM = new GMsettings();
        GM.Enabled = false ;
        GM.Color = ChatColor.WHITE;
        GM.Radius = 100;
        GM.Prefix = "World";
        Effects = new ArrayList<Effect>();
    }
}
