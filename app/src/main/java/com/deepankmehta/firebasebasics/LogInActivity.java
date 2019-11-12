package com.deepankmehta.firebasebasics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    // Variables
    private TextInputEditText edtLoginEmail, edtLoginPassword;
    private MaterialButton btnLogin;
    private TextView txtForgotPassword, txtSignUp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtForgotPassword = findViewById(R.id.forgotPassword);
        txtSignUp = findViewById(R.id.textSignUp);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

    // Logs in the user
    private void logIn() {
        String email = edtLoginEmail.getText().toString();
        String password = edtLoginPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        final FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            database.getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
                                        String name = dataSnapshot.child("name").getValue().toString();
                                        String email = dataSnapshot.child("email").getValue().toString();

                                        Toast.makeText(LogInActivity.this, name + " is logged in", Toast.LENGTH_SHORT).show();

                                        Intent userIntent = new Intent(LogInActivity.this, UserActivity.class);
                                        userIntent.putExtra("name", name);
                                        userIntent.putExtra("email", email);
                                        startActivity(userIntent);
                                        finish();
//                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {
                        Toast.makeText(LogInActivity.this, "Error loggin in", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    // Directs the user to sign up page
    private void signUp() {
        Intent signUpIntent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    // Sends the link to reset password on the email provide
    private void forgotPassword() {
        String email = edtLoginEmail.getText().toString();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LogInActivity.this, "Password reset email sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogInActivity.this, "Please check email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
