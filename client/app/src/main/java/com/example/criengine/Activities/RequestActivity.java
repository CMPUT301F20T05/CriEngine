package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Requested Books Activity. Handles displaying outgoing requests for the user.
 * Outstanding Issues:
 * - Does not currently redirect the user to the book view upon clicking the item in the list.
 */
public class RequestActivity extends AppCompatActivity {
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    ArrayList<Book> borrowerBooks;
    DatabaseWrapper dbw;

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        dbw = DatabaseWrapper.getWrapper();

        borrowerBooks = new ArrayList<>();
//        borrowerBooks.addAll(Arrays.asList(books));
        borrowerBooksListAdapter = new BorrowerBooksListAdapter(this, borrowerBooks);

        ListView headerText = findViewById(R.id.bookListView);
        headerText.setAdapter(borrowerBooksListAdapter);

        dbw.getProfile(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        dbw.getBorrowedOrRequestedBooks(profile).addOnSuccessListener(
                                new OnSuccessListener<List<Book>>() {
                                    @Override
                                    public void onSuccess(List<Book> books) {
                                        borrowerBooks.addAll(books);
                                        borrowerBooksListAdapter.notifyDataSetChanged();
                                    }
                                }
                        );
                    }
                }
        );

        headerText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: redirect to book view
            }
        });
    }
}