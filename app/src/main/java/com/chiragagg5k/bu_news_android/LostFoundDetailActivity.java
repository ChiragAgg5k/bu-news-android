package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class LostFoundDetailActivity extends AppCompatActivity {

    ImageView backBtn, detailedLostFound_Image;
    TextView detailedLostFound_ItemName, detailedLostFound_ItemDescription, detailedLostFound_UploaderName, detailedLostFound_UploaderContact, detailedLostFound_ItemLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_lostfound);

        backBtn = findViewById(R.id.back_button);
        detailedLostFound_ItemName = findViewById(R.id.detailedLostFound_ItemName);
        detailedLostFound_ItemDescription = findViewById(R.id.detailedLostFound_ItemDescription);
        detailedLostFound_UploaderName = findViewById(R.id.detailedLostFound_UploaderName);
        detailedLostFound_UploaderContact = findViewById(R.id.detailedLostFound_UploaderContact);
        detailedLostFound_Image = findViewById(R.id.detailedLostFound_Image);
        detailedLostFound_ItemLocation = findViewById(R.id.detailedLostFound_ItemLocation);

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("item_name");
        String itemDescription = intent.getStringExtra("item_description");
        String itemLocation = intent.getStringExtra("item_location");
        String itemImageURL = intent.getStringExtra("item_image_url");
        String uploaderUID = intent.getStringExtra("uploader_uid");
        String uploaderContact = intent.getStringExtra("uploader_contact");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uploaderUID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uploaderName = snapshot.child("name").getValue().toString();
                detailedLostFound_UploaderName.setText("Uploaded by: " + uploaderName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        detailedLostFound_ItemName.setText(itemName);
        detailedLostFound_ItemDescription.setText("Item Description: " + itemDescription);
        detailedLostFound_UploaderContact.setText("Contact: " + uploaderContact);
        detailedLostFound_ItemLocation.setText("Lost known location : " + itemLocation);

        Picasso.get().load(itemImageURL).into(detailedLostFound_Image);

        backBtn.setOnClickListener(v -> finish());
    }
}

