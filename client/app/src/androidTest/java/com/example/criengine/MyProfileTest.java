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
 * Test class for the my profile fragment. All the UI tests are written here.
 * Robotium test framework is used.
 */
public class MyProfileTest {
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
     * Test to see if we can enter in and save a new Bio for a user profile.
     */
    @Test
    public void addNewBioTest() {
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

        // Navigate to the user profile fragment.
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_profile));

        // Add in new text for the bio.
        assertTrue(solo.waitForText("Edit", 1, 2000));
        solo.clickOnButton("Edit");
        assertTrue(solo.waitForText("Cancel", 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.bio_text), "This is a new bio!");

        // Save and check to see if it was updated.
        solo.clickOnButton("Save");
        solo.sleep(1000);
        assertTrue(solo.waitForText("This is a new bio!", 1, 2000));
    }

    /**
     * Test to see if the cancel will work. (Changes to the bio during the editing stage will not
     * be saved)
     */
    @Test
    public void cancelNewBioTest() {
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

        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_profile));

        // Add in a new bio.
        assertTrue(solo.waitForText("Edit", 1, 2000));
        solo.clickOnButton("Edit");
        assertTrue(solo.waitForText("Cancel", 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.bio_text), "This is a new bio!");

        // Test to see if the cancel did not save the changes.
        solo.clickOnButton("Cancel");
        assertFalse(solo.waitForText("This is a new bio!", 1, 2000));
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
