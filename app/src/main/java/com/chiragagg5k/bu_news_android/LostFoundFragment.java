package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chiragagg5k.bu_news_android.adaptors.LostFoundViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class LostFoundFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public LostFoundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost_found, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.lostFoundViewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Lost"));
        tabLayout.addTab(tabLayout.newTab().setText("Found"));

        tabLayout.setupWithViewPager(viewPager);
        prepareViewPager(viewPager);
    }

    private void prepareViewPager(ViewPager viewPager) {
        LostFoundViewPagerAdapter adapter = new LostFoundViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LostFragment(), "Lost");
        adapter.addFragment(new FoundFragment(), "Found");
        viewPager.setAdapter(adapter);
    }
}