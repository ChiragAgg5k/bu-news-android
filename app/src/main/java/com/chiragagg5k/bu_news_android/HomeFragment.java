package com.chiragagg5k.bu_news_android;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private final String OPEN_WEATHER_MAP_API_KEY = "58ed8b67f0af15587b5f4f88b3457b15";
    private final String OPEN_WEATHER_MAP_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    String selectedCity = "Delhi";

    ImageView weatherIcon;
    TextView weatherDescriptionText, greetingText;
    RecyclerView newsRecyclerView;
    NewsRvAdaptor newsRvAdaptor;
    DatabaseReference databaseReference;
    ArrayList<UploadObject> uploadObjects;
    FirebaseUser user;

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

        weatherDescriptionText = view.findViewById(R.id.weatherDescriptionText);
        greetingText = view.findViewById(R.id.greetingText);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        newsRecyclerView = view.findViewById(R.id.news_rv_home);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        uploadObjects = new ArrayList<>();
        newsRvAdaptor = new NewsRvAdaptor(uploadObjects,getContext());
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        newsRecyclerView.setAdapter(newsRvAdaptor);


        String displayName = user.getDisplayName();
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


        greetingText.setText(greeting + "\n" + firstName);

        String tempUrl = OPEN_WEATHER_MAP_API_URL + "?q=" + selectedCity + "&appid=" + OPEN_WEATHER_MAP_API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

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

                    weatherDescriptionText.setText(description + "\n" + temp + "°C");
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadObjects.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UploadObject news = dataSnapshot.getValue(UploadObject.class);
                    if (news.isPromoted()) {
                        uploadObjects.add(news);
                    }
                }
                Collections.reverse(uploadObjects);
                newsRvAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}