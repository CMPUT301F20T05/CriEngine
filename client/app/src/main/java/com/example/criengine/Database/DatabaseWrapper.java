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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A wrapper class for the database. This is where the front end is capable of communicating with
 * the back end database.
 */
public class DatabaseWrapper {

    private static DatabaseWrapper dbw = null;

    final CollectionReference users;
    final CollectionReference books;
    public FirebaseUser user;
    public String userId;
    private FirebaseFirestore db;

    /**
     * Constructor for the wrapper.
     * @param user The firebase user.
     */
    public DatabaseWrapper(FirebaseUser user) {
        this.user = user;
        this.userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        users = db.collection("users");
        books = db.collection("books");
        dbw = this;
    }

    // public singleton pattern
    public static DatabaseWrapper getWrapper() {
        return dbw;
    }

    /**
     * Gets the profile from the databse.
     * @param userId The id of the user to get the profile from.
     * @return The user profile.
     */
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

    /**
     * Add a profile to the database.
     * @param profile The profile to add.
     * @return The user profile added into the database.
     */
    public Task<Void> addProfile(Profile profile) {
        // otherwise use the bookID
        return users.document(profile.getUserID()).set(profile, SetOptions.merge());
    }

    /**
     * Get a book from the database.
     * @param bookID The ID fo the book.
     * @return The book object.
     */
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

    /**
     * Add a book to the database.
     * @param book The book to be added.
     * @return The book added into the database.
     */
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

    /**
     * Delete a book from the database.
     * @param BookID The ID of the book.
     * @return The book deleted from the database.
     */
    public Task<Void> deleteBook(String BookID) {
        return books.document(BookID).delete();
    }

    /**
     * Get all books owned by a profile.
     * @param owner The owner of the account.
     * @return The list of books owned by that account.
     */
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

    /**
     * Get a list of Borrowed or Requested book from a user.
     * @param user The profile of interest.
     * @return The list of borrowed of requested books.
     */
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

}
