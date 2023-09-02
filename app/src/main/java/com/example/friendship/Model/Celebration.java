package com.example.friendship.Model;

public class Celebration {

    public Celebration(){}

    String eventname;

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLottie1() {
        return lottie1;
    }

    public void setLottie1(String lottie1) {
        this.lottie1 = lottie1;
    }

    public String getLottie2() {
        return lottie2;
    }

    public void setLottie2(String lottie2) {
        this.lottie2 = lottie2;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    String username;
    String lottie1;

    public Celebration(String eventname, String username, String lottie1, String lottie2, String purl) {
        this.eventname = eventname;
        this.username = username;
        this.lottie1 = lottie1;
        this.lottie2 = lottie2;
        this.purl = purl;
    }

    String lottie2;
    String purl;

}
