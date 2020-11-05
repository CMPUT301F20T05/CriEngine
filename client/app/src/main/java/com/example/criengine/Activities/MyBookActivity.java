package com.example.criengine.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

public class MyBookActivity extends BookActivity {
    private Button editCancelBookButton;
    private Button seeRequestsButton;
    private Button deleteSaveBookButton;

    String prevTitle;
    String prevDetail;
    String prevAuthor;

    private Book book;

    AlertDialog confirmDialog;

    private boolean editMode;

    /**
     * Disables editing of the EditText field
     * @param field: the edit text
     */
    private void disableEditText(EditText field) {
        field.setKeyListener(null);
        field.setBackgroundColor(Color.TRANSPARENT);
    }


    /**
     * Enables editing of the EditText field
     * @param field
     */
    private void enableEditText(EditText field) {
        field.setKeyListener((KeyListener) field.getTag());
        field.setBackgroundResource(android.R.drawable.edit_text);
    }

    /**
     * Changes page to view only mode and disables editing
     */
    private void setPageViewOnly() {
        editCancelBookButton.setText(R.string.edit_book);
        if (book.getRequesters().size() == 0) {
            // Only allow the user to see requests if there are requests.
            seeRequestsButton.setVisibility(View.GONE);
        }
        deleteSaveBookButton.setText(R.string.delete_book);

        disableEditText(bookTitle);
        disableEditText(bookDetail);
        disableEditText(bookAuthor);
    }

    /**
     * Changes page to edit mode and allows editing
     */
    private void setPageEditable() {
        deleteSaveBookButton.setText(R.string.save_button);
        seeRequestsButton.setVisibility(View.INVISIBLE);
        editCancelBookButton.setText(R.string.cancel_button);

        enableEditText(bookTitle);
        enableEditText(bookDetail);
        enableEditText(bookAuthor);
    }

    /**
     * Allows the back button to be used as cancel when in edit mode.
     * When in view only mode, the back button works as normal.
     */
    @Override
    public void onBackPressed() {
        if (editMode) {
            // same as cancel button
            bookTitle.setText(prevTitle);
            bookDetail.setText(prevDetail);
            bookAuthor.setText(prevAuthor);
            setPageViewOnly();
            editMode = false;
        } else {
            // go back to previous activity
            Intent intent = new Intent(this, RootActivity.class);
            intent.putExtra("Index", RootActivity.PAGE.MY_BOOKS);
            startActivity(intent);
        }
    }

    /**
     * Set the layout.
     */
    @Override
    protected void inflate() {
        setContentView(R.layout.activity_my_book);
    }

    /**
     * A custom OnCreate method which will
     */
    @Override
    protected void customOnCreate() {
        // Grabs the book.
        if (getIntent().getExtras() != null) {
            book = (Book) getIntent().getSerializableExtra("Book");
        } else {
            // TODO: If the intent fails to send, then redirect user to Error Screen. (in general this should not fail)
        }

        // my book exclusive UI component
        editCancelBookButton = findViewById(R.id.edit_book_button);
        seeRequestsButton = findViewById(R.id.see_requests_button);
        deleteSaveBookButton = findViewById(R.id.delete_book_button);
        confirmDialog = confirmDelete();

        editCancelBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    bookTitle.setText(prevTitle);
                    bookDetail.setText(prevDetail);
                    bookAuthor.setText(prevAuthor);
                    setPageViewOnly();
                } else {
                    prevTitle = bookTitle.getText().toString();
                    prevDetail = bookDetail.getText().toString();
                    prevAuthor = bookAuthor.getText().toString();
                    setPageEditable();
                }
                editMode = !editMode;
            }
        });

        seeRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RequestsForBookActivity.class);
                intent.putExtra("Book", book);
                view.getContext().startActivity(intent);
            }
        });

        deleteSaveBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    setPageViewOnly();
                    editMode = !editMode;
                    // TODO: send data to db
                } else {
                    confirmDialog.show();
                }
            }
        });

        // turn off editing for the two fields
        bookISBN.setInputType(InputType.TYPE_NULL);
        bookISBN.setBackgroundResource(android.R.color.transparent);
        bookStatus.setInputType(InputType.TYPE_NULL);
        bookStatus.setBackgroundResource(android.R.color.transparent);
        bookOwner.setInputType(InputType.TYPE_NULL);
        bookOwner.setBackgroundResource(android.R.color.transparent);
        bookBorrower.setInputType(InputType.TYPE_NULL);
        bookBorrower.setBackgroundResource(android.R.color.transparent);

        bookTitle.setText(book.getTitle());
        bookDetail.setText(book.getDescription());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getIsbn());
        bookStatus.setText(book.getStatus());
        bookOwner.setText(book.getOwner());
        if (book.getBorrower() != null) {
            bookBorrower.setText(book.getBorrower());
        } else {
            bookBorrowerLabel.setVisibility(View.GONE);
            bookBorrower.setVisibility(View.GONE);
        }
//        bookImage.setImageURI(mockBook.getImageURL());

        editMode = false;
        setPageViewOnly();
    }

    // code from: https://stackoverflow.com/questions/11740311/android-confirmation-message-for-delete
    private AlertDialog confirmDelete()
    {
        return new AlertDialog.Builder(this)
                // set title and message and button behaviors
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete this book?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // TODO: remove book from database
                        dialog.dismiss();
                        onBackPressed();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}