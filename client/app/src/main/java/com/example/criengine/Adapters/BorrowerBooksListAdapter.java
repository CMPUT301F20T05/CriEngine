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
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
        final Button bookActionButton = view.findViewById(R.id.bookActionButton);

        final Book book = bookItems.get(position);
        final DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

        bookNameTextView.setText(book.getTitle());
        bookStatusTextView.setText(book.getStatus());

        // TODO: use db username and watchlist to determine state
        // Cancel -- we requested /  watching a book
        // Scan -- we have borrowed / Status == Accepted
        // Ok -- Rejected
        //if ( book.getRequesters().contains(Database.myUsername) || Database.getMyProfile.getWatchList().contains(book))
        if (book.getRequesters().contains(dbw.userId)) {
            bookActionButton.setText("Cancel");
        } else if (book.getBorrower() == dbw.userId) {
            bookStatusTextView.setTextColor(view.getResources().getColor(R.color.status_accepted));
            bookActionButton.setText("Scan");
        } else {
            bookStatusTextView.setText("Rejected");
            bookStatusTextView.setTextColor(view.getResources().getColor(R.color.status_rejected));
            bookActionButton.setText("Ok");
        }

        bookActionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (bookActionButton.getText().toString()) {
                            case("Scan"):
                                // TODO: navigate to scan page
                                break;
                            case("Cancel"):
                                // TODO: removes request from book
                                // Do not break, execute Ok case as well
                            case("Ok"):
                                // TODO: removes request from profile
                                // TODO: reload ListView
                                break;
                        }
                    }
                }
        );

        return view;
    }
}
