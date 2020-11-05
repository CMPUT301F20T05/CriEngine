package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.R;

/**
 * General ProfileActivity class that sets up common features.
 */
public abstract class ProfileFragment extends RootFragment {

    ImageButton userImageButton;
    TextView userTextView;
    EditText bioEditText;
    EditText phoneEditText;
    EditText addressEditText;

    @Override
    public int getFragmentLayout() {
        return R.layout.component_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Must setContentView(layoutID) in child before calling this super method.
        super.onViewCreated(view, savedInstanceState);

        // get all the UI components
        userImageButton = view.findViewById(R.id.user_image);
        userTextView = view.findViewById(R.id.user_profile_text);
        bioEditText = view.findViewById(R.id.bio_text);
        phoneEditText = view.findViewById(R.id.phone_text);
        addressEditText = view.findViewById(R.id.addr_text);

        // save the key listeners
        bioEditText.setTag(bioEditText.getKeyListener());
        phoneEditText.setTag(phoneEditText.getKeyListener());
        addressEditText.setTag(addressEditText.getKeyListener());
    }
}
