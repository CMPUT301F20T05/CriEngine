package com.example.criengine;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton userImageButton;
    private TextView userTextView;
    private EditText bioEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private Button saveButton;
    private Button editCancelButton;

    private String prevBioText;
    private String prevPhoneText;
    private String prevAddressText;

    private boolean editing = false;

    private void setPageViewOnly() {
        saveButton.setVisibility(View.INVISIBLE);
        editCancelButton.setText(R.string.edit_button);

        bioEditText.setKeyListener(null);
        phoneEditText.setKeyListener(null);
        addressEditText.setKeyListener(null);
    }

    private void setPageEditable() {
        saveButton.setVisibility(View.VISIBLE);
        editCancelButton.setText(R.string.cancel_button);

        bioEditText.setKeyListener((KeyListener) bioEditText.getTag());
        phoneEditText.setKeyListener((KeyListener) phoneEditText.getTag());
        addressEditText.setKeyListener((KeyListener) addressEditText.getTag());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get all the UI components
        userImageButton = findViewById(R.id.user_image);
        userTextView = findViewById(R.id.user_profile_text);
        bioEditText = findViewById(R.id.bio_text);
        phoneEditText = findViewById(R.id.phone_text);
        addressEditText = findViewById(R.id.addr_text);
        saveButton = findViewById(R.id.save_button);
        editCancelButton = findViewById(R.id.edit_cancel_button);

        // save the key listeners
        bioEditText.setTag(bioEditText.getKeyListener());
        phoneEditText.setTag(phoneEditText.getKeyListener());
        addressEditText.setTag(addressEditText.getKeyListener());

        editCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    bioEditText.setText(prevBioText);
                    phoneEditText.setText(prevPhoneText);
                    addressEditText.setText(prevAddressText);
                    setPageViewOnly();
                } else {
                    prevBioText = bioEditText.getText().toString();
                    prevPhoneText = phoneEditText.getText().toString();
                    prevAddressText = addressEditText.getText().toString();
                    setPageEditable();
                }
                editing = !editing;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send to database
                setPageViewOnly();
                editing = false;
            }
        });

        setPageViewOnly();
        // TODO: update stuff with user from db
    }
}
