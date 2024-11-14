package com.example.login_kca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private CheckBox showPasswordCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find the login button and set up the click listener
        Button loginButton = findViewById(R.id.login_button);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email and password from the fields
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                // Check if email or password is empty
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password meets the required criteria
                if (!isPasswordValid(password)) {
                    Toast.makeText(LoginActivity.this, "Password must be at least 8 characters, with 1 uppercase, 1 number, and 1 special character", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firebase sign-in
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                // Navigate to the Website activity
                                Intent intent = new Intent(LoginActivity.this, website.class);
                                startActivity(intent);
                                finish(); // To prevent the user from going back to the login page
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Show password toggle functionality
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Move the cursor to the end of the input text after changing input type
            passwordField.setSelection(passwordField.getText().length());
        });

        TextView createAccountButton = findViewById(R.id.create_button);
        createAccountButton.setOnClickListener(v -> {
            // Navigate to the SignupActivity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // Password validation method
    private boolean isPasswordValid(String password) {
        // Regular expression to check for 8 characters, 1 uppercase letter, 1 number, and 1 special character
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[\\W_]).{8,}$";
        return password.matches(regex);
    }
}
