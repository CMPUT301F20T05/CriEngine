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
import static org.junit.Assert.assertFalse;

/**
 * Test class for the my books list view activity. All the UI tests are written here.
 * Robotium test framework is used.
 */
public class MyBooksListTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Test to see if the filter button functions properly.
     */
    @Test
    public void filterButtonTest() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "user2@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "password");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 10 seconds to find
        // at least 1 match.
        assertTrue(solo.waitForText("My Books", 1, 10000));

        solo.clickOnButton("Filter");

        // Check if every option (4 total) is available to be clicked on.
        assertTrue(solo.waitForText("Filter By Status", 1, 2000));
        assertTrue(solo.waitForText("Available", 1, 2000));
        assertTrue(solo.waitForText("Requested", 1, 2000));
        assertTrue(solo.waitForText("Accepted", 1, 2000));
        assertTrue(solo.waitForText("Borrowed", 1, 2000));

        // Filter for only available books.
        solo.clickOnText("Available");
        solo.clickOnText("Confirm");
        assertFalse(solo.waitForText("Accepted", 1, 2000));
        assertFalse(solo.waitForText("Has Requests", 1, 2000));
        assertFalse(solo.waitForText("Borrowed", 1, 2000));

        // Add on two more filters to see if it allows for multiple filters at once.
        solo.clickOnButton("Filter");
        solo.clickOnText("Requested");
        solo.clickOnText("Accepted");
        solo.clickOnText("Confirm");
        assertFalse(solo.waitForText("Borrowed", 1, 2000));
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
