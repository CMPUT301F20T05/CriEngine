package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Requested Books Activity
 * @version  1.0
 */
public class RequestActivity extends AppCompatActivity {
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    ArrayList<Book> borrowerBooks;
    DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // TODO: get borrowed or requested books from db:
        // borrowerBooks = dbw.getMyProfile().getbooksBorrowedOrRequested();
        Map<String, Object> testData = new HashMap<String, Object>(){{
                put("bookID", "123");
                put("owner", "JohnDoe");
                put("title", getString(R.string.lorem_ipsum));
                put("author", "JaneDoe");
                put("description", "A book");
                put("isbn", "isbn");
                put("status", "available");
                put("requesters", new ArrayList<String>());
        }};

        Book book1 = new Book("John Doe", getString(R.string.lorem_ipsum), "JaneDoe","A book", "isbn","available");
        Book book2 = new Book("John Doe", getString(R.string.lorem_ipsum), "JaneDoe","A book", "isbn","available");
        Book book3 = new Book("John Doe", getString(R.string.lorem_ipsum), "JaneDoe","A book", "isbn","available");
        Book book4 = new Book("John Doe", getString(R.string.lorem_ipsum), "JaneDoe","A book", "isbn","available");
        Book[] books = {
                book1, book2, book3, book4
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