package com.example.kcau;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputLayout;

public class Create_Acc extends AppCompatActivity {

    private TextInputLayout username, password;
    public TextView login;
    public Button create_button;
    private LoginData ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("KCA UNIVERSITY");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        create_button = findViewById(R.id.create_button);
        login = findViewById(R.id.login_button);

        ld = new LoginData(this);

        // Set up click listeners
        setupListeners();


    }
    private void setupListeners() {
        registerUser();
        loginUser();

    }
    private void loginUser(){
        create_button.setOnClickListener(v -> registerUser());
        login.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });
    }

    private void registerUser() {
        String usernameInput = username.getEditText() != null ? username.getEditText().getText().toString().trim() : "";
        String passInput = password.getEditText() != null ? password.getEditText().getText().toString().trim() : "";

        if (usernameInput.isEmpty() || passInput.isEmpty()) {
            showMessage("ERROR!!", "EMPTY FIELDS");
            return;
        }

        if (ld.checkUserExists(usernameInput)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean insertSuccess = ld.insertUser(usernameInput, passInput);
        if (insertSuccess) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show();
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        } else {
            showMessage("ERROR...", "Could not register user. Try again!");
        }
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .show();
    }
}
