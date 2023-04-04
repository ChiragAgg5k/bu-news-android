package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseUser user;
    ImageView back_button;
    TextView profile_name, full_name_text, email_text, contact_text, address_text;
    Button edit_profile_button;
    DatabaseReference databaseRef;
    StorageReference storageRef;

    CircleImageView profile_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        profile_name = findViewById(R.id.profile_name);
        full_name_text = findViewById(R.id.full_name_text);
        email_text = findViewById(R.id.email_text);
        contact_text = findViewById(R.id.contact_text);
        address_text = findViewById(R.id.address_text);
        profile_image = findViewById(R.id.profile_image);

        back_button = findViewById(R.id.back_button);
        edit_profile_button = findViewById(R.id.edit_profile_button);

        back_button.setOnClickListener(v -> finish());
        edit_profile_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        if (user == null) {
            edit_profile_button.setEnabled(false);
            edit_profile_button.setBackgroundColor(getResources().getColor(R.color.backgroundColorDarker));
            return;
        }

        profile_name.setText(user.getDisplayName());
        full_name_text.setText(user.getDisplayName());
        email_text.setText(user.getEmail());

        databaseRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String contact = dataSnapshot.child("phoneNo").getValue(String.class);
                String address = dataSnapshot.child("city").getValue(String.class);
                contact_text.setText(contact);
                address_text.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileActivity", "onCancelled: ", databaseError.toException());
            }
        });

        if (user != null) {
            storageRef = FirebaseStorage.getInstance().getReference("profile_images");
            storageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(profile_image);
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;

        Uri profile_image_uri = data.getData();
        profile_image.setImageURI(profile_image_uri);
    }
}
