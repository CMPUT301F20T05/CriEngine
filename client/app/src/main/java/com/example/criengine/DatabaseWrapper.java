package com.example.criengine;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseWrapper {

    private static DatabaseWrapper dbw = null;

    final CollectionReference users;
    final CollectionReference books;
    public FirebaseUser user;
    public String userId;
    private FirebaseFirestore db;

//    public DatabaseWrapper(String myUsername) {
//        this.myUsername = myUsername;
//        db = FirebaseFirestore.getInstance();
//        users = db.collection("users");
//        book = db.collection("books");
//        dbw = this;
//    }

    public DatabaseWrapper(FirebaseUser user) {
        this.user = user;
        this.userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        users = db.collection("users");
        books = db.collection("books");
        dbw = this;
    }

    //public singleton pattern
    public static DatabaseWrapper getWrapper() {
        return dbw;
    }

    public Task<Profile> getProfile(String userId) {
        return users
                .document(userId)
                .get()
                .continueWith(new Continuation<DocumentSnapshot, Profile>() {
                    @Override
                    public Profile then(@NonNull Task<DocumentSnapshot> task) {
                        System.out.println("get profile callback");
                        System.out.println(task.isSuccessful());
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            System.out.println(document);
                            assert document != null;
                            Profile p = document.toObject(Profile.class);
                            System.out.println("success: " + p);
                            return p;
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return null;
                        }
                    }
                });
    }

    public Task<Void> addProfile(Profile profile) {
        // otherwise use the bookID
        return users.document(profile.getUserID()).set(profile, SetOptions.merge());
    }

    public Task<Book> getBook(String bookID) {
        return books
                .document(bookID)
                .get()
                .continueWith(new Continuation<DocumentSnapshot, Book>() {
                    @Override
                    public Book then(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            Log.d(TAG, "Get book data: " + document.getData());
                            System.out.println(document.getData());
                            return document.toObject(Book.class);
//                            return new Book(document.getData());
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return null;
                        }
                    }
                });
    }

    public Task<Void> addBook(Book book) {
        // if book doesn't exist
        if (book.getBookID() == null) {
            // generate ref and add it to the book
            DocumentReference ref = books.document();
            book.setBookID(ref.getId());
            return ref.set(book);
        } else {
            // otherwise use the bookID
            return books.document(book.getBookID()).set(book, SetOptions.merge());
        }
    }

    public Task<Void> deleteBook(String BookID) {
        return books.document(BookID).delete();
    }

    public Task<List<Book>> getOwnedBooks(String BookID) {
        return null;
    }

    public Task<List<Book>> getBorrowedOrRequestedBooks(String BookID) {
        return null;
    }

}
