package com.example.criengine.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Interfaces.IOnBackPressed;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A person's own profile fragment where they can edit their profile.
 * Outstanding Issues:
 * - Does not push changes to the database.
 */
public class MyProfileFragment extends ProfileFragment implements IOnBackPressed {
    private Profile myProfile;

    private Button logoutCancelButton;
    private Button editSaveButton;

    SwipeRefreshLayout swipeRefreshLayout;

    private String prevBioText;
    private String prevPhoneText;
    private String prevAddressText;

    private boolean editing = false;

    /**
     * Changes the page to be in "view only" mode meaning edits cannot be made.
     */
    private void setPageViewOnly() {
        logoutCancelButton.setText(R.string.logout_button);
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
        logoutCancelButton.setText(R.string.cancel_button);
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
    public boolean onBackPressed() {
        if (editing) {
            // same as cancel button
            bioEditText.setText(prevBioText);
            phoneEditText.setText(prevPhoneText);
            addressEditText.setText(prevAddressText);
            setPageViewOnly();
            editing = false;
            return true;
        }
        return false;
    }

    /**
     * Returns the layout for the fragment.
     * @return The layout.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_my_profile;
    }

    /**
     * Called immediately after onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
     * android.os.Bundle) has returned, but before any saved state has been restored in to the view.
     * @param view The view.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // new UI components
        logoutCancelButton = view.findViewById(R.id.logout_cancel_button);
        editSaveButton = view.findViewById(R.id.edit_save_button);

        // Setup Swipe refresh layout to use default root fragment lister
        swipeRefreshLayout = getView().findViewById(R.id.my_profile_swipe_refresh_layout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new RefreshRootListener(swipeRefreshLayout));
        }

        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    // pressing save on data
                    myProfile.setBio(bioEditText.getText().toString());
                    myProfile.setPhone(phoneEditText.getText().toString());
                    myProfile.setAddress(addressEditText.getText().toString());
                    dbw.addProfile(myProfile);
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

        logoutCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    // cancel
                    bioEditText.setText(prevBioText);
                    phoneEditText.setText(prevPhoneText);
                    addressEditText.setText(prevAddressText);
                    setPageViewOnly();
                    editing = false;
                } else {
                    // logout
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    v.getContext().startActivity(intent);
                }
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
                        myProfile = profile;
                        userTextView.setText(getString(R.string.user_profile_text, profile.getUsername()));
                        bioEditText.setText(profile.getBio());
                        phoneEditText.setText(profile.getPhone());
                        addressEditText.setText(profile.getAddress());
                    }
                }
        );
    }
}
