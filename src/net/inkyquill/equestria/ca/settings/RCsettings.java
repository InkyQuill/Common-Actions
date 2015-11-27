package net.inkyquill.equestria.ca.settings;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by Pavel on 27.11.2015.
 */
public class RCsettings {

    private static int walkieItemId;
    private static int bullhornItemId;
    private static int smartphoneItemId;
    private static ChatColor spokenPlayerColor;
    private static ChatColor heardPlayerColor;
    private static ChatColor messageColor;
    private static ChatColor dimMessageColor;
    private static ChatColor generalChannelColor;
    private static ChatColor generalMessagesColor;

    public static void Load()
{
    FileConfiguration config = YamlConfiguration.loadConfiguration(new File(CASettings.plugin.getDataFolder(), "realisticchat.yml"));
    walkieItemId = getConfigItemId("walkieItem", Material.COMPASS.getId());
    bullhornItemId = getConfigItemId("bullhornItem", Material.DIAMOND.getId());
    smartphoneItemId = getConfigItemId("smartphoneItem", Material.WATCH.getId());
    spokenPlayerColor = getConfigColor("chatSpokenPlayerColor", ChatColor.YELLOW);
    heardPlayerColor = getConfigColor("chatHeardPlayerColor", ChatColor.GREEN);
    messageColor = getConfigColor("chatMessageColor", ChatColor.WHITE);
    dimMessageColor = getConfigColor("chatDimMessageColor", ChatColor.DARK_GRAY);
    generalChannelColor = getConfigColor("generalChannelColor", ChatColor.GREEN);
    generalMessagesColor = getConfigColor("generalMessagesColor", ChatColor.YELLOW);


}

    private static ChatColor getConfigColor(String s, ChatColor chatcolor) {
        String s1 = getConfig().getString(s);
        if(s1 == null)
            return chatcolor;
        try
        {
            return ChatColor.valueOf(s1);
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            log.warning((new StringBuilder()).append("Bad color name: ").append(s1).append(", using default: ").append(chatcolor).append(": ").append(illegalargumentexception).toString());
        }
        return chatcolor;
    }

    private static int getConfigItemId(String s, int i) {
        String s1 = getConfig().getString(s);
        if(s1 == null)
            return i;
        Material material = Material.matchMaterial(s1);
        if(material != null)
            return material.getId();
        try
        {
            return Integer.parseInt(s1, 10);
        }
        catch(NumberFormatException numberformatexception)
        {
            log.warning((new StringBuilder()).append("Bad item id: ").append(s1).append(", using default: ").append(i).toString());
        }
        return i;
    }



}
