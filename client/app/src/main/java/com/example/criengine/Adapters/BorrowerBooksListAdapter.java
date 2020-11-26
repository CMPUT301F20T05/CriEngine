package com.example.criengine.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Activities.NonOwnerBookViewActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Activities.ScanActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/*
 * BookListAdapter is a custom ArrayAdapter that can be used to show Book instances in
 * a ListView.
 */
public class BorrowerBooksListAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> bookItems;
    private Context context;
    private DatabaseWrapper dbw;

    private String action;
    private Book book;

    final int SCAN_RESULT_CODE = 0;

    /**
     * Constructor for the class. Instantiates the object.
     *
     * @param context   The context.
     * @param bookItems The list of book items to be displayed.
     */
    public BorrowerBooksListAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems) {
        super(context, 0, bookItems);
        this.context = context;
        this.bookItems = bookItems;
        dbw = DatabaseWrapper.getWrapper();
    }

    /**
     * On return from scan activity, RequestedBookFragment calls this function to update the database that the book has been borrowed or returned.
     * @param barcode   The barcode string scanned
     */
    public void onActivityResult(String barcode) {
        if (action.equals("Return")) {
            dbw.returnBook(book.getBookID(), barcode).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    ((RootActivity) context).refresh(RootActivity.PAGE.REQUESTS);
                }
            });
        } else if (action.equals("Borrow")) {
            dbw.confirmBorrowBook(dbw.userId, book.getBookID(), barcode).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    ((RootActivity) context).refresh(RootActivity.PAGE.REQUESTS);
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

        book = bookItems.get(position);

        // Opens to the book information screen when you click on a specific book.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NonOwnerBookViewActivity.class);
                intent.putExtra("Book", book);
                v.getContext().startActivity(intent);
            }
        });

        headerText.setText(book.getTitle());

        statusText.setText(book.getStatus());

        actionButton.setEnabled(true);
        actionButton.setVisibility(View.VISIBLE);
        switch (book.getStatus()) {
            case "borrowed":
//            statusText.setTextColor(view.getResources().getColor(R.color.status_accepted));
                if (book.isConfirmationNeeded()) {
                    actionButton.setEnabled(false);
                    actionButton.setText("Scanned!");
                } else {
                    actionButton.setEnabled(true);
                    actionButton.setText("Return");
                    actionButton.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((Activity) context).startActivityForResult(new Intent(context, ScanActivity.class), SCAN_RESULT_CODE);
                                    action = "Returned";
                                }
                            }
                    );
                }
                break;
            case "requested":
                actionButton.setText("Cancel");
                actionButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                book.removeRequesters(dbw.userId);
                                dbw.declineRequest(dbw.userId, book.getBookID()).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Boolean> task) {
                                        if (book.getRequesters().size() == 0) {
                                            book.setStatus("available");
                                            dbw.addBook(book);
                                        }
                                        ((RootActivity) context).refresh(RootActivity.PAGE.REQUESTS);
                                    }
                                });
                            }
                        }
                );
                break;
            case "accepted":
                if (book.isConfirmationNeeded()) {
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setText("Borrow");
                    actionButton.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((Activity) context).startActivityForResult(new Intent(context, ScanActivity.class), SCAN_RESULT_CODE);
                                    action = "Borrow";
                                }
                            }
                    );
                } else {
                    actionButton.setVisibility(View.GONE);
                }
                break;
        }

        return view;
    }
}
