package com.example.socialmediaapp;

public class UserData {
   private String Username;
   private String Bio;
   private String Image;
   private String userID;

    public UserData(){

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserData(String username, String bio, String image, String userID) {
        Username = username;
        Bio = bio;
        Image = image;
        this.userID = userID;
    }
}
