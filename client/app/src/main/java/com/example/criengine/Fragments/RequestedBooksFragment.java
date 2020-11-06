package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Requested Books Fragment. Handles displaying information about all outgoing requests.
 * Outstanding Issues:
 * - Does not navigate to a book if selected (activity not implemented).
 */
public class RequestedBooksFragment extends RootFragment {
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    ArrayList<Book> borrowerBooks;
    DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

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

        borrowerBooks = new ArrayList<>();

        borrowerBooksListAdapter = new BorrowerBooksListAdapter(getContext(), borrowerBooks);

        ListView bookNameTextView = getView().findViewById(R.id.bookListView);
        bookNameTextView.setAdapter(borrowerBooksListAdapter);

        dbw.getBorrowedOrRequestedBooks(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> books) {
                        borrowerBooks.addAll(books);
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
}