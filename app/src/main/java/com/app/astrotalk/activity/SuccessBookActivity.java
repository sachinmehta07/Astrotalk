package com.app.astrotalk.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.astrotalk.R;
import com.app.astrotalk.databinding.ActivitySuccessBinding;
import com.app.astrotalk.model.PoojaBookModel;
import com.app.astrotalk.utils.SharedPreferenceManager;
import com.google.gson.Gson;

public class SuccessBookActivity extends AppCompatActivity {

    private ActivitySuccessBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the JSON string from intent
        String poojaJson = getIntent().getStringExtra("poojaJson");

        // Deserialize JSON to PoojaBookModel object
        Gson gson = new Gson();
        PoojaBookModel poojaBookModel = gson.fromJson(poojaJson, PoojaBookModel.class);

        // Update XML layout with data
        if (poojaBookModel != null) {
            binding.poojaTitle.setText(poojaBookModel.getPoojaName());
            binding.poojaBenefit.setText(poojaBookModel.getPoojaBenefits());
            binding.poojaGodDetails.setText(poojaBookModel.getPoojaGodDetails());
        }
        // Store Pooja Booked details
        if (poojaBookModel != null) {
            SharedPreferenceManager.getInstance(this).setPoojaBooked(poojaBookModel.getPoojaId(), poojaBookModel);
        }


        binding.btnSeeBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poojaBookModel != null) {
                    Intent iNxt = new Intent(SuccessBookActivity.this, PoojaBookedActivity.class);
                    iNxt.putExtra("poojaId", poojaBookModel.getPoojaId());
                    startActivity(iNxt);
                    finish();
                } else {
                    Toast.makeText(SuccessBookActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent iNxt = new Intent(SuccessBookActivity.this, DashboardActivity.class);
        iNxt.putExtra("isFromSuccess", "true");
        startActivity(iNxt);
    }
}
