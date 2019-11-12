package com.deepankmehta.firebasebasics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {

    // Variables
    private TextView txtName, txtEmail;
    private MaterialButton btnLogOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email);

        txtName.setText(name);
        txtEmail.setText(email);

        btnLogOut = findViewById(R.id.btnLogOut);
        mAuth = FirebaseAuth.getInstance();

        // Logs out the user
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent loginIntent = new Intent(UserActivity.this, LogInActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}
