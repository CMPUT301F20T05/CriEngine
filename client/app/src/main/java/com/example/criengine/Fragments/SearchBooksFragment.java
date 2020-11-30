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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private CheckBox checkBox;

    private Boolean showAll = Boolean.FALSE;
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

        checkBox = getView().findViewById(R.id.search_all_books);

        // Setup lists of books and profiles
        allBooks = new ArrayList<Book>();
        allProfiles = new ArrayList<Profile>();
        searchBooks = new ArrayList<Book>();
        searchProfiles = new ArrayList<Profile>();

        // Setup adapter
        searchBookAdapter = new SearchBooksListAdapter(getContext(), searchBooks, true);
        searchProfileAdapter = new SearchProfilesListAdapter(getContext(), searchProfiles);

        // Get all books from database
        dbw.searchBooks().addOnSuccessListener(new OnSuccessListener<List<Book>>() {
            @Override
            public void onSuccess(List<Book> books) {
                allBooks.addAll(books);
                if (showAll) {
                    searchBooks.addAll(allBooks);
                } else {
                    for (Book book : allBooks) {
                        if (!book.getStatus().equals("accepted")
                                && !book.getStatus().equals("borrowed")) {
                            searchBooks.add(book);
                        }
                    }
                }
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
                fill_books(s.toString());

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

        // Checkbox listener, shows all book when checked else does not show borrowed or accepted
        // books
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showAll = true;
                fill_books(keyword.getText().toString());
            } else {
                showAll = false;
                fill_books(keyword.getText().toString());
            }
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

    // Function to fill searchbooks with books with given keyword
    public void fill_books(String key) {
        searchBooks.clear();
        for (Book book : allBooks) {
            if ((book.getDescription().toLowerCase().contains(key.toLowerCase())
                    || book.getAuthor().toLowerCase().contains(key.toLowerCase())
                    || book.getTitle().toLowerCase().contains(key.toLowerCase()))) {
                if (showAll) {
                    searchBooks.add(book);
                } else {
                    if (!book.getStatus().equals("accepted")
                            && !book.getStatus().equals("borrowed")) {
                        searchBooks.add(book);
                    }
                }
            }
        }
        searchBookAdapter.notifyDataSetChanged();
    }
}