package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseUser user;
    ImageView back_button;
    TextView profile_name, full_name_text, email_text;
    Button edit_profile_button, change_password_button;

    CircleImageView profile_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        profile_name = findViewById(R.id.profile_name);
        full_name_text = findViewById(R.id.full_name_text);
        email_text = findViewById(R.id.email_text);
        profile_image = findViewById(R.id.profile_image);

        back_button = findViewById(R.id.back_button);
        edit_profile_button = findViewById(R.id.edit_profile_button);
        change_password_button = findViewById(R.id.change_password_button);

        profile_name.setText(user.getDisplayName());
        full_name_text.setText(user.getDisplayName());
        email_text.setText(user.getEmail());

        if(user.getPhotoUrl() != null)
            profile_image.setImageURI(user.getPhotoUrl());

        back_button.setOnClickListener(v -> finish());
        edit_profile_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;

        Uri profile_image_uri = data.getData();
        profile_image.setImageURI(profile_image_uri);
    }
}
