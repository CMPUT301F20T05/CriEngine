package com.example.criengine.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import com.example.criengine.R;

public class MyBookActivity extends BookActivity {

    private Button editBookButton;
    private Button seeRequestsButton;
    private Button deleteBookButton;

    AlertDialog confirmDialog;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_book);
        super.onCreate(savedInstanceState);

        // my book exclusive UI component
        editBookButton = findViewById(R.id.edit_book_button);
        seeRequestsButton = findViewById(R.id.see_requests_button);
        deleteBookButton = findViewById(R.id.delete_book_button);

        confirmDialog = confirmDelete();

//        editBookButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /**
//                 * todo: edit button
//                 *  - on button click: turn on edit mode
//                 *      - title, details, author, status becomes editable
//                 *      - bottom right button becomes "Save"
//                 *          - Save edited text on page + send to database, turn off edit mode
//                 *      - bottom left button becomes "Cancel"
//                 *          - Undo changes and turn off edit mode
//                 */
//            }
//        });
//
//        seeRequestsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: redirect to requests for that book
//            }
//        });
//
//        deleteBookButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                confirmDialog.show();
//            }
//        });

        // todo: set book data
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
                        MyBookActivity.super.onBackPressed();
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