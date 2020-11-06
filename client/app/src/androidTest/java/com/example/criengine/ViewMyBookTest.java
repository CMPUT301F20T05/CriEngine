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

        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Input username and password
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), "intentTestingUser@email.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "intentTesting");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 10 seconds to find
        // at least 1 match.
        assertTrue(solo.waitForText("My Books", 1, 10000));

        addBook();
    }

    /**
     * Test to see if the editing the title and saving it will truly make the change.
     */
    @Test
    public void editTitleTest() {
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
        assertTrue(solo.waitForText("Mock Title", 1, 2000));
        solo.goBack();

    }

    /**
     * Test to see if making an edit to the title and then cancelling will undo the changes.
     */
    @Test
    public void cancelEditTitleTest() {
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
        assertTrue(solo.waitForText("This is a new book Title", 1, 2000));
        solo.goBack();

    }

    /**
     * Test to see if deleting a book will return you to the main screen.
     */
    @Test
    public void deleteBookTest() {
        solo.clickInList(0);

        // Click on delete book.
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        solo.clickOnButton("Delete Book");

        // Wait for the prompt.
//        assertTrue(solo.waitForText("Are you sure you want to delete this book?", 1, 2000));
        solo.clickOnText("DELETE");

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
        solo.clickInList(0);

        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        solo.clickOnButton("Delete Book");

        assertTrue(solo.waitForText("Are you sure you want to delete this book?", 1, 2000));
        solo.clickOnText("Cancel");

        // Make sure we didnt change screens.
        solo.sleep(2000);
        assertTrue(solo.waitForText("Delete Book", 1, 2000));
        solo.goBack();

    }

    public void addBook() {
        solo.clickOnButton("Add A Book");


        assertTrue(solo.waitForText("Save", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.newBookTitle), "Mock Title");
        solo.enterText((EditText) solo.getView(R.id.newBookDesc), "Mock Description");
        solo.enterText((EditText) solo.getView(R.id.newBookAuthor), "Mock Author");
        solo.enterText((EditText) solo.getView(R.id.newBookISBN), "Mock ISBN");
        solo.enterText((EditText) solo.getView(R.id.newBookImageURL), "Mock input");

        solo.clickOnButton("Save");
    }

    public void deleteBook() {
        if (solo.searchText("This is a new title.") || solo.searchText("Mock Title")) {
            solo.clickInList(0);

            solo.clickOnButton("Delete Book");
            solo.clickOnText("DELETE");
        }

    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        deleteBook();
        solo.finishOpenedActivities();
    }
}
