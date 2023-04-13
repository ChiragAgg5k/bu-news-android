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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragagg5k.bu_news_android.adaptors.LostFoundAdaptor;
import com.chiragagg5k.bu_news_android.objects.LostFoundObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoundFragment extends Fragment {

    TextView foundTextView;
    RecyclerView foundRecyclerView;
    ArrayList<LostFoundObject> lostFoundObjects;
    DatabaseReference foundDatabaseReference;


    public FoundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_found, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foundRecyclerView = view.findViewById(R.id.foundRecyclerView);
        foundTextView = view.findViewById(R.id.foundTextView);
        foundDatabaseReference = FirebaseDatabase.getInstance().getReference("lost_found").child("found");

        setClickHereListener();

        lostFoundObjects = new ArrayList<>();

        foundDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lostFoundObjects.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LostFoundObject lostFoundObject = dataSnapshot.getValue(LostFoundObject.class);
                    lostFoundObjects.add(lostFoundObject);
                }

                LostFoundAdaptor lostFoundAdaptor = new LostFoundAdaptor(lostFoundObjects, false);
                foundRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                foundRecyclerView.setAdapter(lostFoundAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setClickHereListener() {
        String foundTextLabel = "Found Something? Upload a found post Here";
        int foundTextLabelLength = foundTextLabel.length();
        SpannableString spannableString = new SpannableString(foundTextLabel);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getContext(), LostFoundUploadActivity.class);
                intent.putExtra("isLost", false);
                startActivity(intent);

            }
        }, foundTextLabelLength - 4, foundTextLabelLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        foundTextView.setText(spannableString);
        foundTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}