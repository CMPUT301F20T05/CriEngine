package com.example.criengine.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.criengine.Activities.NonOwnerBookViewActivity;
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
public class SearchBooksFragment extends Fragment {

    // variable declaration
    private ArrayList<Book> searchBooks;
    private ArrayList<Book> allBooks;
    private SearchBooksListAdapter searchAdapter;
    private EditText keyword;
    private ListView results;
    private DatabaseWrapper dbw;

    public SearchBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_books, container, false);
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
                intent.putExtra("Book", book);
                view.getContext().startActivity(intent);
            }
        };

        results.setOnItemClickListener(selected);
    }
}