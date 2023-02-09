package com.chiragagg5k.bu_news_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    EditText full_name, email, password;
    Button register_button;
    ProgressBar progress_bar;
    FirebaseAuth mAuth;
    FirebaseUser user;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        full_name = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        register_button = view.findViewById(R.id.register_button);

        progress_bar = view.findViewById(R.id.progress_bar);

        register_button.setOnClickListener(v -> {

            String full_name = this.full_name.getText().toString();
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();

            if(full_name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else{

                progress_bar.setVisibility(View.VISIBLE);

                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user = mAuth.getCurrentUser();
                        Toast.makeText(getContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                progress_bar.setVisibility(View.GONE);
            }
        });
    }
}