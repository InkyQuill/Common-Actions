package net.inkyquill.equestria.ca.commands;

import net.inkyquill.equestria.ca.settings.WeatherType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import net.inkyquill.equestria.ca.settings.CASettings;

/**
 * Created by Pavel on 23.11.2015.
 */
public class WeatherCommand implements CommandExecutor {
    static String PIntro;
    static{ PIntro = ChatColor.GOLD + "[CA/Meteo] ";}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(commandSender instanceof ConsoleCommandSender){
            commandSender.sendMessage(PIntro + ChatColor.WHITE + "This command can't be launched from console.");
            return false;
        }

        Player p = (Player)commandSender;
        if(!p.hasPermission(CASettings.weather)) {
            p.sendMessage(PIntro + ChatColor.WHITE +"You don't have permissions for this command.");
            return false;
        }
        if(strings==null || strings.length==0) {
            this.handleNoArgsCommand(p);
            return false;
        }
        else if(strings.length==1)
        {
            if(strings[0].toLowerCase().equals("sun") || strings[0].toLowerCase().equals("sunny") || strings[0].toLowerCase().equals("clear"))
            {
                World w = p.getWorld();
                WorldSettings ws = CASettings.getWorldSettings(w);
                ws.weather = WeatherType.sunny;
                w.setStorm(false);
                p.sendMessage(PIntro + ChatColor.WHITE +"Set weather in " + p.getWorld().getName() + " to sunny");
            }
            else if (strings[0].toLowerCase().equals("rain") || strings[0].toLowerCase().equals("storm") || strings[0].toLowerCase().equals("rainy"))
            {
                World w = p.getWorld();
                WorldSettings ws = CASettings.getWorldSettings(w);
                ws.weather = WeatherType.rainy;
                w.setStorm(true);
                p.sendMessage(PIntro + ChatColor.WHITE +"Set weather in " + p.getWorld().getName() + " to storm");
            }
            else if(strings[0].toLowerCase().equals("normal"))
            {
                World w = p.getWorld();
                WorldSettings ws = CASettings.getWorldSettings(w);
                ws.weather = WeatherType.normal;
                p.sendMessage(PIntro + ChatColor.WHITE +"Set weather in " + p.getWorld().getName() + " to normal");
            }
            else if(strings[0].toLowerCase().equals("status"))
            {
                World w = p.getWorld();
                WorldSettings ws = CASettings.getWorldSettings(w);
                p.sendMessage(PIntro + ChatColor.WHITE +"Current weather in " + w.getName() + " is " + ws.weather.name());
            }
            else if(strings[0].toLowerCase().equals("save"))
            {
                try{CASettings.SaveConfigs();p.sendMessage(PIntro + ChatColor.WHITE +"Configs saved");}
                catch(Exception e){CASettings.L.severe("Couldn't save configs: " + e.getMessage());
                    p.sendMessage(PIntro + ChatColor.WHITE +"Configs not saved: " + e.getMessage());}

            }
            else
                this.handleNoArgsCommand(p);
            return false;
        }
        return false;
    }

    private void handleNoArgsCommand(Player p) {
        p.sendMessage(PIntro + ChatColor.DARK_AQUA + " Usage:");
        p.sendMessage(PIntro +  ChatColor.AQUA + " /meteo sun " + ChatColor.WHITE + " to set sunny weather. You can also use " + ChatColor.AQUA + "sunny, clear");
        p.sendMessage(PIntro +  ChatColor.AQUA + " /meteo rain " + ChatColor.WHITE + " to set rainy weather. You can also use " + ChatColor.AQUA + "rainy, storm");
        p.sendMessage(PIntro +  ChatColor.AQUA + " /meteo normal " + ChatColor.WHITE + " to let Minecraft manage weather.");
        p.sendMessage(PIntro +  ChatColor.AQUA + " /meteo status " + ChatColor.WHITE + " to get current weather in your world.");
    }
}
