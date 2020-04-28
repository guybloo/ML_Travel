package com.example.ml_travel;

import java.io.Serializable;

public class Trip implements Serializable {
    public int index;
    public String name;
    public String area;
    public String place;
    public int time;
    public int level;
    public double length;

    public boolean summer;
    public boolean winter;
    public boolean fall;
    public boolean spring;

    public boolean families;
    public boolean goodWalkers;
    public boolean romantic;
    public boolean circle;
    public boolean bicycle;
    public boolean city;
    public boolean nature;
    public boolean accessible;
    public boolean publicTransport;
    public boolean dogs;
    public boolean flowers;
    public boolean water;
    public boolean bigBags;
    public boolean israelRoute;

    public Trip()
    {
       summer = false;
       winter = false;
       fall = false;
       spring = false;
       families = false;
       goodWalkers = false;
       romantic = false;
       circle = false;
       bicycle = false;
       city = false;
       nature = false;
       accessible = false;
       publicTransport = false;
       dogs = false;
       flowers = false;
       water = false;
       bigBags = false;
       israelRoute = false;
    }

    public Trip(String str) {
        str = str.toLowerCase();
        String[] s = str.split(",");
        index = Integer.parseInt(s[0]);
        name = s[1];
        time = Integer.parseInt(s[2]);
        level = Integer.parseInt(s[3]);
        length = Double.parseDouble(s[4]);
        area = s[5];

        if (s[6].equals("nan"))
            place = "";
        else
            place = s[6];
        summer = Boolean.parseBoolean(s[7]);
        winter = Boolean.parseBoolean(s[8]);
        fall = Boolean.parseBoolean(s[9]);
        spring = Boolean.parseBoolean(s[10]);
        families = Boolean.parseBoolean(s[11]);
        goodWalkers = Boolean.parseBoolean(s[12]);
        romantic = Boolean.parseBoolean(s[13]);
        circle = Boolean.parseBoolean(s[14]);
        bicycle = Boolean.parseBoolean(s[15]);
        city = Boolean.parseBoolean(s[16]);
        nature = Boolean.parseBoolean(s[17]);
        accessible = Boolean.parseBoolean(s[18]);
        publicTransport = Boolean.parseBoolean(s[19]);
        dogs = Boolean.parseBoolean(s[20]);
        flowers = Boolean.parseBoolean(s[21]);
        water = Boolean.parseBoolean(s[22]);
        bigBags = Boolean.parseBoolean(s[23]);
        israelRoute = Boolean.parseBoolean(s[24]);

    }
}
