package com.example.criengine;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

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
    ListView bookNameTextView;
    private ArrayList<String> filterStatus = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        // FIXME: Remove the dummy-code and replace with the following commented out line.
        // myBooks = Database.getMyProfile().getOwned();
        Book book1 = new Book("Ralph", "This is a new Book!", "Author A.", "This is quite a nice description.", "1I2SS02", "available");
        Book book2 = new Book("Ralph", "This is a different Book", "Author B.", "This is quite a good description.", "1I2SS03", "available");
        Book book3 = new Book("Ralph", "A book of Cyn", "Author C.", "This is another description.", "1I2SS04", "available");
        Book book4 = new Book("Ralph", "A book of Lyve", "Author D.", "This is a description.", "1I2SS05", "available");
        Book book5 = new Book("Ralph", "A book of Evil", "Author E.", "This is yet another description.", "1I2SS06", "available");
        book1.addRequesters("Joe Smith");
        book1.setStatus("requested");
        book2.addBorrower("Bill John");
        book2.setStatus("borrowed");
        book3.setPotentialBorrower("Cyn Lord");
        book3.setStatus("accepted");
        book4.setPotentialBorrower("Cyn Lord");
        book4.getPotentialBorrower().setHandoffComplete();
        book4.setStatus("accepted");
        Book[] books = {book1, book2, book3, book4, book5};
        myBooks = new ArrayList<Book>();
        myBooks.addAll(Arrays.asList(books));
        // DUMMY CODE ENDS HERE.

        // Contains the books that will be displayed on the screen.
        displayBooks = new ArrayList<Book>();
        displayBooks.addAll(myBooks);

        // Set the adapter.
        myBooksListAdapter = new MyBooksAdapter(this, displayBooks);

        bookNameTextView = findViewById(R.id.bookListView);
        bookNameTextView.setAdapter(myBooksListAdapter);

        // Opens to the book information screen when you click on a specific book.
        bookNameTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        bookNameTextView.setAdapter(myBooksListAdapter);
    }
}