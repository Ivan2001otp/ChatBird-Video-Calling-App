package com.example.composejava.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.composejava.Model.User;
import com.example.composejava.R;
import com.example.composejava.Ui.CallActivity;
import com.example.composejava.Utils.Helper;
import com.example.composejava.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        checkUserLoggedIn(auth);

        binding.logInBtn.setOnClickListener(v->{
            Log.w("tag", "onCreate: Clicked logIn button");
            Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        });

        binding.signInBtn.setOnClickListener(v->{
            //Helper.hideSoftKeyboardUtil(SignUpActivity.this,binding.signInBtn);
            Log.w("tag", "onCreate: Clicked signIn button");

            String email,username,password;
            email = binding.emailEt.getText().toString().trim();
            username = binding.usernameEt.getText().toString().trim();
            password = binding.passwordEt.getText().toString().trim();

           User signInUser = new User();
           signInUser.setEmail(email);
           signInUser.setPassword(password);
           signInUser.setUsername(username);
           signInUser.setLogout(false);

            Map<String,Object> newUser = new HashMap<>();
            newUser.put("Name",signInUser.getUsername());
            newUser.put("Email",signInUser.getEmail());
            newUser.put("Password",signInUser.getPassword());
            newUser.put("Logout-Status",true);

            int passwordStrengStatus = Helper.passwordChecker(password);

           if(isNetworkAvailable() && passwordStrengStatus==1){
               Toast.makeText(SignUpActivity.this, "Connected to Server.", Toast.LENGTH_SHORT).show();
                try{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Log.w("tag", "onCreate: Signed in Successfully");

                            newUser.put("Logout-Status",false);

                            database.collection("Users")
                                    .document().set(newUser).addOnSuccessListener(unused -> {
                                        Toast.makeText(SignUpActivity.this,"Signed in Successfully !",Toast.LENGTH_SHORT)
                                                .show();

                                        Log.e("tag", "onCreate: Intent builded");
                                        Intent i = new Intent(SignUpActivity.this, CallActivity.class);

                                        i.putExtra("username",signInUser.getUsername());
                                        i.putExtra("email",signInUser.getEmail());
                                        i.putExtra("password",signInUser.getPassword());
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Log.e("tag", "onCreate: Intent started");
                                        startActivity(i);
                                        Log.e("tag", "onCreate: Intent post started");
                                    });
                        }else{
                            Log.w("tag", "onCreate: Error while signing in");
                            Log.e("tag","onCreate:signing in user - error "+task.getException() );
                        }
                    });
                } catch(SecurityException e){
                    Toast.makeText(SignUpActivity.this, "Security Exception "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                catch (Exception e){
                    Toast.makeText(SignUpActivity.this, "General Exception : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
           }else{
               int resultStatus = passwordStrengStatus;

               switch (resultStatus){
                   case -1:
                       showToastMessage("Password length should be at-least 6.");
                       break;

                   case 0:
                       showToastMessage("Password should have alphaNumeric Characters.");
                       break;

                   default:
                       showToastMessage("Poor Internet Connection.Please try again.");
               }
           }

        });
    }

    private void checkUserLoggedIn(FirebaseAuth auth) {


        if(auth.getCurrentUser()!=null){
            String email = auth.getCurrentUser().getEmail();
            String username = auth.getCurrentUser().getDisplayName();
            Intent i = new Intent(SignUpActivity.this, CallActivity.class);
            i.putExtra("username",username);
            i.putExtra("email",email);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    private Boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isAvailable()){
            return true;
        }
        return false;
    }

    private void showToastMessage(String message){
        Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_SHORT)
                .show();
    }
}