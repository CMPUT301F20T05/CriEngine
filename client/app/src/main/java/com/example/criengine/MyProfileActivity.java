package com.example.criengine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyProfileActivity extends ProfileActivity {
    private Button cancelButton;
    private Button editSaveButton;

    private String prevBioText;
    private String prevPhoneText;
    private String prevAddressText;

    private boolean editing = false;

    private void setPageViewOnly() {
        cancelButton.setVisibility(View.INVISIBLE);
        editSaveButton.setText(R.string.edit_button);

        bioEditText.setKeyListener(null);
        phoneEditText.setKeyListener(null);
        addressEditText.setKeyListener(null);
    }

    private void setPageEditable() {
        cancelButton.setVisibility(View.VISIBLE);
        editSaveButton.setText(R.string.save_button);

        bioEditText.setKeyListener((KeyListener) bioEditText.getTag());
        phoneEditText.setKeyListener((KeyListener) phoneEditText.getTag());
        addressEditText.setKeyListener((KeyListener) addressEditText.getTag());
    }

    @Override
    public void onBackPressed() {
        if (editing) {
            // same as cancel button
            bioEditText.setText(prevBioText);
            phoneEditText.setText(prevPhoneText);
            addressEditText.setText(prevAddressText);
            setPageViewOnly();
            editing = false;
        } else {
            // go back to previous activity
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_profile);
        super.onCreate(savedInstanceState);

        // new UI components
        cancelButton = findViewById(R.id.cancel_button);
        editSaveButton = findViewById(R.id.edit_save_button);

        // save the key listeners
        bioEditText.setTag(bioEditText.getKeyListener());
        phoneEditText.setTag(phoneEditText.getKeyListener());
        addressEditText.setTag(addressEditText.getKeyListener());

        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    // pressing save on data
                    // TODO: update database with new modified info
                    setPageViewOnly();
                } else {
                    // pressing edit data
                    prevBioText = bioEditText.getText().toString();
                    prevPhoneText = phoneEditText.getText().toString();
                    prevAddressText = addressEditText.getText().toString();
                    setPageEditable();
                }
                editing = !editing;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bioEditText.setText(prevBioText);
                phoneEditText.setText(prevPhoneText);
                addressEditText.setText(prevAddressText);
                setPageViewOnly();
                editing = false;
            }
        });

        setPageViewOnly();
        // TODO: update stuff with user from db
        // TODO: get username from db
        String username = "Lojpurgator3000";
        userTextView.setText(getString(R.string.user_profile_text, username));
    }
}
