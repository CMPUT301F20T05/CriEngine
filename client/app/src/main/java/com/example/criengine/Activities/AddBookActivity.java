package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

public class AddBookActivity extends AppCompatActivity {

    private Profile bookProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        // Get database for profile
        final DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

        // Set buttons and warning field to views
        final Button cancelButton = findViewById(R.id.newBookCancelButton);
        final Button saveButton = findViewById(R.id.newBookSaveButton);

        // The fields of the book
        final EditText bookTitle = findViewById(R.id.newBookTitle);
        final EditText bookDesc = findViewById(R.id.newBookDesc);
        final EditText bookAuthor = findViewById(R.id.newBookAuthor);
        final EditText bookISBN = findViewById(R.id.newBookISBN);
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
                if (!bookTitle.getText().toString().isEmpty() &&
                        !bookDesc.getText().toString().isEmpty() &&
                        !bookAuthor.getText().toString().isEmpty() &&
                        !bookISBN.getText().toString().isEmpty()) {

                    // Create the book
                    Book newBook = new Book(bookProfile.getUserID(),
                            bookTitle.getText().toString(),
                            bookAuthor.getText().toString(),
                            bookDesc.getText().toString(),
                            bookISBN.getText().toString(),
                            "Available");

                    // Adds in image url if present
                    if (!bookImageURL.getText().toString().isEmpty()) {
                        newBook.setImageURL(bookImageURL.getText().toString());
                    }

                    // Adds new book to database
                    dbw.addBook(newBook);
                    // Go back to my books
                    Intent intent = new Intent(v.getContext(), MyBooksActivity.class);
                    v.getContext().startActivity(intent);
                } else {
                    // Don't create a new book
                    warning.setText(R.string.book_warning);
                }
            }
        });

        // Goes back to my book activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyBooksActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }
}