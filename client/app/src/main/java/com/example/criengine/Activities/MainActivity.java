package com.example.criengine.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.criengine.Activities.MyBooksActivity;
import com.example.criengine.Activities.MyProfileActivity;
import com.example.criengine.Activities.RequestActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final boolean CONFIG_LOGIN_AUTOMATICALLY = true;
    private final String CONFIG_AUTO_LOGIN_EMAIL = "user2@email.com";
    private final String CONFIG_AUTO_LOGIN_PASSWORD = "password";

    private Button myProfileActivityButton;
    private Button myBooksActivityButton;
    private Button requestActivityButton;
    private Button rootActivityButton;
    private Button myBookActivityButton;
    private Button notificationActivityButton;

    DatabaseWrapper dbw; // needs to be initialized by sign in
    Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // router to other activities for testing
        requestActivityButton = findViewById(R.id.requested_books_activity);
        myProfileActivityButton = findViewById(R.id.my_profile_activity_button);
        myBooksActivityButton = findViewById(R.id.myBooksButton);
        rootActivityButton = findViewById(R.id.rootActivityButton);

        requestActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RequestActivity.class);
                v.getContext().startActivity(intent);
            }
        });

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

        rootActivityButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(v.getContext(), RootActivity.class);
                  v.getContext().startActivity(intent);
              }
        });

        // router to book activity
        myBookActivityButton = findViewById(R.id.my_book_activity_button);

        myBookActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyBookActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        notificationActivityButton = findViewById(R.id.notificationButton);
              
        notificationActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NotificationActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        //begin sign in
        // accounts: user1@email.com:password
        //           user2@email.com:password
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null || CONFIG_LOGIN_AUTOMATICALLY) {
            if (CONFIG_LOGIN_AUTOMATICALLY) {
                // Sign in with given username and password
                mAuth.signInWithEmailAndPassword(CONFIG_AUTO_LOGIN_EMAIL, CONFIG_AUTO_LOGIN_PASSWORD)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                final FirebaseUser user = mAuth.getCurrentUser();
                                dbw = new DatabaseWrapper(user);
//                                dbw.getProfile(user.getUid()).addOnSuccessListener(
//                                        new OnSuccessListener<Profile>() {
//                                            @Override
//                                            public void onSuccess(Profile profile) {
//                                                // Add a profile if none existed for this user
//                                                dbw.addProfile(profile);
//                                            }
//                                        }
//                                );
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                // ...
                            }

                            // ...
                        }
                });
        } else {
                // Create a sign in activity
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
        } else {
            if (dbw == null) {
                dbw = new DatabaseWrapper(mAuth.getCurrentUser());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                dbw = new DatabaseWrapper(user);
                dbw.getProfile(user.getUid()).addOnSuccessListener(
                        new OnSuccessListener<Profile>() {
                            @Override
                            public void onSuccess(Profile profile) {
                                if (profile == null) {
                                    // Add a profile if none existed for this user
                                    userProfile = new Profile(user.getUid(), user.getEmail(),"username", "phone_number", "first","last");
                                    dbw.addProfile(userProfile);
                                } else {
                                    userProfile = profile;
                                }
                            }
                        }
                );

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