package com.example.criengine;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.example.criengine.Adapters.NotificationAdapter;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Notification;
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
 * Unit tests for NotificationAdapter
 * Includes constructor and branch tests
 */
@RunWith(RobolectricTestRunner.class)
public class NotificationAdapterTest {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    DatabaseWrapper dbwMock;

    DatabaseWrapper dbw;
    private Context context;

    ArrayList<Notification> notificationList;
    Notification notification;
    Profile profile;

    View adaptedView;
    Button actionButton;
    TextView statusText;
    TextView headerText;

    /**
     * Initializes a mock database and context before every test
     */
    @Before
    public void setup() {
        dbwMock.userId = "testUser";
        dbw = new DatabaseWrapper(dbwMock);

        context = ApplicationProvider.getApplicationContext();

        profile = new Profile();
        notification = new Notification("Dummy Notification");
    }

    /**
     * Initializes a new NotificationAdapter with a list containing only book
     *  and obtains the view of the first item
     *  It then it properties: adaptedView, actionButton and statusText
     */
    public void setupAdaptedItemView() {
        notificationList = new ArrayList<>();
        notificationList.add(notification);
        NotificationAdapter NotificationAdapter =
                new NotificationAdapter(context, notificationList, profile, dbw);
        View itemView = View.inflate(context, R.layout.list_format, null);
        adaptedView = NotificationAdapter.getView(0, itemView, null);
        actionButton = adaptedView.findViewById(R.id.actionButton);
        statusText = adaptedView.findViewById(R.id.statusText);
        headerText = adaptedView.findViewById(R.id.headerText);
    }

    /**
     * Tests NotificationAdapter Constructor
     */
    @Test
    public void NotificationAdapterConstructorTest() {
        NotificationAdapter NotificationAdapter =
                new NotificationAdapter(context, notificationList, profile, dbw);
        Assert.assertNotNull(NotificationAdapter);
    }

    /**
     * Test the adaptedView made by the adapter
     */
    @Test
    public void NotificationAdapterViewTest() {
        setupAdaptedItemView();
        Assert.assertEquals("Dummy Notification", headerText.getText());
        Assert.assertEquals(notification.getDate(), statusText.getText());
        Assert.assertEquals("Dismiss", actionButton.getText());
    }
}
