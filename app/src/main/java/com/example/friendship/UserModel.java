package com.example.friendship;

public class UserModel {
    UserModel(){}

    String purl;


    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    String premium;

    public UserModel(String name, String branch, String year, String shortBio ,String purl,String premium) {
        this.name = name;
        this.branch = branch;
        this.year = year;
        this.shortBio = shortBio;
        this.purl = purl;
        this.premium = premium;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    String branch;
    String year;
    String shortBio;
}
