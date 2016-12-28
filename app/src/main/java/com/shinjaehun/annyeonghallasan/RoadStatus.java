package com.shinjaehun.annyeonghallasan;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class RoadStatus {

    String name;
    String description;
    String date;
    String snowfall;
    String freezing;
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

    public String getSnowfall() {
        return snowfall;
    }

    public void setSnowfall(String snowfall) {
        this.snowfall = snowfall;
    }

    public String getFreezing() {
        return freezing;
    }

    public void setFreezing(String freezing) {
        this.freezing = freezing;
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
