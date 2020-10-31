package com.example.criengine.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.criengine.Objects.Notification;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import java.util.ArrayList;

/*
 * Custom ArrayAdapter that can be used to show notification instances in a ListView
 * @version 1.0
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {
    private ArrayList<Notification> notificationItems;
    private Context context;
    private Profile profile;

    /**
     * Constructor. Extends off of the array adapter.
     * @param context The context of the activity.
     * @param notificationItems The ArrayList of notifications.
     */
    public NotificationAdapter(@NonNull Context context, @NonNull ArrayList<Notification> notificationItems, Profile profile) {
        super(context, 0, notificationItems);
        this.context = context;
        this.notificationItems = notificationItems;
        this.profile = profile;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get Item Data
        View view = convertView;

        if( view == null ) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.book_item, parent, false);
        }

        // Assign all view objects.
        TextView notificationDescription = view.findViewById(R.id.bookNameTextView);
        TextView notificationDate = view.findViewById(R.id.bookStatusTextView);
        Button dismissButton = view.findViewById(R.id.bookActionButton);

        // Get the object that was clicked on from the list.
        final Notification notification = notificationItems.get(position);

        // Set the text and date to their proper values.
        notificationDescription.setText(notification.getDescription());
        notificationDate.setText(notification.getDate());

        // Set the button text.
        dismissButton.setText("Dismiss");

        // Set an action for the button when its clicked.
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationItems.remove(notification);

                // Set the new notification list. From here we can push to the database.
                profile.setNotifications(notificationItems);
                // TODO: push changes to database.

                notifyDataSetChanged();
            }
        });

        return view;
    }
}