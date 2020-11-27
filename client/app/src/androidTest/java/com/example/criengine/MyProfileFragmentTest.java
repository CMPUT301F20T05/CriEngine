package com.example.criengine;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.criengine.Activities.LoginActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the MyProfileFragment. All the UI tests are written here.
 * Robotium test framework is used.
 */
public class MyProfileFragmentTest {
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
    }

    /**
     * Test to see if the fragment is reached when swiped
     */
    @Test
    public void MyProfileFragmentSwipeReachedTest() {
        solo.scrollToSide(Solo.RIGHT);
        assertTrue(solo.waitForText("Profile", 1, 2000));
    }

    /**
     * Test to see if the fragment is reached when tapped
     */
    @Test
    public void MyProfileFragmentTapReachedTest() {
        // Navigate to the user profile fragment.
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_profile));
        assertTrue(solo.waitForText("Profile", 1, 2000));
    }

    /**
     * Test to see if the static texts on screen are all there
     */
    @Test
    public void areStaticTextsVisibleTest() {
        solo.clickOnView(solo.getView(R.id.bottom_navigation_item_profile));
        assertTrue(solo.waitForText("Profile", 1, 2000));
        assertTrue(solo.waitForText("Bio", 1, 2000));
        assertTrue(solo.waitForText("Phone:", 1, 2000));
        assertTrue(solo.waitForText("Address:", 1, 2000));
    }

    /**
     * Test to see if the bio text is editable
     */
    @Test
    public void editableBioTextTest() {
        solo.scrollToSide(Solo.RIGHT);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnButton("Edit");
        String testBioString = "This is a bio text test";
        solo.clearEditText((EditText) solo.getView(R.id.bio_text));
        assertFalse(solo.waitForText(testBioString, 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.bio_text), testBioString);
        assertTrue(solo.waitForText(testBioString, 1, 2000));
    }

    /**
     * Test to see if the phone text is editable
     */
    @Test
    public void editablePhoneTextTest() {
        solo.scrollToSide(Solo.RIGHT);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnButton("Edit");
        String testPhoneString = "9999999999";
        solo.clearEditText((EditText) solo.getView(R.id.phone_text));
        assertFalse(solo.waitForText(testPhoneString, 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.phone_text), testPhoneString);
        assertTrue(solo.waitForText(testPhoneString, 1, 2000));
    }

    /**
     * Test to see if the address text is editable
     */
    @Test
    public void editableAddrTextTest() {
        solo.scrollToSide(Solo.RIGHT);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnButton("Edit");
        String testAddrString = "99 Wayne Gretzky Ave";
        solo.clearEditText((EditText) solo.getView(R.id.phone_text));
        assertFalse(solo.waitForText(testAddrString, 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.phone_text), testAddrString);
        assertTrue(solo.waitForText(testAddrString, 1, 2000));
    }

    /**
     * Test to see if the fragment lets the user cancel the edit
     */
    @Test
    public void editTextCancelTest() {
        solo.scrollToSide(Solo.RIGHT);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnButton("Edit");
        String testString = "This is a bio text test";
        solo.clearEditText((EditText) solo.getView(R.id.bio_text));
        assertFalse(solo.waitForText(testString, 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.bio_text), testString);
        assertTrue(solo.waitForText(testString, 1, 2000));
        solo.clickOnButton("Cancel");
        assertFalse(solo.waitForText(testString, 1, 2000));
    }

    /**
     * Test to see if the fragment lets the user save the edit
     */
    @Test
    public void editTextSaveTest() {
        solo.scrollToSide(Solo.RIGHT);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnButton("Edit");
        String testString = "This is a bio text test";
        solo.clearEditText((EditText) solo.getView(R.id.bio_text));
        assertFalse(solo.waitForText(testString, 1, 2000));
        String prevString = ((EditText) solo.getView(R.id.bio_text)).getText().toString();
        solo.enterText((EditText) solo.getView(R.id.bio_text), testString);
        assertTrue(solo.waitForText(testString, 1, 2000));
        solo.clickOnButton("Save");
        assertTrue(solo.waitForText(testString, 1, 2000));

        // revert the save
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.bio_text));
        solo.enterText((EditText) solo.getView(R.id.bio_text), prevString);
        solo.clickOnButton("Save");
        assertTrue(solo.waitForText(prevString, 1, 2000));
    }

    /**
     * Test to see if the logout button allows the user to logout
     */
    @Test
    public void logoutTest() {
        solo.scrollToSide(Solo.RIGHT);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Closes the activity after each test
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
