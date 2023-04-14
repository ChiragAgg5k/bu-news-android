package com.chiragagg5k.bu_news_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chiragagg5k.bu_news_android.adaptors.NewsRvAdaptor;
import com.chiragagg5k.bu_news_android.adaptors.TopNewsRvAdaptor;
import com.chiragagg5k.bu_news_android.objects.NewsObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

public class HomeFragment extends Fragment {

    final String OPEN_WEATHER_MAP_API_KEY = "58ed8b67f0af15587b5f4f88b3457b15";
    final String OPEN_WEATHER_MAP_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    String selectedCity = "", greeting;
    ImageView weatherIcon;
    TextView weatherDescriptionText, greetingText, greetingUserText, dateText, noSubscribedCategoriesText;
    RecyclerView promotedRecyclerView, subscribedRecyclerView;
    SnapHelper snapHelper;
    boolean hasSubscribedCategories = false;
    TopNewsRvAdaptor promotedNewsRvAdaptor;
    NewsRvAdaptor subscribedNewsRvAdaptor;
    DatabaseReference databaseReference, userReference;
    ArrayList<NewsObject> promotedNewsObjects, subscribedNewsObjects;
    FirebaseUser user;
    Button subscribeButton;
    ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherDescriptionText = view.findViewById(R.id.weatherDescriptionText);
        greetingText = view.findViewById(R.id.greetingText);
        greetingUserText = view.findViewById(R.id.greetingUserText);

        weatherIcon = view.findViewById(R.id.weatherIcon);

        promotedRecyclerView = view.findViewById(R.id.news_rv_home);
        subscribedRecyclerView = view.findViewById(R.id.subscribedNews_rv_home);

        subscribeButton = view.findViewById(R.id.subscribeButton);
        progressBar = view.findViewById(R.id.progress_bar_home);
        progressBar.setVisibility(View.VISIBLE);
        noSubscribedCategoriesText = view.findViewById(R.id.noSubscribedNewsText);

        dateText = view.findViewById(R.id.dateText);
        dateText.setText(UtilityClass.getDate(Calendar.getInstance().getTimeInMillis()));

        subscribeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SubscribeActivity.class);
            startActivity(intent);
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        if (user != null) {
            Log.d("HomeFragment", "onViewCreated: " + user.getUid());
            userReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("name").getValue(String.class);
                    String firstName;
                    if (username != null)
                        firstName = username.split(" ")[0];
                    else firstName = "User";

                    greetingUserText.setText(firstName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            greetingUserText.setText("User");
        }

        promotedNewsObjects = new ArrayList<>();
        subscribedNewsObjects = new ArrayList<>();

        promotedNewsRvAdaptor = new TopNewsRvAdaptor(promotedNewsObjects, getContext());
        subscribedNewsRvAdaptor = new NewsRvAdaptor(subscribedNewsObjects, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        subscribedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        promotedRecyclerView.setLayoutManager(linearLayoutManager);

        promotedRecyclerView.setAdapter(promotedNewsRvAdaptor);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(promotedRecyclerView);

        subscribedRecyclerView.setAdapter(subscribedNewsRvAdaptor);

        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);

        if (hour < 12) {
            greeting = "Good Morning";
        } else if (hour < 16) {
            greeting = "Good Afternoon";
        } else if (hour < 21) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }

        greetingText.setText(greeting);

        if (user != null) {
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    selectedCity = snapshot.child("city").getValue(String.class);
                    try {
                        fetchWeather(selectedCity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    subscribedNewsObjects.clear();
                    hasSubscribedCategories = false;

                    ArrayList<String> subscribedCategories = new ArrayList<>();

                    if (snapshot.hasChild("categories")) {
                        for (DataSnapshot dataSnapshot : snapshot.child("categories").getChildren()) {

                            boolean value = (boolean) Objects.requireNonNull(dataSnapshot.getValue());
                            String key = dataSnapshot.getKey();

                            if (value) {
                                subscribedCategories.add(key);
                                hasSubscribedCategories = true;
                                getNews(key);
                            }
                        }
                    }

                    if (!hasSubscribedCategories) {
                        subscribedRecyclerView.setVisibility(View.GONE);
                        noSubscribedCategoriesText.setText(R.string.no_sub_news);
                    } else {
                        subscribedRecyclerView.setVisibility(View.VISIBLE);
                        noSubscribedCategoriesText.setText(String.format("You have subscribed to the following categories: %s", UtilityClass.arrayListToString(subscribedCategories)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            noSubscribedCategoriesText.setText("Register to subscribe to news categories");
            weatherDescriptionText.setText(R.string.weather_unavailable);
            subscribeButton.setEnabled(false);
            subscribeButton.setBackgroundColor(getResources().getColor(R.color.backgroundColorDarker));
        }


        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promotedNewsObjects.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NewsObject news = dataSnapshot.getValue(NewsObject.class);

                    assert news != null;
                    if (news.isPromoted()) {
                        promotedNewsObjects.add(news);
                    }
                }

                Collections.reverse(promotedNewsObjects);
                promotedNewsRvAdaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getNews(String category) {

        progressBar.setVisibility(View.VISIBLE);
        subscribedNewsObjects.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    NewsObject upload = postSnapShot.getValue(NewsObject.class);

                    assert upload != null;

                    if (upload.isAuthorized()) {
                        if (upload.getCategory().equals(category) || category.equals("All")) {
                            subscribedNewsObjects.add(upload);
                        }
                    }
                }

                Collections.reverse(subscribedNewsObjects);

                progressBar.setVisibility(View.GONE);
                subscribedNewsRvAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void fetchWeather(String selectedCity) {
        String tempUrl = OPEN_WEATHER_MAP_API_URL + "?q=" + selectedCity + "&appid=" + OPEN_WEATHER_MAP_API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObject = jsonArray.getJSONObject(0);

                String description = weatherObject.getString("description");

                JSONObject mainObject = jsonObject.getJSONObject("main");

                double temp = mainObject.getDouble("temp") - 273.15;
                String icon = weatherObject.getString("icon");
                String iconUrl = "https://openweathermap.org/img/w/" + icon + ".png";

                temp = Math.round(temp * 100.0) / 100.0;

                weatherDescriptionText.setText(String.format("%s\n%s°C", description, temp));
                Picasso.get().load(iconUrl).into(weatherIcon);


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        },
                error -> weatherDescriptionText.setText(R.string.weather_unavailable));

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}