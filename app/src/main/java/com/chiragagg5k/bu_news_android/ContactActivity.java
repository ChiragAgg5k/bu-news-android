package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}
