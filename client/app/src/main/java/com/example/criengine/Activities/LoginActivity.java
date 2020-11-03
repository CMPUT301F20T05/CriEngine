package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.criengine.R;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            }
        );
    }
};