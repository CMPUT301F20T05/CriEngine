package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.criengine.R;

public abstract class BookActivity extends AppCompatActivity {
    EditText bookTitle;
    EditText bookDetail;
    EditText bookAuthor;
    EditText bookISBN;
    EditText bookStatus;
    EditText bookOwner;
    TextView bookBorrowerLabel;
    EditText bookBorrower;
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
        bookOwner = findViewById(R.id.bookView_owner);
        bookBorrowerLabel = findViewById(R.id.bookView_borrower_label);
        bookBorrower = findViewById(R.id.bookView_borrower);

        bookTitle.setTag(bookTitle.getKeyListener());
        bookDetail.setTag(bookDetail.getKeyListener());
        bookAuthor.setTag(bookAuthor.getKeyListener());
    }
}