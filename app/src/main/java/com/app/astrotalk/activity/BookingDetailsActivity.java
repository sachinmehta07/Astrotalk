package com.app.astrotalk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.astrotalk.R;
import com.app.astrotalk.databinding.ActivityPoojaBookingDetailsBinding;
import com.app.astrotalk.model.PoojaBookModel;
import com.google.gson.Gson;

public class BookingDetailsActivity extends AppCompatActivity {

    private ActivityPoojaBookingDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPoojaBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the JSON string from intent
        String poojaJson = getIntent().getStringExtra("poojaJson");

        // Deserialize JSON to PoojaBookModel object
        Gson gson = new Gson();
        PoojaBookModel poojaBookModel = gson.fromJson(poojaJson, PoojaBookModel.class);

        // Update XML layout with data
        if (poojaBookModel != null) {
            binding.poojaImg.setImageResource(poojaBookModel.getPoojaImg());
            binding.poojaTitle.setText(poojaBookModel.getPoojaName());
            binding.poojaDesc.setText(poojaBookModel.getPoojaDesc());
            binding.poojaBenefit.setText(poojaBookModel.getPoojaBenefits());
            binding.poojaGodDetails.setText(poojaBookModel.getPoojaGodDetails());

            // Assuming price is available in poojaBookModel, set it to the TextView
            binding.txtPrice.setText("500");
        }
    }
}