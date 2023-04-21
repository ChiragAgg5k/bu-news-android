package com.chiragagg5k.bu_news_android;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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

import org.jetbrains.annotations.NotNull;

/**
 * @author Chirag Aggarwal
 */
public class DashboardActivity extends AppCompatActivity {

    final String GITHUB_URL = "https://github.com/ChiragAgg5k/bu-news-android";
    final String WEBSITE_URL = "https://bu-news.netlify.app/";
    ImageView menuIcon, sideNavProfileImage;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton postButton;
    DrawerLayout drawerLayout;
    NavigationView sideNavigationView;
    TextView sideNavUsername, buHeadline;
    FirebaseUser user;
    Intent intent;
    Uri imageUri;
    DatabaseReference userDatabaseRef;
    StorageReference userRef;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        menuIcon = findViewById(R.id.menu_icon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        sideNavigationView = findViewById(R.id.side_nav_view);
        postButton = findViewById(R.id.post_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        buHeadline = findViewById(R.id.bu_news_healine);
        sideNavUsername = sideNavigationView.getHeaderView(0).findViewById(R.id.side_nav_username);
        sideNavProfileImage = sideNavigationView.getHeaderView(0).findViewById(R.id.side_nav_profile_image);

        intent = getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        if (intent.getStringExtra("imageUri") != null) {
            imageUri = Uri.parse(intent.getStringExtra("imageUri"));
            Picasso.get().load(imageUri).into(sideNavProfileImage);

        } else if (user != null) {
            userRef = FirebaseStorage.getInstance().getReference("profile_images");
            userRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(sideNavProfileImage);
            });
        }

        if (intent.getStringExtra("name") != null)
            sideNavUsername.setText(intent.getStringExtra("name"));
        else if (user != null) {
            userDatabaseRef.child(user.getUid()).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null)
                        sideNavUsername.setText(snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        } else {
            sideNavUsername.setText("User");
        }

        // disabling middle icon
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        // long press listener for bottom navigation
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            View view = bottomNavigationView.findViewById(menuItem.getItemId());
            view.setOnLongClickListener(v -> {
                Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });
        }

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        sideNavigationView.setNavigationItemSelectedListener(this::sideNavigationItemSelected);

        loadFragment(new HomeFragment());

        buHeadline.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(WEBSITE_URL));
            startActivity(intent);
        });

        menuIcon.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        postButton.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            bottomNavigationView.setSelectedItemId(R.id.placeholder);
            YoYo.with(Techniques.Tada)
                    .duration(1000)
                    .repeat(0)
                    .playOn(postButton);

            if (user == null) {
                Toast.makeText(this, "You need to register to post news!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!(getSupportFragmentManager().findFragmentById(R.id.relativeLayout) instanceof PostFragment))
                loadFragment(new PostFragment());
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().findFragmentById(R.id.relativeLayout) instanceof PostFragment) {
                    loadFragment(new HomeFragment());
                    bottomNavigationView.setSelectedItemId(R.id.home);
                } else {

                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return;
                    }

                    if (user == null) {
                        startActivity(new Intent(DashboardActivity.this, AuthenticationActivity.class));
                        finish();
                        return;
                    }

                    new AlertDialog.Builder(DashboardActivity.this)
                            .setTitle("Exit")
                            .setMessage("Are you sure you want to exit?")
                            .setPositiveButton("Yes", (dialog, which) -> finishAffinity())
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private boolean sideNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.nav_logout) {

            if (user != null) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }

            finish();
            startActivity(new Intent(this, AuthenticationActivity.class));

        } else if (menuItem.getItemId() == R.id.about) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(GITHUB_URL));
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.website) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(WEBSITE_URL));
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.contact_side_nav) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;

        if (menuItem.getItemId() == R.id.headlines) {
            if (getSupportFragmentManager().findFragmentById(R.id.relativeLayout) instanceof HeadlinesFragment)
                return true;

            selectedFragment = new HeadlinesFragment();

        } else if (menuItem.getItemId() == R.id.events) {
            if (getSupportFragmentManager().findFragmentById(R.id.relativeLayout) instanceof EventsFragment)
                return true;

            selectedFragment = new EventsFragment();

        } else if (menuItem.getItemId() == R.id.home) {
            if (getSupportFragmentManager().findFragmentById(R.id.relativeLayout) instanceof HomeFragment)
                return true;

            selectedFragment = new HomeFragment();
        } else if (menuItem.getItemId() == R.id.lost_found) {
            if (getSupportFragmentManager().findFragmentById(R.id.relativeLayout) instanceof LostFoundFragment)
                return true;

            selectedFragment = new LostFoundFragment();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.relativeLayout, fragment);
        transaction.commit();
    }
}
