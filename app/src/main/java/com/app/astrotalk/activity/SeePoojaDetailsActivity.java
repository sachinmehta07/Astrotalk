package com.app.astrotalk.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.astrotalk.R;
import com.app.astrotalk.databinding.ActivitySeePoojaDetailsBinding;
import com.app.astrotalk.model.PoojaBookModel;
import com.app.astrotalk.utils.SharedPreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Objects;

public class SeePoojaDetailsActivity extends AppCompatActivity {
    private ActivitySeePoojaDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeePoojaDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Retrieve the JSON string from intent
        String poojaJson = getIntent().getStringExtra("poojaJson");

        // Deserialize JSON to PoojaBookModel object
        Gson gson = new Gson();
        PoojaBookModel poojaBookModel = gson.fromJson(poojaJson, PoojaBookModel.class);

        if (poojaBookModel != null) {
            binding.poojaTitle.setText(poojaBookModel.getPoojaName());
            binding.poojaBenefit.setText(poojaBookModel.getPoojaBenefits());
            binding.poojaGodDetails.setText(poojaBookModel.getPoojaGodDetails());
        }
        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog(poojaBookModel);
            }
        });
    }

    private void showLogoutDialog(PoojaBookModel poojaBookModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_exit, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.text_title);
        title.setText(R.string.are_you_sure_you_want_to_cancel);

// Create the dialog instance
        final AlertDialog dialog = builder.create();

// Now you can access the dialog's window and set its background
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogView.setBackgroundResource(R.drawable.dialog_bg);

        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferenceManager.getInstance(SeePoojaDetailsActivity.this).removePoojaBooked(poojaBookModel.getPoojaId());
                finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Show the dialog
        dialog.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }
}