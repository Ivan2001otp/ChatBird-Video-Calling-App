package com.example.composejava.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.composejava.R;
import com.example.composejava.Ui.CallActivity;
import com.example.composejava.Utils.Helper;
import com.example.composejava.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


       // checkUserLoggedIn(auth);

        binding.logInBtn.setOnClickListener(v->{
            Log.w("tag", "onCreate: Clicked Log in Btn");

            String email  = binding.emailEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    Intent i = new Intent(LoginActivity.this, CallActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }
            });

        });

        binding.signInBtn.setOnClickListener(v->{

            Log.w("tag", "onCreate: clicked sign in btn");
            Intent i = new Intent(LoginActivity.this,SignUpActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
    }

    private void checkUserLoggedIn(FirebaseAuth auth) {
        String email = auth.getCurrentUser().getEmail();
        String username = auth.getCurrentUser().getDisplayName();

        if(auth.getCurrentUser()!=null){
            Intent i = new Intent(LoginActivity.this, CallActivity.class);
            i.putExtra("username",username);
            i.putExtra("email",email);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}