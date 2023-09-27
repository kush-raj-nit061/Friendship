package com.test.friendship.Model;

public class Status {

    String date;

    public long getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(long statusCount) {
        this.statusCount = statusCount;
    }

    long statusCount;

    public Status() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Status(long statusC,String date, String purl, String postUrl, String name, String postlikes, String id) {
        this.date = date;
        this.purl = purl;
        this.postUrl = postUrl;
        this.name = name;
        this.postlikes = postlikes;
        this.id = id;
        this.statusCount = statusC;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostlikes() {
        return postlikes;
    }

    public void setPostlikes(String postlikes) {
        this.postlikes = postlikes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String purl;
    String postUrl;
    String name;
    String postlikes;
    String id;
}
