package com.test.friendship;

public class FeaturedModel {
    String purl;
    String medallottie;
    String name;
    String branch;

    FeaturedModel(){}

    public String getPurl() {
        return purl;
    }

    public FeaturedModel(String purl, String medallottie, String name, String branch, String year, String shortBio) {
        this.purl = purl;
        this.medallottie = medallottie;
        this.name = name;
        this.branch = branch;
        this.year = year;
        this.shortBio = shortBio;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getMedallottie() {
        return medallottie;
    }

    public void setMedallottie(String medallottie) {
        this.medallottie = medallottie;
    }

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

    String year;
    String shortBio;



}
