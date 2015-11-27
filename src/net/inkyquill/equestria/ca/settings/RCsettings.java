package net.inkyquill.equestria.ca.settings;

import org.bukkit.ChatColor;

import java.util.List;

/**
 * Created by Inky Quill on 27.11.2015.
 * Settings for RealChat piece.
 */
public class RCsettings {


    public ChatColor spokenPlayerColor;
    public ChatColor heardPlayerColor;
    public ChatColor messageColor;
    public ChatColor dimMessageColor;

    public double garbleRangeDivisor;
    public double speakingRangeMeters;
    public int yellMax;
    public List<Integer> yellHunger;
    public List<Double> yellDistance;

    public double whisperRangeDecrease;
    public double garblePartialChance;
    public String garbleAllDroppedMessage;
    public String chatLineFormat;
}
