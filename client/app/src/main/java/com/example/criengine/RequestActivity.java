package com.example.criengine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

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
    }
}