package com.example.criengine.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.criengine.Fragments.ProfileFragment;
import com.example.criengine.Interfaces.IOnBackPressed;
import com.example.criengine.R;

public class ErrorFragment extends RootFragment {


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