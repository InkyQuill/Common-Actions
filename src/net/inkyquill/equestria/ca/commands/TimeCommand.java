package net.inkyquill.equestria.ca.commands;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.TimeSettings;
import net.inkyquill.equestria.ca.settings.TimeType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {

    static String PIntro;

    static {
        PIntro = ChatColor.GOLD + "[CA/Time] ";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings == null || strings.length==0)
        {
            handleHelp(commandSender);
        }
        else if(strings.length==1 && strings[0].toLowerCase().equals("help"))
        {
            handleHelp(commandSender);
        }
        else if(strings.length==1 && strings[0].toLowerCase().equals("save"))
        {
            handleSave(commandSender);
        } else if (strings.length == 1 && strings[0].toLowerCase().equals("on")) {
            CASettings.TimeEnabled = true;
            commandSender.sendMessage(PIntro + ChatColor.WHITE + "Time management is now on!");
        } else if (strings.length == 1 && strings[0].toLowerCase().equals("off")) {
            CASettings.TimeEnabled = false;
            commandSender.sendMessage(PIntro + ChatColor.WHITE + "Time management is now off!");
        }
        else if(strings.length==2)
        {
            if(strings[1].toLowerCase().equals("status"))
            {
                if(strings[0].toLowerCase().equals("current")) //we are asking for a status of this world.
                {
                    if(commandSender instanceof ConsoleCommandSender)
                    {
                        commandSender.sendMessage(PIntro + ChatColor.WHITE + "Sorry, but I don't know which world you are in. You're a console!");
                    }
                    else
                    {
                        Player sender = (Player)commandSender;
                        handleStatus(sender, sender.getWorld());
                    }
                }
                else
                {
                    World w = CASettings.plugin.getServer().getWorld(strings[0]);
                    if(w==null) commandSender.sendMessage(PIntro + ChatColor.WHITE + "Sorry, but I don't know the world you asked for. Please check the name.");
                    else
                    {
                        handleStatus(commandSender, w);
                    }
                }
            }
        }
        else if(strings.length==3)
        {
            if(strings[0].toLowerCase().equals("current")) //we are asking for a status of this world.
            {
                if(commandSender instanceof ConsoleCommandSender)
                {
                    commandSender.sendMessage(PIntro + ChatColor.WHITE + "Sorry, but I don't know which world you are in. You're a console!");
                }
                else
                {
                    Player sender = (Player)commandSender;
                    handleSet(sender, sender.getWorld(), strings[1], strings[2]);
                }
            }
            else
            {
                World w = CASettings.plugin.getServer().getWorld(strings[0]);
                if(w==null) commandSender.sendMessage(PIntro + ChatColor.WHITE + "Sorry, but I don't know the world you asked for. Please check the name.");
                else
                {
                    handleSet(commandSender, w, strings[1], strings[2]);
                }
            }
        }
        return false;
    }

    private void handleSet(CommandSender sender, World world, String opt, String val) {

        WorldSettings w = CASettings.getWorldSettings(world);
        TimeSettings ts = w.time;

        if(opt.equals("type"))
        {
            try {
                ts.Type = TimeType.fromString(val);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Time type of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + val);
            }
            catch(Exception e){
                sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong time type!");
            }
        }
        else if(opt.equals("lat"))
        {
            try {
                ts.Latitude = Double.parseDouble(val);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Latitude of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.Latitude);
            }
            catch(Exception e){
                sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong latitude!");
            }
        }
        else if(opt.equals("long"))
        {
            try {
                ts.Longitude = Double.parseDouble(val);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Longitude of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.Longitude);
            }
            catch(Exception e){
                sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong latitude!");
            }
        }
        else if(opt.equals("offset"))
        {
            try {
                ts.Offset = Integer.parseInt(val);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Offset of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.Offset);
            }
            catch(Exception e){
                sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong latitude!");
            }
        }
        else if(opt.equals("sunset"))
        {
                ts.FixedSunset = val;
                sender.sendMessage(PIntro + ChatColor.WHITE + "Sunset of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.FixedSunset);
        }
        else if(opt.equals("sunrise"))
        {
            ts.FixedSunrise = val;
            sender.sendMessage(PIntro + ChatColor.WHITE + "Sunrise of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.FixedSunrise);
        } else if (opt.equals("eachtick")) {
            ts.UpdateEveryTick = val.toLowerCase().equals("true") || val.equals("1") || val.toLowerCase().equals("yes");
            sender.sendMessage(PIntro + ChatColor.WHITE + "Every tick update of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.UpdateEveryTick);
        }
        else if(opt.equals("ticks"))
        {
            try {
                ts.TicksPerCalc = Integer.parseInt(val);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Update duration in ticks of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.TicksPerCalc);
            }
            catch(Exception e){
                sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong ticks value!");
            }
        }
        else if(opt.equals("chaosmin"))
        {
            try {
                ts.ChaosDurationMin = Integer.parseInt(val);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Minimum chaos duration of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.ChaosDurationMin);
            }
            catch(Exception e){
                sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong chaos duration value!");
            }
        }
        else if(opt.equals("chaosmax"))
        {
            try {
                ts.ChaosDurationMax = Integer.parseInt(val);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Maximum chaos duration of " + ChatColor.GREEN + world.getName() + ChatColor.WHITE + " set to " + ChatColor.BLUE + ts.ChaosDurationMax);
            }
            catch(Exception e){
                sender.sendMessage(PIntro + ChatColor.WHITE + "Wrong chaos duration value!");
            }
        }
        else
        {
            handleHelp(sender);
        }
    }

    private void handleStatus(CommandSender sender, World world) {

        WorldSettings w = CASettings.getWorldSettings(world);
        TimeSettings ts = w.time;
        sender.sendMessage(PIntro + ChatColor.WHITE + "Time management is " + (CASettings.TimeEnabled ? ChatColor.GREEN + "on" : (ChatColor.RED + "off")));
        sender.sendMessage(PIntro + ChatColor.WHITE + "World config for " + ChatColor.GREEN + world.getName());
        sender.sendMessage( ChatColor.WHITE +"Type: " + ChatColor.AQUA + ts.Type.name());
        sender.sendMessage(ChatColor.WHITE + "ticks (Update period): " + ts.TicksPerCalc + "; eachtick (Update each tick): " + ts.UpdateEveryTick);
        if(ts.Type== TimeType.real)
        {
            sender.sendMessage( ChatColor.WHITE +"lat (Latitude): " + ts.Latitude + "; long (Longitude): " + ts.Longitude + "; offset: " + ts.Offset);
        }
        else if(ts.Type == TimeType.fixed)
        {
            sender.sendMessage( ChatColor.WHITE +"sunrise: " + ts.FixedSunrise + "; sunset: " + ts.FixedSunset);
        }
        else if(ts.Type == TimeType.chaos)
        {
            sender.sendMessage( ChatColor.WHITE +"chaosmin: " + ts.ChaosDurationMin + "; chaosmax: " + ts.ChaosDurationMax);
        }
    }

    private void handleSave(CommandSender commandSender) {
        try {
            CASettings.SaveConfigs();
            commandSender.sendMessage(PIntro + ChatColor.WHITE + "World configs saved.");
        }
        catch(Exception e)
        {
            commandSender.sendMessage(PIntro + ChatColor.WHITE + "Can't save configs: " + ChatColor.RED + e.getMessage());
        }

    }

    private void handleHelp(CommandSender s) {

        s.sendMessage(PIntro + ChatColor.WHITE + "Usage: ");
        s.sendMessage(ChatColor.AQUA + "/timemanager <worldname|current> status " + ChatColor.WHITE + "shows the world settings");
        s.sendMessage(ChatColor.AQUA + "/timemanager <worldname|current> <option> <value> " + ChatColor.WHITE + "sets the value for selected option");
        s.sendMessage(ChatColor.WHITE + "the options are: " + ChatColor.AQUA + "type, lat, long, offset, sunrise, sunset, ticks, chaosmin, chaosmax, eachtick");
        s.sendMessage(ChatColor.WHITE + "the types are: " + ChatColor.AQUA + "mine, real, fixed, day, night, chaos");
        s.sendMessage(ChatColor.AQUA + "/timemanager save " + ChatColor.WHITE + "saves all world settings");
        s.sendMessage(ChatColor.AQUA + "/timemanager <on|off> " + ChatColor.WHITE + "enables/disables global time management");
    }
}
