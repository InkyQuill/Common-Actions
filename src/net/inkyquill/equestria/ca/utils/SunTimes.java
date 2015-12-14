package net.inkyquill.equestria.ca.utils;

public class SunTimes {
    public final int SunriseH;
    public final int SunriseM;
    public final int SunsetH;
    public final int SunsetM;

    public SunTimes(int hor, int mor, int hoc, int moc) {
        SunriseH = hor;
        SunriseM = mor;
        SunsetH = hoc;
        SunsetM = moc;
    }
}
