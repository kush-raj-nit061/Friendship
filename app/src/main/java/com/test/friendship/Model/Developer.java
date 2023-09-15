package com.test.friendship.Model;

public class Developer {

    public Developer(){}

    String name;
    String post;

    public Developer(String name, String post, String description, String purl) {
        this.name = name;
        this.post = post;
        this.description = description;
        this.purl = purl;
    }

    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    String purl;

}
