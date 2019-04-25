package com.tanmesh.splatter.wsRequestModel;

import java.util.List;

public class UserPostData {
    private List<String> tags;
    private String location;
    private String cuisineName;
    private String image;

    public UserPostData() {
    }

    public UserPostData(List<String> tags, String location, String cuisineName, String image) {
        this.tags = tags;
        this.location = location;
        this.cuisineName = cuisineName;
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCuisineName() {
        return cuisineName;
    }

    public void setCuisineName(String cuisineName) {
        this.cuisineName = cuisineName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "UserPostData{" +
                "tags=" + tags +
                ", location='" + location + '\'' +
                ", cuisineName='" + cuisineName + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
