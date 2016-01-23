package net.inkyquill.equestria.ca.commands;

import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.ItemData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Joiner.on;

public class GMItemCommand implements CommandExecutor {

    static String PIntro;

    static {
        PIntro = ChatColor.GOLD + "[CA/GMItem] ";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] as) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(PIntro + ChatColor.RED + "Sorry, but you can't use this command from console!");
            return false;
        }
        if (!sender.hasPermission(CASettings.gmi)) {
            sender.sendMessage(PIntro + ChatColor.RED + "Sorry, but you have no permissions for this command!");
            return false;
        }

        Player p = (Player) sender;
        ItemStack item = p.getItemInHand();
        if (item == null) {
            sender.sendMessage(PIntro + ChatColor.RED + "You should take item in your hand to manage messages.");
            return false;
        }
        String key = on(":").join(new Object[]{item.getType().name(), item.getDurability()});
        ItemData id = CASettings.getItemSettings(key);

        if (as == null || as.length == 0) {
            helpCommand(sender);
            return false;
        }
        if ("help".equals(as[0])) {
            helpCommand(sender);
            return false;
        }
        if ("clear".equals(as[0])) {
            CASettings.ItemMessages.put(key, new ItemData());
            sender.sendMessage(PIntro + ChatColor.GREEN + "All messages from this item were cleared");
            return true;
        }
        if ("test".equals(as[0])) {
            sender.sendMessage(PIntro + ChatColor.GREEN + "Testing all messages on this item");

            String hold = "HOLD";
            if (id.HoldMessage != null) {
                if (id.HoldType == ItemData.Messagetype.WORLD)
                    hold += " (WORLD, " + id.HoldRadius + "): " + id.HoldMessage;
                else hold += ": " + id.HoldMessage;
            } else
                hold += ": <none>";
            sender.sendMessage(hold);

            hold = "USE";
            if (id.UseMessage != null) {
                if (id.UseType == ItemData.Messagetype.WORLD)
                    hold += " (WORLD, " + id.UseRadius + "): " + id.UseMessage;
                else hold += ": " + id.UseMessage;
            } else
                hold += ": <none>";
            sender.sendMessage(hold);

            hold = "PICKUP";
            if (id.PickupMessage != null) {
                if (id.PickupType == ItemData.Messagetype.WORLD)
                    hold += " (WORLD, " + id.PickupRadius + "): " + id.PickupMessage;
                else hold += ": " + id.PickupMessage;
            } else
                hold += ": <none>";
            sender.sendMessage(hold);
            return true;
        }
        if (as[0].toLowerCase().equals("hold")) {
            if (as.length > 1 && as[1].toLowerCase().equals("clear")) {
                id.HoldMessage = null;
                sender.sendMessage(PIntro + ChatColor.GREEN + "Hold message for this item cleared");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("message")) {
                List<String> arr = new ArrayList<String>();
                arr.addAll(Arrays.asList(as).subList(2, as.length));
                String msg = on(" ").join(arr);
                msg = ColorReplace(msg);
                id.HoldMessage = msg;
                sender.sendMessage(PIntro + ChatColor.GREEN + "Hold message for this item set");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("radius")) {
                id.HoldRadius = Integer.parseInt(as[2]);
                sender.sendMessage(PIntro + ChatColor.GREEN + "Hold message radius for this item set");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("type")) {
                try {
                    id.HoldType = ItemData.Messagetype.valueOf(as[2].toUpperCase());
                    sender.sendMessage(PIntro + ChatColor.GREEN + "Hold message type for this item set");
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(PIntro + ChatColor.RED + "Wrong type, there are two: USER, WORLD");
                    return false;
                }
            }
        }
        if (as[0].toLowerCase().equals("use")) {
            if (as.length > 1 && as[1].toLowerCase().equals("clear")) {
                id.UseMessage = null;
                sender.sendMessage(PIntro + ChatColor.GREEN + "Use message for this item cleared");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("message")) {
                List<String> arr = new ArrayList<String>();
                arr.addAll(Arrays.asList(as).subList(2, as.length));
                String msg = on(" ").join(arr);
                msg = ColorReplace(msg);
                id.UseMessage = msg;
                sender.sendMessage(PIntro + ChatColor.GREEN + "Use message for this item set");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("radius")) {
                id.UseRadius = Integer.parseInt(as[2]);
                sender.sendMessage(PIntro + ChatColor.GREEN + "Use message radius for this item set");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("type")) {
                try {
                    id.UseType = ItemData.Messagetype.valueOf(as[2].toUpperCase());
                    sender.sendMessage(PIntro + ChatColor.GREEN + "Use message type for this item set");
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(PIntro + ChatColor.RED + "Wrong type, there are two: USER, WORLD");
                    return false;
                }
            }
        }
        if (as[0].toLowerCase().equals("pickup")) {
            if (as.length > 1 && as[1].toLowerCase().equals("clear")) {
                id.PickupMessage = null;
                sender.sendMessage(PIntro + ChatColor.GREEN + "Pickup message for this item cleared");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("message")) {
                List<String> arr = new ArrayList<String>();
                arr.addAll(Arrays.asList(as).subList(2, as.length));
                String msg = on(" ").join(arr);
                msg = ColorReplace(msg);
                id.PickupMessage = msg;
                sender.sendMessage(PIntro + ChatColor.GREEN + "Pickup message for this item set");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("radius")) {
                id.PickupRadius = Integer.parseInt(as[2]);
                sender.sendMessage(PIntro + ChatColor.GREEN + "Pickup message radius for this item set");
                return true;
            }
            if (as.length > 2 && as[1].toLowerCase().equals("type")) {
                try {
                    id.PickupType = ItemData.Messagetype.valueOf(as[2].toUpperCase());
                    sender.sendMessage(PIntro + ChatColor.GREEN + "Pickup message type for this item set");
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(PIntro + ChatColor.RED + "Wrong type, there are two: USER, WORLD");
                    return false;
                }
            }
        }
        helpCommand(sender);
        return false;
    }

    private void helpCommand(CommandSender sender) {
        String message = PIntro +
                ChatColor.WHITE +
                "Usage:";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gmi test" +
                ChatColor.WHITE +
                " tests current item messages.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gmi clear" +
                ChatColor.WHITE +
                " clears all messages from this item.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms hold|use|pickup clear" +
                ChatColor.WHITE +
                " clears message for specified mode";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms hold|use|pickup message [message]" +
                ChatColor.WHITE +
                " sets the message for specified mode. You may use %COLORCODE to set colors (COLORCODE is a letter from " + ChatColor.AQUA + "/gms colors" + ChatColor.WHITE + " command) and %(p)% to show player name (only in world messages).";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms hold|use|pickup type user|world" +
                ChatColor.WHITE +
                " sets message type. World messages will be broadcasted to all players in radius.";
        sender.sendMessage(message);
        message = PIntro +
                ChatColor.AQUA +
                "/gms hold|use|pickup radius <value>" +
                ChatColor.WHITE +
                " sets message radius for world messages.";
        sender.sendMessage(message);
    }

    private String ColorReplace(String msg) {
        for (ChatColor col : ChatColor.values()) {
            msg = msg.replace("%" + col.getChar(), "" + col);
            msg = msg.replace(("%" + col.getChar()).toUpperCase(), "" + col);
        }
        return msg;
    }
}
