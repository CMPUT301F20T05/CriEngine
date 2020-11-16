package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.criengine.R;

/**
 * Error/placeholder Fragment.
 * Temporary placeholder for search page while it is developed.
 * To be used as error landing later.
 */
public class ErrorFragment extends RootFragment {

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
    }

    /**
     * Returns the layout.
     * @return The layout.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_error;
    }
}