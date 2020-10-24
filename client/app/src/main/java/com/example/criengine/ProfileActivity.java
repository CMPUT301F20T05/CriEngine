package com.example.criengine;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ProfileActivity extends AppCompatActivity {

    ImageButton userImageButton;
    TextView userTextView;
    EditText bioEditText;
    EditText phoneEditText;
    EditText addressEditText;

    protected void onCreate(Bundle savedInstanceState) {
        // Must setContentView(layoutID) in child before calling this super method.
        super.onCreate(savedInstanceState);

        // get all the UI components
        userImageButton = findViewById(R.id.user_image);
        userTextView = findViewById(R.id.user_profile_text);
        bioEditText = findViewById(R.id.bio_text);
        phoneEditText = findViewById(R.id.phone_text);
        addressEditText = findViewById(R.id.addr_text);

        // save the key listeners
        bioEditText.setTag(bioEditText.getKeyListener());
        phoneEditText.setTag(phoneEditText.getKeyListener());
        addressEditText.setTag(addressEditText.getKeyListener());

        // TODO: update stuff with user from db
        // TODO: get username from db
        String username = "Lojpurgator3000";
        userTextView.setText(getString(R.string.user_profile_text, username));
    }
}
