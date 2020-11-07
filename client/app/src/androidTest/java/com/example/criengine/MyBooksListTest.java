package com.example.criengine;

import android.widget.EditText;
import android.widget.ListView;

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
     * Test to see if the filter button functions properly.
     */
    @Test
    public void filterButtonTest() throws InterruptedException {

        assertTrue(solo.waitForText("This is a new book Title", 1, 2000));

        solo.clickOnButton("Filter");

        // Check if every option (4 total) is available to be clicked on.
        assertTrue(solo.waitForText("Filter By Status", 1, 2000));
        assertTrue(solo.waitForText("Available", 1, 2000));
        assertTrue(solo.waitForText("Requested", 1, 2000));
        assertTrue(solo.waitForText("Accepted", 1, 2000));
        assertTrue(solo.waitForText("Borrowed", 1, 2000));


        // Filter for only available books.
        solo.clickOnCheckBox(0);
        solo.clickOnText("Confirm");

        assertTrue(solo.waitForText("This is a new book Title", 1, 2000));
//        solo.clickOnButton("Filter");
//        solo.clickOnCheckBox(0);
//        solo.clickOnCheckBox(1);
//        solo.clickOnText("Confirm");
//
//
//        solo.waitForDialogToClose();
//        assertFalse(solo.waitForText("This is a new book Title", 1, 2000));

        solo.clickOnButton("Filter");
        solo.clickOnCheckBox(0);
        solo.clickOnText("Confirm");
        assertTrue(solo.waitForText("This is a new book Title", 1, 2000));
    }


    public void addBook() {
        solo.clickOnButton("Add A Book");


        assertTrue(solo.waitForText("Save", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.newBookTitle), "This is a new book Title");
        solo.enterText((EditText) solo.getView(R.id.newBookDesc), "This is a new Description");
        solo.enterText((EditText) solo.getView(R.id.newBookAuthor), "This is a new Author");
        solo.enterText((EditText) solo.getView(R.id.newBookISBN), "This is a new ISBN");
        solo.enterText((EditText) solo.getView(R.id.newBookImageURL), "This is an optional input");

        solo.clickOnButton("Save");
    }

    public void deleteBook() {
        solo.clickInList(0);

        solo.clickOnButton("Delete Book");
        solo.clickOnText("DELETE");

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
