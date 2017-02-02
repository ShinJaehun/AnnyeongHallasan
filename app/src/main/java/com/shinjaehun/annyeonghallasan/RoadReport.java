package com.shinjaehun.annyeonghallasan;

import java.util.ArrayList;

/**
 * Created by shinjaehun on 2017-02-03.
 */

public class RoadReport {
    String updateDate;
    ArrayList<RoadCondition> roadConditions;

    public RoadReport(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setRoadConditions(ArrayList<RoadCondition> roadConditions) {
        this.roadConditions = roadConditions;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public ArrayList<RoadCondition> getRoadConditions() {
        return roadConditions;
    }
}
