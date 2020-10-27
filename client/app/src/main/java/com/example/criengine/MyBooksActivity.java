package com.example.criengine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * My Books Activity
 * @version  1.0
 */
public class MyBooksActivity extends AppCompatActivity {
    private MyBooksAdapter myBooksListAdapter;
    private ArrayList<Book> myBooks;
    private ArrayList<Book> displayBooks;
    private Button addBookButton;
    private Button filterButton;
    private String filterStatus = "No Filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        // FIXME: Remove the dummy-code and replace with the following commented out line.
        // myBooks = Database.getMyProfile().getOwned();
        Book book1 = new Book("10", "Ralph", "This is a new Book!", "Author A.", "This is quite a nice description.", "1I2SS02", "Available");
        Book book2 = new Book("11", "Sam", "This is a different Book", "Author B.", "This is quite a good description.", "1I2SS03", "Available");
        book1.setRequesters("Joe Smith");
        book2.setBorrower("Bill John");
        Book[] books = {book1, book2};
        myBooks = new ArrayList<Book>();
        myBooks.addAll(Arrays.asList(books));

        displayBooks = new ArrayList<Book>();

        if (filterStatus.equals("Available")) {
            for (int i = 0; i < myBooks.size(); i++) {
                if (myBooks.get(i).getRequesters().size() == 0 && myBooks.get(i).getBorrower() == null) {
                    displayBooks.add(myBooks.get(i));
                }
            }
        } else if (filterStatus.equals("Requests")) {
            for (int i = 0; i < myBooks.size(); i++) {
                if (myBooks.get(i).getRequesters().size() > 0) {
                    displayBooks.add(myBooks.get(i));
                }
            }
        } else if (filterStatus.equals("Accepted")) {
            for (int i = 0; i < myBooks.size(); i++) {
                // FIXME need logic for the accepted status.
            }
        } else {

        }

        myBooksListAdapter = new MyBooksAdapter(this, myBooks);

        ListView bookNameTextView = findViewById(R.id.bookListView);
        bookNameTextView.setAdapter(myBooksListAdapter);

        bookNameTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // FIXME: redirect to book view
            }
        });

        addBookButton = findViewById(R.id.add_a_book);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME: redirect to add book activity.
            }
        });

        filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FilterFragment(myBooks).show(getSupportFragmentManager(), "Filter_Status");
            }
        });
    }
}