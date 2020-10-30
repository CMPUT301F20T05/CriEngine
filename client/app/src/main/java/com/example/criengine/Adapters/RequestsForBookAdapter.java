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
import com.example.criengine.Activities.MyBooksActivity;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import java.util.ArrayList;

/*
 * RequestsForBookAdapter is custom ArrayAdapter that can be used to show User profiles that have
 * requested a book in a ListView
 * @version 1.0
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
        String name = userRequests.get(position);

        // Set the text for names / buttons.
        username.setText(name);
        acceptUser.setText("✔");
        rejectUser.setText("❌");

        acceptUser.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: The book status should be changed to accepted.
                    // TODO: The user should be listed as a PotentialBorrower / Borrower on the book.
                    // TODO: Notify the accepted user.
                    // TODO: Notify the rejected users.
                    // TODO: Wipe the requested list.
                    Intent intent = new Intent(v.getContext(), MyBooksActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        );

        rejectUser.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: The book status should be changed to available IF no other requesters.
                    // TODO: The requester should be removed from the list.
                    // TODO: Notify the user of the rejection.
                }
            }
        );
        return view;
    }

}
