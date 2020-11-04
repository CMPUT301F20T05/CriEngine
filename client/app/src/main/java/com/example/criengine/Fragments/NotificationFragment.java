package com.example.criengine.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.criengine.Activities.AddBookActivity;
import com.example.criengine.Adapters.NotificationAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Adapters.MyBooksAdapter;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Notification;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification Fragment.
 * Displays all current notifications for the user.
 */
public class NotificationFragment extends Fragment {
    private NotificationAdapter notificationAdapter;
    private ListView notificationListView;
    private DatabaseWrapper dbw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dummy Code starts here.
        final Profile profile = new Profile();
        profile.addNotification(new Notification("Your request for \"Book 1\" was rejected."));
        profile.addNotification(new Notification("You got a new request for \"Book 2\"."));
        // Dummy code ends here.

        // Set the adapter.
        notificationAdapter = new NotificationAdapter(getContext(), profile.getNotifications(), profile);

        // Assign the view object.
        notificationListView = getView().findViewById(R.id.notificationsListView);
        notificationListView.setAdapter(notificationAdapter);

        // Opens to the book information screen when you click on a specific book.
        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // FIXME: redirect to view-book activity.
            }
        });
    }
}