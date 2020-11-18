package com.example.criengine.Fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserProfileFragment extends ProfileFragment {
    private DatabaseWrapper dbw;

    private String userId;
    private Profile userProfile;
    private ArrayList<Book> userBooks;


    @Override
    public int getFragmentLayout() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        userId = getArguments().getString("userId");
        userBooks = new ArrayList<>();

        dbw = DatabaseWrapper.getWrapper();
        dbw.getProfile(userId).addOnSuccessListener(new OnSuccessListener<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                userProfile = profile;
                userTextView.setText(getString(R.string.user_profile_text, profile.getUsername()));
                bioEditText.setText(profile.getBio());
                phoneEditText.setText(profile.getPhone());
                addressEditText.setText(profile.getAddress());

                dbw.getOwnedBooks(profile).addOnSuccessListener(new OnSuccessListener<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> books) {
                        userBooks.addAll(books);
                    }
                });
            }
        });

        // TODO: create a layout and make a way to request books.
    }
}
