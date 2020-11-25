package com.example.criengine.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.example.criengine.Adapters.RequestsForBookAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Requests for Book Activity. Displays all users who have requested your available book & allows
 * for the owner to reject / accept a requester.
 * Outstanding Issues:
 * - Does not notify the users. (Database not setup)
 * - Does not push the book status/properties to the database.
 */
public class RequestsForBookActivity extends AppCompatActivity implements Serializable {
    private RequestsForBookAdapter userListAdapter;
    private ListView userListView;
    private TextView header;
    private Book book;
    private ArrayList<Profile> userList;
    private DatabaseWrapper dbw;
    private Context context;

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_requests_for_book);
        // Grabs the book.
        if (getIntent().getExtras() != null) {
            book = (Book) getIntent().getSerializableExtra("Book");
        } else {
            Intent intent = new Intent(this, SomethingWentWrong.class);
            startActivity(intent);
            return;
        }
        dbw = DatabaseWrapper.getWrapper();

        dbw.getBookBorrowers(book.getBookID()).addOnSuccessListener(new OnSuccessListener<List<Profile>>() {
            @Override
            public void onSuccess(List<Profile> profiles) {
                userList = (ArrayList<Profile>) profiles;

                // Set the adapter.
                userListAdapter = new RequestsForBookAdapter(context, userList, book);
                userListView.setAdapter(userListAdapter);
            }
        });


        userListView = findViewById(R.id.requestsListView);
        header = findViewById(R.id.requests_for_book_header);
        header.setText("Requests for \"" + book.getTitle() + "\"");

        // Opens to the book information screen when you click on a specific book.
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: redirect to view the user profile.
            }
        });
    }

    /**
     * Overrides the back button so it returns to the main activity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RootActivity.class);
        intent.putExtra("Index", RootActivity.PAGE.MY_BOOKS);
        startActivity(intent);
    }
}