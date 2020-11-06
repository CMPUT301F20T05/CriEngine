package com.example.criengine.Activities;

import android.os.Bundle;
import com.example.criengine.R;

/**
 * User profile activity.
 * Outstanding Issues:
 * - Not currently implemented.
 */
public class UserProfileActivity extends ProfileActivity {

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_profile);
        super.onCreate(savedInstanceState);

        // new UI components

    }
}
