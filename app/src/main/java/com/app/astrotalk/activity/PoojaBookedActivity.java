package com.app.astrotalk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.astrotalk.R;
import com.app.astrotalk.adapter.PoojaBookAdapter;
import com.app.astrotalk.adapter.PoojaBookedAdapter;
import com.app.astrotalk.listeners.onPoojaItemClick;
import com.app.astrotalk.model.PoojaBookModel;
import com.google.gson.Gson;

import java.util.List;

public class PoojaBookedActivity extends AppCompatActivity {

    private PoojaBookedAdapter adapter;
    private List<PoojaBookModel> poojaBookList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooja_booked);
        adapter = new PoojaBookedAdapter(this);

        //Bring Stored Data Here from Share Preafrance
        adapter.setData(poojaBookList, new onPoojaItemClick() {
            @Override
            public void onItemClick(int pos, PoojaBookModel poojaBookModel) {
                Gson gson = new Gson();
                String poojaJson = gson.toJson(poojaBookModel);
                Intent intent = new Intent(PoojaBookedActivity.this, SeePoojaDetailsActivity.class);
                intent.putExtra("poojaJson", poojaJson);
                startActivity(intent);
            }
        });

    }
}