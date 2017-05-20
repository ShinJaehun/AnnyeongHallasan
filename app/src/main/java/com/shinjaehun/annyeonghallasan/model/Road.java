package com.shinjaehun.annyeonghallasan.model;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class Road {

    private String name;
    private String baseDate;
    private int restrict;
    private String section;
    private float snowfall;
    private float freezing;
    private int chain;

    public Road(String name, String baseDate, int restrict, String section, float snowfall, float freezing, int chain) {
        this.name = name;
        this.baseDate = baseDate;
        this.restrict = restrict;
        this.section = section;
        this.snowfall = snowfall;
        this.freezing = freezing;
        this.chain = chain;

    }

    public String getName() {
        return name;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public float getSnowfall() {
        return snowfall;
    }

    public float getFreezing() {
        return freezing;
    }

    public int isRestrict() {
        return restrict;
    }

    public String getSection() {
        return section;
    }

    public int getChain() {
        return chain;
    }
}
