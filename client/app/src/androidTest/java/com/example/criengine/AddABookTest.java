package com.example.criengine;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.criengine.Activities.AddBookActivity;
import com.example.criengine.Activities.CameraActivity;
import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.RootActivity;
import com.example.criengine.Activities.ScanActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Test class for the add a book activity. All the UI tests are written here. Robotium test
 * framework is used.
 */
public class AddABookTest {
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

        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Input username and password
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "intentTestingUser@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "intentTesting");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 50 seconds to find
        // at least 1 match. This is to counter potentially long wait times when logging in.
        assertTrue(solo.waitForText("My Books", 1, 50000));

        solo.clickOnButton("Add A Book");

        // Asserts that the current activity is the AddBookActivity.
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
    }

    /**
     * Test to see if the user is blocked from adding a book without entering in the fields.
     */
    @Test
    public void saveWithNoInfoTest() {
        solo.clickOnButton("Save and Add Photo");
        assertTrue(solo.waitForText("Please Fill Out All Fields", 1, 1000));
    }

    /**
     * Test to see if a user can edit the fields of a new book before creating it. No image attached.
     */
    @Test
    public void addBookWithNoImageTest() {
        solo.enterText((EditText) solo.getView(R.id.newBookTitle), "Mock Title");
        solo.enterText((EditText) solo.getView(R.id.newBookDesc), "This is a new Description");
        solo.enterText((EditText) solo.getView(R.id.newBookAuthor), "This is a new Author");
        solo.enterText((EditText) solo.getView(R.id.newBookISBN), "This is a new ISBN");

        solo.clickOnButton("Save and Add Photo");

        assertTrue(solo.waitForText("Before you go...", 1, 1000));
        solo.clickOnText("NOT NOW");

        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        solo.clickInList(0);
        assertTrue(solo.waitForText("Mock Title", 1, 1000));
        solo.goBack();
        cleanup();
    }

    /**
     * Test to see if a user can edit the fields of a new book before creating it. With adding image.
     */
    @Test
    public void addBookWithImageTest() {
        solo.enterText((EditText) solo.getView(R.id.newBookTitle), "Mock Title");
        solo.enterText((EditText) solo.getView(R.id.newBookDesc), "This is a new Description");
        solo.enterText((EditText) solo.getView(R.id.newBookAuthor), "This is a new Author");
        solo.enterText((EditText) solo.getView(R.id.newBookISBN), "This is a new ISBN");

        solo.clickOnButton("Save and Add Photo");

        assertTrue(solo.waitForText("Before you go...", 1, 1000));
        solo.clickOnText("YES");

        solo.assertCurrentActivity("Wrong Activity", CameraActivity.class);

        // Don't actually add an image. Camera is something that is external to the app.
        // Solo will not be able to use it.
        solo.goBack();

        assertTrue(solo.waitForText("Mock Title", 1, 1000));
        solo.goBack();
        cleanup();
    }

    /**
     * Test to see if the Scan button takes you to the scan activity.
     */
    @Test
    public void scanButtonTest() {
        solo.clickOnButton("Scan");
        solo.assertCurrentActivity("Wrong Activity", ScanActivity.class);
    }

    /**
     * Delete the book to make it ready for the next test.
     */
    public void cleanup() {
        solo.clickInList(0);
        solo.clickOnButton("Delete Book");
        solo.clickOnText("DELETE");

        // Sleep for 3 seconds to let the database properly delete the book.
        solo.sleep(3000);
    }

    /**
     * Closes the activity after each test
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
