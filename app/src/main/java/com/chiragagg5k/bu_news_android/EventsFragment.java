package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.objects.EventsObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EventsFragment extends Fragment {

    RecyclerView eventsRv;
    DatabaseReference eventsRef;
    List<EventsObject> eventsObjects;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);

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
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventsObjects = new ArrayList<>();
        eventsObjects.add(new EventsObject("Event 1", "Event 1 description", UtilityClass.getDate(LocalDate.now())));
        eventsObjects.add(new EventsObject("Event 2", "Event 2 description", UtilityClass.getDate(LocalDate.now().plusDays(1))));
        eventsObjects.add(new EventsObject("Event 3", "Event 3 description", UtilityClass.getDate(LocalDate.now().plusDays(2))));

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("EventsFragment", "onDataChange: " + snapshot);
                eventsObjects.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EventsObject eventsObject = dataSnapshot.getValue(EventsObject.class);
                    eventsObjects.add(eventsObject);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        EventsRvAdaptor eventsRvAdaptor = new EventsRvAdaptor(eventsObjects, getContext());
        eventsRv.setAdapter(eventsRvAdaptor);
        eventsRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}