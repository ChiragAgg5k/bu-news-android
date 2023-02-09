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


public class LoginFragment extends Fragment {

    EditText email, password;
    Button login_button;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login_button = view.findViewById(R.id.login_button);
        login_button.setOnClickListener(v -> {

            email = view.findViewById(R.id.email);
            password = view.findViewById(R.id.password);
            progressBar = view.findViewById(R.id.progressBar);
            String email_text = email.getText().toString();
            String password_text = password.getText().toString();

            if (email_text.isEmpty() || password_text.isEmpty()) {
                Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
            }else{

                progressBar.setVisibility(View.VISIBLE);

                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email_text, password_text).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}