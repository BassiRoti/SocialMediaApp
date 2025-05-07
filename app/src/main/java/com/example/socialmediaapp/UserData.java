package com.example.socialmediaapp;

public class UserData {
   private String Username;
   private String Bio;
   private String Image;

    public UserData(){

    }

    public UserData(String username, String bio, String image) {
        Username = username;
        Bio = bio;
        Image = image;
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
}
