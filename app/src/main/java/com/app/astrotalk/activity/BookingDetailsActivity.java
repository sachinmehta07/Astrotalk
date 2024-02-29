package com.app.astrotalk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.astrotalk.R;
import com.app.astrotalk.databinding.ActivityPoojaBookingDetailsBinding;
import com.app.astrotalk.model.PoojaBookModel;
import com.google.gson.Gson;

import java.util.Random;

public class BookingDetailsActivity extends AppCompatActivity {

    private ActivityPoojaBookingDetailsBinding binding;
    PoojaBookModel poojaBookModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPoojaBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the JSON string from intent
        String poojaJson = getIntent().getStringExtra("poojaJson");

        // Deserialize JSON to PoojaBookModel object
        Gson gson = new Gson();
        poojaBookModel = gson.fromJson(poojaJson, PoojaBookModel.class);
        binding.bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String poojaJson = gson.toJson(poojaBookModel);
                Intent intent = new Intent(BookingDetailsActivity.this, PaymentSelectionActivity.class);
                intent.putExtra("poojaJson", poojaJson);
                startActivity(intent);

            }
        });
        // Update XML layout with data
        if (poojaBookModel != null) {
            binding.poojaImg.setImageResource(poojaBookModel.getPoojaImg());
            binding.poojaTitle.setText(poojaBookModel.getPoojaName());
            binding.poojaDesc.setText(poojaBookModel.getPoojaDesc());
            binding.poojaBenefit.setText(poojaBookModel.getPoojaBenefits());
            binding.poojaGodDetails.setText(poojaBookModel.getPoojaGodDetails());

            // Assuming price is available in poojaBookModel, set it to the TextView
            binding.txtPrice.setText(String.format("%srs", String.valueOf(poojaBookModel.getPrice())));
        }

    }

    public static int generateRandomValue() {
        Random random = new Random();
        int baseValue = random.nextInt(15) + 5; // Generate a random number between 5 and 19
        return baseValue * 100; // Multiply by 100 to get increments of 100
    }
}