package com.example.emon.myapplication2;

public class UserMessageList {
    String mname,mmessage,mimage;
    public UserMessageList()
    {

    }


    public UserMessageList(String mname, String mmessage, String mimage) {
        this.mname = mname;
        this.mmessage = mmessage;
        this.mimage = mimage;

    }

    public String getName() {
        return mname;
    }

    public void setName(String title) {
        this.mname = mname;
    }

    public String getMessage() {
        return mmessage;
    }

    public void setMessage(String desc) {
        this.mmessage = mmessage;
    }

    public String getImage() {
        return mimage;
    }

    public void setImage(String image) {
        this.mimage = image;
    }
}
