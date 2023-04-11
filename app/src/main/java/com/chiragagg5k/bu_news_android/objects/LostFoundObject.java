package com.chiragagg5k.bu_news_android.objects;

public class LostFoundObject {

    private String itemName;
    private String itemDescription;
    private String itemLocation;
    private String itemDate;

    public LostFoundObject() {
        // !IMPORTANT: This constructor is required for Firebase to work
    }

    public LostFoundObject(String itemName, String itemDescription, String itemLocation, String itemDate) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemLocation = itemLocation;
        this.itemDate = itemDate;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getItemDate() {
        return itemDate;
    }
}
