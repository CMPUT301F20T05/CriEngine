package com.example.criengine.Activities;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * A person's personal profile activity where they can edit their profile.
 * Outstanding Issues:
 * - Does not push changes to the database.
 */
public class MyProfileActivity extends ProfileActivity {
    private Button cancelButton;
    private Button editSaveButton;

    private String prevBioText;
    private String prevPhoneText;
    private String prevAddressText;

    private boolean editing = false;

    public int getFragmentLayout() {
        return R.layout.activity_my_profile;
    }

    /**
     * Changes the page to be in "view only" mode meaning edits cannot be made.
     */
    private void setPageViewOnly() {
        cancelButton.setVisibility(View.INVISIBLE);
        editSaveButton.setText(R.string.edit_button);

        bioEditText.setKeyListener(null);
        phoneEditText.setKeyListener(null);
        addressEditText.setKeyListener(null);

        bioEditText.setBackground(null);
        phoneEditText.setBackground(null);
        addressEditText.setBackground(null);
    }

    /**
     * Changes the page to be in "editable" mode where the user can edit the page.
     */
    private void setPageEditable() {
        cancelButton.setVisibility(View.VISIBLE);
        editSaveButton.setText(R.string.save_button);

        bioEditText.setKeyListener((KeyListener) bioEditText.getTag());
        phoneEditText.setKeyListener((KeyListener) phoneEditText.getTag());
        addressEditText.setKeyListener((KeyListener) addressEditText.getTag());

        bioEditText.setBackgroundResource(android.R.drawable.edit_text);
        phoneEditText.setBackgroundResource(android.R.drawable.edit_text);
        addressEditText.setBackgroundResource(android.R.drawable.edit_text);
    }

    /**
     * Allows the back button to be used as cancel when in edit mode.
     * When in view only mode, the back button works as normal.
     */
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

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
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
        DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

        dbw.getProfile(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        userTextView.setText(getString(R.string.user_profile_text, profile.getUsername()));
                        bioEditText.setText(profile.getBio());
                        phoneEditText.setText(profile.getPhone());
                        addressEditText.setText(profile.getAddress());
                        System.out.println(profile);
                    }
                }
        );
    }
}
