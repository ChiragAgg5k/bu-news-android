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

import com.chiragagg5k.bu_news_android.adaptors.LostFoundAdaptor;
import com.chiragagg5k.bu_news_android.objects.LostFoundObject;

import java.util.ArrayList;

public class LostFragment extends Fragment {

    RecyclerView lostRecyclerView;
    TextView lostTextView;

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

        lostRecyclerView = view.findViewById(R.id.lostRecyclerView);
        lostTextView = view.findViewById(R.id.lostTextView);

        String lostTextLabel = "Lost Something? Upload a lost post Here";
        int lostTextLabelLength = lostTextLabel.length();
        SpannableString spannableString = new SpannableString(lostTextLabel);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        }, lostTextLabelLength - 4, lostTextLabelLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lostTextView.setText(spannableString);
        lostTextView.setMovementMethod(LinkMovementMethod.getInstance());

        ArrayList<LostFoundObject> lostFoundObjects = new ArrayList<>();
        lostFoundObjects.add(new LostFoundObject("Laptop", "Dell Inspiron 15 5000 Series", "Room 101", "12/12/2020"));

        LostFoundAdaptor lostFoundAdaptor = new LostFoundAdaptor(lostFoundObjects, true);
        lostRecyclerView.setAdapter(lostFoundAdaptor);
    }
}