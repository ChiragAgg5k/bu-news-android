package com.chiragagg5k.bu_news_android.objects;

import com.chiragagg5k.bu_news_android.UtilityClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;

/**
 * Object to store news data
 *
 * @author Chirag Aggarwal
 */
public class NewsObject {

    private final String newsHeading;
    private final String newsDescription;
    private final String mImageUrl;
    private final String category;
    private final String username;
    private final String date;
    private final boolean promoted;
    private final boolean authorized;

    public NewsObject(String newsHeading, String newsDescription, String category, String imageUrl, String date) {
        this.authorized = true;
        this.promoted = false;
        this.category = category;
        this.mImageUrl = imageUrl;
        this.newsDescription = newsDescription;
        this.newsHeading = newsHeading;
        this.date = date;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.username = user.getDisplayName();
        } else {
            this.username = "Anonymous";
        }
    }

    public NewsObject() {
        // !IMPORTANT: This constructor is required for Firebase to work

        this("", "", "", "", "");
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public String getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public String getDate() {
        return date;
    }

}
