package com.example.criengine;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.criengine.Activities.CameraActivity;
import com.example.criengine.Activities.LoginActivity;
import com.example.criengine.Activities.MyBookActivity;
import com.example.criengine.Activities.RootActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


/**
 * Test class for the camera activity. All the UI tests are written here. Robotium test
 * framework is used.
 * Note: We cannot test the camera as it is considered to be a separate application. Robotium will
 * not extend onto it.
 * Note: We cannot test the delete button in this test as it is dependent on a image initially
 * saved into the db. Without being able to automate the camera, we cannot guarantee an image being
 * available and therefore unable to guarantee the existence of the delete button.
 */
public class CameraScreenTest {
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

        solo.clickInList(0);

        // Asserts that the current activity is the MyBookActivity.
        solo.assertCurrentActivity("Wrong Activity", MyBookActivity.class);

        solo.clickOnButton("Edit Book");

        solo.clickOnView(solo.getView("bookView_image"));

        // Asserts that the current activity is the Camera activity.
        solo.assertCurrentActivity("Wrong Activity", CameraActivity.class);
    }

    /**
     * Test to see if a user can edit the fields of a new book before creating it.
     */
    @Test
    public void clickOnCancelButtonTest() {
        solo.clickOnButton("Cancel");

        // Asserts that the current activity is the My Book activity.
        solo.assertCurrentActivity("Wrong Activity", MyBookActivity.class);
    }

    /**
     * Test to see if a user can edit the fields of a new book before creating it.
     */
    @Test
    public void clickOnSaveButtonTest() {
        solo.clickOnButton("Save");

        // Asserts that the current activity is the My Book activity.
        solo.assertCurrentActivity("Wrong Activity", MyBookActivity.class);
    }

    /**
     * Test to see if a user can edit the fields of a new book before creating it.
     */
    @Test
    public void clickOnImageTest() {
        solo.clickOnButton("Save");

        // Asserts that the current activity is the My Book activity.
        solo.assertCurrentActivity("Wrong Activity", MyBookActivity.class);
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
