package com.example.criengine;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.criengine.Activities.LoginActivity;
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
        TestUtilityMethods.login1(solo);
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
