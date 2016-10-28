package com.maverick.tcs.innovations.talentengagment.beans;

/**
 * Created by 1007546 on 12-09-2016.
 */
public class ActivityBean {

    private String activityId;
    private String employeeId;
    private String time;

    public ActivityBean() {
    }

    public ActivityBean(String activityId, String employeeId, String time) {
        this.activityId = activityId;
        this.employeeId = employeeId;
        this.time = time;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
