package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    FirebaseUser user;
    ImageView back_button;
    TextView logout_button, profile_name, full_name_text, email_text, edit_profile_button, change_password_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        profile_name = findViewById(R.id.profile_name);
        full_name_text = findViewById(R.id.full_name_text);
        email_text = findViewById(R.id.email_text);

        profile_name.setText(user.getDisplayName());
        full_name_text.setText(user.getDisplayName());
        email_text.setText(user.getEmail());

        back_button = findViewById(R.id.back_button);
        logout_button = findViewById(R.id.logout_button);
        edit_profile_button = findViewById(R.id.edit_profile_button);
        change_password_button = findViewById(R.id.change_password_button);

        back_button.setOnClickListener(v -> {
            finish();
        });

        logout_button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish();

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}
