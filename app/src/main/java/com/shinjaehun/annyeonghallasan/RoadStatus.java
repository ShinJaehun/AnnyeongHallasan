package com.shinjaehun.annyeonghallasan;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class RoadStatus {

    String name;
    String description;
    String date;
    int deepSnow;
    int freezingIce;
    boolean bigChain;
    boolean smallChain;

    public RoadStatus(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getDeepSnow() {
        return deepSnow;
    }

    public void setDeepSnow(int deepSnow) {
        this.deepSnow = deepSnow;
    }

    public int getFreezingIce() {
        return freezingIce;
    }

    public void setFreezingIce(int freezingIce) {
        this.freezingIce = freezingIce;
    }

    public boolean isBigChain() {
        return bigChain;
    }

    public void setBigChain(boolean bigChain) {
        this.bigChain = bigChain;
    }

    public boolean isSmallChain() {
        return smallChain;
    }

    public void setSmallChain(boolean smallChain) {
        this.smallChain = smallChain;
    }
}
