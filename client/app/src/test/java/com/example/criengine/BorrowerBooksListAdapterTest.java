package com.example.criengine;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.example.criengine.Adapters.BorrowerBooksListAdapter;
import com.example.criengine.Database.DatabaseWrapper;
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
 * Unit tests for BorrowerBooksListAdapter
 * Includes constructor and branch tests
 */
@RunWith(RobolectricTestRunner.class)
public class BorrowerBooksListAdapterTest {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    DatabaseWrapper dbwMock;

    DatabaseWrapper dbw;
    private Context context;
    Book book;

    View adaptedView;
    Button actionButton;
    TextView statusText;

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

    }

    /**
     * Initializes a new BorrowerBooksListAdapter with a single book
     *  and obtains the view of the first item
     *  It then it properties: adaptedView, actionButton and statusText
     */
    public void setupAdaptedItemView() {
        ArrayList<Book> bookList = new ArrayList<>();
        bookList.add(book);
        BorrowerBooksListAdapter borrowerBooksListAdapter = new BorrowerBooksListAdapter(context, bookList);
        View itemView = View.inflate(context, R.layout.list_format, null);
        adaptedView = borrowerBooksListAdapter.getView(0, itemView, null);
        actionButton = adaptedView.findViewById(R.id.actionButton);
        statusText = adaptedView.findViewById(R.id.statusText);
    }

    /**
     * Tests BorrowerBooksListAdapter Constructor
     */
    @Test
    public void constructorTest() {
        ArrayList<Book> bookList = new ArrayList<>();
        BorrowerBooksListAdapter borrowerBooksListAdapter = new BorrowerBooksListAdapter(context, bookList);
        Assert.assertNotNull(borrowerBooksListAdapter);
    }

    /**
     * Tests the view in the case of a waiting request
     */
    @Test
    public void waitingRequestTest() {
        book.addRequesters(dbwMock.userId);
        setupAdaptedItemView();

        Assert.assertEquals("Cancel", actionButton.getText());
    }

    /**
     * Tests the view in the case of an accepted request
     */
    @Test
    public void acceptedRequestTest() {
        book.setBorrower(dbwMock.userId);
        setupAdaptedItemView();
        Assert.assertEquals("Scan", actionButton.getText());
    }

    /**
     * Tests the view in the case of a rejected request
     */
    @Test
    public void rejectedRequestTest() {
        book.setBorrower("nobody");
        setupAdaptedItemView();
        Assert.assertEquals("Ok", actionButton.getText());
        Assert.assertEquals("Rejected", statusText.getText());
    }
}
