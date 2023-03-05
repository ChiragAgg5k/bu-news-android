package com.chiragagg5k.bu_news_android.objects;

import java.util.ArrayList;

public class UserObject {
    private String name;
    private final ArrayList<String> categories;
    private final String phoneNo;
    private final boolean admin;

    public UserObject() {
        // !IMPORTANT: This constructor is required for Firebase to work
        this.name = "";
        this.admin = false;
        this.phoneNo = "";
        this.categories = new ArrayList<>();
    }

    public UserObject(String name, String phoneNo) {
        this.name = name;
        this.admin = false;
        this.phoneNo = phoneNo;
        this.categories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
