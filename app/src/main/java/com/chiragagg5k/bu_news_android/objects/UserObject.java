package com.chiragagg5k.bu_news_android.objects;

import java.util.ArrayList;

public class UserObject {
    private String name;
    private ArrayList<String> categories;
    private String phoneNo;
    private boolean admin;

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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public void removeCategory(String category) {
        categories.remove(category);
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

}
