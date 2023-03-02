package com.chiragagg5k.bu_news_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    final int duration = 10;
    final int pixelsToMove = 30;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    String selectedCity = "Delhi";
    ImageView weatherIcon;
    TextView weatherDescriptionText, greetingText, greetingUserText, dateText, noSubscribedCategoriesText;
    RecyclerView newsRecyclerView;
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            newsRecyclerView.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };
    NewsRvAdaptor newsRvAdaptor;
    DatabaseReference databaseReference, userReference;
    ArrayList<NewsObject> uploadObjects;
    FirebaseUser user;
    Button subscribeButton;
    ProgressBar progressBar;
    String day, date, month;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Calendar calendar = Calendar.getInstance();
        day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, getResources().getConfiguration().locale);
        date = DateFormat.format("dd", calendar).toString();
        month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, getResources().getConfiguration().locale);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherDescriptionText = view.findViewById(R.id.weatherDescriptionText);
        greetingText = view.findViewById(R.id.greetingText);
        greetingUserText = view.findViewById(R.id.greetingUserText);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        newsRecyclerView = view.findViewById(R.id.news_rv_home);
        subscribeButton = view.findViewById(R.id.subscribeButton);
        progressBar = view.findViewById(R.id.progress_bar_home);
        progressBar.setVisibility(View.VISIBLE);
        noSubscribedCategoriesText = view.findViewById(R.id.noSubscribedNewsText);

        dateText = view.findViewById(R.id.dateText);
        dateText.setText(String.format("%s, %s %s", day, date, month));

        subscribeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SubscribeActivity.class);
            startActivity(intent);
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        userReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        uploadObjects = new ArrayList<>();
        newsRvAdaptor = new NewsRvAdaptor(uploadObjects, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setAdapter(newsRvAdaptor);


        // Horizontal scrolling of news
        newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastItem == linearLayoutManager.getItemCount() - 1) {
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(() -> {
                        newsRecyclerView.setAdapter(null);
                        newsRecyclerView.setAdapter(newsRvAdaptor);
                        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                    }, 2000);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);

        String displayName = user.getDisplayName();

        assert displayName != null;
        String firstName = displayName.split(" ")[0];
        String greeting = "";

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
        greetingUserText.setText(firstName);

        String tempUrl = OPEN_WEATHER_MAP_API_URL + "?q=" + selectedCity + "&appid=" + OPEN_WEATHER_MAP_API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        },
                error -> {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    Log.d("Error", Objects.requireNonNull(error.getMessage()));
                });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadObjects.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NewsObject news = dataSnapshot.getValue(NewsObject.class);

                    assert news != null;
                    if (news.isPromoted()) {
                        uploadObjects.add(news);
                    }
                }

                Collections.reverse(uploadObjects);
                newsRvAdaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("categories")) {
                    for (DataSnapshot dataSnapshot : snapshot.child("categories").getChildren()) {

                        String category = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        if (category.equals("true")) {
                            noSubscribedCategoriesText.setVisibility(View.GONE);
                            return;
                        }
                    }

                    noSubscribedCategoriesText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}