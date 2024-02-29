package com.app.astrotalk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.astrotalk.R;
import com.app.astrotalk.model.PoojaBookModel;
import com.google.gson.Gson;

public class PaymentSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection);
        // Retrieve the JSON string from intent
        String poojaJson = getIntent().getStringExtra("poojaJson");

        // Deserialize JSON to PoojaBookModel object
        Gson gson = new Gson();
        PoojaBookModel poojaBookModel = gson.fromJson(poojaJson, PoojaBookModel.class);
        TextView textView = findViewById(R.id.txtTxtPrice);
        textView.setText(String.format("%srs", String.valueOf(poojaBookModel.getPrice())));
        findViewById(R.id.ivMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btnProceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check which radio button is selected
                RadioGroup radioGroup = findViewById(R.id.radioGroupPaymentOptions);  // replace with your actual RadioGroup ID
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId != -1) {
                    // A radio button is selected
                    RadioButton selectedRadioButton = findViewById(selectedId);

                    // You can add more conditions based on the selected radio button if needed

                    // Proceed with your code
                    Gson gson = new Gson();
                    String poojaJson = gson.toJson(poojaBookModel);
                    Intent intent = new Intent(PaymentSelectionActivity.this, SuccessBookActivity.class);
                    intent.putExtra("poojaJson", poojaJson);
                    startActivity(intent);
                } else {
                    // No radio button is selected, show a message or take appropriate action
                    Toast.makeText(PaymentSelectionActivity.this, "Please select a payment", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}