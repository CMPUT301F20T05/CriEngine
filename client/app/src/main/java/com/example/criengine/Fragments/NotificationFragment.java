package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Adapters.NotificationAdapter;
import com.example.criengine.R;

/**
 * Notification Fragment.
 * Displays all current notifications for the user.
 */
public class NotificationFragment extends RootFragment {
    private NotificationAdapter notificationAdapter;
    private ListView notificationListView;

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

        // Set the adapter.
        notificationAdapter = new NotificationAdapter(getContext(),
                RootActivity.dummyProfile.getNotifications(),
                RootActivity.dummyProfile,
                dbw
            );

        // Assign the view object.
        notificationListView = getView().findViewById(R.id.notificationsListView);
        notificationListView.setAdapter(notificationAdapter);
    }
}