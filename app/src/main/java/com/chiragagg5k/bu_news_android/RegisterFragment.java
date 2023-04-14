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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private final String emailRegex = "^[a-zA-Z\\d+_.-]+@[a-zA-Z\\d.-]+$";
    EditText full_name, email, password, address, phone_no;
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
        address = view.findViewById(R.id.address);
        phone_no = view.findViewById(R.id.phone);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        register_button.setOnClickListener(v -> {

            String full_name = this.full_name.getText().toString().trim();
            String finalFull_name = UtilityClass.convertToTitleCase(full_name);

            String email = this.email.getText().toString().toLowerCase().trim();
            String password = this.password.getText().toString().trim();
            String phone_no = this.phone_no.getText().toString().trim();
            String address = this.address.getText().toString().trim();

            if (full_name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                playShakeAnimation();
                return;
            }

            if (!email.matches(emailRegex)) {
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                playShakeAnimation();
                return;
            }

            register_button.setEnabled(false);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();

                    // Add user to database
                    assert user != null;
                    String userId = user.getUid();
                    UserObject newUser = new UserObject(finalFull_name, phone_no, address);

                    databaseRef.child(userId).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), DashboardActivity.class));
                        }
                    });


                    this.full_name.setText("");
                    this.email.setText("");
                    this.password.setText("");
                    this.phone_no.setText("");
                    this.address.setText("");

                } else {
                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .repeat(0)
                            .playOn(view.findViewById(R.id.register_button));

                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e -> {
                register_button.setEnabled(true);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        });
    }

    private void playShakeAnimation() {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(requireView().findViewById(R.id.register_button));
    }
}