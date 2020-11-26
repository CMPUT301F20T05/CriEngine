package com.example.criengine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Activities.MyBookActivity;
import com.example.criengine.Activities.NonOwnerBookViewActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

import java.util.ArrayList;

/**
 * Adapter for search fragment
 */
public class SearchBooksListAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> items;
    private Context context;
    private DatabaseWrapper dbw;
    private Boolean search;

    public SearchBooksListAdapter(@NonNull Context context, @NonNull ArrayList<Book> books,
                                  @NonNull Boolean search) {
        super(context, 0, books);
        this.context = context;
        this.items = books;
        this.search = search;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        dbw = DatabaseWrapper.getWrapper();
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.search_list, parent, false);
        }

        // Get the object from the xml file.
        TextView searchTitle = view.findViewById(R.id.search_book_title);
        TextView searchDesc = view.findViewById(R.id.search_book_desc);
        TextView searchStatus = view.findViewById(R.id.search_book_status);
        final TextView searchUser = view.findViewById(R.id.search_book_user);

        final Book book = items.get(position);

        searchTitle.setText(book.getTitle());
        searchDesc.setText(book.getDescription());
        searchStatus.setText(book.getStatus());

        // Get the book's owner username
        dbw.getProfile(book.getOwner()).addOnSuccessListener(new OnSuccessListener<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                searchUser.setText(profile.getUsername());
            }
        });

        // Opens to the book information screen when you click on a specific book.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NonOwnerBookViewActivity.class);
                // Book was clicked in search fragment
                if (search) {
                    intent.putExtra("Page", RootActivity.PAGE.SEARCH);
                // Book was clicked in another activity
                } else {
                    intent.putExtra("Page", RootActivity.PAGE.OTHER);
                }
                intent.putExtra("Book", book);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
