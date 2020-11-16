package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Fragments.FilterFragment.OnFragmentInteractionListener;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Requested Books Fragment. Handles displaying information about all outgoing requests.
 * Outstanding Issues:
 * - Does not navigate to a book if selected (activity not implemented).
 */
public class RequestedBooksFragment extends RootFragment implements OnFragmentInteractionListener {
    Button filterButton;
    List<String> filters = Arrays.asList("Requested", "Watched", "Borrowing", "Accepted");
    List<String> activeFilters = new ArrayList<>();
    ArrayList<Book> borrowerBooks = new ArrayList<>();
    ArrayList<Book> displayBooks = new ArrayList<>();
    BorrowerBooksListAdapter borrowerBooksListAdapter;

    /**
     * Get the layout associated with the fragment.
     * @return The layout associated with the fragment.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_request;
    }

    /**
     * Called immediately after onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
     * android.os.Bundle) has returned, but before any saved state has been restored in to the view.
     * @param view The view.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied.
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

        borrowerBooksListAdapter = new BorrowerBooksListAdapter(getContext(), displayBooks);

        final ListView bookNameTextView = getView().findViewById(R.id.bookListView);
        bookNameTextView.setAdapter(borrowerBooksListAdapter);

        dbw.getBorrowedOrRequestedBooks(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> books) {
                        borrowerBooks.addAll(books);
                        displayBooks.addAll(books);
                        borrowerBooksListAdapter.notifyDataSetChanged();
                    }
                }
        );

        bookNameTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Navigate to book-view (non owner view)
            }
        });
    }

    /**
     * Updates the list of books to be displayed depending on the active filters received from
     * the filter fragment
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
            for(Book book: borrowerBooks){
                boolean isBorrowed = book.getStatus().equals("borrowed") && book.getBorrower() == dbw.userId;
                boolean isAccepted = book.getStatus().equals("accepted") && book.getBorrower() == dbw.userId;
                boolean isRequested = book.getStatus().equals("requested");
                if((activeFilters.contains("Requested") && isRequested)
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
}