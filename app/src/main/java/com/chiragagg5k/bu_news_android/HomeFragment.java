package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView categories_rv, news_rv;
    ArrayList<String> category_names;
    LinearLayoutManager linearLayoutManager_HORIZONTAL, linearLayoutManager_VERTICAL;
    CategoriesRvAdaptor categories_adaptor;
    NewsRvAdaptor news_adaptor;
    List<UploadObject> uploadObjects;
    DatabaseReference databaseReference;
    TextView loadingTV;

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
        news_rv = view.findViewById(R.id.news_rv);
        loadingTV = view.findViewById(R.id.loading_tv);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            loadingTV.setText("No news found");
        }, 4000);

        uploadObjects = new ArrayList<>();

        categories_rv.setHasFixedSize(true);

        category_names = new ArrayList<>();
        category_names.add("All");
        category_names.add("Sports");
        category_names.add("Entertainment");
        category_names.add("Technology");
        category_names.add("Clubs Related");

        linearLayoutManager_VERTICAL = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager_HORIZONTAL = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        categories_adaptor = new CategoriesRvAdaptor(category_names);
        categories_rv.setLayoutManager(linearLayoutManager_HORIZONTAL);
        categories_rv.setAdapter(categories_adaptor);

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadObjects.clear();

                for(DataSnapshot postSnapShot : snapshot.getChildren()){
                    UploadObject upload = postSnapShot.getValue(UploadObject.class);

                    assert upload != null;

                    if (upload.isAuthorized()) {
                        uploadObjects.add(upload);
                    }
                }

                news_adaptor = new NewsRvAdaptor(uploadObjects);
                news_rv.setLayoutManager(linearLayoutManager_VERTICAL);
                news_rv.setAdapter(news_adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}