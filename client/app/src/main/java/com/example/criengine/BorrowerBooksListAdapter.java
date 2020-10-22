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
 */
public class BorrowerBooksListAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> bookItems;
    private Context context;

    public BorrowerBooksListAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems) {
        super(context, 0, bookItems);
        this.context = context;
        this.bookItems = bookItems;
    }

    // @Override
    // public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    // }

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

        bookStatusTextView.setText("Available");    // TODO: FIX ME
        // bookStatusTextView.setText(book.getStatus());


        // Cancel -- we requested /  watching a book
        // Scan -- we have borrowed / Status == Accepted
        // Ok -- Rejected
        //if ( book.getRequesters().contains(Database.myUsername) || Database.getMyProfile.getWatchList().contains(book))
        if ( book.getRequesters().contains("genericUsername123")  ) { // TODO: FIX ME
            bookActionButton.setText("Cancel");
        } else if(book.getBorrower() == "genericUsername123") {
            bookActionButton.setText("Scan");
        } else {
            bookStatusTextView.setText("Rejected");
            bookActionButton.setText("Ok");
        }


        return view;
    }

}
