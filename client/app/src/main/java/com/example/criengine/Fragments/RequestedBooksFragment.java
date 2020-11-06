package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

import java.util.ArrayList;


/**
 * Requested Books Fragment
 *
 * @version 1.0
 */
public class RequestedBooksFragment extends RootFragment {
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    ArrayList<Book> borrowerBooks;

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

        bookNameTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: redirect to book view
            }
        });
    }
}