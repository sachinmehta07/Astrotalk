package com.app.astrotalk.activity;

import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.astrotalk.R;
import com.app.astrotalk.adapter.UserReviewAdapter;
import com.app.astrotalk.databinding.ActivityAstroProfileBinding;
import com.app.astrotalk.model.UserReviewModel;
import com.app.astrotalk.utils.SharedPreferenceManager;
import com.app.astrotalk.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import okhttp3.internal.Util;
import timber.log.Timber;

public class ProfileActivity extends AppCompatActivity {
    private String phoneNumber;
    private String astroName;

    private static final int REQUEST_CALL_PERMISSION = 1;
    private ActivityAstroProfileBinding binding;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAstroProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.a));
        getData();
        onClickListeners();
        setDataVideoCall();
    }

    private void setDataVideoCall() {
        URL serverURL;
        try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder().setServerURL(serverURL)

                // When using JaaS, set the obtained JWT here
                //.setToken("MyJWT")
                // Different features flags can be set
                // .setFeatureFlag("toolbox.enabled", false)
                // .setFeatureFlag("filmstrip.enabled", false)

                .setFeatureFlag("welcomepage.enabled", false).build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        registerForBroadcastMessages();
    }

    private void onClickListeners() {
        binding.BackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    Intent iNxt = new Intent(ProfileActivity.this, ChatToAstroActivity.class);
                    iNxt.putExtra("profilePicUrl", getIntent().getIntExtra("profilePicUrl", 0));
                    iNxt.putExtra("name", getIntent().getStringExtra("name"));
                    iNxt.putExtra("userId", getIntent().getStringExtra("userId"));
                    startActivity(iNxt);

                } else {
                    // here i want to show this dialog
                    Utils.showLoginDialog(ProfileActivity.this, getString(R.string.login_chat));
                }


            }
        });

        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                // Check for Android 6.0 or higher
                //                // Check for call permission
                //                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //                    // Permission is granted, proceed with the call
                //                    if (phoneNumber != null) {
                //                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                //                        startActivity(callIntent);
                //                    } else {
                //                        Toast.makeText(ProfileActivity.this, "Not able to fetch Phone number !! Try Again", Toast.LENGTH_SHORT).show();
                //                    }
                //
                //                } else {
                //                    // Request permission if not granted
                //                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                //                }

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (checkPermissions()) {
                        showCallDialog();
                    } else {
                        requestPermissions();
                    }
                } else {
                    // here i want to show this dialog
                    Utils.showLoginDialog(ProfileActivity.this, getString(R.string.login_call));
                }
            }
        });
    }

    private void getData() {
        // Get data from intent
        Intent intent = getIntent();
        String Address = intent.getStringExtra("Address");
        String name = intent.getStringExtra("name");
        String astrologyType = intent.getStringExtra("astrologyType");
        String experience = intent.getStringExtra("experience");
        String language = intent.getStringExtra("language");
        String aboutAstrology = intent.getStringExtra("aboutAstrology");
        int userPic = intent.getIntExtra("profilePicUrl", 0);
        phoneNumber = intent.getStringExtra("phoneNumber");
        astroName = name;
        String userReviewsJson = intent.getStringExtra("userReviewsJson");

        // Convert JSON string to UserReviewModel list using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserReviewModel>>() {
        }.getType();
        ArrayList<UserReviewModel> userReviews = gson.fromJson(userReviewsJson, type);
        if (userReviews != null && !userReviews.isEmpty()) {
            Collections.shuffle(userReviews);
            UserReviewAdapter userReviewAdapter = new UserReviewAdapter(userReviews, this);
            binding.rvUserReviews.setAdapter(userReviewAdapter);
        }


        // Create a Random object
        Random random = new Random();

        // Generate a random float between 3 and 5 (inclusive)
        float randomRating = 3 + random.nextFloat() * (5 - 3);

        binding.rbAstroRate.setRating(randomRating);
        // Call the method to set the data in the views
        setAstrologerData(Address, name, astrologyType, experience, language, aboutAstrology, userPic);

    }

    @SuppressLint("SetTextI18n")
    private void setAstrologerData(String Address, String name, String astrologyType, String experience, String language, String aboutAstrology, int userPic) {

        binding.ivProfile.setImageResource(userPic);
        binding.txtName.setText("Name: " + name);
        binding.txtAstroType.setText("Astrologer Type: " + astrologyType);
        binding.txtExp.setText("Exp: " + experience);
        binding.txtLang.setText("Lang: " + language);
        binding.txtAbout.setText("About Astrology: " + aboutAstrology);
        binding.txtAddress.setText("Address: " + Address);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
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
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, check for audio permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // Request audio permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_REQUEST_CODE);
                } else {
                    showCallDialog();
                    // Both permissions granted, proceed with video call
                }
            } else {
                requestPermissions();
                // Camera permission denied
                // Handle this scenario, inform user or request permissions again
            }
        } else if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Audio permission granted, proceed with video call

            } else {
                requestPermissions();
                // Audio permission denied
                // Handle this scenario, inform user or request permissions again
            }
        }
    }

    private boolean checkPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int recordAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return cameraPermission == PackageManager.PERMISSION_GRANTED && recordAudioPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void registerForBroadcastMessages() {

        IntentFilter intentFilter = new IntentFilter();
    
            /* This registers for every possible event sent from JitsiMeetSDK
               If only some of the events are needed, the for loop can be replaced
               with individual statements:
               ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
                    intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                    ... other events
             */
        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void onBroadcastReceived(Intent intent) {

        if (intent != null) {

            BroadcastEvent event = new BroadcastEvent(intent);

            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    Timber.i("Conference Joined with url%s", event.getData().get("url"));
                    break;
                case PARTICIPANT_JOINED:
                    Timber.i("Participant joined%s", event.getData().get("name"));
                    break;
            }
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };

    public void setCall(String roomName) {
        if (!roomName.isEmpty()) {
            // Build options object for joining the conference. The SDK will merge the default
            // one we set earlier and this one when joining.
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(roomName)
                    // Settings for audio and video
                    .setFeatureFlag("welcomepage.enabled", false).setAudioMuted(true).setVideoMuted(true).build();
            // Launch the new activity with the given options. The launch() method takes care
            // of creating the required Intent and passing the options.
            JitsiMeetActivity.launch(ProfileActivity.this, options);

        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

//    private void showVideoCallDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.calling_dialog, null);
//        builder.setView(dialogView);
//
//        EditText editText = dialogView.findViewById(R.id.edEnterRoomName);
//
//
//        // Create the dialog instance
//        final AlertDialog dialog = builder.create();
//
//        // Now you can access the dialog's window and set its background
//        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogView.setBackgroundResource(R.drawable.dialog_bg);
//
//        Button btnYes = dialogView.findViewById(R.id.btn_yes);
//
//        btnYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String text = editText.getText().toString();
//                if (!text.isEmpty()) {
//                    if (text.length() >= 4) {
//                        setCall(text);
//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                    } else {
//                        Toast.makeText(ProfileActivity.this, "Please Enter Minimum 4 character", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(ProfileActivity.this, "Enter the room name please", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        // Show the dialog
//        dialog.show();
//    }

    private void makePhoneCall(String phoneNumber) {
        Log.d("TAG", "makePhoneCall: ");
        // Check permission again right before making the call
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {

                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);

            } else {
                Toast.makeText(this, "Phone number is not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            Toast.makeText(this, "Call permission required", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCallDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_exit, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.text_title);
        TextView txvPhoneNum = dialogView.findViewById(R.id.txvPhoneNum);
        TextView txvPhone = dialogView.findViewById(R.id.txvPhone);

        txvPhoneNum.setVisibility(VISIBLE);
        txvPhone.setVisibility(VISIBLE);

        txvPhoneNum.setText(String.format("Contact : %s", getIntent().getStringExtra("name")));
        txvPhone.setText(phoneNumber);

        title.setText(R.string.select_call_type);

// Create the dialog instance
        final AlertDialog dialog = builder.create();

// Now you can access the dialog's window and set its background
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogView.setBackgroundResource(R.drawable.dialog_bg);

        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        btnYes.setText(R.string.audio_call);
        Button btnNo = dialogView.findViewById(R.id.btn_no);
        btnNo.setText(R.string.video_call);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(phoneNumber);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                showVideoCallDialog();
                Intent iNxt = new Intent(ProfileActivity.this, VideoCallingActivity.class);
                iNxt.putExtra("phoneNumber", phoneNumber);
                iNxt.putExtra("name", astroName);
                startActivity(iNxt);
            }
        });
        // Show the dialog
        dialog.show();
    }

}