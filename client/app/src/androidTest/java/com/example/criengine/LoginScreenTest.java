package com.example.criengine;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Test class for the login activity. All the UI tests are written here. Robotium test framework is
 * used
 */
public class LoginScreenTest {
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
     * Log into an existing test account.
     */
    @Test
    public void successfulLoginTest() {
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
    }

    /**
     * Log into an invalid test account.
     */
    @Test
    public void failedLoginTest() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "user200@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "password");

        solo.clickOnButton("Login");

        // Wait for database validation.
        solo.sleep(5000);

        // Check to make sure the activity was not switched.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
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
