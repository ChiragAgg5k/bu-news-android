package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneralNewsFragment extends Fragment {


    DatabaseReference databaseReference;
    NewsRvAdaptor news_adaptor;
    List<UploadObject> uploadObjects;
    RecyclerView news_rv;
    TextView general_loading_tv;

    public GeneralNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        news_rv = view.findViewById(R.id.news_rv);
        general_loading_tv = view.findViewById(R.id.general_loading_tv);

        Handler handler = new Handler();
        handler.postDelayed(() -> general_loading_tv.setText("No General News Found"), 4000);

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        uploadObjects = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadObjects.clear();

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    UploadObject upload = postSnapShot.getValue(UploadObject.class);

                    assert upload != null;

                    if (upload.isAuthorized()) {
                        uploadObjects.add(upload);
                    }
                }

                Collections.reverse(uploadObjects);

                news_adaptor = new NewsRvAdaptor(uploadObjects);
                news_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                news_rv.setAdapter(news_adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}