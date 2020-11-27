package com.example.criengine;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.UserProfileActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Test class for the my request view activity. All the UI tests are written here.
 * Robotium test framework is used.
 */
public class MyRequestTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        TestUtilityMethods.login1(solo);
    }

    /**
     * Test to see if we can navigate to my requests.
     */
    @Test
    public void navigateToMyRequests() {
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_my_requests));
        assertTrue(solo.waitForText("My Requests", 1, 2000));
    }

    /**
     * Test to see if we can view requests for a book and the person's profile for who is requesting
     * it.
     */
    @Test
    public void makeRequestAndReject() {
        TestUtilityMethods.addBook(solo);
        TestUtilityMethods.logout(solo);
        TestUtilityMethods.login2(solo);
        TestUtilityMethods.searchBook(solo);

        solo.clickInList(0);
        solo.clickOnButton("Request This Book");
        TestUtilityMethods.logout(solo);

        // Checks if we got the notification in the first account.
        TestUtilityMethods.login1(solo);
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_notifications));
        solo.clickOnButton("Dismiss");

        // See if we can view their information.
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_my_books));
        solo.clickOnButton("See Requests");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.goBack();

        // Decline their request and app should automatically return to book list view.
        solo.clickOnButton("✖");
        assertTrue(solo.waitForText("Available", 1, 2000));
        TestUtilityMethods.logout(solo);

        // See if the second user got the notification that they were rejected.
        TestUtilityMethods.login2(solo);
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_notifications));
        solo.clickOnButton("Dismiss");
        TestUtilityMethods.logout(solo);

        // Cleanup.
        TestUtilityMethods.login1(solo);
        TestUtilityMethods.cleanup(solo);
    }

    /**
     * Test to see if we can view requests for a book and the person's profile for who is requesting
     * it. Then accept the book and test the workflow.
     */
    @Test
    public void makeRequestAndAccept() {
        TestUtilityMethods.addBook(solo);
        TestUtilityMethods.logout(solo);
        TestUtilityMethods.login2(solo);
        TestUtilityMethods.searchBook(solo);

        solo.clickInList(0);
        solo.clickOnButton("Request This Book");
        TestUtilityMethods.logout(solo);

        // Checks if we got the notification in the first account.
        TestUtilityMethods.login1(solo);
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_notifications));
        solo.clickOnButton("Dismiss");

        // Accept the request. Book should now be "Accepted" status.
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_my_books));
        solo.clickOnButton("See Requests");
        solo.clickOnButton("✔");
        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForText("Accepted", 1, 2000));
        TestUtilityMethods.logout(solo);

        // See if the second user got the notification that they were accepted.
        TestUtilityMethods.login2(solo);
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_notifications));
        solo.clickOnButton("Dismiss");
        TestUtilityMethods.logout(solo);

        // Cleanup.
        TestUtilityMethods.login1(solo);
        TestUtilityMethods.cleanup(solo);
    }

    /**
     * Closes the activity after each test
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
