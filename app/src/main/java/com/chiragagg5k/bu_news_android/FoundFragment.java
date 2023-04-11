package com.chiragagg5k.bu_news_android;

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
import androidx.recyclerview.widget.RecyclerView;

public class FoundFragment extends Fragment {

    TextView foundTextView;
    RecyclerView foundRecyclerView;


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

       foundRecyclerView  = view.findViewById(R.id.foundRecyclerView);
        foundTextView = view.findViewById(R.id.foundTextView);

        String foundTextLabel = "Found Something? Upload a found post Here";
        int foundTextLabelLength = foundTextLabel.length();
        SpannableString spannableString = new SpannableString(foundTextLabel);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        }, foundTextLabelLength - 4, foundTextLabelLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        foundTextView.setText(spannableString);
        foundTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}