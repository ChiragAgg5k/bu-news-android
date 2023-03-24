package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.adaptors.EventsRvAdaptor;
import com.chiragagg5k.bu_news_android.objects.EventsObject;
import com.chiragagg5k.bu_news_android.objects.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment {

    RecyclerView eventsRv;
    List<EventsObject> eventsObjects;
    Button addEventBtn;
    DatabaseReference userRef;
    FirebaseUser user;

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isAdmin = Boolean.parseBoolean(snapshot.child("admin").getValue().toString());
                if(isAdmin)
                    addEventBtn.setVisibility(View.VISIBLE);
                else
                    addEventBtn.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        eventsObjects = new ArrayList<>();
        long today = Calendar.getInstance().getTimeInMillis();
        eventsObjects.add(new EventsObject("Event 1", "This is the description of the event", UtilityClass.getDate(today)));
        eventsObjects.add(new EventsObject("Event 2", "This is the description of the event", UtilityClass.getDate(today)));
        eventsObjects.add(new EventsObject("Event 3", "This is the description of the event", UtilityClass.getDate(today)));

        EventsRvAdaptor eventsRvAdaptor = new EventsRvAdaptor(eventsObjects, getContext());
        eventsRv.setAdapter(eventsRvAdaptor);
        eventsRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}