package com.example.criengine.Fragments;

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

import com.example.criengine.Adapters.SearchBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class SearchBooksFragment extends Fragment {

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
        dbw = DatabaseWrapper.getWrapper();

        allBooks = new ArrayList<Book>();
        searchBooks = new ArrayList<Book>();
        searchAdapter = new SearchBooksListAdapter(getContext(), searchBooks);

        dbw.searchBooks().addOnSuccessListener(new OnSuccessListener<List<Book>>() {
            @Override
            public void onSuccess(List<Book> books) {
                allBooks.addAll(books);
                searchBooks.addAll(allBooks);
                searchAdapter.notifyDataSetChanged();
            }
        });

        results = getView().findViewById(R.id.search_result_list);
        results.setAdapter(searchAdapter);

        keyword = getView().findViewById(R.id.search_box);

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
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        AdapterView.OnItemClickListener selected = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = searchBooks.get(position);
            }
        };

        results.setOnItemClickListener(selected);
    }
}