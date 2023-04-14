package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.adaptors.EventsRvAdaptor;
import com.chiragagg5k.bu_news_android.objects.EventsObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventsFragment extends Fragment {

    RecyclerView eventsRv;
    List<EventsObject> eventsObjects;
    Button addEventBtn;
    DatabaseReference userRef, eventsRef;
    FirebaseUser user;
    ProgressBar progressBar;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsRv = view.findViewById(R.id.events_rv);
        addEventBtn = view.findViewById(R.id.add_event_btn);
        progressBar = view.findViewById(R.id.events_progress_bar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventsObjects = new ArrayList<>();
        setEventsRv();

        if (user != null) {
            checkAndEnableAddEventBtn();
        } else {
            addEventBtn.setVisibility(View.GONE);
        }

        addEventBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AddEventActivity.class);
            startActivity(intent);
        });

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsObjects.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EventsObject eventsObject = dataSnapshot.getValue(EventsObject.class);
                    eventsObjects.add(eventsObject);
                }
                setEventsRv();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEventsRv() {

        // Sort the events by date
        eventsObjects.sort((o1, o2) -> {
            long o1Date = o1.getEventDate();
            long o2Date = o2.getEventDate();
            return Long.compare(o1Date, o2Date);
        });

        EventsRvAdaptor eventsRvAdaptor = new EventsRvAdaptor(eventsObjects, getContext());
        eventsRv.setAdapter(eventsRvAdaptor);
        eventsRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void checkAndEnableAddEventBtn() {
        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAdmin = Boolean.parseBoolean(Objects.requireNonNull(snapshot.child("admin").getValue()).toString());
                if (isAdmin)
                    addEventBtn.setVisibility(View.VISIBLE);
                else
                    addEventBtn.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}