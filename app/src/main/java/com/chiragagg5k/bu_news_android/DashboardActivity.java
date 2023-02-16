package com.chiragagg5k.bu_news_android;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    ImageView profileImage;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton postButton;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profileImage = findViewById(R.id.profile_icon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        postButton = findViewById(R.id.post_button);

        // disabling middle icon
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        loadFragment(new HomeFragment());

        profileImage.setOnClickListener(v -> {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(0)
                    .playOn(profileImage);
            startActivity(new Intent(this, ProfileActivity.class));
        });

        postButton.setOnClickListener(v -> {
            bottomNavigationView.setSelectedItemId(R.id.placeholder);
            YoYo.with(Techniques.Tada)
                    .duration(1000)
                    .repeat(0)
                    .playOn(postButton);
                loadFragment(new PostFragment());
        });
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;

        if(menuItem.getItemId() == R.id.home)
            selectedFragment = new HomeFragment();
        else if(menuItem.getItemId() == R.id.events)
            selectedFragment = new EventsFragment();
        else if(menuItem.getItemId() == R.id.helpdesk)
            selectedFragment = new HelpDeskFragment();
        else if(menuItem.getItemId() == R.id.lost_found)
            selectedFragment = new LostFoundFragment();

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        return true;
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
