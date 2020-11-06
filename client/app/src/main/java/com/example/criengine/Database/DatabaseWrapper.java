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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Boolean debug = false;

    // Constructor for tests
    public DatabaseWrapper(DatabaseWrapper dbw) {
        DatabaseWrapper.dbw = dbw;
        this.user = dbw.user;
        this.userId = null;
        db = null;
        users = null;
        books = null;
    }

    public DatabaseWrapper(FirebaseFirestore dbw, FirebaseUser user) {
        this.user = user;
        this.userId = null;
        this.db = null;
        this.users = dbw.collection("users");
        this.books = dbw.collection("books");
        DatabaseWrapper.dbw = this;
    }

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
                            try {
                                return document.toObject(Profile.class);
                            }
                            catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                return null;
                            }
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
                            try {
                                return document.toObject(Book.class);
                            }
                            catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                return null;
                            }
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
     * @param book The  book to be deleted.
     * @return The book deleted from the database.
     */
    public Task<Boolean> deleteBook(final Book book) {
        return db.runTransaction(new Transaction.Function<Boolean>() {

            @Nullable
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                DocumentSnapshot userSnapshot = transaction.get(users.document(borrowerUid));
                DocumentSnapshot bookSnapshot = transaction.get(books.document(book.getBookID()));

                List<String> requesterList = (List<String>) bookSnapshot.get("requesters");
                if (requesterList == null) {
                    requesterList = new ArrayList<>();
                }

                String bookOwnerID = (String) bookSnapshot.get("owner");
                DocumentSnapshot bookOwnerProfile = null;
                List<String> ownedBookList = null;
                if (bookOwnerID != null) {
                    bookOwnerProfile = transaction.get(users.document(bookOwnerID));
                    ownedBookList = (List<String>) bookOwnerProfile.get("booksOwned");
                    ownedBookList.remove(book.getBookID());
                }

                String bookBorrowerID = (String) bookSnapshot.get("borrower");
                DocumentSnapshot bookBorrowerProfile = null;
                List<String> borrowedBookList = null;
                if (bookBorrowerID != null) {
                    bookBorrowerProfile = transaction.get(users.document(bookBorrowerID));
                    borrowedBookList = (List<String>) bookBorrowerProfile.get("booksBorrowedOrRequested");
                    borrowedBookList.remove(book.getBookID());
                }

                Map<String, List<String>> toDeleteUserBookListMap = new HashMap<>();
                for (String userID : requesterList) {
                    DocumentSnapshot requesterProfile = transaction.get(users.document(userID));
                    List<String> requestedBookList = (List<String>) requesterProfile.get("booksBorrowedOrRequested");
                    requestedBookList.remove(book.getBookID());
                    toDeleteUserBookListMap.put(userID, requestedBookList);
                }
                // Write
                for (String userID : requesterList) {
                    List<String> requestedBookList = toDeleteUserBookListMap.get(userID);
                    transaction.update(users.document(userID), "booksBorrowedOrRequested", requestedBookList);
                }

                if (ownedBookList != null) {
                    transaction.update(users.document(bookOwnerID), "booksOwned", ownedBookList);
                }

                if (borrowedBookList != null) {
                    transaction.update(users.document(bookBorrowerID), "booksBorrowedOrRequested", borrowedBookList);
                }

                transaction.delete(books.document(book.getBookID()));



//                if (profileList.contains(borrowerUid) || bookList.contains(bookID)) {
//                    return false;
//                }
//
//                bookList.add(bookID);
//                profileList.add(borrowerUid);
//                transaction.update(users.document(borrowerUid), "booksBorrowedOrRequested", bookList);
//                transaction.update(books.document(bookID), "status", "requested");
//                transaction.update(books.document(bookID), "requesters", profileList);
                return true;
            };
        });
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
                .whereEqualTo("owner", owner.getUserID())
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Book>>() {
                    @Override
                    public List<Book> then(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            assert query != null;
                            try {
                                return query.toObjects(Book.class);
                            }
                            catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                return null;
                            }
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return new ArrayList<Book>();
                        }
                    }
                });
    }

    /**
     * Get a list of Borrowed or Requested book from a user.
     * @param userID The profile of interest.
     * @return The list of borrowed of requested books.
     */
    public Task<List<Book>> getBorrowedOrRequestedBooks(String userID) {
        return books
                .whereArrayContains("requesters", userID)
                .get()
                .continueWith(new Continuation<QuerySnapshot, List<Book>>() {
                    @Override
                    public List<Book> then(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            assert query != null;
                            try {
                                return query.toObjects(Book.class);
                            }
                            catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                return null;
                            }
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            return new ArrayList<Book>();
                        }
                    }
                });
    }


    // TODO you cant request your own book
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
                transaction.update(books.document(bookID), "status", "requested");
                transaction.update(books.document(bookID), "requesters", profileList);
                return true;
            };
        });
    }
    public Task<Boolean> acceptRequest (final String borrowerUid, final String bookID) {
        return db.runTransaction(new Transaction.Function<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                // read
                DocumentSnapshot acceptedUserSnapshot = transaction.get(users.document(borrowerUid));
                DocumentSnapshot bookSnapshot = transaction.get(books.document(bookID));


                List<String> bookList = (List<String>) acceptedUserSnapshot.get("booksBorrowedOrRequested");
                if (bookList == null) {
                    bookList = new ArrayList<>();
                }
                List<String> notificationList = (List<String>) acceptedUserSnapshot.get("notification");
                if (bookList == null) {
                    bookList = new ArrayList<>();
                }
                List<String> profileList = (List<String>) bookSnapshot.get("requesters");
                if (profileList == null) {
                    profileList = new ArrayList<>();
                }

                if (!profileList.contains(borrowerUid) || !bookList.contains(bookID)) {
                    return false;
                }

                Map<String, List<String>> rejectedUserBookListMap = new HashMap<>();
                Map<String, List<String>> rejectedUserNotificationListMap = new HashMap<>();

                for (String profileID : profileList) {
                    if (profileID.compareTo(borrowerUid) == 0) {
                        DocumentSnapshot rejectedUserSnapshot = transaction.get(users.document(profileID));
                        List<String> rejectedUserBookList = (List<String>) rejectedUserSnapshot.get("booksBorrowedOrRequested");
                        if (rejectedUserBookList == null) {
                            rejectedUserBookList = new ArrayList<>();
                        }
                        rejectedUserBookList.remove(profileID);

                        List<String> rejectedUserNotificationList = (List<String>) rejectedUserSnapshot.get("notification");
                        if (rejectedUserNotificationList == null) {
                            rejectedUserNotificationList = new ArrayList<>();
                        }
                        // TODO: we need a notification table this is just a test
                        notificationList.add(bookID);

                        rejectedUserBookListMap.put(profileID, rejectedUserBookList);
                        rejectedUserNotificationListMap.put(profileID, rejectedUserNotificationList);
                    }
                }

                //write

                for (String profileID : profileList) {

                    transaction.update(users.document(profileID), "booksBorrowedOrRequested", rejectedUserBookListMap.get(profileID));
                    transaction.update(users.document(profileID), "notifications", rejectedUserNotificationListMap.get(profileID));

                }

                bookList.remove(bookID);
                // TODO: we need a notification table this is just a test
                notificationList.add(bookID);


                transaction.update(users.document(borrowerUid), "booksBorrowedOrRequested", bookList);
                // TODO: we need a notification table this is just a test
                transaction.update(users.document(borrowerUid), "notifications", notificationList);

                transaction.update(books.document(bookID), "requesters", new ArrayList<String>());
                transaction.update(books.document(bookID), "status", "accepted");
                transaction.update(books.document(bookID), "borrower", borrowerUid);


                return true;
            }
        });
    }
    public Task<Boolean> declineRequest (final String borrowerUid, final String bookID) {
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
                List<String> notificationList = (List<String>) userSnapshot.get("notifications");
                if (notificationList == null) {
                    notificationList = new ArrayList<>();
                }
                List<String> profileList = (List<String>) bookSnapshot.get("requesters");
                if (profileList == null) {
                    profileList = new ArrayList<>();
                }

                if (!profileList.contains(borrowerUid) || !bookList.contains(bookID)) {
                    return false;
                }

                bookList.remove(bookID);
                profileList.remove(borrowerUid);
                // TODO: we need a notification table this is just a test
                notificationList.add(bookID);

                transaction.update(users.document(borrowerUid), "booksBorrowedOrRequested", bookList);
                transaction.update(books.document(bookID), "requesters", profileList);
                // TODO: we need a notification table this is just a test
                transaction.update(users.document(borrowerUid), "notifications", notificationList);

                return null;
            }
        });
    }
    public Task<Void> borrowBook (String borrowerUid, String ISBN) {
        return null;
    }
    public Task<Void> confirmBorrowBook (String borrowerUid, String ISBN) {
        return null;
    }
    public Task<Void> returnBook (String borrowerUid, String ISBN) {
        return null;
    }
    public Task<Void> confrimReturnBook (String borrowerUid, String ISBN) {
        return null;
    }

}
