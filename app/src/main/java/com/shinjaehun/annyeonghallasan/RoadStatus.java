package com.shinjaehun.annyeonghallasan;

/**
 * Created by shinjaehun on 2016-12-25.
 */

public class RoadStatus {

    String name;
    String description;
    String date;
    boolean restriction;
    String section;
    Integer snowfall;
    Integer freezing;
    boolean snowChainBig;
    boolean snowChainSmall;

    public RoadStatus(String name, String description, String date, boolean restriction, String section, Integer snowfall, Integer freezing, boolean snowChainBig, boolean snowChainSmall) {
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

    public Integer getSnowfall() {
        return snowfall;
    }

    public Integer getFreezing() {
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
