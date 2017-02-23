package com.shinjaehun.annyeonghallasan.model;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class Road {

    private String name;
    private String date;
    private boolean restrict;
    private String section;
    private float snowfall;
    private float freezing;

    public String chain;

    public Road(String name, String date, boolean restrict, String section, float snowfall, float freezing, String chain) {
        this.name = name;
        this.date = date;
        this.restrict = restrict;
        this.section = section;
        this.snowfall = snowfall;
        this.freezing = freezing;
        this.chain = chain;

    }

    public String getName() {
        return name;
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

    public boolean isRestrict() {
        return restrict;
    }

    public String getSection() {
        return section;
    }

    public String getChain() {
        return chain;
    }
}
