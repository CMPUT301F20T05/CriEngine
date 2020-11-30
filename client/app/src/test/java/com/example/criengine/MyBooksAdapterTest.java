package com.example.criengine;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.example.criengine.Adapters.MyBooksAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Fragments.MyBooksListFragment;
import com.example.criengine.Objects.Book;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

/**
 * Unit tests for MyBooksAdapter
 * Includes constructor and branch tests
 */
@RunWith(RobolectricTestRunner.class)
public class MyBooksAdapterTest {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    DatabaseWrapper dbwMock;

    DatabaseWrapper dbw;
    private Context context;
    Book book;

    View adaptedView;
    Button actionButton;
    TextView statusText;

    MyBooksListFragment fragment;

    /**
     * Initializes a mock database and context before every test
     */
    @Before
    public void setup() {
        dbwMock.userId = "testUser";
        dbw = new DatabaseWrapper(dbwMock);

        context = ApplicationProvider.getApplicationContext();

        book = new Book();
        book.setTitle("Test");
        book.setStatus("");

        fragment = new MyBooksListFragment();
    }

    /**
     * Initializes a new MyBooksAdapter with a list containing only book
     *  and obtains the view of the first item
     *  It then it properties: adaptedView, actionButton and statusText
     */
    public void setupAdaptedItemView() {
        ArrayList<Book> bookList = new ArrayList<>();
        bookList.add(book);
        MyBooksAdapter MyBooksAdapter = new MyBooksAdapter(context, bookList, fragment);
        View itemView = View.inflate(context, R.layout.list_format, null);
        adaptedView = MyBooksAdapter.getView(0, itemView, null);
        actionButton = adaptedView.findViewById(R.id.actionButton);
        statusText = adaptedView.findViewById(R.id.statusText);
    }

    /**
     * Tests BorrowerBooksListAdapter Constructor
     */
    @Test
    public void MyBooksAdapterConstructorTest() {
        ArrayList<Book> bookList = new ArrayList<>();
        MyBooksAdapter MyBooksAdapter = new MyBooksAdapter(context, bookList, fragment);
        Assert.assertNotNull(MyBooksAdapter);
    }

    /**
     * Tests the view in the case of a requested book
     */
    @Test
    public void MyBooksAdapterBookRequestedTest() {
        book.setStatus("requested");
        setupAdaptedItemView();
        Assert.assertEquals("See Requests", actionButton.getText());
        Assert.assertEquals("Has Requests", statusText.getText());
    }

    /**
     * Tests the view in the case of an available book
     */
    @Test
    public void MyBooksAdapterBookAvailableTest() {
        book.setStatus("none");
        setupAdaptedItemView();
        Assert.assertEquals("Available", statusText.getText());
    }
}
