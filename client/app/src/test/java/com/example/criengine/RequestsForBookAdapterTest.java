package com.example.criengine;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.example.criengine.Adapters.RequestsForBookAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;

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
 * Unit tests for RequestsForBookAdapter
 * Includes constructor and branch tests
 */
@RunWith(RobolectricTestRunner.class)
public class RequestsForBookAdapterTest {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    DatabaseWrapper dbwMock;

    DatabaseWrapper dbw;
    private Context context;

    Profile requester;
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

        requester = new Profile();
    }

    /**
     * Initializes a new RequestsForBookAdapter with the given list
     *  and obtains the view of the first item
     *  It then it properties: adaptedView, actionButton and statusText
     */
    public void setupAdaptedItemView(ArrayList<Profile> list) {
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
        ArrayList<Profile> requesterList = new ArrayList<>();
        requesterList.add(requester);
        setupAdaptedItemView(requesterList);
    }


    /**
     * Tests Adapter constructor
     */
    @Test
    public void ConstructorTest() {
        ArrayList<Profile> requesterList = new ArrayList<>();
        RequestsForBookAdapter RequestsForBookAdapter =
                new RequestsForBookAdapter(context, requesterList, book);
        Assert.assertNotNull(RequestsForBookAdapter);
    }
}
