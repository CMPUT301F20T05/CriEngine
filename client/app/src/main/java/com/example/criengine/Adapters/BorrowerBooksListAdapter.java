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
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import java.util.ArrayList;

/*
 * BookListAdapter is a custom ArrayAdapter that can be used to show Book instances in
 * a ListView.
 * Outstanding Issues:
 * - Does not retrieve info/push changes to the database.
 */
public class BorrowerBooksListAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> bookItems;
    private Context context;

    /**
     * Constructor for the class. Instantiates the object.
     * @param context The context.
     * @param bookItems The list of book items to be displayed.
     */
    public BorrowerBooksListAdapter(@NonNull Context context, @NonNull ArrayList<Book> bookItems) {
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

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_format, parent, false);
        }

        TextView headerText = view.findViewById(R.id.headerText);
        TextView statusText = view.findViewById(R.id.statusText);
        Button actionButton = view.findViewById(R.id.actionButton);

        Book book = bookItems.get(position);

        headerText.setText(book.getTitle());

        statusText.setText(book.getStatus());

        // TODO: use db username and watchlist to determine state
        // Cancel -- we requested /  watching a book
        // Scan -- we have borrowed / Status == Accepted
        // Ok -- Rejected
        // if ( book.getRequesters().contains(Database.myUsername) || Database.getMyProfile.getWatchList().contains(book))
        DatabaseWrapper dbw = DatabaseWrapper.getWrapper();
        if (book.getRequesters().contains(dbw.userId)) {
            actionButton.setText("Cancel");
            // todo: remove once implemented functionality
            actionButton.setEnabled(false);
            actionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: remove request from profile && book
                        }
                    }
            );

        } else if (book.getBorrower() == dbw.userId) {
            statusText.setTextColor(view.getResources().getColor(R.color.status_accepted));
            actionButton.setText("Scan");
            // todo: remove once implemented functionality
            actionButton.setEnabled(false);
            actionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO : navigate to scan page
                        }
                    }
            );
        } else {
            statusText.setText("Rejected");
            statusText.setTextColor(view.getResources().getColor(R.color.status_rejected));
            actionButton.setText("Ok");
            // todo: remove once implemented functionality
            actionButton.setEnabled(false);
            actionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: remove request from profile
                        }
                    }
            );
        }

        return view;
    }
}
