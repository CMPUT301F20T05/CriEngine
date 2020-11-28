package com.example.criengine.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Fragments.FilterFragment.OnFragmentInteractionListener;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Requested Books Fragment. Handles displaying information about all outgoing requests.
 */
public class RequestedBooksFragment extends RootFragment implements OnFragmentInteractionListener {
    Button filterButton;
    ListView bookListView;
    List<String> filters = Arrays.asList("Requested", "Borrowing", "Accepted");
    List<String> activeFilters = new ArrayList<>();
    ArrayList<Book> borrowerBooks = new ArrayList<>();
    ArrayList<Book> displayBooks = new ArrayList<>();
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    final int SCAN_RESULT_CODE = 0;

    /**
     * Get the layout associated with the fragment.
     *
     * @return The layout associated with the fragment.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_request;
    }

    /**
     * Called immediately after onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
     * android.os.Bundle) has returned, but before any saved state has been restored in to the view.
     *
     * @param view               The view.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Opens the filter fragment where you can filter information.
        filterButton = getView().findViewById(R.id.requests_filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FilterFragment("Filter By Status",
                        filters,
                        activeFilters,
                        RequestedBooksFragment.this
                ).show(getChildFragmentManager(), "Filter_Status");
            }
        });

        borrowerBooksListAdapter = new BorrowerBooksListAdapter(getContext(), displayBooks, this);

        bookListView = getView().findViewById(R.id.bookListView);
        bookListView.setAdapter(borrowerBooksListAdapter);

        dbw.getBorrowedOrRequestedBooks(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> books) {
                        for(Book book: books) {
                            if (book == null || book.getStatus() == null){
                                continue;
                            }
                            if(book.getStatus().equals("borrowed")
                                || book.getStatus().equals("requested")
                                || book.getStatus().equals("accepted")) {
                                borrowerBooks.add(book);
                                displayBooks.add(book);
                            }
                        }
                        borrowerBooksListAdapter.notifyDataSetChanged();
                    }
                }
        );

        // Setup Swipe refresh layout to use default root fragment lister
        swipeRefreshLayout = getView().findViewById(R.id.my_requests_swipe_refresh_layout);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new RefreshRootListener(swipeRefreshLayout));
        }
    }

    /**
     * Updates the list of books to be displayed depending on the active filters received from
     * the filter fragment
     *
     * @param activeFilters the list of filters enabled when the FilterFragment confirm button
     *                      is pressed
     */
    @Override
    public void onConfirmPressed(List<String> activeFilters) {
        this.activeFilters = activeFilters;

        // Wipe the array so we can start anew.
        displayBooks.clear();

        // Modifies the array so that only the filtered status's are displayed.
        if (activeFilters.size() > 0) {
            for (Book book : borrowerBooks) {
                if (book == null || book.getStatus() == null){
                    continue;
                }
                boolean isRequested = book.getStatus().equals("requested");
                boolean userIsBorrower = book.getBorrower() != null && book.getBorrower().equals(dbw.userId);
                boolean isBorrowed = book.getStatus().equals("borrowed") && userIsBorrower;
                boolean isAccepted = book.getStatus().equals("accepted") && userIsBorrower;
                if ((activeFilters.contains("Requested") && isRequested)
                        || (activeFilters.contains("Borrowing") && isBorrowed)
                        || (activeFilters.contains("Accepted") && isAccepted))
                    displayBooks.add(book);
            }
        } else {
            // If no filter was chosen, then display all the books.
            // NOTE: We cannot make the displayBooks = myBooks. This assigns a pointer that we don't
            // want.
            displayBooks.addAll(borrowerBooks);
        }
        borrowerBooksListAdapter.notifyDataSetChanged();
    }

    /**
     * On return from scan activity called from MyBooksAdapter, pass data to adapter to update book
     *
     * @param requestCode: the request code corresponding to the scan activity
     * @param resultCode:  the result code of if the activity was successful
     * @param data:        payload of intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // Check that it is the ScanActivity with an OK result
        if (requestCode == SCAN_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String barcodeData = data.getStringExtra("barcode");
                String bookID = data.getStringExtra("bookID");

                borrowerBooksListAdapter.onActivityResult(barcodeData, bookID);
            }
        }
    }
}