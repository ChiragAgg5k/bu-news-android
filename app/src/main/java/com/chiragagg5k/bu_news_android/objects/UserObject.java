package com.chiragagg5k.bu_news_android.objects;

import java.util.ArrayList;

public class UserObject {
    private final ArrayList<String> categories;
    private final String phoneNo, city;
    private final boolean admin;
    private String name;

    public UserObject() {
        // !IMPORTANT: This constructor is required for Firebase to work
        this("", "");
    }

    public UserObject(String name, String phoneNo, String city) {
        this.name = name;
        this.admin = false;
        this.phoneNo = phoneNo;
        this.city = city;
        this.categories = new ArrayList<>();
    }

    public UserObject(String name, String phoneNo) {
        this(name, phoneNo, "");
    }

    public UserObject(String name) {
        this(name, "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public String getCity() {
        return this.city;
    }

}
