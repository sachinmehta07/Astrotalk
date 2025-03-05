package com.app.astrotalk.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.astrotalk.R;
import com.app.astrotalk.utils.SharedPreferenceManager;
import com.app.astrotalk.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    // taking instance of FirebaseAuth

        mAuth = FirebaseAuth.getInstance();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_theme));

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        progressbar = findViewById(R.id.progressbar);

        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

        findViewById(R.id.signupNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        findViewById(R.id.txSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new
                        Intent(LoginActivity.this,
                        DashboardActivity.class)
                );
            }
        });

    }

    private void loginUserAccount() {

        if(!Utils.isNetworkAvailable(this)){
            Toast.makeText(this, R.string.no_internet_connection_found_ncheck_your_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        // show the visibility of progress bar to show loading

        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password, name;

        email = emailTextView.getText().toString();

        password = passwordTextView.getText().toString();

        name = ((EditText) findViewById(R.id.txtName)).getText().toString();

        // Check for empty email and password

        if (email.isEmpty() && password.isEmpty() && name.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                            "Email and password cannot be empty!",
                            Toast.LENGTH_LONG)
                    .show();
            // hide the progress bar
            progressbar.setVisibility(View.GONE);

            return;
        }

        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                            "Name cannot be empty!",
                            Toast.LENGTH_LONG)
                    .show();
            // hide the progress bar
            progressbar.setVisibility(View.GONE);
            return;
        }


        // Check for empty email
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                            "Email cannot be empty!",
                            Toast.LENGTH_LONG)
                    .show();
            // hide the progress bar
            progressbar.setVisibility(View.GONE);
            return;
        }

        // Check for empty password
        if (password.isEmpty() || password.length() < 4) {
            Toast.makeText(getApplicationContext(),
                            "Password must be at least 4 digit long!",
                            Toast.LENGTH_LONG)
                    .show();
            // hide the progress bar
            progressbar.setVisibility(View.GONE);
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(),
                            "Invalid email address!",
                            Toast.LENGTH_LONG)
                    .show();
            // hide the progress bar
            progressbar.setVisibility(View.GONE);
            return;
        }

        // You can add more validations for password here if needed

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    SharedPreferenceManager.getInstance(LoginActivity.this).setUserLoggedIn(true);
                                    SharedPreferenceManager.getInstance(LoginActivity.this).setUserName(String.valueOf(((EditText) findViewById(R.id.txtName)).getText()));
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(intent);

                                } else {
                                    // sign-in failed
                                    // Sign-in failed
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseAuthInvalidUserException) {
                                        // User not found or has been disabled
                                        Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_LONG).show();
                                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        // Incorrect password
                                        Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_LONG).show();
                                    } else {
                                        // Other errors
                                        if (e != null) {
                                            Toast.makeText(getApplicationContext(), "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }


    // Method to validate email address format
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}