package com.app.voting;

import static com.app.voting.utils.Const.DETAILS;
import static com.app.voting.utils.Const.currentUser;
import static com.app.voting.utils.FireBase.USER_REF;
import static com.app.voting.utils.FireBase.firebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.voting.databinding.ActivityLoginBinding;
import com.app.voting.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String email, password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(getApplicationContext());

        auth = FirebaseAuth.getInstance();

        binding.buttonSignUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
        });

        binding.buttonSignIn.setOnClickListener(v -> {
            email = binding.editTextTextEmailAddress.getText().toString().trim();
            password = binding.editTextTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                binding.editTextTextEmailAddress.setError("Required!");
                binding.editTextTextEmailAddress.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.editTextTextPassword.setError("Required!");
                binding.editTextTextPassword.requestFocus();
                return;
            }
            if (password.length() != 6) {
                binding.editTextTextPassword.setError("6 Characters required!");
                binding.editTextTextPassword.requestFocus();
                return;
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        USER_REF.child(auth.getUid()).child(DETAILS).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    UserModel userModel = snapshot.getValue(UserModel.class);
                                    if (userModel != null) {
                                        if (email.equals(userModel.getEmail()) && password.equals(userModel.getPassword())) {
                                            currentUser = userModel;
                                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "User not exist's! or \n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            login(auth.getUid());
        }
    }

    private void login(String uid) {
        USER_REF.child(uid).child(DETAILS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        currentUser = userModel;
                        Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}