package com.example.criengine.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.criengine.R;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Contains the fragment that appears if a user wants to filter information when viewing books they
 * own.
 */
public class MyBooksListFilterFragment extends DialogFragment implements Serializable, CompoundButton.OnCheckedChangeListener {
    private CheckBox avaliableCheckBox;
    private CheckBox requestsCheckBox;
    private CheckBox acceptedCheckBox;
    private CheckBox borrowedCheckBox;
    private ArrayList<String> filterStatus;
    private OnFragmentInteractionListener listener;

    // Contains the method that the MyBooksActivity needs to implement.
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(ArrayList<String> filterStatus);
    }

    // Empty Constructor.
    public MyBooksListFilterFragment() {}

    /**
     * Constructor 2. Passes down the filter status arraylist which contains all the filters
     * that the user wants to view.
     * @param filterStatus The ArrayList containing all the status's the user wants to view.
     */
    public MyBooksListFilterFragment(ArrayList<String> filterStatus) {
        this.filterStatus = filterStatus;
    }

    /**
     * Sets the listener.
     * @param context The context of the activity.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MyBooksListFragment) getParentFragment();
    }

    /**
     * Creates the fragment.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied.
     * @return The dialog allowing the user to select a filer(s).
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_filter_fragment, null);
        avaliableCheckBox = view.findViewById(R.id.checkbox_available_filter);
        requestsCheckBox = view.findViewById(R.id.checkbox_requests_filter);
        acceptedCheckBox = view.findViewById(R.id.checkbox_accepted_filter);
        borrowedCheckBox = view.findViewById(R.id.checkbox_borrowed_filter);

        avaliableCheckBox.setText("Available");
        requestsCheckBox.setText("Requested");
        acceptedCheckBox.setText("Accepted");
        borrowedCheckBox.setText("Borrowed");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder
                .setView(view)
                .setTitle("Filter By Status");

        // Get the saved status of the checkbox's.
        if (filterStatus.contains("available")) {
            avaliableCheckBox.setChecked(true);
        }
        if (filterStatus.contains("requested")) {
            requestsCheckBox.setChecked(true);
        }
        if (filterStatus.contains("accepted")) {
            acceptedCheckBox.setChecked(true);
        }
        if (filterStatus.contains("borrowed")) {
            borrowedCheckBox.setChecked(true);
        }

        // Sets the listener to modify the ArrayList if a certain check box was selected.
        avaliableCheckBox.setOnCheckedChangeListener(this);
        requestsCheckBox.setOnCheckedChangeListener(this);
        acceptedCheckBox.setOnCheckedChangeListener(this);
        borrowedCheckBox.setOnCheckedChangeListener(this);

        // Transfer the information to the parent activity.
        return builder
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onConfirmPressed(filterStatus);
                    }})
                .create();
    }

    /**
     * Adds the requests status to the list. This eventually determines what the view wants to
     * view.
     * @param checkBox The check box view.
     * @param isChecked Whether the check box is currently active.
     */
    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        if (isChecked) {
            filterStatus.add(checkBox.getText().toString().toLowerCase());
        } else {
            filterStatus.remove(checkBox.getText().toString().toLowerCase());
        }
    }
}
