package com.example.criengine.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.criengine.Activities.NonOwnerBookViewActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Adapters.SearchBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
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
    private ArrayList<Book> allBooks;
    private SearchBooksListAdapter searchAdapter;
    private EditText keyword;
    private ListView results;
    private DatabaseWrapper dbw;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        // Setup lists of books
        allBooks = new ArrayList<Book>();
        searchBooks = new ArrayList<Book>();
        // Setup adapter
        searchAdapter = new SearchBooksListAdapter(getContext(), searchBooks);

        // Get all books from database
        dbw.searchBooks().addOnSuccessListener(new OnSuccessListener<List<Book>>() {
            @Override
            public void onSuccess(List<Book> books) {
                allBooks.addAll(books);
                searchBooks.addAll(allBooks);
                searchAdapter.notifyDataSetChanged();
            }
        });

        // Set listview and adapter
        results = getView().findViewById(R.id.search_result_list);
        results.setAdapter(searchAdapter);

        keyword = getView().findViewById(R.id.search_box);

        // Search all books whenever keyword text is changed
        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBooks.clear();
                for (Book book : allBooks) {
                    if (book.getDescription().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchBooks.add(book);
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Let users click on a book and take them to the book page
        AdapterView.OnItemClickListener selected = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = searchBooks.get(position);
                Intent intent = new Intent(view.getContext(), NonOwnerBookViewActivity.class);
                intent.putExtra("Page", RootActivity.PAGE.SEARCH);
                intent.putExtra("Book", book);
                view.getContext().startActivity(intent);
            }
        };

        results.setOnItemClickListener(selected);

        // Setup Swipe refresh layout to use default root fragment lister
        swipeRefreshLayout = getView().findViewById(R.id.search_swipe_refresh_layout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new RefreshRootListener(swipeRefreshLayout));
        }
    }
}