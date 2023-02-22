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

    RecyclerView categories_rv;
    ArrayList<String> category_names;
    LinearLayoutManager linearLayoutManager_HORIZONTAL;
    CategoriesRvAdaptor categories_adaptor;

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

        categories_rv = view.findViewById(R.id.categories_rv);

        categories_rv.setHasFixedSize(true);

        category_names = new ArrayList<>();
        category_names.add("General");
        category_names.add("Clubs Related");
        category_names.add("Events");
        category_names.add("Sports");

        linearLayoutManager_HORIZONTAL = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        categories_adaptor = new CategoriesRvAdaptor(category_names);
        categories_rv.setLayoutManager(linearLayoutManager_HORIZONTAL);
        categories_rv.setAdapter(categories_adaptor);

    }

}