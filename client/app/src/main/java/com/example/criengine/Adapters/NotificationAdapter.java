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

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Notification;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import java.util.ArrayList;

/*
 * Custom ArrayAdapter that can be used to show notification instances in a ListView.
 */
public class NotificationAdapter extends ArrayAdapter<String> {
    private ArrayList<String> notificationItems;
    private Context context;
    private Profile profile;
    private DatabaseWrapper dbw;

    /**
     * Constructor. Extends off of the array adapter.
     * @param context The context of the activity.
     * @param notificationItems The ArrayList of notifications.
     */
    public NotificationAdapter(@NonNull Context context,
                               @NonNull ArrayList<String> notificationItems,
                               Profile profile,
                               DatabaseWrapper dbw
                               ) {
        super(context, 0, notificationItems);
        this.context = context;
        this.notificationItems = notificationItems;
        this.profile = profile;
        this.dbw = dbw;
    }

    /**
     * Returns a view with the properly formatted information.
     * @param position The position from the list.
     * @param convertView The old view to reuse (if possible).
     * @param parent The parent view group.
     * @return The view that displays the formatted data at the specified position in the data set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get Item Data
        View view = convertView;

        if( view == null ) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_format, parent, false);
        }

        // Assign all view objects.
        TextView notificationDescription = view.findViewById(R.id.headerText);
        TextView notificationDate = view.findViewById(R.id.statusText);
        Button dismissButton = view.findViewById(R.id.actionButton);

        // Object to be displayed.
        final Notification notification = new Notification(notificationItems.get(position));

        // Set the text and date to their proper values.
        notificationDescription.setText(notification.getDescription());
        notificationDate.setText(notification.getDate());

        // Set the button text.
        dismissButton.setText("Dismiss");
        final int finalPos = position;

        // Set an action for the button when its clicked.
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationItems.remove(finalPos);

                // Set the new notification list. From here we can push to the database.
                profile.setNotifications(notificationItems);
                dbw.addProfile(profile);

                notifyDataSetChanged();
                dbw.notifyChanged();
            }
        });

        return view;
    }
}