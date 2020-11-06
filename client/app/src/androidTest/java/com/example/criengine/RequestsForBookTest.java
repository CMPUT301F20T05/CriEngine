package com.example.criengine;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.RequestsForBookActivity;
import com.example.criengine.Activities.RootActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for the requests for (my) book class. All the UI tests are written here.
 * Robotium test framework is used.
 */
public class RequestsForBookTest {
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
     * Test to see if accepting a requester will return to the main screen.
     */
    @Test
    public void acceptARequestTest() {
        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "user2@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "password");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 10 seconds to find
        // at least 1 match.
        assertTrue(solo.waitForText("My Books", 1, 10000));
        solo.clickOnButton("See Requests");

        // Asserts that the current activity is the RequestsForBookActivity.
        solo.assertCurrentActivity("Wrong Activity", RequestsForBookActivity.class);
        assertTrue(solo.waitForText("✔", 1, 2000));

        solo.clickOnButton("✔");

        // Asserts that the current activity is the RequestsForBookActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);
    }

    /**
     * Test to see if declining a request will remain on the same page.
     */
    @Test
    public void declineARequestTest() {
        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "user2@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "password");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 10 seconds to find
        // at least 1 match.
        assertTrue(solo.waitForText("My Books", 1, 10000));
        solo.clickOnButton("See Requests");

        // Asserts that the current activity is the RequestsForBookActivity.
        solo.assertCurrentActivity("Wrong Activity", RequestsForBookActivity.class);
        assertTrue(solo.waitForText("✖", 1, 2000));

        solo.clickOnButton("✖");

        // Asserts that the current activity is the RequestsForBookActivity.
        solo.assertCurrentActivity("Wrong Activity", RequestsForBookActivity.class);
    }

    /**
     * Test to see if declining all request will return to main screen.
     */
    @Test
    public void declineAllRequestsTest() {
        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "user2@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "password");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 10 seconds to find
        // at least 1 match.
        assertTrue(solo.waitForText("My Books", 1, 10000));
        solo.clickOnButton("See Requests");

        // Asserts that the current activity is the RequestsForBookActivity.
        solo.assertCurrentActivity("Wrong Activity", RequestsForBookActivity.class);

        // Decline all requests.
        while (solo.waitForText("✖", 1, 2000)) {
            solo.clickOnButton("✖");
        }

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);
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
