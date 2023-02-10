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
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    EditText full_name, email, password;
    Button register_button;
    ProgressBar progress_bar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore database;

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

        progress_bar = view.findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        register_button.setOnClickListener(v -> {

            String full_name = this.full_name.getText().toString();
            String email = this.email.getText().toString().toLowerCase();
            String password = this.password.getText().toString();

            if(full_name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(!email.matches(STUDENT_ID)){
                Toast.makeText(getContext(), "Please enter a valid student email", Toast.LENGTH_SHORT).show();
            }else if(password.length() < 6){
                Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            }
            else{

                progress_bar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user = mAuth.getCurrentUser();
                        database.collection("users").document(user.getUid()).set(new User(full_name, email, password));

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