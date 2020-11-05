package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Database.DatabaseWrapper;

public abstract class RootFragment extends Fragment {
    protected RootActivity root;
    protected DatabaseWrapper dbw;

    public abstract int getFragmentLayout();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (RootActivity) getActivity();
        dbw = DatabaseWrapper.getWrapper();
        return inflater.inflate(getFragmentLayout(), container, false);
    }
}
