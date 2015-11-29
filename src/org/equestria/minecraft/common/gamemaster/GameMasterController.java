/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 */
package org.equestria.minecraft.common.gamemaster;

import net.inkyquill.equestria.ca.CommonAbilities;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMasterController {
    private static final Logger log;
    public static String ITEMS_FILE;
    private static GameMasterController instance;

    static {
        log = Logger.getLogger("GameMasterController");
        ITEMS_FILE = "/ItemActions.ser";
    }

    private CommonAbilities plugin;
    private Map<String, WorldMessageParams> paramsMap = new HashMap<String, WorldMessageParams>();
    private Map<Class, List<ItemMessage>> itemsMap = new HashMap<Class, List<ItemMessage>>();
    private String ACTION_HOLD = "HOLD";
    private String ACTION_PICKUP = "PICKUP";
    private String ACTION_USE = "USE";

    private GameMasterController(CommonAbilities plugin) {
        this.plugin = plugin;
    }

    public static synchronized GameMasterController getInstance(CommonAbilities plugin) {
        if (instance == null) {
            instance = new GameMasterController(plugin);
            instance.loadItems();
        }
        return instance;
    }

    private void saveItems() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(this.plugin.getDataFolder() + ITEMS_FILE);
            fos = new FileOutputStream(file, false);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.itemsMap);
            oos.close();
            fos.close();
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to save " + ITEMS_FILE, ex);
        }
        finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            }
            catch (IOException e) {
                log.log(Level.SEVERE, "Unable to save " + ITEMS_FILE, e);
            }
        }
    }

    private void loadItems() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File file = new File(this.plugin.getDataFolder() + ITEMS_FILE);
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            this.itemsMap = (Map) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            log.log(Level.INFO, "Items messages is empty");
        }
        catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load " + ITEMS_FILE, ex);
        }
        catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Unable to load " + ITEMS_FILE, e);
        }
        finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
            catch (IOException e) {
                log.log(Level.SEVERE, "Unable to load " + ITEMS_FILE, e);
            }
        }
    }

    public boolean setColor(String colorStr, String callerName) {
        Map<String, WorldMessageParams> map = this.paramsMap;
        synchronized (map) {
            try {
                ChatColor color = ChatColor.valueOf(colorStr);
                WorldMessageParams param = this.paramsMap.get(callerName);
                if (param == null) {
                    param = this.initMessageParam();
                }
                param.setColor(color);
                this.paramsMap.put(callerName, param);
            }
            catch (IllegalArgumentException ex) {
                return false;
            }
            return true;
        }
    }

    private synchronized WorldMessageParams initMessageParam() {
        WorldMessageParams param = new WorldMessageParams();
        param.setColor(ChatColor.WHITE);
        param.setRadius(30);
        param.setWorldPrefix("[World] ");
        return param;
    }

    public void setRadius(int radius, String callerName) {
        Map<String, WorldMessageParams> map = this.paramsMap;
        synchronized (map) {
            WorldMessageParams param = this.paramsMap.get(callerName);
            if (param == null) {
                param = this.initMessageParam();
            }
            param.setRadius(radius);
            this.paramsMap.put(callerName, param);
        }
    }

    public void setPrefix(String prefix, String callerName) {
        Map<String, WorldMessageParams> map = this.paramsMap;
        synchronized (map) {
            WorldMessageParams param = this.paramsMap.get(callerName);
            if (param == null) {
                param = this.initMessageParam();
            }
            param.setWorldPrefix(String.format("[%s] ", prefix));
            this.paramsMap.put(callerName, param);
        }
    }

    public void sendWorldMessage(String message, Player caller) {
        Map<String, WorldMessageParams> map = this.paramsMap;
        synchronized (map) {
            StringBuilder phraseBuilder = new StringBuilder();
            WorldMessageParams param = this.paramsMap.get(caller.getName());
            if (param == null) {
                param = this.initMessageParam();
            }
            this.paramsMap.put(caller.getName(), param);
            phraseBuilder.append(param.getColor());
            phraseBuilder.append(param.getWorldPrefix());
            phraseBuilder.append(param.getColor());
            phraseBuilder.append(message);
            int radius = param.getRadius();
            for (Player recipient : Bukkit.getOnlinePlayers()) {

                if (caller.equals(recipient)) {
                    caller.sendMessage(phraseBuilder.toString());
                    continue;
                }
                if (!caller.getWorld().equals(recipient.getWorld()) || (caller.getLocation().distance(recipient.getLocation())) > (double) radius)
                    continue;
                recipient.sendMessage(phraseBuilder.toString());
            }
        }
    }

    public void removeMessageFromItem(String action, Player caller) {
        Map<Class, List<ItemMessage>> map = this.itemsMap;
        synchronized (map) {
            ItemStack item = caller.getItemInHand();
            String id = StringUtils.join(new Object[]{item.getTypeId(), item.getDurability()}, ":");
            Class actionClass = this.getActionClass(action);
            if (actionClass != null) {
                List<ItemMessage> list = this.getItemsForActionClass(actionClass);
                for (ItemMessage existedItemMessage : list) {
                    if (!existedItemMessage.getId().equals(id)) continue;
                    list.remove(existedItemMessage);
                    caller.sendMessage("Action message was deleted for " + id);
                    this.saveItems();
                    return;
                }
            }
        }
    }

    public void addMessageToItem(String action, String message, Player caller, boolean generateId) {
        Map<Class, List<ItemMessage>> map = this.itemsMap;
        synchronized (map) {
            ItemStack item = caller.getItemInHand();
            if (item.getDurability() == 0 && generateId) {
                Random rand = new Random();
                item.setDurability((short)rand.nextInt(600));
                item.getData().setData((byte)item.getDurability());
            }
            String id = StringUtils.join(new Object[]{item.getTypeId(), item.getDurability()}, ":");
            WorldMessageParams itemMessageParam = this.initMessageParam();
            ItemMessage itemMessage = new ItemMessage();
            itemMessage.setMessageParameter(itemMessageParam);
            itemMessage.setId(id);
            itemMessage.setMessage(message);
            Class actionClass = this.getActionClass(action);
            if (actionClass != null) {
                List<ItemMessage> list = this.getItemsForActionClass(actionClass);
                boolean notExist = true;
                for (ItemMessage existedItemMessage : list) {
                    if (!existedItemMessage.getId().equals(id)) continue;
                    existedItemMessage.setMessage(message);
                    notExist = false;
                    caller.sendMessage("Action message was changed for " + id);
                }
                if (notExist) {
                    list.add(itemMessage);
                    this.itemsMap.put(actionClass, list);
                    caller.sendMessage("Action message was added for " + id);
                }
            }
            this.saveItems();
        }
    }

    private void showDebugItem(ItemStack item, Player caller) {
        caller.sendMessage("Amount " + item.getAmount());
        caller.sendMessage("getDurability " + item.getDurability());
        caller.sendMessage("getTypeId " + item.getTypeId());
        if (item.getData() != null) {
            caller.sendMessage("getData " + item.getData().getData());
        }
    }

    private synchronized List<ItemMessage> getItemsForActionClass(Class actionClass) {
        List<ItemMessage> list = this.itemsMap.get(actionClass);
        if (list == null) {
            list = new ArrayList<ItemMessage>();
        }
        return list;
    }

    public boolean setItemColor(String action, String colorStr, Player caller) {
        Map<Class, List<ItemMessage>> map = this.itemsMap;
        synchronized (map) {
            try {
                ChatColor color = ChatColor.valueOf(colorStr);
                Class actionClass = this.getActionClass(action);
                ItemStack item = caller.getItemInHand();
                String id = StringUtils.join(new Object[]{item.getTypeId(), item.getDurability()}, ":");
                if (actionClass != null) {
                    List<ItemMessage> list = this.getItemsForActionClass(actionClass);
                    boolean notExist = true;
                    for (ItemMessage existedItemMessage : list) {
                        if (!existedItemMessage.getId().equals(id)) continue;
                        existedItemMessage.getMessageParameter().setColor(color);
                        notExist = false;
                        caller.sendMessage("Action color was changed for " + id);
                    }
                    if (notExist) {
                        caller.sendMessage("No message for " + id);
                    }
                }
            }
            catch (IllegalArgumentException ex) {
                return false;
            }
            this.saveItems();
            return true;
        }
    }

    public void setItemPrefix(String action, String prefix, Player caller) {
        Map<Class, List<ItemMessage>> map = this.itemsMap;
        synchronized (map) {
            Class actionClass = this.getActionClass(action);
            ItemStack item = caller.getItemInHand();
            String id = StringUtils.join(new Object[]{item.getTypeId(), item.getDurability()}, ":");
            if (actionClass != null) {
                List<ItemMessage> list = this.getItemsForActionClass(actionClass);
                boolean notExist = true;
                for (ItemMessage existedItemMessage : list) {
                    if (!existedItemMessage.getId().equals(id)) continue;
                    existedItemMessage.getMessageParameter().setWorldPrefix(String.format("[%s] ", prefix.trim()));
                    notExist = false;
                    caller.sendMessage("Action prefix was changed for " + id);
                    this.saveItems();
                }
                if (notExist) {
                    caller.sendMessage("No message for " + id);
                }
            }
        }
    }

    private Class getActionClass(String action) {
        if (this.ACTION_HOLD.equals(action)) {
            return PlayerItemHeldEvent.class;
        }
        if (this.ACTION_PICKUP.equals(action)) {
            return PlayerPickupItemEvent.class;
        }
        if (this.ACTION_USE.equals(action)) {
            return PlayerInteractEvent.class;
        }
        return null;
    }

    public void sendMessage(Class clazz, Player receiver, String itemId) {
        Map<Class, List<ItemMessage>> map = this.itemsMap;
        synchronized (map) {
            List<ItemMessage> list = this.getItemsForActionClass(clazz);
            for (ItemMessage message : list) {
                if (message == null || !message.getId().equals(itemId)) continue;
                this.sendMessage(message, receiver);
            }
        }
    }

    public boolean hasMessage(String itemId) {
        Map<Class, List<ItemMessage>> map = this.itemsMap;
        synchronized (map) {
            Set<Class> classesSet = this.itemsMap.keySet();
            for (Class keyClass : classesSet) {
                List<ItemMessage> list = this.getItemsForActionClass(keyClass);
                for (ItemMessage message : list) {
                    if (message == null || !message.getId().equals(itemId)) continue;
                    return true;
                }
            }
            return false;
        }
    }

    private void sendMessage(ItemMessage message, Player receiver) {
        WorldMessageParams param = message.getMessageParameter();
        StringBuilder phraseBuilder = new StringBuilder();
        phraseBuilder.append(param.getColor());
        phraseBuilder.append(param.getWorldPrefix());
        phraseBuilder.append(param.getColor());
        phraseBuilder.append(message.getMessage());
        receiver.sendMessage(phraseBuilder.toString());
    }
}

