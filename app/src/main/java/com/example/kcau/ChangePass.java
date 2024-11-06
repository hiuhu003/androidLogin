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

public class ChangePass extends AppCompatActivity {

    private TextInputLayout username, password;
    private TextView login;
    private Button changepassword_button;
    private LoginData ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("KCA UNIVERSITY");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.blogin);
        changepassword_button = findViewById(R.id.changepassword_button);

        ld = new LoginData(this);

        changepassword_button.setOnClickListener(v -> changePassword());
        login.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });
    }

    private void changePassword() {
        String usernameInput = username.getEditText() != null ? username.getEditText().getText().toString().trim() : "";
        String newPassword = password.getEditText() != null ? password.getEditText().getText().toString() : "";

        if (usernameInput.isEmpty() || newPassword.isEmpty()) {
            showMessage("ERROR!!", "EMPTY FIELDS");
            return;
        }

        if (ld.checkUserExists(usernameInput)) {
            boolean success = ld.updatePassword(usernameInput, newPassword);
            if (success) {
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
            } else {
                showMessage("ERROR", "Password change failed. Try again.");
            }
        } else {
            showMessage("ERROR", "User not found.");
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
