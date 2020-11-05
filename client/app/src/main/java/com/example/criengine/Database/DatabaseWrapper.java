package com.example.criengine.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseWrapper {

    private static DatabaseWrapper dbw = null;

    final CollectionReference users;
    final CollectionReference books;
    public FirebaseUser user;
    public String userId;
    private FirebaseFirestore db;


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
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            return document.toObject(Profile.class);
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
                            return document.toObject(Book.class);
//                            return new Book(document.getData());
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return null;
                        }
                    }
                });
    }

    public Task<Void> addBook(final Book book) {
        // if book doesn't exist
        if (book.getBookID() == null) {
            // generate ref and add it to the book and it's profile
            DocumentReference ref = books.document();
            book.setBookID(ref.getId());
            dbw.getProfile(book.getOwner()).addOnSuccessListener(new OnSuccessListener<Profile>() {
                @Override
                public void onSuccess(Profile profile) {
                    ArrayList<String> books = profile.getBooksOwned();
                    books.add(book.getBookID());
                    profile.setBooksOwned(books);
                    dbw.addProfile(profile);
                }
            });
            return ref.set(book);
        } else {
            // otherwise use the bookID
            return books.document(book.getBookID()).set(book, SetOptions.merge());
        }
    }

    // Have a user request a book
    public Task<Void> requestBook(final Book book) {
        // Add userId to list of book's requesters
        ArrayList<String> requesters = (ArrayList<String>) book.getRequesters();
        requesters.add(userId);
        book.setRequesters(requesters);

        // Add book to list of user's requested books
        dbw.getProfile(userId).addOnSuccessListener(new OnSuccessListener<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                ArrayList<String> books = profile.getBooksBorrowedOrRequested();
                books.add(book.getBookID());
                profile.setBooksBorrowedOrRequested(books);
                dbw.addProfile(profile);
            }
        });
        // Update book in database
        return addBook(book);
    }

    // Have owner accept accept a borrowers request
    public Task<Void> acceptRequest(Book book, String borrowerId) {
        book.setBorrower(borrowerId);
        book.setStatus("accepted");
        return addBook(book);
    }

    public Task<Void> deleteBook(String BookID) {
        return books.document(BookID).delete();
    }

    public Task<List<Profile>> getRequesters(Book book) {
        ArrayList<String> requesters = (ArrayList<String>) book.getRequesters();
        if (requesters.isEmpty()) {
            List<Profile> profiles = new ArrayList<Profile>();
            return Tasks.forResult(profiles);
        }
        return users
                .whereIn("userID", requesters)
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Profile>>() {
                   @Override
                   public List<Profile> then(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           QuerySnapshot query = task.getResult();
                           assert query != null;
                           return query.toObjects(Profile.class);
                       } else {
                           Log.d(TAG, "Get Failure: " + task.getException());
                           return new ArrayList<Profile>();
                       }
                   }
                });
    }

    public Task<List<Book>> getOwnedBooks(Profile owner) {
        ArrayList<String> ownedBooks = owner.getBooksOwned();
        // TODO see if we can get away without owner (make the database get ownedbooks from uid)
        if (ownedBooks.isEmpty()) {
            List<Book> books = new ArrayList<Book>();
            return Tasks.forResult(books);
        }
        return books
                .whereIn("bookID", ownedBooks)
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Book>>() {
                    @Override
                    public List<Book> then(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            assert query != null;
                            return query.toObjects(Book.class);
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return new ArrayList<Book>();
                        }
                    }
                });
    }

    public Task<List<Book>> getBorrowedOrRequestedBooks(Profile user) {
        ArrayList<String> requestedBooks = user.getBooksBorrowedOrRequested();
        if (requestedBooks.isEmpty()) {
            List<Book> books = new ArrayList<Book>();
            return Tasks.forResult(books);
        }
        return books
                .whereIn("bookID", requestedBooks)
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Book>>() {
                    @Override
                    public List<Book> then(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            assert query != null;
                            return query.toObjects(Book.class);
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return new ArrayList<Book>();
                        }
                    }
                });
    }

    public Task<List<Book>> keywordSearch(String keyword) {
        String words[] = keyword.split(", ", 0);
        ArrayList<String> search = new ArrayList<String>();
        Collections.addAll(search, words);
        return books
                .whereIn("description", search)
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Book>>(){
                    @Override
                    public List<Book> then(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            assert query != null;
                            return query.toObjects(Book.class);
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return new ArrayList<Book>();
                        }
                    }
                });
    }

}
