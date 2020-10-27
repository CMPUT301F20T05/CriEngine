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
 * MyBooksAdapter is custom ArrayAdapter that can be used to show Book instances in
 * a ListView
 * @version 1.0
 */
public class MyBooksAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> bookItems;
    private Context context;

    public MyBooksAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems) {
        super(context, 0, bookItems);
        this.context = context;
        this.bookItems = bookItems;
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

        TextView bookNameTextView = view.findViewById(R.id.bookNameTextView);
        TextView bookStatusTextView = view.findViewById(R.id.bookStatusTextView);
        Button bookActionButton = view.findViewById(R.id.bookActionButton);

        Book book = bookItems.get(position);

        bookNameTextView.setText(book.getTitle());
        bookStatusTextView.setText(book.getStatus());

        // Cancel -- we requested /  watching a book
        // Scan -- we have borrowed / Status == Accepted
        // Ok -- Rejected
        //if ( book.getRequesters().contains(Database.myUsername) || Database.getMyProfile.getWatchList().contains(book))
        if (book.getRequesters().size() > 0 ) {
            bookActionButton.setText("See Requests");
            bookStatusTextView.setText("Has Requests");
        } else if (book.getBorrower() != null) {
            bookActionButton.setText("Scan");
            bookStatusTextView.setText("Borrowed");
        } else if (book.getPotentialBorrower() != null) {
            bookStatusTextView.setText("Accepted");
            if (!book.getPotentialBorrower().getHandOffCompelte()) {
                bookActionButton.setText("Location");
            } else {
                bookActionButton.setText("Scan");
            }
        } else {
            bookStatusTextView.setText("Available");
            bookActionButton.setVisibility(View.GONE);
        }

        return view;
    }

}