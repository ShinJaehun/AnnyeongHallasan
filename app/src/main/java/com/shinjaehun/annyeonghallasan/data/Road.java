package com.shinjaehun.annyeonghallasan.data;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class Road {

    private String name;
    private String description;
    private String date;
    private boolean restriction;
    private String section;
    private float snowfall;
    private float freezing;
    private boolean snowChainBig;
    private boolean snowChainSmall;

    public Road(String name, String description, String date, boolean restriction, String section, float snowfall, float freezing, boolean snowChainBig, boolean snowChainSmall) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.restriction = restriction;
        this.section = section;
        this.snowfall = snowfall;
        this.freezing = freezing;
        this.snowChainBig = snowChainBig;
        this.snowChainSmall = snowChainSmall;
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

    public float getSnowfall() {
        return snowfall;
    }

    public float getFreezing() {
        return freezing;
    }

    public boolean isSnowChainBig() {
        return snowChainBig;
    }

    public boolean isSnowChainSmall() {
        return snowChainSmall;
    }

    public boolean isRestriction() {
        return restriction;
    }

    public String getSection() {
        return section;
    }

}
