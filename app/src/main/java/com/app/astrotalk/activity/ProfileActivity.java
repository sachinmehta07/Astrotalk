package com.app.astrotalk.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.astrotalk.R;

public class ProfileActivity extends AppCompatActivity {
    private final String phoneNumber = "8320577653";
    private static final int REQUEST_CALL_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro_profile);

        findViewById(R.id.Back_Iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.btnChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNxt = new Intent(ProfileActivity.this, ChatToAstroActivity.class);
                iNxt.putExtra("profilePicUrl", getIntent().getIntExtra("profilePicUrl",0));
                iNxt.putExtra("name", getIntent().getStringExtra("name"));
                startActivity(iNxt);
            }
        });

        findViewById(R.id.btnCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for Android 6.0 or higher
                // Check for call permission
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, proceed with the call
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    startActivity(callIntent);
                } else {
                    // Request permission if not granted
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                }
            }
        });

        // Assuming you have received data from the previous screen
        // Get data from intent
        Intent intent = getIntent();
        String profilePicUrl = intent.getStringExtra("profilePicUrl");
        String name = intent.getStringExtra("name");
        String astrologyType = intent.getStringExtra("astrologyType");
        String experience = intent.getStringExtra("experience");
        String language = intent.getStringExtra("language");
        String aboutAstrology = intent.getStringExtra("aboutAstrology");
        int userPic = intent.getIntExtra("profilePicUrl", 0);
        // Call the method to set the data in the views
        setAstrologerData(profilePicUrl, name, astrologyType, experience, language, aboutAstrology, userPic);

    }

    // Method to set Astrologer data in the views
    @SuppressLint("SetTextI18n")
    private void setAstrologerData(String profilePicUrl, String name, String astrologyType, String experience, String language, String aboutAstrology, int userPic) {
        // Get references to the views
        ImageView profilePicImageView = findViewById(R.id.ivProfile);
        TextView nameTextView = findViewById(R.id.txtName);
        TextView astrologyTypeTextView = findViewById(R.id.txtAstroType);
        TextView experienceTextView = findViewById(R.id.txtExp);
        TextView languageTextView = findViewById(R.id.txtLang);
        TextView aboutAstrologyTextView = findViewById(R.id.txtAbout);

        // Set data in the views
        // You may need to use a library like Picasso or Glide to load the image from the URL
        // For simplicity, we assume you have a local resource for the profile picture.

        profilePicImageView.setImageResource(userPic);
        nameTextView.setText("Name: " + name);
        astrologyTypeTextView.setText("Astrology Type: " + astrologyType);
        experienceTextView.setText("Experience: " + experience);
        languageTextView.setText("Language: " + language);
        aboutAstrologyTextView.setText("About Astrology: " + aboutAstrology);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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