package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.adaptors.LostFoundAdaptor;
import com.chiragagg5k.bu_news_android.objects.LostFoundObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LostFragment extends Fragment {

    RecyclerView lostRecyclerView;
    TextView lostTextView;
    DatabaseReference lostDatabaseReference;
    ArrayList<LostFoundObject> lostFoundObjects;
    FirebaseUser user;
    ProgressBar progressBar;

    public LostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();

        lostRecyclerView = view.findViewById(R.id.lostRecyclerView);
        lostTextView = view.findViewById(R.id.lostTextView);
        progressBar = view.findViewById(R.id.lostProgressBar);
        lostDatabaseReference = FirebaseDatabase.getInstance().getReference("lost_found").child("lost");

        if (user != null) {
            setClickHereListener();
        } else {
            lostTextView.setText("Lost Something? Register to upload a post");
        }

        lostFoundObjects = new ArrayList<>();

        lostDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lostFoundObjects.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LostFoundObject lostFoundObject = dataSnapshot.getValue(LostFoundObject.class);
                    lostFoundObjects.add(lostFoundObject);
                }

                LostFoundAdaptor lostFoundAdaptor = new LostFoundAdaptor(lostFoundObjects, true, getContext());
                lostRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                lostRecyclerView.setAdapter(lostFoundAdaptor);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setClickHereListener() {
        String lostTextLabel = "Lost Something? Upload a lost post Here";
        int lostTextLabelLength = lostTextLabel.length();
        SpannableString spannableString = new SpannableString(lostTextLabel);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getContext(), LostFoundUploadActivity.class);
                intent.putExtra("isLost", true);
                startActivity(intent);

            }
        }, lostTextLabelLength - 4, lostTextLabelLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        lostTextView.setText(spannableString);
        lostTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}