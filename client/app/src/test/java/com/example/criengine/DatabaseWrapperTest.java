package com.example.criengine;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DatabaseWrapperTest {

@Rule
public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    FirebaseFirestore firestoreMock;

    @Mock
    FirebaseUser userMock;

//    @Mock


    @Mock
    Task<DocumentSnapshot> mockDocumentTaskSnapshot;


    @Mock
    Task<Book> mockTaskBook;

    DatabaseWrapper dbw;

    CollectionReference bookRefMock;
    CollectionReference userRefMock;

    @Before
    public void setup() {
        userRefMock = mock(CollectionReference.class, RETURNS_DEEP_STUBS);
        bookRefMock = mock(CollectionReference.class, RETURNS_DEEP_STUBS);
        when(firestoreMock.collection("users")).thenReturn(userRefMock);
        when(firestoreMock.collection("books")).thenReturn(bookRefMock);
        dbw = new DatabaseWrapper(firestoreMock, userMock);
    }

    @Test
    public void databaseConstructorTest() {
        Assert.assertNotNull(dbw);
        Assert.assertNotNull(DatabaseWrapper.getWrapper());
    }

    @Captor
    ArgumentCaptor<Continuation<DocumentSnapshot, Profile>> continuationProfileCaptor;
    @Test
    public void databaseGetProfileTest() throws Exception {
        DocumentSnapshot mockDocumentSnapshot = mock(DocumentSnapshot.class, RETURNS_DEEP_STUBS);
        when(userRefMock.document(Mockito.anyString()).get()).thenReturn(mockDocumentTaskSnapshot);

        dbw.getProfile("testUserId");

        verify(mockDocumentTaskSnapshot).continueWith(continuationProfileCaptor.capture());

        Continuation<DocumentSnapshot, Profile> continuation = continuationProfileCaptor.getValue();
        when(mockDocumentTaskSnapshot.isSuccessful()).thenReturn(true);
        when(mockDocumentTaskSnapshot.getResult()).thenReturn(mockDocumentSnapshot);

        Profile profile = new Profile();
        when(mockDocumentSnapshot.toObject((Class<Object>) Mockito.any())).thenReturn(profile);

        Profile returnedProfile = continuation.then(mockDocumentTaskSnapshot);
        Assert.assertNotNull(profile);
        Assert.assertEquals(profile, returnedProfile);
    }

    @Captor
    ArgumentCaptor<Continuation<DocumentSnapshot, Book>> continuationBookCaptor;
    @Test
    public void databaseGetBookTest() throws Exception {
        DocumentSnapshot mockDocumentSnapshot = mock(DocumentSnapshot.class, RETURNS_DEEP_STUBS);
        when(bookRefMock.document(Mockito.anyString()).get()).thenReturn(mockDocumentTaskSnapshot);

        dbw.getBook("testUserId");

        verify(mockDocumentTaskSnapshot).continueWith(continuationBookCaptor.capture());

        Continuation<DocumentSnapshot, Book> continuation = continuationBookCaptor.getValue();
        when(mockDocumentTaskSnapshot.isSuccessful()).thenReturn(true);
        when(mockDocumentTaskSnapshot.getResult()).thenReturn(mockDocumentSnapshot);

        Book book = new Book();
        when(mockDocumentSnapshot.toObject((Class<Object>) Mockito.any())).thenReturn(book);

        Book returnedBook = continuation.then(mockDocumentTaskSnapshot);
        Assert.assertNotNull(book);
        Assert.assertEquals(book, returnedBook);
    }



}
