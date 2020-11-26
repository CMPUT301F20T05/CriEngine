package com.example.criengine.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.criengine.Activities.AddBookActivity;
import com.example.criengine.Adapters.MyBooksAdapter;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * My Books List Fragment.
 * Displays all owned books and their status's.
 */
public class MyBooksListFragment extends RootFragment implements MyBooksListFilterFragment.OnFragmentInteractionListener {
    private MyBooksAdapter myBooksListAdapter;
    private ArrayList<Book> myBooks;
    private ArrayList<Book> displayBooks;
    private Button addBookButton;
    private Button filterButton;
    private ListView headerText;
    private ArrayList<String> filterStatus = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    final int SCAN_RESULT_CODE = 0;

    /**
     * Returns the layout.
     * @return The layout.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_my_books;
    }

    /**
     * Called when creating the fragment.
     * @param view The view.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Contains the books that will be displayed on the screen.
        displayBooks = new ArrayList<>();
        myBooks = new ArrayList<>();

        // Set the adapter.
        myBooksListAdapter = new MyBooksAdapter(getContext(), displayBooks, this);

        // Setup the adapter.
        headerText = getView().findViewById(R.id.bookListView);
        headerText.setAdapter(myBooksListAdapter);

        dbw.getProfile(dbw.userId).addOnSuccessListener(
                profile -> dbw.getOwnedBooks(profile).addOnSuccessListener(
                        books -> {
                            myBooks.addAll(books);
                            displayBooks.addAll(myBooks);
                            myBooksListAdapter.notifyDataSetChanged();
                        }
                )
        );

        // Opens to the add-a-book screen when you click the button.
        addBookButton = getView().findViewById(R.id.add_a_book);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddBookActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // Opens the filter fragment where you can filter information.
        filterButton = getView().findViewById(R.id.filter_button);
        filterButton.setOnClickListener(v -> new MyBooksListFilterFragment(filterStatus).show(getChildFragmentManager(), "Filter_Status"));

        // Setup Swipe refresh layout to use default root fragment lister
        swipeRefreshLayout = getView().findViewById(R.id.my_books_swipe_refresh_layout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new RefreshRootListener(swipeRefreshLayout));
        }
    }

    /**
     * Handles modifying what is displayed on the screen if the user chooses to filter the info.
     * @param newStatus Contains the different status' that the user wants to display.
     */
    @Override
    public void onConfirmPressed(ArrayList<String> newStatus) {
        filterStatus = newStatus;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check that it is the ScanActivity with an OK result
        if (requestCode == SCAN_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String barcodeData = data.getStringExtra("barcode");
                Log.d("testing", "barcode data=" + barcodeData);

                myBooksListAdapter.onActivityResult(barcodeData);
            }
        }
    }
}