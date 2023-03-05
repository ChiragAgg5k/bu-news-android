package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.Objects;

/**
 * Apologies for so much repeated code. I know it's not the best way to do it, but I am lazy.
 */
public class SubscribeActivity extends AppCompatActivity {

    ImageView backBtn;
    CheckBox allCheckBox, generalCheckBox, sportsCheckBox, eventsCheckBox, clubRelatedCheckBox;
    Button subscribeBtn;
    FirebaseUser user;
    DatabaseReference databaseReference;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        backBtn = findViewById(R.id.back_button);
        subscribeBtn = findViewById(R.id.subscribe_button);

        allCheckBox = findViewById(R.id.subscribe_all);
        generalCheckBox = findViewById(R.id.subscribe_general);
        sportsCheckBox = findViewById(R.id.subscribe_sports);
        eventsCheckBox = findViewById(R.id.subscribe_events);
        clubRelatedCheckBox = findViewById(R.id.subscribe_club_related);

        databaseReference.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("General")) {
                    generalCheckBox.setChecked(Objects.requireNonNull(dataSnapshot.child("General").getValue()).toString().equals("true"));
                }

                if (dataSnapshot.hasChild("Sports")) {
                    sportsCheckBox.setChecked(Objects.requireNonNull(dataSnapshot.child("Sports").getValue()).toString().equals("true"));
                }

                if (dataSnapshot.hasChild("Event")) {
                    eventsCheckBox.setChecked(Objects.requireNonNull(dataSnapshot.child("Event").getValue()).toString().equals("true"));
                }

                if (dataSnapshot.hasChild("Clubs Related")) {
                    clubRelatedCheckBox.setChecked(Objects.requireNonNull(dataSnapshot.child("Clubs Related").getValue()).toString().equals("true"));
                }

                allCheckBox.setChecked(generalCheckBox.isChecked() && sportsCheckBox.isChecked() && eventsCheckBox.isChecked() && clubRelatedCheckBox.isChecked());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        allCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                generalCheckBox.setChecked(true);
                sportsCheckBox.setChecked(true);
                eventsCheckBox.setChecked(true);
                clubRelatedCheckBox.setChecked(true);
            } else {
                generalCheckBox.setChecked(false);
                sportsCheckBox.setChecked(false);
                eventsCheckBox.setChecked(false);
                clubRelatedCheckBox.setChecked(false);
            }
        });

        generalCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (sportsCheckBox.isChecked() && eventsCheckBox.isChecked() && clubRelatedCheckBox.isChecked()) {
                    allCheckBox.setChecked(true);
                }
            } else {
                allCheckBox.setChecked(false);
            }
        });

        sportsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (generalCheckBox.isChecked() && eventsCheckBox.isChecked() && clubRelatedCheckBox.isChecked()) {
                    allCheckBox.setChecked(true);
                }
            } else {
                allCheckBox.setChecked(false);
            }
        });

        eventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (generalCheckBox.isChecked() && sportsCheckBox.isChecked() && clubRelatedCheckBox.isChecked()) {
                    allCheckBox.setChecked(true);
                }
            } else {
                allCheckBox.setChecked(false);
            }
        });

        clubRelatedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (generalCheckBox.isChecked() && sportsCheckBox.isChecked() && eventsCheckBox.isChecked()) {
                    allCheckBox.setChecked(true);
                }
            } else {
                allCheckBox.setChecked(false);
            }
        });

        backBtn.setOnClickListener(v -> finish());

        subscribeBtn.setOnClickListener(v -> subscribe());

    }

    private void subscribe() {

        subscribeBtn.setText(R.string.subscribing);
        subscribeBtn.setEnabled(false);

        handler.postDelayed(() -> {
            Toast.makeText(this, "Subscribed Successfully", Toast.LENGTH_SHORT).show();

            subscribeBtn.setText(R.string.subscribe);
            subscribeBtn.setEnabled(true);
            finish();
        }, 1000);

        if (allCheckBox.isChecked()) {
            databaseReference.child("categories").child("General").setValue(true);
            databaseReference.child("categories").child("Sports").setValue(true);
            databaseReference.child("categories").child("Event").setValue(true);
            databaseReference.child("categories").child("Clubs Related").setValue(true);
        } else {
            databaseReference.child("categories").child("General").setValue(generalCheckBox.isChecked());
            databaseReference.child("categories").child("Sports").setValue(sportsCheckBox.isChecked());
            databaseReference.child("categories").child("Event").setValue(eventsCheckBox.isChecked());
            databaseReference.child("categories").child("Clubs Related").setValue(clubRelatedCheckBox.isChecked());
        }
    }
}

