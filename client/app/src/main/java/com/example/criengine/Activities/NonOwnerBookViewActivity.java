package com.example.criengine.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

/**
 * Handles displaying information about a book. Items such as the title, author, description etc.
 * Outstanding Issues:
 * - Does not retrieve all information from the database.
 */
public class NonOwnerBookViewActivity extends AppCompatActivity {
    private Button requestBookButton;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDetail;
    private TextView bookISBN;
    private TextView bookStatus;
    private TextView bookOwner;
    private TextView bookBorrower;
    private TextView bookBorrowerLabel;
    DatabaseWrapper dbw = DatabaseWrapper.getWrapper();
    private Book book;

    /**
     * A custom onCreate() method. Allows for the usage for fragments in the activity.
     * Without this method, there is the possible issue of the fragment being null when it is called
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Grabs the book.
        if (getIntent().getExtras() != null) {
            book = (Book) getIntent().getSerializableExtra("Book");
        } else {
            // TODO: If the intent fails to send, then redirect user to Error Screen. (in general this should not fail)
        }

        setContentView(R.layout.activity_non_owner_book_view);

        // my book exclusive UI component
        requestBookButton = findViewById(R.id.request_book_button);
        bookTitle = findViewById(R.id.bookView_title);
        bookDetail = findViewById(R.id.bookView_detail);
        bookAuthor = findViewById(R.id.bookView_author);
        bookISBN = findViewById(R.id.bookView_ISBN);
        bookStatus = findViewById(R.id.bookView_status);
        bookOwner = findViewById(R.id.bookView_owner);
        bookBorrower = findViewById(R.id.bookView_borrower);
        bookBorrowerLabel = findViewById(R.id.bookView_borrower_label);

        checkIfAvailable();

        bookTitle.setText(book.getTitle());
        bookDetail.setText(book.getDescription());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getIsbn());
        bookStatus.setText(book.getStatus());
        bookOwner.setText(book.getOwnerUsername());
        if (book.getBorrower() != null) {
            bookBorrower.setText(book.getBorrower());
        } else {
            bookBorrowerLabel.setVisibility(View.GONE);
            bookBorrower.setVisibility(View.GONE);
        }
        // TODO: Get the book image from the database.
//        bookImage.setImageURI(book.getImageURL());
    }

    /**
     * Check if the book can be requested. Sets the button to the appropriate text.
     */
    public void checkIfAvailable() {
        if (!book.getStatus().equals("available")) {
            requestBookButton.setEnabled(true);
            requestBookButton.setText("Request This Book");
            requestBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: Add the user as a requester.
                }
            });
        } else {
            requestBookButton.setEnabled(false);
            requestBookButton.setText("Book is not available");
        }
    }
}