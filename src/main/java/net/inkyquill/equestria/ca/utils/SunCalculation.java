package net.inkyquill.equestria.ca.utils;

public class SunCalculation {

    public static SunTimes Compute(int day, int mon, int year, double longitude, double latitude) {
        int GGG = 1;
        if (year <= 1585) GGG = 0;
        double JD = -1 * Math.floor(7 * (Math.floor((mon + 9) / 12) + year) / 4);
        double S = 1;
        if ((mon - 9) < 0) S = -1;
        double A = Math.abs(mon - 9);
        double J1 = Math.floor(year + S * Math.floor(A / 7));
        J1 = -1 * Math.floor((Math.floor(J1 / 100) + 1) * 3 / 4);
        JD = JD + Math.floor(275 * mon / 9) + day + (GGG * J1);
        JD = JD + 1721027 + 2 * GGG + 367 * year - 0.5;
        double J2 = JD;
        double RAD = 180 / Math.PI;
        double ET = 0.016718;
        double VP = 8.22E-5;
        double P = 4.93204;
        double M0 = 2.12344;
        double MN = 1.72019E-2;
        double T0 = 2444000.5;
        //    S = 2415020.5;
        P = P + (J2 - T0) * VP / 100;
        double AM = M0 + MN * (J2 - T0);
        AM = AM - 2 * Math.PI * Math.floor(AM / (2 * Math.PI));
        double V = AM + 2 * ET * Math.sin(AM) + 1.25 * ET * ET * Math.sin(2 * AM);
        if (V < 0) {
            V = 2 * Math.PI + V;
        }
        double L = P + V;
        L = L - 2 * Math.PI * Math.floor(L / (2 * Math.PI));
        double Z = (J2 - 2415020.5) / 365.2422;
        double OB = 23.452294 - (0.46845 * Z + 0.00000059 * Z * Z) / 3600;
        OB = OB / RAD;
        double DC = Math.asin(Math.sin(OB) * Math.sin(L));
        // double  AR = Math.acos(Math.cos(L) / Math.cos(DC));
        //     if (L > Math.PI) {
        //         AR = 2 * Math.PI - AR;
        //     }
        OB = OB * RAD;
        L = L * RAD;
        //     AR = AR * 12 / Math.PI;
        // double  H = Math.floor(AR);
        // double  M = Math.floor((AR - Math.floor(AR)) * 60);
        //    S = ((AR - Math.floor(AR)) * 60 - M) * 60;
        DC = DC * RAD;
        // double D = Math.abs(DC);
        // double G1=0D;
        //     if (DC > 0) {
        //         G1 = Math.floor(D);
        //     } else {
        //         G1 = (-1) * Math.floor(D);
        //     }
        // double  M1 = Math.floor((D - Math.floor(D)) * 60);
        // double  S1 = ((D - Math.floor(D)) * 60 - M1) * 60;
        //     if (DC < 0) {
        //         M1 = -M1;
        //         S1 = -S1;
        //     }
        double MR = 0.04301;
        double F = 13750.987;
        double C = 2 * ET * F * Math.sin(AM) + 1.25 * ET * ET * F * Math.sin(2 * AM);
        double R = -MR * F * Math.sin(2 * (P + AM)) + MR * MR * F * Math.sin(4 * (P + AM)) / 2;
        ET = C + R;
        double H0 = Math.acos(-Math.tan(latitude / RAD) * Math.tan(DC / RAD));
        H0 = H0 * RAD;
        double VD = 0.9856 * Math.sin(OB / RAD) * Math.cos(L / RAD) / Math.cos(DC / RAD);
        double VDOR = VD * (-H0 + 180) / 360;
        double DCOR = DC + VDOR;
        double HORTO = -Math.acos(-Math.tan(latitude / RAD) * Math.tan(DCOR / RAD));
        double VHORTO = 5 / (6 * Math.cos(latitude / RAD) * Math.cos(DCOR / RAD) * Math.sin(HORTO));
        HORTO = (HORTO * RAD + VHORTO) / 15;
        double TUORTO = HORTO + ET / 3600 - longitude / 15 + 12;
        double HOR = Math.floor(TUORTO);
        double MOR = Math.floor((TUORTO - HOR) * 60 + 0.5);
        //double  TUC = 12 + ET / 3600 - Lon / 15;
        //double  HC = Math.floor(TUC);
        //double  MC = Math.floor((TUC - HC) * 60 + 0.5);
        double VDOC = VD * (H0 + 180) / 360;
        double DCOC = DC + VDOC;
        double HOC = Math.acos(-Math.tan(latitude / RAD) * Math.tan(DCOC / RAD));
        double VHOC = 5 / (6 * Math.cos(latitude / RAD) * Math.cos(DCOC / RAD) * Math.sin(HOC));
        HOC = (HOC * RAD + VHOC) / 15;
        double TUOC = HOC + ET / 3600 - longitude / 15 + 12;
        HOC = Math.floor(TUOC);
        double MOC = Math.floor((TUOC - HOC) * 60 + 0.5);
        //double  HCUL = 90 - Lat + (DCOR + DCOC) / 2;
        //double  GCUL = Math.floor(HCUL);
        //double  MCUL = Math.floor((HCUL - GCUL) * 60 + 0.5);
        //double  ACOC = Math.acos(-Math.sin(DCOC / RAD) / Math.cos(Lat / RAD)) * RAD;
        //double  ACOR = 360 - Math.acos(-Math.sin(DCOR / RAD) / Math.cos(Lat / RAD)) * RAD;
        //double  GACOC = Math. floor(ACOC);
        //double  MACOC = Math.floor((ACOC - GACOC) * 60 + 0.5);
        //double  GACOR = Math.floor(ACOR);
        //double  MACOR = Math.floor((ACOR - GACOR) * 60 + 0.5);

        if (HOC > 24) {
            HOC -= 24;
        }
        return new SunTimes((int) HOR, (int) MOR, (int) HOC, (int) MOC);
    }
}

