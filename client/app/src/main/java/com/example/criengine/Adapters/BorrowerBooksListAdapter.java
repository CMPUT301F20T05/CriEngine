package com.example.criengine.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.criengine.Activities.NonOwnerBookViewActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Activities.ScanActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/*
 * BookListAdapter is a custom ArrayAdapter that can be used to show Book instances in
 * a ListView.
 */
public class BorrowerBooksListAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> bookItems;
    private ArrayList<Book> wished;
    private Context context;
    private Fragment fragment;
    private DatabaseWrapper dbw;

    private String action;
    final int SCAN_RESULT_CODE = 0;

    /**
     * Constructor for the class. Instantiates the object.
     *
     * @param context   The context.
     * @param bookItems The list of book items to be displayed.
     */
    public BorrowerBooksListAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems, Fragment fragment, ArrayList<Book> wished) {
        super(context, 0, bookItems);
        this.context = context;
        this.fragment = fragment;
        this.bookItems = bookItems;
        this.wished = wished;
        dbw = DatabaseWrapper.getWrapper();
    }

    /**
     * On return from scan activity, RequestedBookFragment calls this function to update the database that the book has been borrowed or returned.
     *
     * @param barcode The barcode string scanned
     */
    public void onActivityResult(String barcode, String bookID) {
        if (action.equals("Return")) {
            dbw.returnBook(bookID, barcode).addOnSuccessListener(new OnSuccessListener<Boolean>() {
                @Override
                public void onSuccess(Boolean success) {
                    if (success) {
                        ((RootActivity)context).refresh(RootActivity.PAGE.REQUESTS);
                    } else {
                        Toast.makeText(context, "ISBN Scan failed, did you scan the wrong book?", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (action.equals("Borrow")) {
            dbw.confirmBorrowBook(dbw.userId, bookID, barcode).addOnSuccessListener(new OnSuccessListener<Boolean>() {
                @Override
                public void onSuccess(Boolean success) {
                    if (success) {
                        ((RootActivity)context).refresh(RootActivity.PAGE.REQUESTS);
                    } else {
                        Toast.makeText(context, "ISBN Scan failed, did you scan the wrong book?", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * Returns a view with the properly formatted information.
     *
     * @param position    The position from the list.
     * @param convertView The old view to reuse (if possible).
     * @param parent      The parent view group.
     * @return The view that displays the formatted data at the specified position in the data set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get Item Data
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_format, parent, false);
        }

        TextView headerText = view.findViewById(R.id.headerText);
        TextView statusText = view.findViewById(R.id.statusText);
        Button actionButton = view.findViewById(R.id.actionButton);

        final Book book = bookItems.get(position);

        // Opens to the book information screen when you click on a specific book.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NonOwnerBookViewActivity.class);
                intent.putExtra("Book", book);
                intent.putExtra("Page", RootActivity.PAGE.REQUESTS);
                v.getContext().startActivity(intent);
            }
        });

        headerText.setText(book.getTitle());

        actionButton.setEnabled(true);
        String statusString = book.getStatus().substring(0, 1).toUpperCase()
                + book.getStatus().substring(1);
        statusText.setText(statusString);

        actionButton.setVisibility(View.VISIBLE);
        actionButton.setEnabled(true);
        switch (book.getStatus()) {
            case "borrowed":
                statusText.setText("Borrowed");
                if (book.isConfirmationNeeded()) {
                    actionButton.setEnabled(false);
                    actionButton.setText("Scanned!");
                } else {
                    actionButton.setEnabled(true);
                    actionButton.setText("Return");
                    actionButton.setOnClickListener(
                            v -> {
                                Intent intent = new Intent(context, ScanActivity.class);
                                intent.putExtra("BookID", book.getBookID());
                                fragment.startActivityForResult(intent, SCAN_RESULT_CODE);
                                action = "Return";
                            }
                    );
                }
                break;
            case "requested":
                statusText.setText("Requested");
                actionButton.setText("Cancel");
                actionButton.setOnClickListener(
                        v -> {
                            book.removeRequesters(dbw.userId);
                            dbw.declineRequest(dbw.userId, book.getBookID()).addOnCompleteListener(task -> {
                                if (book.getRequesters().size() == 0) {
                                    book.setStatus("available");
                                    dbw.addBook(book);
                                }
                                ((RootActivity) context).refresh(RootActivity.PAGE.REQUESTS);
                            });
                        }
                );
                break;
            case "accepted":
                if (book.isConfirmationNeeded()) {
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setText("Borrow");
                    statusText.setText("Accepted");
                    actionButton.setOnClickListener(
                            v -> {
                                Intent intent = new Intent(context, ScanActivity.class);
                                intent.putExtra("BookID", book.getBookID());
                                fragment.startActivityForResult(intent, SCAN_RESULT_CODE);
                                action = "Borrow";
                            }
                    );
                } else {
                    actionButton.setVisibility(View.GONE);
                }
                break;
        }

        if (wished.contains(book)) {
            statusText.setText("Wishlisted");
            actionButton.setVisibility(View.GONE);
        }

        return view;
    }
}
