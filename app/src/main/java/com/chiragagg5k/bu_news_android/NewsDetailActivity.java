package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    TextView news_heading_tv, news_description_tv;
    ImageView news_image_iv, backBtn;

    String news_heading, news_description, news_image_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        Intent intent = getIntent();
        news_heading = intent.getStringExtra("news_heading");
        news_description = intent.getStringExtra("news_description");
        news_image_url = intent.getStringExtra("news_image_url");

        news_heading_tv = findViewById(R.id.detailed_news_title);
        news_description_tv = findViewById(R.id.detailed_news_description);
        news_image_iv = findViewById(R.id.detailed_news_image);
        backBtn = findViewById(R.id.back_button);

        news_heading_tv.setText(news_heading);
        Picasso.get().load(news_image_url).into(news_image_iv);
        news_description_tv.setText(news_description);

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}
