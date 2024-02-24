package com.app.astrotalk.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.app.astrotalk.R;
import com.app.astrotalk.utils.SharedPreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_theme));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPreferenceManager.getInstance(SplashActivity.this).isUserLoggedIn()){
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }

            }
        }, 3000);
    }
}