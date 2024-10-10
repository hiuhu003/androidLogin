package com.example.kcau;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    // Declare UI components
    private TextInputLayout name, username, password;
    private TextView create, change_pass;
    private Button login;
    private LoginData ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize UI components
        name = findViewById(R.id.et_name);
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
                String nameInput = name.getEditText() != null ? name.getEditText().getText().toString().trim() : "";
                String usernameInput = username.getEditText() != null ? username.getEditText().getText().toString().trim() : "";
                String passwordInput = password.getEditText() != null ? password.getEditText().getText().toString() : "";

                // Validate input fields
                if (nameInput.isEmpty() || usernameInput.isEmpty() || passwordInput.isEmpty()) {
                    showMessage("ERROR!!", "EMPTY FIELDS");
                    return;
                }

                // Validate login credentials
                Cursor loginStatus = null;
                try {
                    loginStatus = ld.validate(nameInput, usernameInput, passwordInput);
                    if (loginStatus != null && loginStatus.getCount() > 0) {
                        // Successful login
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getApplicationContext(), Home.class);
                        startActivity(in);
                    } else {
                        // Failed login
                        showMessage("ERROR...", "Invalid username or password");
                    }
                } catch (Exception e) {
                    showMessage("ERROR", "An error occurred: " + e.getMessage());
                } finally {
                    if (loginStatus != null) {
                        loginStatus.close();
                    }
                }
            }
        });
    }



    private void createUser() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Create_Acc.class);
                startActivity(i);
            }
        });
    }

    private void changePassword() {
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChangePass.class);
                startActivity(i);
            }
        });
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
