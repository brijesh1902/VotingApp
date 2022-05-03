package com.app.voting.model;

public class UserModel {

    private String Name, Email, Password, uid;

    public UserModel(){}

    public UserModel(String name, String email, String password, String uid) {
        Name = name;
        Email = email;
        Password = password;
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
