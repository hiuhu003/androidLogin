package com.example.login_kca;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailField, passwordField, confirmPasswordField;
    private CheckBox showPasswordCheckBox;
    private TextView passwordRequirements;
    private Button signUpButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize the views
        emailField = findViewById(R.id.email_sign);  // Assuming you have an email field
        passwordField = findViewById(R.id.password_sign);
        confirmPasswordField = findViewById(R.id.confirm);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        passwordRequirements = findViewById(R.id.passwordRequirements);
        signUpButton = findViewById(R.id.create);

        // Initially hide the password requirements
        passwordRequirements.setVisibility(View.GONE);

        // Show/Hide Passwords based on checkbox state
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show both passwords
                passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                confirmPasswordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // Hide both passwords
                passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirmPasswordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Move the cursor to the end of the input text after changing input type
            passwordField.setSelection(passwordField.getText().length());
            confirmPasswordField.setSelection(confirmPasswordField.getText().length());
        });

        // Add TextWatcher to password field to check real-time password strength
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String password = passwordField.getText().toString();

                // Show the password requirements TextView
                passwordRequirements.setVisibility(View.VISIBLE);

                // Validate the password
                if (isPasswordValid(password)) {
                    passwordRequirements.setText("Password meets requirements.");
                    passwordRequirements.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    passwordRequirements.setText("Password must be at least 8 characters, include 1 uppercase letter, 1 number, and 1 special character.");
                    passwordRequirements.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Handle sign-up button click
        signUpButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            // Check if email or password is empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if password and confirm password match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if password is valid
            if (!isPasswordValid(password)) {
                Toast.makeText(SignUpActivity.this, "Password does not meet the required criteria", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the email already exists
            mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // If email is already in use, show a message
                            if (!task.getResult().getSignInMethods().isEmpty()) {
                                Toast.makeText(SignUpActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // Email is not registered, create the user
                                createAccount(email, password);
                            }
                        } else {
                            // Handle error
                            Toast.makeText(SignUpActivity.this, "Error checking email availability", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Create a new account
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Account created successfully
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                        // Redirect to login page after successful sign-up
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();  // Close the sign-up page so the user can't go back to it
                    } else {
                        // If sign-up fails, display a message
                        Toast.makeText(SignUpActivity.this, "Sign-up failed. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Password validation method
    private boolean isPasswordValid(String password) {
        // Regular expression to check for 8 characters, 1 uppercase letter, 1 number, and 1 special character
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[\\W_]).{8,}$";
        return password.matches(regex);
    }
}
