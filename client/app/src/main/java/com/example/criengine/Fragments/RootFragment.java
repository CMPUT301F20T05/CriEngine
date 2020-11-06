package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Database.DatabaseWrapper;

/**
 * Generalized root fragment class.
 * Initializes the database wrapper that all rootFragments will require.
 */
public abstract class RootFragment extends Fragment {
    protected RootActivity root;
    protected DatabaseWrapper dbw;

    /**
     * Returns the fragment layout.
     * @return The fragment layout.
     */
    public abstract int getFragmentLayout();

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     * @return The view hierarchy associated with the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (RootActivity) getActivity();
        dbw = DatabaseWrapper.getWrapper();
        return inflater.inflate(getFragmentLayout(), container, false);
    }
}
