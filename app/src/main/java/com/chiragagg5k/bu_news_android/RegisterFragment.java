package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    EditText full_name, email, password;
    Button register_button;
    FirebaseAuth mAuth;
    FirebaseUser user;
    final private String STUDENT_ID = "^e[1-2][1-9]cseu\\d\\d\\d\\d@bennett.edu.in";


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

        mAuth = FirebaseAuth.getInstance();

        register_button.setOnClickListener(v -> {

            String full_name = this.full_name.getText().toString();
            full_name = convertToTitleCase(full_name);

            String email = this.email.getText().toString().toLowerCase();
            String password = this.password.getText().toString();

            if(full_name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(!email.matches(STUDENT_ID)){
                this.email.setError("Please enter a valid student email");
            } else{

                register_button.setText(getResources().getString(R.string.registering));

                String finalFull_name = full_name;
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(finalFull_name)
                                .build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(
                                task1 -> {
                                    if(task1.isSuccessful()) {
                                        startActivity(new Intent(getContext(), DashboardActivity.class));
                                        Toast.makeText(getContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), "Error: " + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        );

                        register_button.setText(getResources().getString(R.string.register));
                    }else{
                        Toast.makeText(getContext(), "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        register_button.setText(getResources().getString(R.string.register));
                    }
                });
            }
        });
    }

    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}