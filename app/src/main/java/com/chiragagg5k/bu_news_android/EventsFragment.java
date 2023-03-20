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

import com.chiragagg5k.bu_news_android.objects.EventsObject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventsFragment extends Fragment {

    RecyclerView eventsRv;

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

        List<EventsObject> eventsObjects = new ArrayList<>();
        eventsObjects.add(new EventsObject("Event 1", "Event 1 Description", LocalDate.now().plus(1, ChronoUnit.DAYS)));
        eventsObjects.add(new EventsObject("Event 2", "Event 2 Description", LocalDate.now().plus(2, ChronoUnit.DAYS)));
        eventsObjects.add(new EventsObject("Event 3", "Event 3 Description", LocalDate.now().plus(3, ChronoUnit.DAYS)));
        eventsObjects.add(new EventsObject("Event 4", "Event 4 Description", LocalDate.now().plus(0, ChronoUnit.DAYS)));

        List<EventsObject> sortedEventsObjects = eventsObjects.stream()
                .sorted(Comparator.comparing(EventsObject::getEventLocalDate))
                .collect(Collectors.toList());

        EventsRvAdaptor eventsRvAdaptor = new EventsRvAdaptor(sortedEventsObjects, getContext());
        eventsRv.setAdapter(eventsRvAdaptor);
        eventsRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}