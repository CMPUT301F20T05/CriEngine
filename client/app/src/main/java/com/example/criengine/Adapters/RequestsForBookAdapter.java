package com.example.criengine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Notification;
import com.example.criengine.R;
import java.util.ArrayList;

/*
 * RequestsForBookAdapter is custom ArrayAdapter that can be used to show User profiles that have
 * requested a book in a ListView
 */
public class RequestsForBookAdapter extends ArrayAdapter<String> {
    private ArrayList<String> userRequests;
    private Book book;
    private Context context;

    /**
     * Constructor.
     * @param context The context of the activity.
     * @param userRequests The list of users requesting a book.
     */
    public RequestsForBookAdapter(@NonNull Context context, @NonNull ArrayList<String> userRequests, Book book) {
        super(context, 0, userRequests);
        this.context = context;
        this.userRequests = userRequests;
        this.book = book;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Set the view.
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.user_accept_or_reject, parent, false);
        }

        // Assign the proper view for each button / text.
        TextView username = view.findViewById(R.id.user_name);
        Button acceptUser = view.findViewById(R.id.user_accept);
        Button rejectUser = view.findViewById(R.id.user_reject);

        // Get the name of the user.
        final String name = userRequests.get(position);

        // Set the text for names / buttons.
        username.setText(name);
        acceptUser.setText("✔");
        rejectUser.setText("❌");

        // Notifications to be sent to the appropriate users.
        Notification rejectedNotification = new Notification("Your request on \"" + book.getTitle() + "\" was refused.");
        Notification acceptedNotification = new Notification("Your request on \"" + book.getTitle() + "\" was accepted!");

        acceptUser.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    book.setStatus("accepted");
                    // TODO: The user should be listed as a PotentialBorrower / Borrower on the book. (?)
                    for (int i = 0; i < userRequests.size(); i++) {
                        if (userRequests.get(i).equals(name)) {
                            // TODO: Notify the accepted user + push changes to db.
                        } else {
                            // TODO: Notify the rejected user(s) + push changes to db.
                        }
                    }
                    userRequests.clear();
                    // TODO: Push changes to database.

                    Intent intent = new Intent(v.getContext(), RootActivity.class);
                    intent.putExtra("Index", RootActivity.PAGE.MY_BOOKS);
                    v.getContext().startActivity(intent);
                }
            }
        );

        rejectUser.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Notify the user of the rejection.
                    userRequests.remove(name);
                    // TODO: Push changes to database.

                    if (userRequests.size() == 0) {
                        book.setStatus("available");
                        // TODO: Push changes to database.

                        // Return to previous activity automatically.
                        Intent intent = new Intent(v.getContext(), RootActivity.class);
                        intent.putExtra("Index", RootActivity.PAGE.MY_BOOKS);
                        v.getContext().startActivity(intent);
                    }

                    notifyDataSetChanged();
                }
            }
        );
        return view;
    }
}
