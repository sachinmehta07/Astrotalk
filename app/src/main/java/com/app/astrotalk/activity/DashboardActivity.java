package com.app.astrotalk.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.astrotalk.R;
import com.app.astrotalk.adapter.HomeMenuAdapter;
import com.app.astrotalk.databinding.HomeContentMainBinding;
import com.app.astrotalk.databinding.HomeMenuOptionBinding;
import com.app.astrotalk.databinding.NavigationDrawerHeaderBinding;
import com.app.astrotalk.fragmments.CallFragment;
import com.app.astrotalk.fragmments.ChatFragment;
import com.app.astrotalk.fragmments.HomeFragment;
import com.app.astrotalk.fragmments.PoojaFragment;
import com.app.astrotalk.listeners.OnMenuItemClickListener;
import com.app.astrotalk.model.HomeMenuModel;
import com.app.astrotalk.utils.SharedPreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    FirebaseAuth auth;
    //    Button button;
//    TextView textView;
    FirebaseUser user;

    private HomeMenuOptionBinding homeScreenBinding;
    private HomeContentMainBinding homeScreenContentMainBinding;
    private final ArrayList<HomeMenuModel> homeMenuModelArrayList = new ArrayList<>();
    private View headerView;
    private NavigationDrawerHeaderBinding headerBinding;
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeScreenBinding = HomeMenuOptionBinding.inflate(getLayoutInflater());
        homeScreenContentMainBinding = homeScreenBinding.homeContetntLay;
        setContentView(homeScreenBinding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        auth = FirebaseAuth.getInstance();
//        button = findViewById(R.id.logout);
//        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            textView.setText(user.getEmail());
//        }
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
//            }
//        });


        headerView = homeScreenBinding.nvHomeMenu.getHeaderView(0);
        headerBinding = NavigationDrawerHeaderBinding.bind(headerView);

//        homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navHome);

        headerBinding.txUserNameDisplay.setText(SharedPreferenceManager.getInstance(this).getUserName());

        fragmentManager = getSupportFragmentManager();

//        replaceFragments(new HomeScreenFragment(),R.id.navHome);

        headerBinding.headerUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeScreenBinding.drawerLayoutHome.closeDrawer(GravityCompat.START);
            }
        });

        //All Click Home
        onClickListeners();
        arrayDataAdd();
        if (getIntent().hasExtra("isFromSuccess")) {
            if (Objects.equals(getIntent().getStringExtra("isFromSuccess"), "true")) {
                replaceFragments(new HomeFragment());
            }
        }

        //Default Fragment Load
//        if(getIntent().hasExtra("isFromProductListActivity")) {
//            if(getIntent().getStringExtra("isFromProductListActivity").equals("true") ){
//                replaceFragments(new CartFragment());
//
//            }
//        }
//        else {
//
//        }
    }

    public void onClickListeners() {

        //Bottom Navigation Bar Click-Events
        homeScreenContentMainBinding.bnBarHomeScreen.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    replaceFragments(new HomeFragment());
                    return true;
                } else if (itemId == R.id.navChat) {
                    replaceFragments(new ChatFragment());
                    return true;
                } else if (itemId == R.id.navCall) {
                    replaceFragments(new CallFragment());
                    return true;
                } else if (itemId == R.id.navPooja) {
                    replaceFragments(new PoojaFragment());
                    return true;
                }
                return false;
            }
        });
        homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navHome);
    }

    public void arrayDataAdd() {

        homeMenuModelArrayList.add(new HomeMenuModel(R.drawable.home_ic, "Home", "Discover Astrologers in various categories."));
        homeMenuModelArrayList.add(new HomeMenuModel(R.drawable.ic_chat, "Chat", "Engage in direct conversations"));
        homeMenuModelArrayList.add(new HomeMenuModel(R.drawable.ic_call, "calls", "Connect with Astrologers instantly calls."));
        homeMenuModelArrayList.add(new HomeMenuModel(R.drawable.ic_pooja, "Pooja", "Book personalized Pooja sessions"));
        homeMenuModelArrayList.add(new HomeMenuModel(R.drawable.logout_ic, "Logout", "Securely log out from the app"));

        HomeMenuAdapter homeMenuAdapter = new HomeMenuAdapter(homeMenuModelArrayList, new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, String menuItem) {
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.flHomeScreenMain);

                if (menuItem.replace(" ", "").equalsIgnoreCase(homeMenu.HOME.toString())) {
                    homeScreenBinding.drawerLayoutHome.closeDrawer(GravityCompat.START);
                    if (!(currentFragment instanceof HomeFragment)) {
                        homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navHome);
                        homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navHome);
                        replaceFragments(new HomeFragment());
                    }
                } else if (menuItem.replace(" ", "").equalsIgnoreCase(homeMenu.Chat.toString())) {
                    if (currentFragment instanceof ChatFragment) {
                        homeScreenBinding.drawerLayoutHome.closeDrawer(GravityCompat.START);
                    } else {
                        replaceFragments(new ChatFragment());
                        homeScreenBinding.drawerLayoutHome.closeDrawer(GravityCompat.START);
                        homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navChat);
                    }
                } else if (menuItem.replace(" ", "").equalsIgnoreCase(homeMenu.Logout.toString())) {
                    SharedPreferenceManager.getInstance(DashboardActivity.this).clearUserLoggedIn();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                } else if (menuItem.replace(" ", "").equalsIgnoreCase(homeMenu.Pooja.toString())) {
                    homeScreenBinding.drawerLayoutHome.closeDrawer(GravityCompat.START);
                    if (!(currentFragment instanceof PoojaFragment)) {
                        homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navPooja);
                        replaceFragments(new PoojaFragment());
                    }
                } else if (menuItem.replace(" ", "").equalsIgnoreCase(homeMenu.calls.toString())) {
                    homeScreenBinding.drawerLayoutHome.closeDrawer(GravityCompat.START);
                    if (!(currentFragment instanceof CallFragment)) {
                        homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navCall);
                        replaceFragments(new CallFragment());
                    }
                }
            }
        });
        homeScreenBinding.rvOptionMenuList.setAdapter(homeMenuAdapter);
    }

    //nav Home menu Open
    public void drawerOpen() {
        homeScreenBinding.drawerLayoutHome.openDrawer(GravityCompat.START);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.flHomeScreenMain);

        if (currentFragment instanceof HomeFragment) {
            showExitDialog();


        } else if (currentFragment instanceof ChatFragment) {

            replaceFragments(new HomeFragment());
            homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navHome);

        } else if (currentFragment instanceof CallFragment) {

            replaceFragments(new HomeFragment());
            homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navHome);

        } else if (currentFragment instanceof PoojaFragment) {

            replaceFragments(new HomeFragment());
            homeScreenContentMainBinding.bnBarHomeScreen.setSelectedItemId(R.id.navHome);

        } else if (homeScreenBinding.drawerLayoutHome.isDrawerOpen(GravityCompat.START)) {

            homeScreenBinding.drawerLayoutHome.closeDrawer(GravityCompat.START);

        } else {

            Log.d("MSG", "onBackPressed: ELSE");

        }
    }

    @SuppressLint("NonConstantResourceId")
    public void replaceFragments(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flHomeScreenMain, fragment);
        transaction.commit();
    }

    public enum homeMenu {
        HOME, Chat, calls, Pooja, Logout,
    }

    public void setUserData(String text) {

        headerBinding.txUserNameDisplay.setText(text);

    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_exit, null);
        builder.setView(dialogView);

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
                finishAffinity();
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

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_exit, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.text_title);
        title.setText("Are you sure you want to logout");

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
                SharedPreferenceManager.getInstance(DashboardActivity.this).clearUserLoggedIn();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));

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

}