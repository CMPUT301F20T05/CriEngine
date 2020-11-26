package com.example.criengine.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.criengine.Adapters.SearchBooksListAdapter;
import com.example.criengine.Adapters.SearchProfilesListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Lets user search for a books description
 */
public class SearchBooksFragment extends RootFragment {

    // variable declaration
    private ArrayList<Book> searchBooks;
    private ArrayList<Profile> searchProfiles;
    private ArrayList<Book> allBooks;
    private ArrayList<Profile> allProfiles;

    private SearchBooksListAdapter searchBookAdapter;
    private SearchProfilesListAdapter searchProfileAdapter;

    private EditText keyword;
    private ListView bookResults;
    private ListView profileResults;
    private TextView bookLabel;
    private TextView profileLabel;

    private DatabaseWrapper dbw;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout swipeProfileRefreshLayout;

    public SearchBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Get the layout associated with the fragment.
     * @return The layout.
     */
    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_search_books;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup database wrapper
        dbw = DatabaseWrapper.getWrapper();

        final String[] bookString = {""};
        final String[] profileString = {""};

        bookLabel = getView().findViewById(R.id.search_book_label);
        bookLabel.setText(R.string.search_book);
        profileLabel = getView().findViewById(R.id.search_profile_label);
        profileLabel.setText(R.string.search_profile);

        // Setup lists of books and profiles
        allBooks = new ArrayList<Book>();
        allProfiles = new ArrayList<Profile>();
        searchBooks = new ArrayList<Book>();
        searchProfiles = new ArrayList<Profile>();

        // Setup adapter
        searchBookAdapter = new SearchBooksListAdapter(getContext(), searchBooks);
        searchProfileAdapter = new SearchProfilesListAdapter(getContext(), searchProfiles);

        // Get all books from database
        dbw.searchBooks().addOnSuccessListener(new OnSuccessListener<List<Book>>() {
            @Override
            public void onSuccess(List<Book> books) {
                allBooks.addAll(books);
                searchBooks.addAll(allBooks);
                searchBookAdapter.notifyDataSetChanged();
            }
        });

        // Get all profiles from database
        dbw.searchProfiles().addOnSuccessListener(new OnSuccessListener<List<Profile>>() {
            @Override
            public void onSuccess(List<Profile> profiles) {
                allProfiles.addAll(profiles);
                searchProfiles.addAll(allProfiles);
                searchProfileAdapter.notifyDataSetChanged();
            }
        });

        // Set listview and adapter
        bookResults = getView().findViewById(R.id.search_result_list);
        bookResults.setAdapter(searchBookAdapter);
        profileResults = getView().findViewById(R.id.search_profile_list);
        profileResults.setAdapter(searchProfileAdapter);

        keyword = getView().findViewById(R.id.search_box);

        // Search all books/profiles whenever keyword text is changed
        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookString[0] = "Books Matching: " + s.toString();
                bookLabel.setText(bookString[0]);
                searchBooks.clear();
                for (Book book : allBooks) {
                    if (book.getDescription().toLowerCase().contains(s.toString().toLowerCase())
                        && !book.getStatus().equals("Accepted")
                        && !book.getStatus().equals("Borrowed")) {
                        searchBooks.add(book);
                    }
                }
                searchBookAdapter.notifyDataSetChanged();

                profileString[0] = "Profiles Matching: " + s.toString();
                profileLabel.setText(profileString[0]);
                searchProfiles.clear();
                for (Profile profile : allProfiles) {
                    if (profile.getUsername().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchProfiles.add(profile);
                    }
                }
                searchProfileAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Setup Swipe refresh layout to use default root fragment lister
        swipeRefreshLayout = getView().findViewById(R.id.search_swipe_refresh_layout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new RefreshRootListener(swipeRefreshLayout));
        }

        // Setup Swipe refresh layout to use default root fragment listener
        swipeProfileRefreshLayout = getView().findViewById(R.id.search_profile_swipe_refresh_layout);
        if (swipeProfileRefreshLayout != null) {
            swipeProfileRefreshLayout.setOnRefreshListener(new RefreshRootListener(swipeProfileRefreshLayout));
        }
    }
}