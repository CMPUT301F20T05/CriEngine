package com.example.criengine.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Objects.Book;
import com.example.criengine.R;

import java.util.ArrayList;

/**
 * Adapter for search fragment
 */
public class SearchBooksListAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> items;
    private Context context;

    public SearchBooksListAdapter(@NonNull Context context, @NonNull ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.items = books;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.search_list, parent, false);
        }

        // Get the object from the xml file.
        TextView searchTitle = view.findViewById(R.id.search_book_title);
        TextView searchDesc = view.findViewById(R.id.search_book_desc);
        TextView searchStatus = view.findViewById(R.id.search_book_status);

        Book book = items.get(position);

        searchTitle.setText(book.getTitle());
        searchDesc.setText(book.getDescription());
        searchStatus.setText(book.getStatus());

        return view;
    }
}
