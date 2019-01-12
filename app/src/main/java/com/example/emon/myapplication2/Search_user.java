package com.example.emon.myapplication2;

public class Search_user {
   private String username,gender,profilepic,id;

    public Search_user(String username, String gender, String profilepic,String id) {
        this.username = username;
        this.gender = gender;
        this.profilepic = profilepic;
        this.id=id;
    }

    public Search_user() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getId() {
        return id;
    }

    public void setId(String username) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
