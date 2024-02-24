package com.app.astrotalk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.astrotalk.R;

public class ProfileActivity extends AppCompatActivity {

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
}