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
            view = inflater.inflate(R.layout.list_format, parent, false);
        }

        TextView headerText = view.findViewById(R.id.headerText);
        TextView statusText = view.findViewById(R.id.statusText);
        Button actionButton = view.findViewById(R.id.actionButton);

        final Book book = bookItems.get(position);

        headerText.setText(book.getTitle());
        statusText.setText(book.getStatus());

        // Modify the button and status seen from the screen depending on the status of the book.
        switch (book.getStatus()) {
            case "requested":
                actionButton.setText("See Requests");
                statusText.setText("Has Requests");
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), RequestsForBookActivity.class);
                        intent.putExtra("Book", book);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            case "borrowed":
                actionButton.setText("Scan");
                statusText.setText("Borrowed");
                break;
            case "accepted":
                statusText.setText("Accepted");
                if (!book.getPotentialBorrower().getHandOffCompelte()) {
                    actionButton.setText("Location");
                } else {
                    actionButton.setText("Scan");
                }
                break;
            default:
                statusText.setText("Available");
                actionButton.setVisibility(View.GONE);
                break;
        }

        return view;
    }
}