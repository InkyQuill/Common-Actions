package net.inkyquill.equestria.ca.settings;

import net.inkyquill.equestria.ca.utils.MoonCalculation;
import net.inkyquill.equestria.ca.utils.SunCalculation;
import net.inkyquill.equestria.ca.utils.SunTimes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by obruchnikov_pa on 26.11.2015.
 */
public class TimeSettings {

    private long ChaosStarted;
    public TimeType Type;
    public double Latitude;
    public double Longitude;
    public double Offset;

    public String FixedSunrise;
    public String FixedSunset;
    public long CalculatedTime;
    public int TicksPerCalc;
    public int ChaosDurationMin;
    public int ChaosDurationMax;
    public int ChaosCurrentDuration;
    private int CurrentTick;

    public TimeSettings()
    {
        Type = TimeType.mine;
        CalculatedTime = -1;
        CurrentTick = 0;
        ChaosCurrentDuration = -1;
        ChaosStarted = new Date().getTime();
    }

    public void RealCalculate() {
        if (++CurrentTick == TicksPerCalc || CalculatedTime == -1) {
            float timeMilliseconds = (float)(new Date().getTime() % 1000) / 1000.0f;
            String theTime = new Date().toString().substring(11, 19);
            float timeOfDayInSeconds = this.getTimeInSeconds(theTime) + timeMilliseconds; // + timeOffset;
            CalculatedTime = (long)this.calculateRealTimeToMCTime(timeOfDayInSeconds);
            CurrentTick = 0;
        }
    }

    public float getTimeInSeconds(String theTime) {
        int hour = Integer.parseInt(theTime.substring(0, 2));
        int minute = Integer.parseInt(theTime.substring(3, 5));
        int second = Integer.parseInt(theTime.substring(6, 8));
        return (float)(hour * 60 * 60) + (float)(minute * 60) + (float)second;
    }

    public float calculateRealTimeToMCTime(float timeOfDay) {
        float MCTime;
        float MCTimePerDay = 24000.0f;
        float RLTimePerDay = 86400.0f;
        int calculatedMcRlTime = (int)((timeOfDay + 1200.0f) % RLTimePerDay);

        SunTimes sun = retrieveSunsetAndSunrise();

        float sunrise = sun.SunriseH*60*60 + sun.SunriseM*60;
        float sunset = sun.SunsetH*60*60 + sun.SunsetM*60;

        if ((float)calculatedMcRlTime > sunrise && (float)calculatedMcRlTime < sunset) {
            float sunTime = sunset - sunrise;
            float currentRLSunsetOffset = (float)calculatedMcRlTime - sunrise;
            MCTime = currentRLSunsetOffset / sunTime * 15600.0f - 1800.0f;
        } else {
            float nightTime = sunrise + (86400.0f - sunset);
            float offsetToSunset = (float)calculatedMcRlTime > sunset ? (float)calculatedMcRlTime - sunset : 86400.0f - sunset + (float)calculatedMcRlTime;
            MCTime = offsetToSunset / nightTime * 8200.0f + 13800.0f;
        }
        int lunarIndex = retrieveMoonphase()+4;
        return MCTime + (float)lunarIndex * MCTimePerDay;
    }

    public SunTimes retrieveSunsetAndSunrise() {
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DATE);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);

        return SunCalculation.Compute(currentDay,currentMonth,currentYear,Longitude,Latitude);
    }

    public int retrieveMoonphase() {
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DATE);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        return MoonCalculation.Moonphase(currentDay,currentMonth,currentYear);
    }

    public void ChaosCalculate() {
        if(ChaosCurrentDuration == -1)
        {
            ChaosCurrentDuration = (int)(Math.random() * (ChaosDurationMax-ChaosDurationMin)) + ChaosDurationMin;
            CalculatedTime = (long)(Math.random()*24000);
        }
        if(new Date().getTime() > ChaosStarted + ChaosCurrentDuration*60*20)
        {
            ChaosStarted = new Date().getTime();
            ChaosCurrentDuration = (int)(Math.random() * (ChaosDurationMax-ChaosDurationMin)) + ChaosDurationMin;
            CalculatedTime = (long)(Math.random()*24000);
        }
    }

    public void FixedCalculate() {
        if (++CurrentTick == TicksPerCalc || CalculatedTime == -1.0f) {
            float timeMilliseconds = (float)(new Date().getTime() % 1000) / 1000.0f;
            String theTime = new Date().toString().substring(11, 19);
            float timeOfDayInSeconds = this.getTimeInSeconds(theTime) + timeMilliseconds; // + timeOffset;
            CalculatedTime = this.calculateFixedTimeToMCTime(timeOfDayInSeconds);
            CurrentTick = 0;
        }
    }

    private long calculateFixedTimeToMCTime(float timeOfDay) {
        float MCTime;
        float MCTimePerDay = 24000.0f;
        float RLTimePerDay = 86400.0f;
        int calculatedMcRlTime = (int)((timeOfDay + 1200.0f) % RLTimePerDay);

        String[] sunrisearr = FixedSunrise.split(",");
        String[] sunsetarr = FixedSunset.split(",");

        float sunrise = Integer.parseInt(sunrisearr[0])*60*60 + Integer.parseInt(sunrisearr[1])*60;
        float sunset = Integer.parseInt(sunsetarr[0])*60*60 + Integer.parseInt(sunsetarr[1])*60;

        if ((float)calculatedMcRlTime > sunrise && (float)calculatedMcRlTime < sunset) {
            float sunTime = sunset - sunrise;
            float currentRLSunsetOffset = (float)calculatedMcRlTime - sunrise;
            MCTime = currentRLSunsetOffset / sunTime * 15600.0f - 1800.0f;
        } else {
            float nightTime = sunrise + (86400.0f - sunset);
            float offsetToSunset = (float)calculatedMcRlTime > sunset ? (float)calculatedMcRlTime - sunset : 86400.0f - sunset + (float)calculatedMcRlTime;
            MCTime = offsetToSunset / nightTime * 8200.0f + 13800.0f;
        }
        int lunarIndex = 4;
        return (long)(MCTime + (float)lunarIndex * MCTimePerDay);
    }


}
