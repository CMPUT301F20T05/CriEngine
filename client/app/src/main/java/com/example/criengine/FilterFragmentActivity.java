package com.example.criengine;

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
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Contains the fragment that appears if a user wants to filter information when viewing books they
 * own.
 */
public class FilterFragmentActivity extends DialogFragment implements Serializable {
    private CheckBox avaliableButton;
    private CheckBox requestsButton;
    private CheckBox acceptedButton;
    private ArrayList<String> filterStatus;
    private OnFragmentInteractionListener listener;

    // Contains the method that the MyBooksActivity needs to implement.
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(ArrayList<String> filterStatus);
    }

    // Constructor.
    public FilterFragmentActivity() {}

    public FilterFragmentActivity(ArrayList<String> filterStatus) {
        this.filterStatus = filterStatus;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Creates the fragment.
     * @param savedInstanceState
     * @return The ArrayList containing the filter status information.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_filter_fragment, null);
        avaliableButton = view.findViewById(R.id.checkbox_available_filter);
        requestsButton = view.findViewById(R.id.checkbox_requests_filter);
        acceptedButton = view.findViewById(R.id.checkbox_accepted_filter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder
                .setView(view)
                .setTitle("Filter By Status")
                .setNegativeButton("Cancel", null);

        // Get the saved status of the checkbox's.
        if (filterStatus.contains("Available")) {
            avaliableButton.setChecked(true);
        }
        if (filterStatus.contains("Requests")) {
            requestsButton.setChecked(true);
        }
        if (filterStatus.contains("Accepted")) {
            acceptedButton.setChecked(true);
        }

        // Record the filters that the user wants to view.
        avaliableButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (avaliableButton.isChecked()) {
                    filterStatus.add("Available");
                } else {
                    filterStatus.remove("Available");
                }
            }
        });

        requestsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (requestsButton.isChecked()) {
                    filterStatus.add("Requests");
                } else {
                    filterStatus.remove("Requests");
                }
            }
        });

        acceptedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (acceptedButton.isChecked()) {
                    filterStatus.add("Accepted");
                } else {
                    filterStatus.remove("Accepted");
                }
            }
        });

        // Transfer the information to the parent activity.
        return builder
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onConfirmPressed(filterStatus);
                    }})
                .create();
    }
}
