package com.example.criengine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

import com.example.criengine.Activities.RequestsForBookActivity;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

import java.util.ArrayList;

/*
 * MyBooksAdapter is custom ArrayAdapter that can be used to show Book instances in
 * a ListView
 * @version 1.0
 */
public class MyBooksAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> bookItems;
    private Context context;

    /**
     * Constructor. Extends off of the array adapter.
     * @param context The context of the activity.
     * @param bookItems The ArrayList of books.
     */
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

        final Book book = bookItems.get(position);

        bookNameTextView.setText(book.getTitle());
        bookStatusTextView.setText(book.getStatus());

        // Modify the button and status seen from the screen depending on the status of the book.
        switch (book.getStatus()) {
            case "requested":
                bookActionButton.setText("See Requests");
                bookStatusTextView.setText("Has Requests");
                bookActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), RequestsForBookActivity.class);
                        intent.putExtra("Book", book);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            case "borrowed":
                bookActionButton.setText("Scan");
                bookStatusTextView.setText("Borrowed");
                break;
            case "accepted":
                bookStatusTextView.setText("Accepted");
                if (!book.getPotentialBorrower().getHandOffCompelte()) {
                    bookActionButton.setText("Location");
                } else {
                    bookActionButton.setText("Scan");
                }
                break;
            default:
                bookStatusTextView.setText("Available");
                bookActionButton.setVisibility(View.GONE);
                break;
        }

        return view;
    }
}