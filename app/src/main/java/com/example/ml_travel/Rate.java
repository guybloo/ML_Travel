package com.example.ml_travel;

import java.io.Serializable;

public class Rate implements Serializable {
    public String user;
    public int item;
    public double rate;

    public Rate(String u, int i, double r) {
        user = u;
        item = i;
        rate = r;
    }

    public double getRate() {
        return rate;
    }
}
