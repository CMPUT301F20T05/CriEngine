package com.example.criengine.Adapters;

import android.app.Activity;
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
import com.example.criengine.Activities.RequestsForBookActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Activities.ScanActivity;
import com.example.criengine.Database.DatabaseWrapper;

import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/*
 * MyBooksAdapter is custom ArrayAdapter that can be used to owned books in a list view.
 * Outstanding Issues:
 * - Does not redirect to scanning activity. (Need to implement)
 */
public class MyBooksAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> bookItems;
    private Context context;
    private DatabaseWrapper dbw;

    private String action;
    private Book book;

    final int SCAN_RESULT_CODE = 0;

    /**
     * Constructor. Extends off of the array adapter.
     * @param context The context of the activity.
     * @param bookItems The ArrayList of books.
     */
    public MyBooksAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems) {
        super(context, 0, bookItems);
        this.context = context;
        this.bookItems = bookItems;
        dbw = DatabaseWrapper.getWrapper();
    }

    /**
     * On return from scan activity, MyBooksListFragment calls this function to update the database that the book has been returned or lent.
     * @param barcode   The barcode string scanned
     */
    public void onActivityResult(String barcode) {
        if (action.equals("Return")) {
            dbw.confirmReturnBook(book.getBookID(), barcode).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    ((RootActivity)context).refresh(RootActivity.PAGE.MY_BOOKS);
                }
            });
        } else if (action.equals("Lend")) {
            dbw.borrowBook(book.getBookID(), barcode).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    ((RootActivity)context).refresh(RootActivity.PAGE.MY_BOOKS);
                }
            });
        }
    }

    /**
     * Returns a view with the properly formatted information.
     * @param position The position from the list.
     * @param convertView The old view to reuse (if possible).
     * @param parent The parent view group.
     * @return The view that displays the formatted data at the specified position in the data set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get Item Data
        View view = convertView;

        // Setup the inflater and view if there was none given.
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_format, parent, false);
        }

        // Get the object from the xml file.
        TextView headerText = view.findViewById(R.id.headerText);
        TextView statusText = view.findViewById(R.id.statusText);
        Button actionButton = view.findViewById(R.id.actionButton);

        // Get the book to be displayed.
        book = bookItems.get(position);

        // Opens to the book information screen when you click on a specific book.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyBookActivity.class);
                intent.putExtra("Book", book);
                v.getContext().startActivity(intent);
            }
        });

        // Set the text for the header and status.
        headerText.setText(book.getTitle());

        // Modify the button and status seen from the screen depending on the status of the book.
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setEnabled(true);
        switch (book.getStatus()) {
            case "requested":
                actionButton.setText("See Requests");
                statusText.setText("Has Requests");
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), RequestsForBookActivity.class);
                        intent.putExtra("Book", book);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            case "borrowed":
                statusText.setText("Borrowed");
                if(book.isConfirmationNeeded()) {
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setText("Return");
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((Activity) context).startActivityForResult(new Intent(context, ScanActivity.class), SCAN_RESULT_CODE);
                            action = "Return";
                        }
                    });
                } else {
                    actionButton.setVisibility(View.GONE);
                }
                break;
            case "accepted":
                statusText.setText("Accepted");
                    if (book.isConfirmationNeeded()) {
                        actionButton.setText("Scanned!");
                        actionButton.setEnabled(false);
                    } else {
                        actionButton.setText("Lend");
                        actionButton.setEnabled(true);
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((Activity) context).startActivityForResult(new Intent(context, ScanActivity.class), SCAN_RESULT_CODE);
                                action = "Lend";
                            }
                        });
                    }
                break;
            default:
                statusText.setText("Available");
                actionButton.setVisibility(View.GONE);
                break;
        }
        return view;
    }
}