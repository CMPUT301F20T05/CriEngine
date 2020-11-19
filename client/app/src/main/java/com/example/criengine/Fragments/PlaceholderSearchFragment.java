package com.example.criengine.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.criengine.R;

/**
 * Placeholder for search fragment to access user profiles
 */
public class PlaceholderSearchFragment extends RootFragment {

    private final String testUID = "j3sxbUfANMXqOvXjN4gXj3ofluo2";
    private Button testUserButton;

    /**
     * Returns the layout.
     * @return The layout.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.placeholder_search_fragment;
    }

    /**
     * Called when creating the fragment.
     * @param view The view.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testUserButton = view.findViewById(R.id.test_user_profile_button);
        testUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(testUID, "userId");
                UserProfileFragment frag = new UserProfileFragment();
                frag.setArguments(bundle);

                // TODO: currently doesn't work
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.addToBackStack(null);
//                transaction.replace(R.id.view_pager, frag).commit();
            }
        });
    }
}
