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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    // Variables
    private TextInputEditText edtSignUpName, edtSignUpEmail, edtSignUpPassword, edtSignUpConfirmPassword;
    private MaterialButton btnSignUp;
    private TextView txtLogIn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtSignUpName = findViewById(R.id.edtSignUpName);
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        edtSignUpConfirmPassword = findViewById(R.id.edtSignUpConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtLogIn = findViewById(R.id.textLogIn);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        txtLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    // Signs up the user
    private void signUp() {
        final String name = edtSignUpName.getText().toString();
        final String email = edtSignUpEmail.getText().toString();
        String password = edtSignUpPassword.getText().toString();
        String confirmPassword = edtSignUpConfirmPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(confirmPassword)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference root = database.getReference().child("users").child(mAuth.getCurrentUser().getUid());
                            root.child("name").setValue(name);
                            root.child("email").setValue(email);

                            Toast.makeText(SignUpActivity.this, name + " signed Up successfully", Toast.LENGTH_SHORT).show();

                            logIn();
                            finish();
                        }
                    }
                });
            } else {
                Toast.makeText(SignUpActivity.this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Directs the user to login page
    private void logIn() {
        Intent loginIntent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(loginIntent);
    }

}
