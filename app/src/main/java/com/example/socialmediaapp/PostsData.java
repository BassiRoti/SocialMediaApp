package com.example.socialmediaapp;

import java.util.List;

public class PostsData {
    public String userID;
    public String caption;
    public String timespam;
    public List<String> imageUrls;

    public PostsData(){

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTimespam() {
        return timespam;
    }

    public void setTimespam(String timespam) {
        this.timespam = timespam;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public PostsData(String userID, String caption, String timespam, List<String> imageUrls) {
        this.userID = userID;
        this.caption = caption;
        this.timespam = timespam;
        this.imageUrls = imageUrls;
    }
}
