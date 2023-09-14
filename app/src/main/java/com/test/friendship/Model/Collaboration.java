package com.test.friendship.Model;

public class Collaboration {

    String projectname;
    String projecttype;
    String requirement;
    String description;
    String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Collaboration() {
    }

    String purl;

    public Collaboration(String id,String projectname, String projecttype, String requirement, String description, String date, String purl) {
        this.projectname = projectname;
        this.projecttype = projecttype;
        this.requirement = requirement;
        this.description = description;
        this.date = date;
        this.id = id;
        this.purl = purl;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(String projecttype) {
        this.projecttype = projecttype;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
