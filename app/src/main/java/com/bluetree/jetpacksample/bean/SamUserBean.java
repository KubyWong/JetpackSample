package com.bluetree.jetpacksample.bean;

public class SamUserBean {
    private  String firstName;
    private  String lastName;
    public SamUserBean(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}