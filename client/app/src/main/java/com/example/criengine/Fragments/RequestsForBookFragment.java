package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.example.criengine.Adapters.RequestsForBookAdapter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Requests for Book Fragment. Displays all users who have requested an available book & allows for
 * the owner to reject / accept a borrower.
 */
public class RequestsForBookFragment extends Fragment {
    private RequestsForBookAdapter userListAdapter;
    private ListView userListView;
    private TextView header;
    private Book book;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_requests_for_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        book = (Book) bundle.getSerializable("Book");

        // Grabs the gears object from DisplayGearListActivity.java
//        if (getIntent().getExtras() != null) {
//            book = (Book) getIntent().getSerializableExtra("Book");
//        } else {
//            // TODO: If the intent fails to send, then redirect user to Error Screen. (in general this should not fail)
//        }

        // Set the adapter.
        userListAdapter = new RequestsForBookAdapter(getContext(), (ArrayList<String>) book.getRequesters(), book);

        userListView = getView().findViewById(R.id.requestsListView);
        header = getView().findViewById(R.id.requests_for_book_header);
        header.setText("Requests for \"" + book.getTitle() + "\"");

        userListView.setAdapter(userListAdapter);

        // Opens to the book information screen when you click on a specific book.
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: redirect to view the user profile.
            }
        });
    }
}