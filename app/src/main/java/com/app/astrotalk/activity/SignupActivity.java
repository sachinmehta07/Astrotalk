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
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private EditText txtName ,emailTextView, passwordTextView;
    private Button Btn;
    public ProgressBar progressbar;
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
        setContentView(R.layout.activity_signup);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_theme));
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        txtName = findViewById(R.id.txtName);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
        findViewById(R.id.loginNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });

        findViewById(R.id.txSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new
                        Intent(SignupActivity.this,
                        DashboardActivity.class)
                );
            }
        });
    }



    private void registerNewUser() {

        if(!Utils.isNetworkAvailable(this)){
            Toast.makeText(this, R.string.no_internet_connection_found_ncheck_your_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // Check for empty email or password
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(),
                                "Please enter email!",
                                Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                                "Please enter password!",
                                Toast.LENGTH_LONG)
                        .show();
            }
            // hide the progress bar
            progressbar.setVisibility(View.GONE);
            return;
        }

        if (password.length() < 4) {
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

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {


                            // Registration successful
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG).show();

// Save user login state
                            SharedPreferenceManager.getInstance(SignupActivity.this).setUserLoggedIn(true);

// If you're collecting the user's name during registration, save it
                            String name = ((EditText) findViewById(R.id.txtName)).getText().toString();
                            if (!TextUtils.isEmpty(name)) {
                                SharedPreferenceManager.getInstance(SignupActivity.this).setUserName(name);
                            }

// Take user directly to dashboard after successful registration
                            Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
// Clear the back stack so user can't go back to registration screen
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            // Registration failed
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // User with this email already exists
                                Toast.makeText(getApplicationContext(), "Email is already registered!", Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                // Weak password
                                Toast.makeText(getApplicationContext(), "Password is too weak!", Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // Invalid email format
                                Toast.makeText(getApplicationContext(), "Invalid email format!", Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                // Invalid email (e.g., doesn't exist)
                                Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseNetworkException) {
                                // Network-related issue
                                Toast.makeText(getApplicationContext(), "Network error! Please check your internet connection.", Toast.LENGTH_LONG).show();
                            } else {
                                // Other registration failures
                                Toast.makeText(getApplicationContext(), "Registration failed!! Please try again later", Toast.LENGTH_LONG).show();
                            }
                            // hide the progress bar
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
        finish();
    }
}