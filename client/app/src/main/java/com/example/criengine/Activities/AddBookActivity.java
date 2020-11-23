package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Database.GoogleBooksWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Allows for the addition of a new book to the database through either manual entries or through
 * the scanning feature.
 * Outstanding Issues:
 * - Implement the scanning feature.
 * - Implement the addition of images.
 */
public class AddBookActivity extends AppCompatActivity {
    private Profile bookProfile;

    EditText bookTitle;
    EditText bookDesc;
    EditText bookAuthor;
    EditText bookISBN;

    final int SCAN_RESULT_CODE = 0;

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        // Get database for profile
        final DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

        // Set buttons and warning field to views
        final Button cancelButton = findViewById(R.id.newBookCancelButton);
        final Button saveButton = findViewById(R.id.newBookSaveButton);
        final Button scanButton = findViewById(R.id.newBookScanButton);

        // The fields of the book
        bookTitle = findViewById(R.id.newBookTitle);
        bookDesc = findViewById(R.id.newBookDesc);
        bookAuthor = findViewById(R.id.newBookAuthor);
        bookISBN = findViewById(R.id.newBookISBN);
        // TODO: replace editTExt with an actual image
        final EditText bookImageURL = findViewById(R.id.newBookImageURL);
        final TextView warning = findViewById(R.id.newBookWarning);


        // Get the current profile
        dbw.getProfile(dbw.userId).addOnSuccessListener(new OnSuccessListener<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                bookProfile = profile;
            }
        });

        // Save button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookTitle.getText().toString().isEmpty()
                        || bookDesc.getText().toString().isEmpty()
                        || bookAuthor.getText().toString().isEmpty()
                        || bookISBN.getText().toString().isEmpty()) {

                    warning.setText(R.string.book_warning);
                    return;
                }

                // Create the book
                Book newBook = new Book(bookProfile.getUserID(),
                        bookProfile.getUsername(),
                        bookTitle.getText().toString(),
                        bookAuthor.getText().toString(),
                        bookDesc.getText().toString(),
                        bookISBN.getText().toString(),
                        "available");

                // Adds in image url if present
                if (!bookImageURL.getText().toString().isEmpty()) {
                    newBook.setImageURL(bookImageURL.getText().toString());
                }

                // Adds new book to database
                dbw.addBook(newBook);
                // Go back to my-books
                onBackPressed();
            }
        });

        // Goes to scan activity
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBookActivity.this, ScanActivity.class);
                startActivityForResult(intent, SCAN_RESULT_CODE);
            }
        });

        // Goes back to my book activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * Overrides the back button so it returns to the main activity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RootActivity.class);
        intent.putExtra("Index", RootActivity.PAGE.MY_BOOKS);
        startActivity(intent);
    }

    /**
     * On return from the scan activity, look up book corresponding to ISBN code and fill data
     * @param requestCode: the request code corresponding to the scan activity
     * @param resultCode: the result code of if the activity was successful
     * @param data: payload of intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SCAN_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String barcodeData = data.getStringExtra("barcode");
                Log.d("testing", "barcode data=" + barcodeData);

                try {
                    Book book = new GoogleBooksWrapper().getBook(barcodeData);
                    Log.d("testing book", "title");
                    bookTitle.setText(book.getTitle());
                    bookAuthor.setText(book.getAuthor());
                    bookDesc.setText(book.getDescription());
                    bookISBN.setText(book.getIsbn());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}