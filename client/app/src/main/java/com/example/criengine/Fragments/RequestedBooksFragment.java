package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Requested Books Fragment
 *
 * @version 1.0
 */
public class RequestedBooksFragment extends RootFragment {
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    ArrayList<Book> borrowerBooks;
    DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_request;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: remove dummy code
        borrowerBooks = new ArrayList<Book>();
        borrowerBooks.add(new Book(
                "Jane Doe", "Biography", "Nike Cage",
                "A Biography of Jane Doe", "ISBN1234", "available"));

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
                //TODO: redirect to book view
            }
        });
    }
}