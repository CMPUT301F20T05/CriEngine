package com.example.criengine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
 * BookListAdapter is custom ArrayAdapter that can be used to show Book instances in
 * a ListView
 * @version 1.0
 */
public class BorrowerBooksListAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> bookItems;
    private Context context;

    public BorrowerBooksListAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems) {
        super(context, 0, bookItems);
        this.context = context;
        this.bookItems = bookItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get Item Data
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.book_item, parent, false);
        }

        TextView bookNameTextView = view.findViewById(R.id.bookNameTextView);
        TextView bookStatusTextView = view.findViewById(R.id.bookStatusTextView);
        Button bookActionButton = view.findViewById(R.id.bookActionButton);

        Book book = bookItems.get(position);

        bookNameTextView.setText(book.getTitle());

        bookStatusTextView.setText(book.getStatus());

        // TODO: use db username and watchlist to determine state
        // Cancel -- we requested /  watching a book
        // Scan -- we have borrowed / Status == Accepted
        // Ok -- Rejected
        //if ( book.getRequesters().contains(Database.myUsername) || Database.getMyProfile.getWatchList().contains(book))
        DatabaseWrapper dbw = DatabaseWrapper.getWrapper();
        if (book.getRequesters().contains(dbw.userId)) {
            bookActionButton.setText("Cancel");
            bookActionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: remove request from profile && book
                        }
                    }
            );

        } else if (book.getBorrower() == dbw.userId) {
            bookStatusTextView.setTextColor(view.getResources().getColor(R.color.status_accepted));
            bookActionButton.setText("Scan");
            bookActionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO : navigate to scan page
                        }
                    }
            );
        } else {
            bookStatusTextView.setText("Rejected");
            bookStatusTextView.setTextColor(view.getResources().getColor(R.color.status_rejected));
            bookActionButton.setText("Ok");
            bookActionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: remove request from profile
                        }
                    }
            );
        }


        return view;
    }

}
