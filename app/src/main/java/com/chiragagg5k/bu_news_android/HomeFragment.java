package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView rv;
    ArrayList<String> category_names;
    LinearLayoutManager linearLayoutManager;
    CategoriesRvAdaptor adaptor;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.categories_rv);
        category_names = new ArrayList<>();
        category_names.add("Sports");
        category_names.add("Entertainment");
        category_names.add("Politics");
        category_names.add("Technology");

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adaptor = new CategoriesRvAdaptor(category_names);

        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adaptor);
    }
}