package PTF;

import java.util.Arrays;

public class TechnicalSkillCategories {
    private double Programming;
    private double Networking;
    private double Analytics;
    private double Web;

    public TechnicalSkillCategories() {
    }

    public TechnicalSkillCategories(double programming, double networking, double analytics, double web) {
        Programming = programming;
        Networking = networking;
        Analytics = analytics;
        Web = web;
    }

    public double getProgramming() {
        return Programming;
    }

    public void setProgramming(double programming) {
        Programming = programming;
    }

    public double getNetworking() {
        return Networking;
    }

    public void setNetworking(double networking) {
        Networking = networking;
    }

    public double getAnalytics() {
        return Analytics;
    }

    public void setAnalytics(double analytics) {
        Analytics = analytics;
    }

    public double getWeb() {
        return Web;
    }

    public void setWeb(double web) {
        Web = web;
    }

    @Override
    public String toString() {
        return
                "W " + Web +
                " P " + Programming + " N " + Networking +
                " A " + Analytics;
    }
    static TechnicalSkillCategories fromString(String line){
        //Line
        String[] components = line.split(" ");
        String id = components[0];
        double w = Double.parseDouble(components[1]);
        double p = Double.parseDouble(components[3]);
        double n = Double.parseDouble(components[5]);
        double a = Double.parseDouble(components[7]);

        return new TechnicalSkillCategories(p,n,a,w);
    }
}
