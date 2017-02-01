package com.shinjaehun.annyeonghallasan;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class RoadCondition {

    String name;
    String description;
    String date;
    boolean restriction;
    String section;
    Float snowfall;
    Float freezing;
    boolean snowChainBig;
    boolean snowChainSmall;

    public RoadCondition(String name, String description, String date, boolean restriction, String section, Float snowfall, Float freezing, boolean snowChainBig, boolean snowChainSmall) {
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

    public Float getSnowfall() {
        return snowfall;
    }

    public Float getFreezing() {
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
