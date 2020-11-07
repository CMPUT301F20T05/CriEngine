package com.example.criengine;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.criengine.Activities.AddBookActivity;
import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.RootActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;


/**
 * Test class for the add a book activity. All the UI tests are written here. Robotium test
 * framework is used.
 * Note: The addition of the book to the database will not be tested as database methods will not
 * be tested. Confirmed by TA.
 */
public class AddABookTest {
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

        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Input username and password
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "intentTestingUser@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "intentTesting");

        solo.clickOnButton("Login");

    }

    /**
     * Test to see if a user can edit the fields of a new book before creating it.
     */
    @Test
    public void editFieldsTest() {


        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 10 seconds to find
        // at least 1 match.
        assertTrue(solo.waitForText("My Books", 1, 10000));

        solo.clickOnButton("Add A Book");

        // Asserts that the current activity is the AddBookActivity.
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);

        assertTrue(solo.waitForText("Save", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.newBookTitle), "This is a new book Title");
        solo.enterText((EditText) solo.getView(R.id.newBookDesc), "This is a new Description");
        solo.enterText((EditText) solo.getView(R.id.newBookAuthor), "This is a new Author");
        solo.enterText((EditText) solo.getView(R.id.newBookISBN), "This is a new ISBN");
        solo.enterText((EditText) solo.getView(R.id.newBookImageURL), "This is an optional input");

        solo.clickOnButton("Cancel");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
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
