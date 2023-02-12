package com.chiragagg5k.bu_news_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sets appearance to light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_layout);

        ViewPager viewPager = findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
        super.onBackPressed();
    }
}

class AuthenticationPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentList = new ArrayList<>();

    public AuthenticationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }
}
