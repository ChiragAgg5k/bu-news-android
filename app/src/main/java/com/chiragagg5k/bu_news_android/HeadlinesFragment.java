package com.chiragagg5k.bu_news_android;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chiragagg5k.bu_news_android.adaptors.CategoriesRvAdaptor;
import com.chiragagg5k.bu_news_android.adaptors.NewsRvAdaptor;
import com.chiragagg5k.bu_news_android.objects.NewsObject;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HeadlinesFragment extends Fragment implements CategoriesRvAdaptor.CategoryClickListener {

    RecyclerView categories_rv, news_rv;
    ArrayList<String> category_names;
    CategoriesRvAdaptor categories_adaptor;
    NewsRvAdaptor news_adaptor;
    ProgressBar progressBar;
    ArrayList<NewsObject> uploadObjects;
    DatabaseReference databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;

    public HeadlinesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_headlines, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categories_rv = view.findViewById(R.id.categories_rv);
        news_rv = view.findViewById(R.id.news_rv_headlines);
        progressBar = view.findViewById(R.id.progress_bar_headlines);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);

        uploadObjects = new ArrayList<>();
        category_names = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        // Add all the categories to the category_names arraylist
        category_names.addAll(Arrays.asList(getResources().getStringArray(R.array.category_names)));

        categories_adaptor = new CategoriesRvAdaptor(category_names, getContext(), this);

        categories_rv.setAdapter(categories_adaptor);

        getNews("All"); // Inflating uploadObjects with general news by default
        news_adaptor = new NewsRvAdaptor(uploadObjects, getContext());

        news_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        news_rv.setAdapter(news_adaptor);
        news_adaptor.notifyDataSetChanged();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getNews("All");
            categories_rv.smoothScrollToPosition(0);
            categories_adaptor.setSelectedPosition(0);
            categories_adaptor.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * This method clears the uploadObjects arraylist and then adds all the news of the category passed
     *
     * @param category The category of news to be fetched
     */
    private void getNews(String category) {

        progressBar.setVisibility(View.VISIBLE);
        uploadObjects.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    NewsObject upload = postSnapShot.getValue(NewsObject.class);

                    assert upload != null;

                    if (upload.isAuthorized()) {
                        if (upload.getCategory().equals(category) || category.equals("All")) {
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

    /**
     * Called when a category is clicked
     *
     * @param position The position of the category clicked
     */
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