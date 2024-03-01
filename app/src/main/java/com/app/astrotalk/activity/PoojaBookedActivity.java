package com.app.astrotalk.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.astrotalk.R;
import com.app.astrotalk.adapter.PoojaBookAdapter;
import com.app.astrotalk.adapter.PoojaBookedAdapter;
import com.app.astrotalk.databinding.ActivityPoojaBookedBinding;
import com.app.astrotalk.listeners.onPoojaItemClick;
import com.app.astrotalk.model.PoojaBookModel;
import com.app.astrotalk.utils.SharedPreferenceManager;
import com.app.astrotalk.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PoojaBookedActivity extends AppCompatActivity {
    private PoojaBookedAdapter adapter;

    private ActivityPoojaBookedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPoojaBookedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new PoojaBookedAdapter(this);
        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                if (Utils.isNetworkAvailable(PoojaBookedActivity.this)) {
                    adapter.filter(cs.toString());
                    binding.ivRemove.setVisibility(VISIBLE);
                    if (adapter != null) if ((adapter.poojaList.size() == 0)) {
                        binding.txNoResult.setVisibility(VISIBLE);
                    } else {
                        binding.txNoResult.setVisibility(GONE);
                    }
                } else {
                    Toast.makeText(PoojaBookedActivity.this, R.string.no_internet_connection_found_ncheck_your_connection, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().isEmpty()) {
                    binding.ivRemove.setVisibility(View.INVISIBLE);
                }
                // TODO Auto-generated method stub
            }
        });

        binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edSearch.setText("");
                binding.ivRemove.setVisibility(View.INVISIBLE);
            }
        });
        //Bring Stored Data Here from Share Preafrance

        // Retrieve stored Pooja Booked details
        // Retrieve all Pooja IDs (assuming you have them stored somewhere)
//        List<Integer> allPoojaIds = getAllPoojaIds();
//
//        // Retrieve all booked Pooja models
//        List<PoojaBookModel> bookedPoojaList = new ArrayList<>();
//        if (allPoojaIds.size() > 1) {
//            binding.btnBook.setVisibility(GONE);
//            binding.txtEmptyMsg.setVisibility(GONE);
//            binding.rvPoojaData.setVisibility(VISIBLE);
//            binding.clSearchView.setVisibility(VISIBLE);
//            for (int poojaId : allPoojaIds) {
//                PoojaBookModel bookedPooja = SharedPreferenceManager.getInstance(this).getPoojaBooked(poojaId);
//                if (bookedPooja != null) {
//                    bookedPoojaList.add(bookedPooja);
//                }
//            }
//        } else {
//            binding.btnBook.setVisibility(VISIBLE);
//            binding.txtEmptyMsg.setVisibility(VISIBLE);
//            binding.rvPoojaData.setVisibility(GONE);
//            binding.clSearchView.setVisibility(GONE);
//        }

        // Update adapter data with stored Pooja Booked details

//        adapter.setData(bookedPoojaList, new onPoojaItemClick() {
//            @Override
//            public void onItemClick(int pos, PoojaBookModel poojaBookModel) {
//                // Handle item click if needed
//                Gson gson = new Gson();
//                String poojaJson = gson.toJson(poojaBookModel);
//                Intent intent = new Intent(PoojaBookedActivity.this, SeePoojaDetailsActivity.class);
//                intent.putExtra("poojaJson", poojaJson);
//                startActivity(intent);
//            }
//        });
        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNxt = new Intent(PoojaBookedActivity.this, DashboardActivity.class);
                iNxt.putExtra("IsFromBookNow", "true");
                startActivity(iNxt);
            }
        });
        binding.rvPoojaData.setAdapter(adapter);


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("isFromDashBoard")) {
            finish();
        } else {
            Intent iNxt = new Intent(PoojaBookedActivity.this, DashboardActivity.class);
            iNxt.putExtra("isFromDashBoard", "true");
            startActivity(iNxt);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
        refreshBookedPoojaData();
    }

    private List<Integer> getAllPoojaIds() {
        List<Integer> allPoojaIds = new ArrayList<>();
        // Iterate through all Pooja IDs stored in SharedPreferences
        for (int i = 1; i <= Utils.getPoojaBookList().size(); i++) { // Assuming MAX_POOJA_ID is the maximum possible Pooja ID
            if (SharedPreferenceManager.getInstance(this).getPoojaBooked(i) != null) {
                allPoojaIds.add(i);
            }
        }
        return allPoojaIds;
    }

    private void refreshBookedPoojaData() {
        // Retrieve all Pooja IDs
        List<Integer> allPoojaIds = getAllPoojaIds();

        // Retrieve all booked Pooja models
        List<PoojaBookModel> bookedPoojaList = new ArrayList<>();

        for (int poojaId : allPoojaIds) {
            PoojaBookModel bookedPooja = SharedPreferenceManager.getInstance(this).getPoojaBooked(poojaId);
            if (bookedPooja != null) {
                bookedPoojaList.add(bookedPooja);
            }
        }

        // Update adapter data with refreshed booked Pooja details
        adapter.setData(bookedPoojaList, (pos, poojaBookModel) -> {
            // Handle item click if needed
            Gson gson = new Gson();
            String poojaJson = gson.toJson(poojaBookModel);
            Intent intent = new Intent(PoojaBookedActivity.this, SeePoojaDetailsActivity.class);
            intent.putExtra("poojaJson", poojaJson);
            startActivity(intent);
        });

        // Update UI visibility based on data availability
        if (bookedPoojaList.isEmpty()) {
            binding.btnBook.setVisibility(VISIBLE);
            binding.txtEmptyMsg.setVisibility(VISIBLE);
            binding.rvPoojaData.setVisibility(GONE);
            binding.clSearchView.setVisibility(GONE);
        } else {
            binding.btnBook.setVisibility(GONE);
            binding.txtEmptyMsg.setVisibility(GONE);
            binding.rvPoojaData.setVisibility(VISIBLE);
            binding.clSearchView.setVisibility(VISIBLE);
        }
    }
}
