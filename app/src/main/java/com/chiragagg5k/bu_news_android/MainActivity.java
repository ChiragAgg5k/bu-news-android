package com.chiragagg5k.bu_news_android;

import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText full_name, email, password;
    Button register_button;
    ProgressBar progress_bar;
    TextView link_login;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        full_name = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register_button = findViewById(R.id.register_button);
        progress_bar = findViewById(R.id.progressBar);
        link_login = findViewById(R.id.link_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        link_login.setOnClickListener(view -> setContentView(R.layout.login_layout));

        register_button.setOnClickListener(view -> {
            String name = full_name.getText().toString();
            String email_id = email.getText().toString();
            String pass = password.getText().toString();

            // check if any of the fields are empty
            if(name.isEmpty() || email_id.isEmpty() || pass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else{
                progress_bar.setVisibility(View.VISIBLE);

                // register the user
                firebaseAuth.createUserWithEmailAndPassword(email_id,pass).addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                // TODO: Add the user to the database

                            }else{
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                            progress_bar.setVisibility(View.GONE);
                        }
                );
            }
        });
    }
}