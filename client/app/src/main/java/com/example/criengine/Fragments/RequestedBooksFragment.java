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
public class RequestedBooksFragment extends Fragment {
    BorrowerBooksListAdapter borrowerBooksListAdapter;
    ArrayList<Book> borrowerBooks;
    DatabaseWrapper dbw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        borrowerBooks = new ArrayList<Book>();
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

    @Override
    public void onStart() {
        super.onStart();
        /* Crashes because dbw is null?
        dbw = DatabaseWrapper.getWrapper();
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
        );*/
    }
}