package com.maverick.tcs.innovations.talentengagment.beans;

import java.util.ArrayList;

/**
 * Created by 1007546 on 12-09-2016.
 */
public class JsonDataBean {
    private ArrayList<ActivityBean> activities = new ArrayList<>();

    public JsonDataBean(ArrayList<ActivityBean> activities) {
        this.activities = activities;
    }

    public ArrayList<ActivityBean> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivityBean> activities) {
        this.activities = activities;
    }
}
