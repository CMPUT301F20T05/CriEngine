package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.criengine.Adapters.NotificationAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Notification;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;

/**
 * Notification Activity. Displays all current notifications that a user may have.
 * @version  1.0
 */
public class NotificationActivity extends AppCompatActivity {
    private NotificationAdapter notificationAdapter;
    private ListView notificationListView;
    private DatabaseWrapper dbw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Dummy Code starts here.
        final Profile profile = new Profile();
        profile.addNotification(new Notification("Your request for \"Book 1\" was rejected."));
        profile.addNotification(new Notification("You got a new request for \"Book 2\"."));
        // Dummy code ends here.

        // Set the adapter.
        notificationAdapter = new NotificationAdapter(this, profile.getNotifications(), profile);

        // Assign the view object.
        notificationListView = findViewById(R.id.notificationsListView);
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
