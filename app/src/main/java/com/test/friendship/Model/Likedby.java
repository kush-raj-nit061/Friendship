package com.test.friendship.Model;

public class Likedby {
    Likedby(){}
    String purl;
    String userId;

    public String getPurl() {
        return purl;
    }

    public Likedby(String purl, String userId) {
        this.purl = purl;
        this.userId = userId;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
