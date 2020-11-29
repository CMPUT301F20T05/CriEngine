package com.example.criengine.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.criengine.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic fragment for the purpose of managing a list of filters
 */
public class FilterFragment extends DialogFragment implements Serializable {
    private String title;
    private List<String> filters;
    private List<String> activeFilters;
    private OnFragmentInteractionListener listener;

    /**
     * Constructor Initializes all required properties
     *
     * @param filters The List of all possible filters
     * @param activeFilters The List containing only the enabled filters.
     * @param listener external action listener
     */
    public FilterFragment(String title, List<String> filters,
                          List<String> activeFilters, OnFragmentInteractionListener listener) {
        this.title = title;
        this.filters = filters;
        this.activeFilters = activeFilters;
        this.listener = listener;
    }

    /**
     * Creates the fragment.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied.
     * @return The dialog allowing the user to select a filer(s).
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter, null);

        // Populate the layout with all the required checkboxes
        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_multiple_choice, filters.toArray());
        final GridView gridView = (GridView) view.findViewById(R.id.checkbox_grid);
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        gridView.setAdapter(adapter);

        // initialize the checkboxes
        for (int i = 0; i < adapter.getCount(); i++) {
            gridView.setItemChecked(i, activeFilters.contains(filters.get(i)));
        }

        // finalize the fragment view
        this.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view).setTitle(title);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activeFilters = new ArrayList<>();
                SparseBooleanArray checkedItems = gridView.getCheckedItemPositions();
                for (int j = 0; j < checkedItems.size(); j++) {
                    if (checkedItems.valueAt(j)) {
                        activeFilters.add(filters.get(j));
                    }
                }
                // Transfer the information to the parent activity.
                listener.onConfirmPressed(activeFilters);
            }
        });
        return builder.create();
    }

    /**
     * Contains the event listener methods for the FilterFragment
     * The user must implement and pass an object of this interface to the constructor
     */
    public interface OnFragmentInteractionListener {
        /**
         * The method that is run when the confirm button is pressed
         * @param activeFilters the list of all enabled filters when confirm is pressed
         */
        void onConfirmPressed(List<String> activeFilters);
    }
}
