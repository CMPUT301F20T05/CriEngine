package com.example.criengine.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
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

    public Task<Void> deleteBook(String BookID) {
        return books.document(BookID).delete();
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


    public Task<Boolean> makeRequest (final String borrowerUid, final String bookID) {
        return db.runTransaction(new Transaction.Function<Boolean>() {

            @Nullable
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot userSnapshot = transaction.get(users.document(borrowerUid));
                DocumentSnapshot bookSnapshot = transaction.get(books.document(bookID));
                List<String> bookList = (List<String>) userSnapshot.get("booksBorrowedOrRequested");

                if (bookList == null) {
                    bookList = new ArrayList<>();
                }

                List<String> profileList = (List<String>) bookSnapshot.get("requesters");

                if (profileList == null) {
                    profileList = new ArrayList<>();
                }

                if (profileList.contains(borrowerUid) || bookList.contains(bookID)) {
                    return false;
                }

                bookList.add(bookID);
                profileList.add(borrowerUid);
                transaction.update(users.document(borrowerUid), "booksBorrowedOrRequested", bookList);
                transaction.update(books.document(bookID), "requesters", profileList);
                return true;
            };
        });
    }
    public Task<Void> acceptRequest (String borrowerUid, String bookID) {
        return null;
    }
    public Task<Void> declineRequest (String borrowerUid, String bookID) {
        return null;
    }
    public Task<Void> borrowBook (String borrowerUid, String ISBN) {
        return null;
    }
    public Task<Void> confrimBorrowBook (String borrowerUid, String ISBN) {
        return null;
    }
    public Task<Void> returnBook (String borrowerUid, String ISBN) {
        return null;
    }
    public Task<Void> confrimReturnBook (String borrowerUid, String ISBN) {
        return null;
    }

}
