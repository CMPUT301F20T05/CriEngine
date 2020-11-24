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

        TestUtilityMethods.addBook(solo);
    }

    /**
     * Test to see if the filter button functions properly.
     */
    @Test
    public void filterButtonTest() {
        assertTrue(solo.waitForText("Mock Title", 1, 2000));

        solo.clickOnButton("Filter");

        // Check if the dialog has opened up.
        assertTrue(solo.waitForText("Filter By Status", 1, 2000));


        // Filter for only available books.
        solo.clickOnCheckBox(0);
        solo.clickOnText("Confirm");
        solo.waitForDialogToClose();

        // Assert that only the available books are displayed.
        assertFalse(solo.waitForText("Accepted", 1, 1000));
        assertFalse(solo.waitForText("Has Requests", 1, 1000));
        assertFalse(solo.waitForText("Borrowed", 1, 1000));

        // Filter books with requests.
        solo.clickOnButton("Filter");
        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        solo.clickOnText("Confirm");
        solo.waitForDialogToClose();

        // Assert that only the requested books are displayed
        assertFalse(solo.waitForText("Accepted", 1, 1000));
        assertFalse(solo.waitForText("Borrowed", 1, 1000));
        assertFalse(solo.waitForText("Available", 1, 1000));

        // Filter for available and requested books.
        solo.clickOnButton("Filter");
        solo.clickOnCheckBox(0);
        solo.clickOnText("Confirm");
        solo.waitForDialogToClose();

        // Assert that only the available books are displayed.
        assertFalse(solo.waitForText("Accepted", 1, 1000));
        assertFalse(solo.waitForText("Borrowed", 1, 1000));
    }

    /**
     * Closes the activity after each test
     */
    @After
    public void tearDown() {
        TestUtilityMethods.cleanup(solo);
        solo.finishOpenedActivities();
    }
}
