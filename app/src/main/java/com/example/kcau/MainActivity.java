package com.example.kcau;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    // Declare UI components
    private TextInputLayout username, password;
    private TextView create, change_pass;
    private Button login;
    private LoginData ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("KCA UNIVERSITY");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Initialize UI components
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.login_button);
        create = findViewById(R.id.create);
        change_pass = findViewById(R.id.change_password);

        // Initialize LoginData object
        ld = new LoginData(this);

        // Set up click listeners
        setupListeners();
    }

    private void setupListeners() {
        createUser();
        loginUser();
        changePassword();
    }

    private void loginUser() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String usernameInput = getTextInput(username);
                String passwordInput = getTextInput(password);

                // Validate input fields
                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                    showMessage("ERROR!!", "EMPTY FIELDS");
                    return;
                }

                // Validate login credentials
                try (Cursor loginStatus = ld.validate(usernameInput, passwordInput)) {
                    if (loginStatus != null && loginStatus.getCount() > 0) {
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    } else {
                        showMessage("ERROR...", "Invalid username or password");
                    }
                } catch (Exception e) {
                    showMessage("ERROR", "An error occurred: " + e.getMessage());
                }
            }
        });
    }

    private String getTextInput(TextInputLayout inputLayout) {
        return inputLayout.getEditText() != null ? inputLayout.getEditText().getText().toString().trim() : "";
    }

    private void createUser() {
        create.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Create_Acc.class);
            startActivity(i);
        });
    }

    private void changePassword() {
        change_pass.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ChangePass.class);
            startActivity(i);
        });
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();
    }
}
