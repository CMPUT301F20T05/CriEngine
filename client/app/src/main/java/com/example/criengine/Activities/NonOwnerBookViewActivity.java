package com.example.criengine.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDetail;
    private EditText bookISBN;
    private EditText bookStatus;
    private EditText bookOwner;
    private EditText bookBorrower;
    private TextView bookBorrowerLabel;
    private DatabaseWrapper dbw = DatabaseWrapper.getWrapper();
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
            Intent intent = new Intent(this, SomethingWentWrong.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_non_owner_book_view);

        // My book UI components.
        requestBookButton = findViewById(R.id.request_book_button);
        bookTitle = findViewById(R.id.bookView_title);
        bookDetail = findViewById(R.id.bookView_detail);
        bookAuthor = findViewById(R.id.bookView_author);
        bookISBN = findViewById(R.id.bookView_ISBN);
        bookStatus = findViewById(R.id.bookView_status);
        bookOwner = findViewById(R.id.bookView_owner);
        bookBorrower = findViewById(R.id.bookView_borrower);
        bookBorrowerLabel = findViewById(R.id.bookView_borrower_label);

        disableEditText(bookTitle);
        disableEditText(bookDetail);
        disableEditText(bookAuthor);
        disableEditText(bookISBN);
        disableEditText(bookStatus);
        disableEditText(bookOwner);
        disableEditText(bookBorrower);

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
        // TODO: Need to retrieve user profile & check if it already exists in list of requesters.
        if (!book.getStatus().equals("borrowed") && !book.getStatus().equals("accepted")) {
            requestBookButton.setEnabled(true);
            requestBookButton.setText("Request This Book");
            requestBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: Add the user as a requester in the database.
                    requestBookButton.setEnabled(false);
                    requestBookButton.setText("Request Sent");
                }
            });
        } else {
            requestBookButton.setEnabled(false);
            requestBookButton.setText("Cannot Request This Book");
        }
    }

    /**
     * Allows for incorporating the "Component_book.xml" file into this activity.
     * Treats the EditText as a TextView but keeps the formatting the same.
     * @param editText The view object.
     */
    public void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.BLACK);
    }
}