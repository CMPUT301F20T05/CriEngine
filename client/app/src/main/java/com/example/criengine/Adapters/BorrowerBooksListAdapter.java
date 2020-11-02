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
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

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
            view = inflater.inflate(R.layout.list_format, parent, false);
        }

        TextView headerText = view.findViewById(R.id.headerText);
        TextView statusText = view.findViewById(R.id.statusText);
        Button actionButton = view.findViewById(R.id.actionButton);

        Book book = bookItems.get(position);

        headerText.setText(book.getTitle());

        statusText.setText(book.getStatus());

        // TODO: use db username and watchlist to determine state
        // Cancel -- we requested /  watching a book
        // Scan -- we have borrowed / Status == Accepted
        // Ok -- Rejected
        //if ( book.getRequesters().contains(Database.myUsername) || Database.getMyProfile.getWatchList().contains(book))
        DatabaseWrapper dbw = DatabaseWrapper.getWrapper();
        if (book.getRequesters().contains(dbw.userId)) {
            actionButton.setText("Cancel");
            actionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: remove request from profile && book
                        }
                    }
            );

        } else if (book.getBorrower() == dbw.userId) {
            statusText.setTextColor(view.getResources().getColor(R.color.status_accepted));
            actionButton.setText("Scan");
            actionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO : navigate to scan page
                        }
                    }
            );
        } else {
            statusText.setText("Rejected");
            statusText.setTextColor(view.getResources().getColor(R.color.status_rejected));
            actionButton.setText("Ok");
            actionButton.setOnClickListener(
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
