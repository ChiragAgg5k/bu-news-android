package com.chiragagg5k.bu_news_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chiragagg5k.bu_news_android.objects.UserObject;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    final private String STUDENT_ID = "^e[1-2][1-9]cseu\\d\\d\\d\\d@bennett.edu.in";
    EditText full_name, email, password, confirm_password, phone_no;
    Button register_button;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseRef;


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
        confirm_password = view.findViewById(R.id.confirm_password);
        phone_no = view.findViewById(R.id.phone);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        register_button.setOnClickListener(v -> {

            String full_name = this.full_name.getText().toString();
            String finalFull_name = UtilityClass.convertToTitleCase(full_name);

            String email = this.email.getText().toString().toLowerCase();
            String password = this.password.getText().toString();
            String confirm_password = this.confirm_password.getText().toString();
            String phone_no = this.phone_no.getText().toString();

            register_button.setText(getResources().getString(R.string.registering));

            if (full_name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                register_button.setText(getResources().getString(R.string.register));
                playShakeAnimation();
                return;
            }

            if (!email.matches(STUDENT_ID)) {
                Toast.makeText(getContext(), "Please enter a valid student email", Toast.LENGTH_SHORT).show();
                register_button.setText(getResources().getString(R.string.register));
                playShakeAnimation();
                return;
            }

            if (!password.equals(confirm_password)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                register_button.setText(getResources().getString(R.string.register));
                playShakeAnimation();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();

                    // Add user to database
                    assert user != null;
                    String userId = user.getUid();
                    UserObject newUser = new UserObject(finalFull_name, phone_no);

                    databaseRef.child(userId).setValue(newUser);

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(finalFull_name)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(
                            task1 -> {
                                if (task1.isSuccessful()) {
                                    startActivity(new Intent(getContext(), DashboardActivity.class));
                                    Toast.makeText(getContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Error: " + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                } else {
                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .repeat(0)
                            .playOn(view.findViewById(R.id.register_button));

                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            register_button.setText(getResources().getString(R.string.register));
        });
    }

    private void playShakeAnimation() {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(requireView().findViewById(R.id.register_button));
    }
}