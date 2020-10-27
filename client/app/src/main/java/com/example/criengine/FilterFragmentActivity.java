package com.example.criengine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.io.Serializable;
import java.util.ArrayList;

public class FilterFragmentActivity extends DialogFragment implements Serializable {
    private Button avaliableButton;
    private Button requestsButton;
    private Button acceptedButton;
    private Button noFilterButton;
    private ArrayList<Book> myBooks;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(ArrayList<Book> myBooks);
        void onConfirmPressed();
    }

    public FilterFragmentActivity() {}

    public FilterFragmentActivity(ArrayList<Book> myBooks) {
        this.myBooks = myBooks;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_filter_fragment, null);
        noFilterButton = view.findViewById(R.id.no_filter_button);
        avaliableButton = view.findViewById(R.id.available_button);
        requestsButton = view.findViewById(R.id.requests_button);
        acceptedButton = view.findViewById(R.id.accepted_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder
                .setView(view)
                .setTitle("Filter By Status")
                .setNegativeButton("Cancel", null);

        if (this.city != null) {
            cityName.setText(city.getCityName());
            provinceName.setText(city.getProvinceName());
            return builder
                    .setTitle("Edit City")
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String city = cityName.getText().toString();
                            String province = provinceName.getText().toString();
                            editCity(city, province);
                            listener.onConfirmPressed();
                        }}).create();
        }
        return builder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String city = cityName.getText().toString();
                        String province = provinceName.getText().toString();
                        listener.onOkPressed(new City(city, province));
                    }})
                .create();
    }

    void filter(String newName, String newProvince) {
        this.city.setName(newName);
        this.city.setProvince(newProvince);
    }
}
