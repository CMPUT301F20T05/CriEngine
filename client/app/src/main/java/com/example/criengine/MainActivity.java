package com.example.criengine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button myProfileActivityButton;
    private Button myBooksActivityButton;
    private Button requestActivityButton;
    DatabaseWrapper dbw; // needs to be initialized by sign in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestActivityButton = findViewById(R.id.requested_books_activity);

        requestActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RequestActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // router to other activities for testing
        myProfileActivityButton = findViewById(R.id.my_profile_activity_button);
        myBooksActivityButton = findViewById(R.id.myBooksButton);

        myProfileActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyProfileActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        myBooksActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyBooksActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        //begin sign in
        // accounts: user1@email.com:password
        //           user2@email.com:password
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
//                        .setLogo(R.drawable.my_great_logo)
//                        .setTheme(R.style.MySuperAppTheme)
                        .build(),
                1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println(user);
                assert user != null;
                dbw = new DatabaseWrapper(user);

                // Add a profile if none existed for this user
                Profile profile = new Profile(user.getUid(), user.getEmail(),"username", "phone_number", "first","last");
                dbw.addProfile(profile);

//                Test code please ignore
//                ArrayList<String> requester = new ArrayList<String>();
//                requester.add("xyz");
//                requester.add("abc");
//                Book book = new Book("testBook", "owner", "title", "author", "description", "isbn", "Active", "borrower", requester, "geolocation", "imageURL");
//                dbw.addBook(book);
//
//                Book book2 = new Book("testBook2", "owner", "title", "author", "description", "isbn", "Active", "borrower", requester, "geolocation", "imageURL");
//                dbw.addBook(book2);
//
//                ArrayList<String> ownedBooks = profile.getBooksOwned();
//                ownedBooks.add(book.getBookID());
//                ownedBooks.add(book2.getBookID());
//
//                profile.setBooksOwned(ownedBooks);
//
//                dbw.getOwnedBooks(profile).addOnSuccessListener(
//                        new OnSuccessListener<List<Book>>() {
//                            @Override
//                            public void onSuccess(List<Book> ownBooks) {
//                                for (Book i : ownBooks) {
//                                    System.out.println(i);
//                                    System.out.println(i.getBookID());
//                                }
//                            }
//                        }
//                );
                // ...user1
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}