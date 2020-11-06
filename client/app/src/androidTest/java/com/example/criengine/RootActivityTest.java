package com.example.criengine;

import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager2.widget.ViewPager2;

import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.RootActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * RootActvity test Suite
 * Uses Robotium Framework to test UI interaction
 */
public class RootActivityTest {
    private Solo solo;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Initialize solo instance before all tests
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        // Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "user2@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "password");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        viewPager = solo.getCurrentActivity().findViewById(R.id.view_pager);
        bottomNav = solo.getCurrentActivity().findViewById(R.id.bottom_navigation);
    }

    /**
     * Tests that the activity starts on the right page
     */
    @Test
    public void initialPageTest() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        assertTrue(solo.waitForText("My Books", 1, 10000));
        Assert.assertEquals(viewPager.getCurrentItem(), 3);
        Assert.assertEquals(bottomNav.getSelectedItemId(), R.id.bottom_navigation_item_my_books);
    }

    /**
     * Swipe test
     */
    @Test
    public void swipeTest() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);
        assertTrue(solo.waitForText("My Books", 1, 10000));

        // Right Swipe - My profile
        solo.scrollToSide(Solo.RIGHT);;

        assertTrue(solo.waitForText("My Profile", 1, 10000));
        Assert.assertEquals(bottomNav.getSelectedItemId(), R.id.bottom_navigation_item_profile);

        // Left Swipe - My books
        solo.scrollToSide(Solo.LEFT);;

        assertTrue(solo.waitForText("My Books", 1, 10000));
        Assert.assertEquals(bottomNav.getSelectedItemId(), R.id.bottom_navigation_item_my_books);

        // Left Swipe - My Requests
        solo.scrollToSide(Solo.LEFT);

        assertTrue(solo.waitForText("My Requests", 1, 10000));
        Assert.assertEquals(bottomNav.getSelectedItemId(), R.id.bottom_navigation_item_my_requests);

        // Left Swipe - Notifications
        solo.scrollToSide(Solo.LEFT);

        assertTrue(solo.waitForText("Notifications", 1, 10000));
        Assert.assertEquals(bottomNav.getSelectedItemId(), R.id.bottom_navigation_item_notifications);

        // Left Swipe - Search
        solo.scrollToSide(Solo.LEFT);

        assertTrue(solo.waitForText("Search", 1, 10000));
        Assert.assertEquals(bottomNav.getSelectedItemId(), R.id.bottom_navigation_item_search);
    }

    /**
     * BottomNavigation test
     */
    @Test
    public void bottomNavigationTest() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);
        assertTrue(solo.waitForText("My Books", 1, 10000));

        // Search Menu Button
        View searchMenuItem
                = solo.getCurrentActivity().findViewById(R.id.bottom_navigation_item_search);
        solo.clickOnView(searchMenuItem);
        assertTrue(solo.waitForText("Search", 1, 5000));
        assertEquals(viewPager.getCurrentItem(), 0);

        // Notification Menu Button
        View notificationsMenuItem
                = solo.getCurrentActivity().findViewById(R.id.bottom_navigation_item_notifications);
        solo.clickOnView(notificationsMenuItem);
        assertTrue(solo.waitForText("Notifications", 1, 5000));
        assertEquals(viewPager.getCurrentItem(), 1);

        // My Requests Menu Button
        View myRequestsMenuItem
                = solo.getCurrentActivity().findViewById(R.id.bottom_navigation_item_my_requests);
        solo.clickOnView(myRequestsMenuItem);
        assertTrue(solo.waitForView(R.id.my_requests_layout,1,5000));
        assertEquals(viewPager.getCurrentItem(), 2);

        // My Books Menu Button
        View myBooksmenuItem
                = solo.getCurrentActivity().findViewById(R.id.bottom_navigation_item_my_books);
        solo.clickOnView(myBooksmenuItem);
        assertTrue(solo.waitForText("My Books", 2, 5000));
        assertEquals(viewPager.getCurrentItem(), 3);

        // My Profile Menu Button
        View myProfileMenuItem
                = solo.getCurrentActivity().findViewById(R.id.bottom_navigation_item_profile);
        solo.clickOnView(myProfileMenuItem);
        assertTrue(solo.waitForText("My Profile", 1, 5000));
        assertEquals(viewPager.getCurrentItem(), 4);

    }
}
