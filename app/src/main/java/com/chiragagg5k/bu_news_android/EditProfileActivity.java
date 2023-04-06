package com.chiragagg5k.bu_news_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView backBtn;
    FloatingActionButton edit_image_btn;
    Uri imageUri;
    FirebaseUser user;
    CircleImageView profile_image;
    Button saveBtn;
    TextView name, studentMail;
    EditText editName, editContact, editAddress;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String contact, address, username;

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
        editContact = findViewById(R.id.edit_contact_text);
        editAddress = findViewById(R.id.edit_address_text);
        studentMail = findViewById(R.id.student_mail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("name").getValue(String.class);
                contact = dataSnapshot.child("phoneNo").getValue(String.class);
                address = dataSnapshot.child("city").getValue(String.class);
                editAddress.setText(address);
                editContact.setText(contact);
                name.setText(username);
                editName.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileActivity", "onCancelled: ", databaseError.toException());
            }
        });

        if (user != null) {
            storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(profile_image);
            });
        }

        assert user != null;
        studentMail.setText(user.getEmail());

        backBtn.setOnClickListener(v -> onBackPressed());

        edit_image_btn.setOnClickListener(v -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        saveBtn.setOnClickListener(v -> {

            String name = UtilityClass.convertToTitleCase(editName.getText().toString().trim());
            String contact = editContact.getText().toString().trim();
            String address = UtilityClass.convertToTitleCase(editAddress.getText().toString().trim());

            if (imageUri == null && name.equals(this.username) && contact.equals(this.contact) && address.equals(this.address)) {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty()) {
                editName.setError("Name is required");
                editName.requestFocus();
                return;
            }

            if (imageUri != null) {
                StorageTask<UploadTask.TaskSnapshot> uploadTask = storageReference.child(user.getUid()).putFile(imageUri);
                uploadTask.addOnSuccessListener(o -> storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> imageUri = uri));
            }

            databaseReference.child(user.getUid()).child("name").setValue(name);
            databaseReference.child(user.getUid()).child("phoneNo").setValue(contact);
            databaseReference.child(user.getUid()).child("city").setValue(address);

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("name", name);
            if (imageUri != null)
                intent.putExtra("imageUri", imageUri.toString());

            finish();
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (imageUri != null || !editName.getText().toString().equals(username)) {
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

