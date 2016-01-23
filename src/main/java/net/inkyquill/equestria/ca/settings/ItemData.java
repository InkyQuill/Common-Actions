package net.inkyquill.equestria.ca.settings;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ItemData implements ConfigurationSerializable {

    public String HoldMessage;
    public String UseMessage;
    public String PickupMessage;

    public Messagetype HoldType;
    public Messagetype UseType;
    public Messagetype PickupType;

    public int HoldRadius;
    public int UseRadius;
    public int PickupRadius;

    public ItemData() {
        HoldMessage = null;
        UseMessage = null;
        PickupMessage = null;

        HoldType = Messagetype.USER;
        UseType = Messagetype.USER;
        PickupType = Messagetype.USER;

        HoldRadius = 20;
        UseRadius = 20;
        PickupRadius = 20;
    }

    public ItemData(Map<String, Object> map) {

        if (map.containsKey("hold.message")) {
            HoldMessage = (String) map.get("hold.message");
            try {
                HoldType = Messagetype.valueOf((String) map.get("hold.type"));
            } catch (Exception e) {
                HoldType = Messagetype.USER;
            }
            if (HoldType == Messagetype.WORLD)
                HoldRadius = (Integer) map.get("hold.radius");
            else
                HoldRadius = 20;
        }
        if (map.containsKey("use.message")) {
            UseMessage = (String) map.get("use.message");
            try {
                UseType = Messagetype.valueOf((String) map.get("use.type"));
            } catch (Exception e) {
                UseType = Messagetype.USER;
            }
            if (UseType == Messagetype.WORLD)
                UseRadius = (Integer) map.get("use.radius");
            else
                UseRadius = 20;
        }
        if (map.containsKey("pickup.message")) {
            PickupMessage = (String) map.get("pickup.message");
            try {
                PickupType = Messagetype.valueOf((String) map.get("pickup.type"));
            } catch (Exception e) {
                PickupType = Messagetype.USER;
            }
            if (PickupType == Messagetype.WORLD)
                PickupRadius = (Integer) map.get("pickup.radius");
            else
                PickupRadius = 20;
        }
    }

    @Override
    public Map<String, Object> serialize() {

        HashMap<String, Object> map = new HashMap<String, Object>();

        if (HoldMessage != null) {
            map.put("hold.message", HoldMessage);
            map.put("hold.type", HoldType.name());
            if (HoldType == Messagetype.WORLD)
                map.put("hold.radius", HoldRadius);
        }
        if (UseMessage != null) {
            map.put("use.message", UseMessage);
            map.put("use.type", UseType.name());
            if (UseType == Messagetype.WORLD)
                map.put("use.radius", UseRadius);
        }
        if (PickupMessage != null) {
            map.put("pickup.message", PickupMessage);
            map.put("pickup.type", PickupType.name());
            if (PickupType == Messagetype.WORLD)
                map.put("pickup.radius", PickupRadius);
        }
        return map;
    }

    public enum Messagetype {
        USER, WORLD
    }
}
