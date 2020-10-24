package com.example.criengine;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class DatabaseWrapper {

    private static DatabaseWrapper dbw = null;

    final CollectionReference users;
    final CollectionReference book;
    public String myUsername;
    private FirebaseFirestore db;

    public DatabaseWrapper(String myUsername) {
        this.myUsername = myUsername;
        db = FirebaseFirestore.getInstance();
        users = db.collection("users");
        book = db.collection("books");
        dbw = this;
    }

    //public singleton pattern
    public static DatabaseWrapper getWrapper() {
        if (dbw == null) {
            dbw = new DatabaseWrapper(null);
        }
        return dbw;
    }

    public Task<Profile> getProfile(String username) {
        return users
                .document(username)
                .get()
                .continueWith(new Continuation<DocumentSnapshot, Profile>() {
                    @Override
                    public Profile then(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, "Get document data: " + document.getData());
                            return new Profile(document.getData());
                        } else {
                            Log.d(TAG, "Get get failed: ", task.getException());
                            return null;
                        }
                    }
                });
    }
}
