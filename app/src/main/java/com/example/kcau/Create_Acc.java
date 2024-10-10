package com.example.kcau;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Create_Acc extends AppCompatActivity {

    private TextInputLayout name, username, password;
    private Button create_button;
    private LoginData ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        name = findViewById(R.id.et_name);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        create_button = findViewById(R.id.create_button);

        ld = new LoginData(this);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput = name.getEditText() != null ? name.getEditText().getText().toString().trim() : "";
                String usernameInput = username.getEditText() != null ? username.getEditText().getText().toString().trim() : "";
                String passInput = password.getEditText() != null ? password.getEditText().getText().toString().trim() : "";

                if (nameInput.isEmpty() || usernameInput.isEmpty() || passInput.isEmpty()) {
                    showMessage("ERROR!!", "EMPTY FIELDS");
                    return;
                }

                Cursor loginStatus = null;
                try {
                    loginStatus = ld.validate(nameInput, usernameInput, passInput);
                    if (loginStatus != null && loginStatus.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getApplicationContext(), Home.class);
                        startActivity(in);
                    } else {
                        showMessage("ERROR...", "TRY AGAIN!!!!!");
                    }
                } catch (SQLException e) {
                    showMessage("Database Error", "An error occurred while accessing the database.");
                } finally {
                    if (loginStatus != null) {
                        loginStatus.close();  // Always close the cursor to prevent memory leaks
                    }
                }
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
