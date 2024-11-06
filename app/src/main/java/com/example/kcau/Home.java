package com.example.kcau;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity {
    public Button buttonWebsite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Replace with the actual layout file for Home

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("KCA UNIVERSITY");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        buttonWebsite = findViewById(R.id.kcaubtn);
        setUpEventListeners();
    }

    private void setUpEventListeners(){
        browse();
    }

    private void browse(){
        // Open the KCA University website when the second button is clicked
        buttonWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.kcau.ac.ke"));
                startActivity(browserIntent);
            }
        });
    }
}

