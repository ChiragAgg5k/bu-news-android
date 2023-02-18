package com.chiragagg5k.bu_news_android;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    ImageView profileImage;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton postButton;
    DrawerLayout drawerLayout;
    NavigationView sideNavigationView;
    TextView sideNavUsername;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profileImage = findViewById(R.id.profile_icon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        sideNavigationView = findViewById(R.id.side_nav_view);
        postButton = findViewById(R.id.post_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        sideNavUsername = sideNavigationView.getHeaderView(0).findViewById(R.id.side_nav_username);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            sideNavUsername.setText(user.getDisplayName());
        }

        // disabling middle icon
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        sideNavigationView.setNavigationItemSelectedListener(this::sideNavigationItemSelected);

        loadFragment(new HomeFragment());

        profileImage.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        postButton.setOnClickListener(v -> {
            bottomNavigationView.setSelectedItemId(R.id.placeholder);
            YoYo.with(Techniques.Tada)
                    .duration(1000)
                    .repeat(0)
                    .playOn(postButton);
            loadFragment(new PostFragment());
        });
    }

    private boolean sideNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AuthenticationActivity.class));

        } else if (menuItem.getItemId() == R.id.about) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;

        if (menuItem.getItemId() == R.id.home)
            selectedFragment = new HomeFragment();
        else if (menuItem.getItemId() == R.id.events)
            selectedFragment = new EventsFragment();
        else if (menuItem.getItemId() == R.id.helpdesk)
            selectedFragment = new HelpDeskFragment();
        else if (menuItem.getItemId() == R.id.lost_found)
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
