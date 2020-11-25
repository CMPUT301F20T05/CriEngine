package com.example.criengine;

import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Contains utility methods used by the tests.
 */
public class TestUtilityMethods {
    /**
     * Add a mock book.
     */
    public static void addBook(Solo solo) {
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
     */
    public static void cleanup(Solo solo) {
        solo.clickInList(0);
        solo.clickOnButton("Delete Book");
        solo.clickOnText("DELETE");

        // Sleep for 3 seconds to let the database properly delete the book.
        solo.sleep(3000);
    }
}
