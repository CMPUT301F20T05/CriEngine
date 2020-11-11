package com.example.criengine.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Adapters.NotificationAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.R;

/**
 * Notification Fragment.
 * Displays all current notifications for the user.
 */
public class NotificationFragment extends Fragment {
    private NotificationAdapter notificationAdapter;
    private ListView notificationListView;
    private DatabaseWrapper dbw;

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
        return inflater.inflate(R.layout.activity_notification, container, false);
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
                RootActivity.dummyProfile);

        // Assign the view object.
        notificationListView = getView().findViewById(R.id.notificationsListView);
        notificationListView.setAdapter(notificationAdapter);
    }
}