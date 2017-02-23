package com.shinjaehun.annyeonghallasan.model;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class Road {

    private String name;
    private String date;
    private int restriction;
    private String section;
    private float snowfall;
    private float freezing;

    private int chain;

    public Road(String name, String date, int restriction, String section, float snowfall, float freezing, int chain) {
        this.name = name;
        this.date = date;
        this.restriction = restriction;
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

    public int isRestriction() {
        return restriction;
    }

    public String getSection() {
        return section;
    }

    public int getChain() {
        return chain;
    }
}
