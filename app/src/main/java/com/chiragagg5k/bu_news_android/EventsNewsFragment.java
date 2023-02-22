package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EventsNewsFragment extends Fragment {


    DatabaseReference databaseReference;
    NewsRvAdaptor news_adaptor;
    ArrayList<UploadObject> uploadObjects;
    RecyclerView events_rv;
    TextView loading_tv;

    public EventsNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        events_rv = view.findViewById(R.id.events_news_rv);
        loading_tv = view.findViewById(R.id.loading_tv);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading_tv.setText("No Events News Found");
            }
        }, 4000);

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        uploadObjects = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadObjects.clear();

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    UploadObject upload = postSnapShot.getValue(UploadObject.class);

                    assert upload != null;

                    if (upload.isAuthorized()) {
                        if (upload.getCategory().equals("Events")) {
                            uploadObjects.add(upload);
                        }
                    }
                }

                news_adaptor = new NewsRvAdaptor(uploadObjects);
                events_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                events_rv.setAdapter(news_adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}