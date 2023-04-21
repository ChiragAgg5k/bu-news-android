package com.chiragagg5k.bu_news_android.objects;

import com.chiragagg5k.bu_news_android.UtilityClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LostFoundObject {

    FirebaseUser user;
    private String itemName;
    private String itemDescription;
    private String itemLocation;
    private long itemDate;
    private String toContactUID;
    private String itemImageURL;
    private String contactNo;

    public LostFoundObject() {
        // !IMPORTANT: This constructor is required for Firebase to work
    }

    public LostFoundObject(String itemName, String itemDescription, String itemLocation, String contactNo, String imageUrl) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemLocation = itemLocation;
        this.contactNo = contactNo;
        this.itemDate = UtilityClass.getCurrentDateInMilliSeconds();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.toContactUID = (user != null) ? user.getUid() : null;
        this.itemImageURL = imageUrl;
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

    public long getItemDate() {
        return itemDate;
    }

    public String getToContactUID() {
        return toContactUID;
    }

    public String getItemImageURL() {
        return itemImageURL;
    }

    public String getContactNo() {
        return contactNo;
    }

}
