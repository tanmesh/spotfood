package com.tanmesh.splatter.wsRequestModel;

public class UserActivityData {
    String userId;
    String activityId;

    public UserActivityData(String userId, String activityId) {
        this.userId = userId;
        this.activityId = activityId;
    }

    public UserActivityData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
