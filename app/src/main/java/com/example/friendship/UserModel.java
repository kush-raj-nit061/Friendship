package com.example.friendship;

public class UserModel {
    UserModel(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
    String purl;
    String birthday;
    String books;
    String foods;
    String qualitydislike;
    String hobbies;
    String location;
    String qualitylike;
    String travellike;
    String userId;

    public UserModel(String status, String purl, String birthday, String books, String foods, String qualitydislike, String hobbies, String location, String qualitylike, String travellike, String userId, String premium, String name, String branch, String year, String shortBio) {
        this.status = status;
        this.purl = purl;
        this.birthday = birthday;
        this.books = books;
        this.foods = foods;
        this.qualitydislike = qualitydislike;
        this.hobbies = hobbies;
        this.location = location;
        this.qualitylike = qualitylike;
        this.travellike = travellike;
        this.userId = userId;
        this.premium = premium;
        this.name = name;
        this.branch = branch;
        this.year = year;
        this.shortBio = shortBio;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public String getQualitydislike() {
        return qualitydislike;
    }

    public void setQualitydislike(String qualitydislike) {
        this.qualitydislike = qualitydislike;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQualitylike() {
        return qualitylike;
    }

    public void setQualitylike(String qualitylike) {
        this.qualitylike = qualitylike;
    }

    public String getTravellike() {
        return travellike;
    }

    public void setTravellike(String travellike) {
        this.travellike = travellike;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    String premium;



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
