package com.example.criengine;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.RootActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;


/**
 * Test class for the notification activity. All the UI tests are written here.
 * Robotium test framework is used.
 * Note: Dismissing the notification will not be tested as that is a database call. These will not
 * be tested as confirmed by the TA.
 */
public class NotificationTest {
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
    }

    /**
     * Test to see if we can navigate to the notifications fragment.
     */
    @Test
    public void navigateToNotificationsTest() {
        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Input username and password
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "intentTestingUser@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "intentTesting");

        solo.clickOnButton("Login");

        // Returns True if you can find "My Books" on the screen. Waits 30 seconds to find
        // at least 1 match.
        assertTrue(solo.waitForText("My Books", 1, 30000));

        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_notifications));

        assertTrue(solo.waitForText("Notifications", 1, 2000));
    }

    /**
     * Closes the activity after each test
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
