package com.example.criengine;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.MyBookActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

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
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        TestUtilityMethods.login(solo, "intentTestingUser@email.com");
        TestUtilityMethods.addBook(solo);

        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", MyBookActivity.class);
    }

    /**
     * Test to see if the editing the title and saving it will truly make the change.
     */
    @Test
    public void editTitleTest() {
        // Enter in a new title and save changes.
        solo.clickOnButton("Edit Book");
        solo.clearEditText((EditText) solo.getView(R.id.bookView_title));
        solo.enterText((EditText) solo.getView(R.id.bookView_title), "A good title.");
        solo.clickOnButton("Save");

        // Test to see if the changes were acknowledged.
        assertTrue(solo.waitForText("A good title.", 1, 2000));
    }

    /**
     * Test to see if making an edit to the title and then cancelling will undo the changes.
     */
    @Test
    public void cancelEditTitleTest() {
        // Enter in a new book title and cancel changes.
        solo.clickOnButton("Edit Book");
        solo.clearEditText((EditText) solo.getView(R.id.bookView_title));
        solo.enterText((EditText) solo.getView(R.id.bookView_title), "New title.");
        solo.clickOnButton("Cancel");

        // Make sure that the changes were not saved.
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        assertTrue(solo.waitForText("Mock Title", 1, 2000));
    }

    /**
     * Test to see if clicking on cancel while in the delete dialog will return to the original
     * activity.
     */
    @Test
    public void cancelDeleteBookTest() {
        solo.clickOnButton("Delete Book");

        assertTrue(solo.waitForText("Are you sure you want to delete this book?", 1, 2000));
        solo.clickOnText("Cancel");

        // Make sure we didn't change screens.
        solo.assertCurrentActivity("Wrong Activity", MyBookActivity.class);
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
