package com.chiragagg5k.bu_news_android;

public class User {
    private String name, student_mail, password;

    public User(String name, String student_mail, String password) {
        this.name = name;
        this.student_mail = student_mail;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getStudent_mail() {
        return student_mail;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudent_mail(String student_mail) {
        this.student_mail = student_mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
