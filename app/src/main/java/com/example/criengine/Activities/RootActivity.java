package com.example.criengine.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.criengine.Fragments.MyBooksListFragment;
import com.example.criengine.Fragments.MyProfileFragment;
import com.example.criengine.Fragments.NotificationFragment;
import com.example.criengine.Fragments.RequestedBooksFragment;
import com.example.criengine.Interfaces.IOnBackPressed;
import com.example.criengine.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The central navigation activity. Handles creating the following fragments:
 * - Search fragment
 * - Notifications fragment
 * - Outgoing Requests fragment
 * - My Books fragment
 * - My Profile fragment
 *
 * Outstanding Issues:
 * - Does not refresh information on page swipe.
 */
public class RootActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView navigation;

    public static enum PAGE {
        SEARCH(0),
        NOTIFICATIONS(1),
        REQUESTS(2),
        MY_BOOKS(3),
        PROFILE(4);

        public final int value;

        private PAGE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new onNavItemSelect());

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new RootPagerFragmentAdapter(this));
        viewPager.registerOnPageChangeCallback(new onPageChange());

        // If returning from another activity, this can control which screen to navigate to.
        if (getIntent().getExtras() != null) {
            int index = ((PAGE) getIntent().getSerializableExtra("Index")).getValue();
            viewPager.setCurrentItem(index);
        } else {
            // Returns to my books. This will be the home screen.
            viewPager.setCurrentItem(PAGE.MY_BOOKS.getValue());
        }
    }

    /**
     * FragmentStateAdapter for the ViewPager
     */
    private class RootPagerFragmentAdapter extends FragmentStateAdapter {
        public RootPagerFragmentAdapter(@NonNull FragmentActivity fa) { super(fa); }

        @Override
        public int getItemCount() { return 5; }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new RequestedBooksFragment();
                case 1:
                    return new NotificationFragment();
                case 2:
                    return new RequestedBooksFragment();
                case 3:
                    return new MyBooksListFragment();
                case 4:
                    return new MyProfileFragment();
            }

            return null;
        }
    }

    /**
     * OnPageChangeCallback for the root ViewPager
     * It updates the navigation selected item when the page is swiped
     */
    private class onPageChange extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            int id = navigation.getMenu().getItem(position).getItemId();
            navigation.setSelectedItemId(id);
            // TODO: Refresh stuff goes here.
        }
    }

    /**
     * OnItemSelectedListener for the BottomNavigationView
     * sets the ViewPager page to the index of the menu item selected
     * ie. selecting the 2nd menu item will open the 2nd ViewPager page
     */
    private class onNavItemSelect implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem selectedItem) {
            for (int i = 0; i < navigation.getMenu().size(); i++) {
                if (navigation.getMenu().getItem(i) == selectedItem) {
                    viewPager.setCurrentItem(i);
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Changes the viewPager page to the given page
     * @param index page index
     */
    public void goToPage(int index) {
        viewPager.setCurrentItem(index);
    }

    /**
     * Overrides the back button so it does not return to the previous screen unless
     * a fragment implements the IOnBackPressed interface.
     */
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());

        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            // TODO: ideally this should take you back to the previous screen
//            super.onBackPressed();
        }
        return;
    }
}