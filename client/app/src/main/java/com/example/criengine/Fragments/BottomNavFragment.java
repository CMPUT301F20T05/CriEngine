package com.example.criengine.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.criengine.Activities.MyBookActivity;
import com.example.criengine.Activities.MyBooksActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * BottomNavigation Fragment that can be used in other Activities
 * A {@link Fragment} subclass.
 */
public class BottomNavFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new onNavItemSelect());
        // Select None
    }

    /**
     * OnItemSelectedListener for the BottomNavigationView
     * Starts the RootActivity at the selected menu page
     */
    private class onNavItemSelect implements BottomNavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem selectedItem) {
            Intent intent = new Intent(getContext(), RootActivity.class);
            switch(selectedItem.getItemId()) {
                case R.id.bottom_navigation_item_my_books:
                    intent.putExtra("Index", RootActivity.PAGE.MY_BOOKS );
                    break;
                case R.id.bottom_navigation_item_my_requests:
                    intent.putExtra("Index", RootActivity.PAGE.REQUESTS );
                    break;
                case R.id.bottom_navigation_item_notifications:
                    intent.putExtra("Index", RootActivity.PAGE.NOTIFICATIONS );
                    break;
                case R.id.bottom_navigation_item_profile:
                    intent.putExtra("Index", RootActivity.PAGE.PROFILE );
                    break;
                case R.id.bottom_navigation_item_search:
                    intent.putExtra("Index", RootActivity.PAGE.SEARCH );
                    break;
            }
            startActivity(intent);

            return true;
        }
    }

}