package com.app.astrotalk.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.astrotalk.R;
import com.app.astrotalk.adapter.UserReviewAdapter;
import com.app.astrotalk.databinding.ActivityAstroProfileBinding;
import com.app.astrotalk.model.UserReviewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity {
    private String phoneNumber;
    private static final int REQUEST_CALL_PERMISSION = 1;
    private ActivityAstroProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAstroProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getData();
        onClickListeners();
    }

    private void onClickListeners() {
        binding.BackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNxt = new Intent(ProfileActivity.this, ChatToAstroActivity.class);
                iNxt.putExtra("profilePicUrl", getIntent().getIntExtra("profilePicUrl", 0));
                iNxt.putExtra("name", getIntent().getStringExtra("name"));
                startActivity(iNxt);
            }
        });

        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for Android 6.0 or higher
                // Check for call permission
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, proceed with the call
                    if (phoneNumber != null) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        startActivity(callIntent);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Not able to fetch Phone number !! Try Again", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Request permission if not granted
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                }
            }
        });
    }

    private void getData() {
        // Get data from intent
        Intent intent = getIntent();
        String Address = intent.getStringExtra("Address");
        String name = intent.getStringExtra("name");
        String astrologyType = intent.getStringExtra("astrologyType");
        String experience = intent.getStringExtra("experience");
        String language = intent.getStringExtra("language");
        String aboutAstrology = intent.getStringExtra("aboutAstrology");
        int userPic = intent.getIntExtra("profilePicUrl", 0);
        phoneNumber = intent.getStringExtra("phoneNumber");

        String userReviewsJson = intent.getStringExtra("userReviewsJson");

        // Convert JSON string to UserReviewModel list using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserReviewModel>>() {
        }.getType();
        ArrayList<UserReviewModel> userReviews = gson.fromJson(userReviewsJson, type);
        if (userReviews != null && userReviews.size() > 0) {
            Collections.shuffle(userReviews);
            UserReviewAdapter userReviewAdapter = new UserReviewAdapter(userReviews, this);
            binding.rvUserReviews.setAdapter(userReviewAdapter);
        }


         // Create a Random object
        Random random = new Random();

         // Generate a random float between 3 and 5 (inclusive)
        float randomRating = 3 + random.nextFloat() * (5 - 3);

        binding.rbAstroRate.setRating(randomRating);
        // Call the method to set the data in the views
        setAstrologerData(Address, name, astrologyType, experience, language, aboutAstrology, userPic);

    }

    @SuppressLint("SetTextI18n")
    private void setAstrologerData(String Address, String name, String astrologyType, String experience, String language, String aboutAstrology, int userPic) {
        binding.ivProfile.setImageResource(userPic);
        binding.txtName.setText("Name: " + name);
        binding.txtAstroType.setText("Astrologer Type: " + astrologyType);
        binding.txtExp.setText("Exp: " + experience);
        binding.txtLang.setText("Lang: " + language);
        binding.txtAbout.setText("About Astrology: " + aboutAstrology);
        binding.txtAddress.setText("Address: " + Address);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted after request, make the call
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } else {
                // Handle permission denied case
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}