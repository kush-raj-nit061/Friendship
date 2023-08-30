package com.example.friendship.Model;

public class Events {
    String date;
    String month;
    String description;
    String link1;
    String link2;
    public Events(){}

    public Events(String date, String month, String description, String link1, String link2, String link3, String eventpurl, String eventname, String eventtitle) {
        this.date = date;
        this.month = month;
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
        this.eventpurl = eventpurl;
        this.eventname = eventname;
        this.eventtitle = eventtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

    public String getEventpurl() {
        return eventpurl;
    }

    public void setEventpurl(String eventpurl) {
        this.eventpurl = eventpurl;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventtitle() {
        return eventtitle;
    }

    public void setEventtitle(String eventtitle) {
        this.eventtitle = eventtitle;
    }

    String link3;
    String eventpurl;
    String eventname;
    String eventtitle;
}
