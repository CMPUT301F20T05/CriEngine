package com.example.criengine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * User profile activity.
 * Outstanding Issues:
 * - Not currently implemented.
 */
public class UserProfileActivity extends ProfileActivity {

    private DatabaseWrapper dbw;

    private String userId;
    private Profile userProfile;
    private ArrayList<Book> userBooks;

    /**
     * The layout used by UserProfileActivity
     * @return The layout used by UserProfileActivity
     */
    public int getFragmentLayout() {
        return R.layout.activity_user_profile;
    }

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // new UI components
        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("userId");
        } else {
            Intent intent = new Intent(this, SomethingWentWrong.class);
            startActivity(intent);
            return;
        }
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
    }
}
