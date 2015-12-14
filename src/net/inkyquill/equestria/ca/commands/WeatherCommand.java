package net.inkyquill.equestria.ca.commands;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.WeatherType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class WeatherCommand implements CommandExecutor {
    static String PIntro;
    static{ PIntro = ChatColor.GOLD + "[CA/Meteo] ";}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {


        if (!sender.hasPermission(CASettings.weather)) {
            sender.sendMessage(PIntro + ChatColor.WHITE + "You don't have permissions for this command.");
            return false;
        }
        if(strings==null || strings.length==0) {
            this.handleNoArgsCommand(sender);
            return false;
        } else if (strings.length > 0)
        {
            World w = GetWorld(sender, strings);
            if (w == null) return false;

            if(strings[0].toLowerCase().equals("sun") || strings[0].toLowerCase().equals("sunny") || strings[0].toLowerCase().equals("clear"))
            {

                WorldSettings ws = CASettings.getWorldSettings(w);
                ws.weather = WeatherType.sunny;
                w.setStorm(false);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Set weather in " + w.getName() + " to sunny");
            }
            else if (strings[0].toLowerCase().equals("rain") || strings[0].toLowerCase().equals("storm") || strings[0].toLowerCase().equals("rainy"))
            {

                WorldSettings ws = CASettings.getWorldSettings(w);
                ws.weather = WeatherType.storm;
                w.setStorm(true);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Set weather in " + w.getName() + " to storm");
            }
            else if(strings[0].toLowerCase().equals("normal"))
            {

                WorldSettings ws = CASettings.getWorldSettings(w);
                ws.weather = WeatherType.normal;
                sender.sendMessage(PIntro + ChatColor.WHITE + "Set weather in " + w.getName() + " to normal");
            }
            else if(strings[0].toLowerCase().equals("status"))
            {

                WorldSettings ws = CASettings.getWorldSettings(w);
                sender.sendMessage(PIntro + ChatColor.WHITE + "Current weather in " + w.getName() + " is " + ws.weather.name());
            }
            else if(strings[0].toLowerCase().equals("save"))
            {
                try {
                    CASettings.SaveConfigs();
                    sender.sendMessage(PIntro + ChatColor.WHITE + "Configs saved");
                }
                catch(Exception e){CASettings.L.severe("Couldn't save configs: " + e.getMessage());
                    sender.sendMessage(PIntro + ChatColor.WHITE + "Configs not saved: " + e.getMessage());
                }

            }
            else
                this.handleNoArgsCommand(sender);
            return false;
        }
        return false;
    }

    private World GetWorld(CommandSender sender, String[] strings) {
        World w = null;
        if (strings.length > 1) {
            w = CASettings.plugin.getServer().getWorld(strings[1]);
        }
        if (w == null) {
            sender.sendMessage(PIntro + ChatColor.YELLOW + "Couldn't find the world. Falling back to current.");

            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(PIntro + ChatColor.RED + "You are a console. Can't get your world.");
                return null;
            } else
                w = ((Player) sender).getWorld();
        }
        return w;
    }

    private void handleNoArgsCommand(CommandSender p) {
        p.sendMessage(PIntro + ChatColor.DARK_AQUA + " Usage:");
        p.sendMessage(PIntro + ChatColor.AQUA + " /meteo sun [world]" + ChatColor.WHITE + " to set sunny weather. [world] is optional. You can also use " + ChatColor.AQUA + "sunny, clear");
        p.sendMessage(PIntro + ChatColor.AQUA + " /meteo rain [world]" + ChatColor.WHITE + " to set rainy weather. [world] is optional. You can also use " + ChatColor.AQUA + "rainy, storm");
        p.sendMessage(PIntro + ChatColor.AQUA + " /meteo normal [world]" + ChatColor.WHITE + " to let Minecraft manage weather. [world] is optional.");
        p.sendMessage(PIntro + ChatColor.AQUA + " /meteo status [world]" + ChatColor.WHITE + " to get current weather in your world. [world] is optional.");
    }
}
