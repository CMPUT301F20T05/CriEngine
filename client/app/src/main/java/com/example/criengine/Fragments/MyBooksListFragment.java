package com.example.criengine.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.criengine.Activities.AddBookActivity;
import com.example.criengine.Activities.MyBookActivity;
import com.example.criengine.Adapters.MyBooksAdapter;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;

/**
 * My Books List Fragment.
 * Displays all owned books and their status's.
 */
public class MyBooksListFragment extends RootFragment implements MyBooksListFilterFragment.OnFragmentInteractionListener {
    private MyBooksAdapter myBooksListAdapter;
    private ArrayList<Book> myBooks;
    private ArrayList<Book> displayBooks;
    private Button addBookButton;
    private Button filterButton;
    private ListView headerText;
    private ArrayList<String> filterStatus = new ArrayList<>();

    @Override
    public int initRootFrag() {
        return R.layout.activity_my_books;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Contains the books that will be displayed on the screen.
        displayBooks = new ArrayList<>();
        myBooks = new ArrayList<>();

        // Set the adapter.
        myBooksListAdapter = new MyBooksAdapter(getContext(), displayBooks);

        // Setup the adapter.
        headerText = getView().findViewById(R.id.bookListView);
        headerText.setAdapter(myBooksListAdapter);

        // TODO: please pass this activity a profile object somehow, so we dont need to do this nightmare double call
        dbw.getProfile(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        dbw.getOwnedBooks(profile).addOnSuccessListener(
                                new OnSuccessListener<List<Book>>() {
                                    @Override
                                    public void onSuccess(List<Book> books) {
                                        myBooks.addAll(books);
                                        displayBooks.addAll(myBooks);
                                        myBooksListAdapter.notifyDataSetChanged();
                                    }
                                }
                        );
                    }
                }
        );

        // Opens to the book information screen when you click on a specific book.
        headerText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MyBookActivity.class);
                intent.putExtra("Book", displayBooks.get(position));
                view.getContext().startActivity(intent);
            }
        });

        // Opens to the add-a-book screen when you click the button.
        addBookButton = getView().findViewById(R.id.add_a_book);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddBookActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // Opens the filter fragment where you can filter information.
        filterButton = getView().findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyBooksListFilterFragment(filterStatus).show(getChildFragmentManager(), "Filter_Status");
            }
        });
    }

    /**
     * Handles modifying what is displayed on the screen if the user chooses to filter the info.
     * @param newStatus Contains the different status' that the user wants to display.
     */
    @Override
    public void onConfirmPressed(ArrayList<String> newStatus) {
        filterStatus = newStatus;

        // Wipe the array so we can start anew.
        displayBooks.clear();

        // Modifies the array so that only the filtered status's are displayed.
        if (filterStatus.size() > 0) {
            for (int status = 0; status < filterStatus.size(); status++) {
                for (int i = 0; i < myBooks.size(); i++) {
                    if (myBooks.get(i).getStatus().equals(filterStatus.get(status))) {
                        displayBooks.add(myBooks.get(i));
                    }
                }
            }
        } else {
            // If no filter was chosen, then display all the books.
            // NOTE: We cannot make the displayBooks = myBooks. This assigns a pointer that we don't
            // want.
            displayBooks.addAll(myBooks);
        }
        myBooksListAdapter.notifyDataSetChanged();
    }
}