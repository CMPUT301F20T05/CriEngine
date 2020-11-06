package com.example.criengine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ApplicationProvider;

import com.example.criengine.Adapters.RequestsForBookAdapter;
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
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class RequestsForBookAdapterTest {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    DatabaseWrapper dbwMock;

    DatabaseWrapper dbw;
    private Context context;

    String requester;
    Book book;

    View adaptedView;
    Button rejectedButton;
    Button acceptButton;
    TextView usernameTextView;

    /**
     * Initializes a mock database and context before every test
     */
    @Before
    public void setup() {
        dbwMock.userId = "testUser";
        dbw = new DatabaseWrapper(dbwMock);

        context = ApplicationProvider.getApplicationContext();

        book = new Book();

        requester = "";
    }

    /**
     * Initializes a new RequestsForBookAdapter with the given list
     *  and obtains the view of the first item
     *  It then it properties: adaptedView, actionButton and statusText
     */
    public void setupAdaptedItemView(ArrayList<String> list) {
        RequestsForBookAdapter RequestsForBookAdapter =
                new RequestsForBookAdapter(context, list, book);
        View itemView = View.inflate(context, R.layout.user_accept_or_reject, null);
        adaptedView = RequestsForBookAdapter.getView(0, itemView, null);
        usernameTextView = adaptedView.findViewById(R.id.user_name);
        rejectedButton = adaptedView.findViewById(R.id.user_reject);
        acceptButton = adaptedView.findViewById(R.id.user_accept);
    }


    /**
     * Initializes a new RequestsForBookAdapter with a list containing only the requester property
     *  and obtains the view of the first item
     *  It then it properties: adaptedView, actionButton and statusText
     */
    public void setupAdaptedItemView() {
        ArrayList<String> requesterList = new ArrayList<>();
        requesterList.add(requester);
        setupAdaptedItemView(requesterList);
    }


    /**
     * Tests Adapter constructor
     */
    @Test
    public void ConstructorTest() {
        ArrayList<String> requesterList = new ArrayList<>();
        RequestsForBookAdapter RequestsForBookAdapter =
                new RequestsForBookAdapter(context, requesterList, book);
        Assert.assertNotNull(RequestsForBookAdapter);
    }

    /**
     * Tests that the Requester username is displayed
     */
    @Test
    public void RequesterUsernameTest() {
        requester = "user1";
        setupAdaptedItemView();
        Assert.assertEquals(requester, usernameTextView.getText());
    }

    /*
    @Test
    public void AcceptRequestTest() {
        ArrayList<String> requesterList = new ArrayList<>();
        requesterList.add("user1");

        setupAdaptedItemView(requesterList);
        activity.addContentView(adaptedView, new ViewGroup.LayoutParams(0,0));

        Assert.assertNull(book.getStatus());
        acceptButton.performClick();
        Assert.assertEquals("accepted", book.getStatus());
        Assert.assertEquals(0, requesterList.size());
    }

    @Test
    public void RejectRequestTest() {
        ArrayList<String> requesterList = new ArrayList<>();
        requesterList.add("user1");
        requesterList.add("user2");

        setupAdaptedItemView(requesterList);

        rejectedButton.performClick();
        setupAdaptedItemView(requesterList);
        Assert.assertEquals(1, requesterList.size());


        rejectedButton.performClick();
        setupAdaptedItemView(requesterList);
        Assert.assertEquals("available", book.getStatus());
        Assert.assertEquals(0, requesterList.size());
    }
    */

}
