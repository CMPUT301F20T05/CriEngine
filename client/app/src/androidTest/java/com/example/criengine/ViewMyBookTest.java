package com.example.criengine;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.MyBookActivity;
import com.example.criengine.Activities.RootActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for the my book fragment. All the UI tests are written here.
 * Robotium test framework is used.
 */
public class ViewMyBookTest {
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
     * Test to see if the editing the title and saving it will truly make the change.
     */
    @Test
    public void editTitleTest() {
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

        solo.clickInList(0);

        // Enter in a new title and save changes.
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        solo.clickOnButton("Edit Book");
        assertTrue(solo.waitForText("Cancel", 1, 2000));
        solo.clearEditText((EditText) solo.getView(R.id.bookView_title));
        solo.enterText((EditText) solo.getView(R.id.bookView_title), "This is a new title.");
        solo.clickOnButton("Save");

        // Test to see if the changes were acknowledged.
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        assertTrue(solo.waitForText("This is a new title.", 1, 2000));
    }

    /**
     * Test to see if making an edit to the title and then cancelling will undo the changes.
     */
    @Test
    public void cancelEditTitleTest() {
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

        solo.clickInList(0);

        // Enter in a new book title and cancel changes.
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        solo.clickOnButton("Edit Book");
        assertTrue(solo.waitForText("Cancel", 1, 2000));
        solo.clearEditText((EditText) solo.getView(R.id.bookView_title));
        solo.enterText((EditText) solo.getView(R.id.bookView_title), "This is a new title.");
        solo.clickOnButton("Cancel");

        // Make sure that the changes were not saved.
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        assertFalse(solo.waitForText("This is a new title.", 1, 2000));
    }

    /**
     * Test to see if deleting a book will return you to the main screen.
     */
    @Test
    public void deleteBookTest() {
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

        solo.clickInList(0);

        String title = solo.getView(R.id.bookView_title).toString();

        // Click on delete book.
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        solo.clickOnButton("Delete Book");

        // Wait for the prompt.
        assertTrue(solo.waitForText("Are you sure you want to delete this book?", 1, 2000));
        solo.clickOnText("Delete");

        // Make sure we changed screens.
        solo.sleep(2000);
        assertTrue(solo.waitForText("My Books", 1, 2000));
    }

    /**
     * Test to see if clicking on cancel while in the delete dialog will return to the original
     * activity.
     */
    @Test
    public void cancelDeleteBookTest() {
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

        solo.clickInList(0);

        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        solo.clickOnButton("Delete Book");

        assertTrue(solo.waitForText("Are you sure you want to delete this book?", 1, 2000));
        solo.clickOnText("Cancel");

        // Make sure we didnt change screens.
        solo.sleep(2000);
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
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
