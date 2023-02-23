package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class HomeFragment extends Fragment implements CategoriesRvAdaptor.CategoryClickListener{

    RecyclerView categories_rv,news_rv;
    ArrayList<String> category_names;
    CategoriesRvAdaptor categories_adaptor;
    NewsRvAdaptor news_adaptor;
    ProgressBar progressBar;
    ArrayList<UploadObject> uploadObjects;
    DatabaseReference databaseReference;

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
        news_rv = view.findViewById(R.id.news_rv_home);
        progressBar = view.findViewById(R.id.progress_bar_home);

        uploadObjects = new ArrayList<>();
        category_names = new ArrayList<>();

        category_names.add("General");
        category_names.add("Clubs Related");
        category_names.add("Events");
        category_names.add("Sports");


        categories_adaptor = new CategoriesRvAdaptor(category_names, getContext(), this);

        news_rv.setLayoutManager(new LinearLayoutManager(getContext()));


        categories_rv.setAdapter(categories_adaptor);

        getNews("General" );
        news_adaptor = new NewsRvAdaptor(uploadObjects, getContext());
        news_rv.setAdapter(news_adaptor);
        news_adaptor.notifyDataSetChanged();

    }

    private void getNews(String category) {
        progressBar.setVisibility(View.VISIBLE);
        uploadObjects.clear();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    UploadObject upload = postSnapShot.getValue(UploadObject.class);

                    assert upload != null;

                    if (upload.isAuthorized()) {
                        if (upload.getCategory().equals(category) || category.equals("General")) {
                            uploadObjects.add(upload);
                        }
                    }
                }

                Collections.reverse(uploadObjects);

                progressBar.setVisibility(View.GONE);
                news_adaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = category_names.get(position);
        getNews(category);
        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .repeat(0)
                .playOn(news_rv);
    }
}