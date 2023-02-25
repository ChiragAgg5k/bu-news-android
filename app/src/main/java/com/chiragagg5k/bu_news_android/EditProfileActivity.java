package com.chiragagg5k.bu_news_android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView backBtn;
    FloatingActionButton edit_image_btn;
    Uri imageUri;
    FirebaseUser user;
    CircleImageView profile_image;
    Button saveBtn;
    TextView name, studentMail;
    EditText editName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backBtn = findViewById(R.id.back_button_edit_profile);
        edit_image_btn = findViewById(R.id.edit_image_btn);
        profile_image = findViewById(R.id.profile_image_edit_profile);
        saveBtn = findViewById(R.id.save_button);
        name = findViewById(R.id.profile_name_edit_profile);
        editName = findViewById(R.id.edit_name);
        studentMail = findViewById(R.id.student_mail);

        user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (user.getPhotoUrl() != null)
            profile_image.setImageURI(user.getPhotoUrl());

        name.setText(user.getDisplayName());
        editName.setText(user.getDisplayName());
        studentMail.setText(user.getEmail());

        backBtn.setOnClickListener(v -> {
            finish();
        });

        edit_image_btn.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

        saveBtn.setOnClickListener(v -> {
            if (imageUri == null)
                imageUri = user.getPhotoUrl();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(editName.getText().toString())
                    .setPhotoUri(imageUri)
                    .build();

            user.updateProfile(profileUpdates);

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            assert data != null;
            imageUri = data.getData();
            profile_image.setImageURI(imageUri);
        }
    }
}

