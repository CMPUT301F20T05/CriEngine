package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Adapters.MyBooksAdapter;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * My Books Activity. Displays all owned books and their status's.
 * Navigation to:
 *  - Add-A-Book activity
 *  - View book information activity
 *  - See-Requests activity
 *  - Scan-Book activity
 *  - See-Location activity
 * @version  1.0
 */
public class MyBooksActivity extends AppCompatActivity implements FilterFragmentActivity.OnFragmentInteractionListener {
    private MyBooksAdapter myBooksListAdapter;
    private ArrayList<Book> myBooks;
    private ArrayList<Book> displayBooks;
    private Button addBookButton;
    private Button filterButton;
    ListView headerText;
    private ArrayList<String> filterStatus = new ArrayList<String>();

    DatabaseWrapper dbw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        myBooks = new ArrayList<Book>();
        dbw = DatabaseWrapper.getWrapper();

        // Contains the books that will be displayed on the screen.
        displayBooks = new ArrayList<Book>();
        displayBooks.addAll(myBooks);

        // Set the adapter.
        myBooksListAdapter = new MyBooksAdapter(this, displayBooks);

        headerText = findViewById(R.id.bookListView);
        headerText.setAdapter(myBooksListAdapter);

        // TODO: please pass this activity a profile object somehow, so we dont need to do this nightmare double call
        dbw.getProfile(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        dbw.getOwnedBooks(profile).addOnSuccessListener(
                                new OnSuccessListener<List<Book>>() {
                                    @Override
                                    public void onSuccess(List<Book> books) {
                                        myBooks.addAll(books);
                                        // TODO: This will definitely break filters, let's stop that somehow
                                        displayBooks.addAll(myBooks);
                                        myBooksListAdapter.notifyDataSetChanged();
                                    }
                                }
                        );
                    }
                }
        );

        // Opens to the book information screen when you click on a specific book.
        headerText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // FIXME: redirect to view-book activity.
            }
        });

        // Opens to the add-a-book screen when you click the button.
        addBookButton = findViewById(R.id.add_a_book);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME: redirect to add-book activity.
            }
        });

        // Opens the filter fragment where you can filter information.
        filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FilterFragmentActivity(filterStatus).show(getSupportFragmentManager(), "Filter_Status");
            }
        });
    }

    /**
     * Handles modifying what is displayed on the screen if the user chooses to filter the info.
     * @param filterStatus Contains the different status' that the user wants to display.
     */
    @Override
    public void onConfirmPressed(ArrayList<String> filterStatus) {
        this.filterStatus = filterStatus;

        // Wipe the array so we can start anew.
        displayBooks.clear();

        // Modifies the array so that only the filtered status's are displayed.
        if (filterStatus.size() > 0) {
            for (int status = 0; status < filterStatus.size(); status++) {
                for (int i = 0; i < myBooks.size(); i++) {
                    if (myBooks.get(i).getStatus().equals(filterStatus.get(status))) {
                        displayBooks.add(myBooks.get(i));
                    }
                }
            }
        } else {
            // If no filter was chosen, then display all the books.
            // NOTE: We cannot make the displayBooks = myBooks. This assigns a pointer that we don't
            // want.
            displayBooks.addAll(myBooks);
        }
        myBooksListAdapter.notifyDataSetChanged();
    }
}