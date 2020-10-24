package com.example.criengine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Requested Books Activity
 * @version  1.0
 */
public class RequestActivity extends AppCompatActivity {
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    ArrayList<Book> borrowerBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // TODO: FIX ME
        // borrowerBooks = Database.getMyProfile().getbooksBorrowedOrRequested();
        Book[] books = {
                new Book("This is a really looooooooooooooooooooooooooooooooooooooooong book name"),
                new Book("This is a really looooooooooooooooooooooooooooooooooooooooong book name"),
                new Book("This is a really looooooooooooooooooooooooooooooooooooooooong book name"),
                new Book("This is a really looooooooooooooooooooooooooooooooooooooooong book name"),
                new Book("This is a really looooooooooooooooooooooooooooooooooooooooong book name"),
                new Book("This is a really looooooooooooooooooooooooooooooooooooooooong book name"),
                new Book("This is a really looooooooooooooooooooooooooooooooooooooooong book name")

        };
        borrowerBooks = new ArrayList<Book>();
        borrowerBooks.addAll(Arrays.asList(books));
        borrowerBooksListAdapter = new BorrowerBooksListAdapter(this, borrowerBooks);

        ListView bookNameTextView = findViewById(R.id.bookListView);
        bookNameTextView.setAdapter(borrowerBooksListAdapter);

        bookNameTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: redirect to book view
            }
        });
    }
}