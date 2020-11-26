package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.criengine.Adapters.NotificationAdapter;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * Notification Fragment.
 * Displays all current notifications for the user.
 */
public class NotificationFragment extends RootFragment {
    private NotificationAdapter notificationAdapter;
    private ListView notificationListView;
    private ArrayList<String> notificationList;
    private Profile myProfile;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Get the layout associated with the fragment.
     * @return The layout.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_notification;
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
        super.onCreate(savedInstanceState);

        // Assign the view object.
        notificationListView = getView().findViewById(R.id.notificationsListView);

        dbw.getProfile(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        notificationList = profile.getNotifications();
                        myProfile = profile;
                        // Set the adapter.
                        notificationAdapter = new NotificationAdapter(getContext(),notificationList,myProfile, dbw);
                        notificationListView.setAdapter(notificationAdapter);
                    }
                }
        );

        // Setup Swipe refresh layout to use default root fragment lister
        swipeRefreshLayout = getView().findViewById(R.id.notifications_swipe_refresh_layout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new RefreshRootListener(swipeRefreshLayout));
        }
    }
}