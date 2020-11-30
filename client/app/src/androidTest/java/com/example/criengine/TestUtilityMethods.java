package com.example.criengine;

import android.widget.EditText;

import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.RootActivity;
import com.robotium.solo.Solo;

import static junit.framework.TestCase.assertTrue;

/**
 * Contains utility methods used by the tests.
 */
public class TestUtilityMethods {
    /**
     * Log in with an intentTesting Account.
     * @param solo The solo object.
     * @param user The username for the account.
     */
    public static void login(Solo solo, String user) {
        // Asserts that the current activity is the LoginActivity.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Input username and password
        solo.enterText((EditText) solo.getView(R.id.loginEditTextEmail), user);
        solo.enterText((EditText) solo.getView(R.id.loginEditTextPassword), "intentTesting");

        solo.clickOnButton("Login");

        // Asserts that the current activity is the RootActivity.
        solo.assertCurrentActivity("Wrong Activity", RootActivity.class);

        // Returns True if you can find "My Books" on the screen. Waits 50 seconds to find
        // at least 1 match. This is to counter potentially long wait times when logging in.
        assertTrue(solo.waitForText("My Books", 1, 50000));
    }

    /**
     * Add a mock book.
     * @param solo The solo object.
     */
    public static void addBook(Solo solo) {
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_my_books));
        solo.clickOnButton("Add A Book");
        solo.enterText((EditText) solo.getView(R.id.newBookTitle), "Mock Title");
        solo.enterText((EditText) solo.getView(R.id.newBookDesc), "This is a new Description");
        solo.enterText((EditText) solo.getView(R.id.newBookAuthor), "This is a new Author");
        solo.enterText((EditText) solo.getView(R.id.newBookISBN), "This is a new ISBN");
        solo.clickOnButton("Save and Add Photo");
        solo.clickOnText("NOT NOW");
    }

    /**
     * Delete the book to make it ready for the next test.
     * @param solo The solo object.
     */
    public static void cleanup(Solo solo) {
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_my_books));
        solo.clickInList(0);
        solo.clickOnButton("Delete Book");
        solo.clickOnText("DELETE");
        solo.sleep(2000);
    }

    /**
     * Log out of the current account.
     * @param solo The solo object.
     */
    public static void logout(Solo solo) {
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_profile));
        solo.clickOnButton("Logout");
    }

    /**
     * Search for a book. (Mock Book)
     * @param solo The solo object.
     */
    public static void searchBook(Solo solo) {
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_search));
        assertTrue(solo.waitForText("Books Matching:", 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.search_box), "Mock Title");
        assertTrue(solo.waitForText("Mock Title", 4, 2000));
    }
}