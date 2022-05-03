package com.app.voting;

import static com.app.voting.utils.Const.DETAILS;
import static com.app.voting.utils.FireBase.UID;
import static com.app.voting.utils.FireBase.USER_REF;
import static com.app.voting.utils.FireBase.firebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.voting.databinding.ActivitySignupBinding;
import com.app.voting.model.UserModel;
import com.app.voting.utils.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    String name, email, password, confirm_password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.buttonSignUp.setOnClickListener(v -> {

            name = binding.Name.getText().toString().trim();
            email = binding.EmailAddress.getText().toString().trim();
            password = binding.Password.getText().toString().trim();
            confirm_password = binding.confPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                binding.Name.setError("Required!");
                binding.Name.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                binding.EmailAddress.setError("Required!");
                binding.EmailAddress.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.Password.setError("Required!");
                binding.Password.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(confirm_password)) {
                binding.confPassword.setError("Required!");
                binding.confPassword.requestFocus();
                return;
            }
            if (password.length() != 6) {
                binding.Password.setError("6 Characters required!");
                binding.Password.requestFocus();
                return;
            }
            if (!confirm_password.equals(password)) {
                binding.confPassword.setError("Password not matching!");
                binding.confPassword.requestFocus();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        UserModel userModel = new UserModel(name, email, password, auth.getUid());
                        USER_REF.child(auth.getUid()).child(DETAILS).setValue(userModel);
                        Const.currentUser = userModel;
                        Toast.makeText(SignupActivity.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Try again! \n"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });


    }
}