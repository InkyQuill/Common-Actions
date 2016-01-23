package net.inkyquill.equestria.ca.runnable;


import net.inkyquill.equestria.ca.settings.CASettings;
import net.inkyquill.equestria.ca.settings.TimeType;
import net.inkyquill.equestria.ca.settings.WorldSettings;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TimeUpdater
        extends BukkitRunnable {


    public void run() {
        if (CASettings.TimeEnabled)
            new TimeUpdater().runTaskLater(CASettings.plugin, 1);
        this.setEquivalentTime();
    }

    public void setEquivalentTime() {

        List<World> worlds = CASettings.plugin.getServer().getWorlds();

        for (World w : worlds) {
            WorldSettings ws = CASettings.getWorldSettings(w);
            if (ws.time.Type == TimeType.mine) continue;

            ws.time.CurrentTick++;

            if (ws.time.CurrentTick > ws.time.TicksPerCalc) {
                ws.time.CurrentTick = 0;
                if (ws.time.Type == TimeType.day)
                    w.setFullTime(6000);
                else if (ws.time.Type == TimeType.night)
                    w.setFullTime(18000);
                else if (ws.time.Type == TimeType.chaos) {
                    ws.time.ChaosCalculate();
                    w.setFullTime(ws.time.CalculatedTime);
                } else if (ws.time.Type == TimeType.real) {
                    ws.time.RealCalculate();
                    w.setFullTime(ws.time.CalculatedTime);
                } else if (ws.time.Type == TimeType.fixed) {
                    ws.time.FixedCalculate();
                    w.setFullTime(ws.time.CalculatedTime);
                }
            } else if (ws.time.UpdateEveryTick) {
                if (ws.time.Type == TimeType.day)
                    w.setFullTime(6000);
                else if (ws.time.Type == TimeType.night)
                    w.setFullTime(18000);
                else if (ws.time.Type == TimeType.chaos ||
                        ws.time.Type == TimeType.real ||
                        ws.time.Type == TimeType.fixed) {
                    w.setFullTime(ws.time.CalculatedTime);
                }
            }
        }
    }
}

