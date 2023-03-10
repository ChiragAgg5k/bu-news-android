package com.chiragagg5k.bu_news_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

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
    StorageReference storageReference;
    DashboardActivity dashboardActivity = new DashboardActivity();

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
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        assert user != null;
        if (user.getPhotoUrl() != null)
            profile_image.setImageURI(user.getPhotoUrl());

        name.setText(user.getDisplayName());
        editName.setText(user.getDisplayName());
        studentMail.setText(user.getEmail());

        backBtn.setOnClickListener(v -> onBackPressed());

        edit_image_btn.setOnClickListener(v -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        saveBtn.setOnClickListener(v -> {

            String name = editName.getText().toString();

            if (imageUri == null && name.equals(user.getDisplayName())) {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty()) {
                editName.setError("Name is required");
                editName.requestFocus();
                return;
            }

            if (imageUri != null) {
                StorageTask uploadTask = storageReference.child(user.getUid()).putFile(imageUri);
                uploadTask.addOnSuccessListener(o -> storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> imageUri = uri));
            } else {
                imageUri = user.getPhotoUrl();
            }

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(editName.getText().toString())
                    .setPhotoUri(imageUri)
                    .build();

            user.updateProfile(profileUpdates);

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

            finish();
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("name", editName.getText().toString());
            intent.putExtra("imageUri", imageUri.toString());
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (imageUri != null || !editName.getText().toString().equals(user.getDisplayName())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to discard changes?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            Dialog dialog = builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
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

