package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.criengine.R;

public abstract class BookActivity extends AppCompatActivity {
    EditText bookTitle;
    EditText bookDetail;
    EditText bookAuthor;
    EditText bookISBN;
    EditText bookStatus;
    ImageView bookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Must setContentView(layoutID) in child activity before calling super method
        super.onCreate(savedInstanceState);

        bookTitle = findViewById(R.id.bookView_title);
        bookDetail = findViewById(R.id.bookView_detail);
        bookAuthor = findViewById(R.id.bookView_author);
        bookStatus = findViewById(R.id.bookView_status);
        bookISBN = findViewById(R.id.bookView_ISBN);
        bookImage = findViewById(R.id.bookView_image);

        bookTitle.setTag(bookTitle.getKeyListener());
        bookDetail.setTag(bookDetail.getKeyListener());
        bookAuthor.setTag(bookAuthor.getKeyListener());
    }


    /**
     * TODO:
     *  - modes:
     *      - request book (if book.status = available)
     *      - add book to wishlist (if book.status = borrowed)
     *      - manage book (if book.owner = account.username)
     *          - buttons:
     *              - "edit book"
     *              - "see requests"
     *              - "delete book" => confirmation popup?
     */
}