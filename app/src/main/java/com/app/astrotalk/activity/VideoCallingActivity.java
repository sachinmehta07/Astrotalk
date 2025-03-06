package com.app.astrotalk.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.astrotalk.R;
import com.app.astrotalk.databinding.ActivityVideoCallingBinding;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import timber.log.Timber;

public class VideoCallingActivity extends AppCompatActivity {
    private ActivityVideoCallingBinding binding;
    private String phoneNumber;
    private static final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoCallingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        setDataVideoCall();

        onClickListeners();

    }

    private void onClickListeners() {
        binding.BackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StringBuilder sb = new StringBuilder();

        if (!phoneNumber.isEmpty()) {
            sb.append("+91").append(" ").append(phoneNumber);
            binding.txvPhoneNumber.setText(sb);
        } else {
            binding.txvPhoneNumber.setText(R.string.msg_empty_num);
        }

        binding.btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissionAndSaveContact()) {
                    saveToContacts(phoneNumber);
                } else {
                    reqPermission();
                }

            }
        });

        binding.btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp(phoneNumber);
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!binding.edEnterRoomName.getText().toString().isEmpty()) {
                    if (binding.edEnterRoomName.length() >= 4) {
                        setCall(binding.edEnterRoomName.getText().toString());
                    } else {
                        Toast.makeText(VideoCallingActivity.this, "Please Enter Minimum 4 character", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VideoCallingActivity.this, "Enter the room name please", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void reqPermission() {
        // Request the permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_CONTACTS},
                PERMISSION_REQUEST_CODE);
    }


    private boolean checkPermissionAndSaveContact() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save the contact
                saveToContacts(phoneNumber);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied to write contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveToContacts(String phoneNumber) {
        // Create intent to insert a new contact
        Log.d("TAG", "saveToContacts: ");
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        // Pre-fill the phone number
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);

        // Start the contact creation activity
        startActivity(intent);

    }

    // Method to open WhatsApp with the given phone number
    private void openWhatsApp(String phoneNumber) {
        try {
            // Format the number with country code if not already present
            String formattedNumber = phoneNumber.replaceAll("[^\\d]", ""); // Remove non-digits

            // Make sure the number has the country code (91 for India)
            if (!formattedNumber.startsWith("91")) {
                formattedNumber = "91" + formattedNumber;
            }

            // Predefined message - URL encoded
            String message = "Hey " + getIntent().getStringExtra("name") + ", I want help \uD83D\uDE4B\uD83C\uDFFB\u200D♂\uFE0F";
            String encodedMessage = Uri.encode(message);

            // Create the WhatsApp URI with the + prefix for international format and the message
            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+" + formattedNumber + "&text=" + encodedMessage);


            // Create and start the intent
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            // WhatsApp not installed
            Toast.makeText(this, "WhatsApp not installed on your device", Toast.LENGTH_SHORT).show();

            // Optional: direct to Play Store to download WhatsApp
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.whatsapp")));
            } catch (Exception ex) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
            }
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
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

    public void setCall(String roomName) {
        Log.d("TAG", "setCall: ");
        if (!roomName.isEmpty()) {
            // Build options object for joining the conference. The SDK will merge the default
            // one we set earlier and this one when joining.
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(roomName)
                    // Settings for audio and video
                    .setFeatureFlag("welcomepage.enabled", false).setAudioMuted(true).setVideoMuted(true).build();
            // Launch the new activity with the given options. The launch() method takes care
            // of creating the required Intent and passing the options.
            JitsiMeetActivity.launch(VideoCallingActivity.this, options);

        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}