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
import com.example.criengine.Activities.RequestsForBookActivity;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import java.util.ArrayList;

/*
 * MyBooksAdapter is custom ArrayAdapter that can be used to owned books in a list view.
 * Outstanding Issues:
 * - Does not redirect to scanning/location activities. (Need to implement)
 */
public class MyBooksAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> bookItems;
    private Context context;

    /**
     * Constructor. Extends off of the array adapter.
     * @param context The context of the activity.
     * @param bookItems The ArrayList of books.
     */
    public MyBooksAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems) {
        super(context, 0, bookItems);
        this.context = context;
        this.bookItems = bookItems;
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
        final Book book = bookItems.get(position);

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
        switch (book.getStatus()) {
            case "requested":
                actionButton.setText("See Requests");
                statusText.setText("Has Requests");
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setEnabled(true); // Enable the button.
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
                actionButton.setText("Scan");
                statusText.setText("Borrowed");
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setEnabled(false); // Temp disable the button.
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Redirect to Scan activity.
                    }
                });
                break;
            case "accepted":
                statusText.setText("Accepted");
                if (book.getGeolocation() == null) {
                    actionButton.setText("Location");
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Redirect to Geo-Location page.
                        }
                    });
                } else {
                    actionButton.setText("Scan");
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Redirect to Scan activity.
                        }
                    });
                }
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setEnabled(false); // Temp disable the button.
                break;
            default:
                statusText.setText("Available");
                actionButton.setVisibility(View.GONE);
                actionButton.setEnabled(false); // Temp disable the button.
                break;
        }

        return view;
    }
}