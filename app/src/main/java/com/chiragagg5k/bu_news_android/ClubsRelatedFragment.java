package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubsRelatedFragment extends Fragment {


    DatabaseReference databaseReference;
    NewsRvAdaptor news_adaptor;
    List<UploadObject> uploadObjects;
    RecyclerView clubs_news_rv;
    TextView clubs_loading_tv;

    public ClubsRelatedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clubs_related, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clubs_news_rv = view.findViewById(R.id.clubs_news_rv);
        clubs_loading_tv = view.findViewById(R.id.clubs_loading_tv);

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        uploadObjects = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadObject uploadObject = postSnapshot.getValue(UploadObject.class);
                    assert uploadObject != null;
                    if (uploadObject.getCategory().equals("Clubs Related")) {
                        uploadObjects.add(uploadObject);
                    }
                }
                news_adaptor = new NewsRvAdaptor(uploadObjects);
                clubs_news_rv.setAdapter(news_adaptor);
                clubs_loading_tv.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}