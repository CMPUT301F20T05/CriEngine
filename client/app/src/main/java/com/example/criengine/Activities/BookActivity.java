package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.criengine.R;

/**
 * Abstract book activity class. Main purpose is to set up the view objects.
 */
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

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Must setContentView(layoutID) in child activity before calling super method
        super.onCreate(savedInstanceState);
        inflate();

        // Set the view object for each attribute.
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
        customOnCreate();
    }

    /**
     * A custom onCreate() method. Allows for the usage for fragments in the activity.
     * Without this method, there is the possible issue of the fragment being null when it is called
     */
    abstract protected void customOnCreate();

    /**
     * Allows for inflating of the activity and assigning it a layout.
     */
    abstract protected void inflate();
}