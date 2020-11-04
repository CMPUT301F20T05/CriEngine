package com.example.criengine.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.criengine.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomNavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomNavFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false);
    }
}