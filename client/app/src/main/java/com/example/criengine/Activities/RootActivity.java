package com.example.criengine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Fragments.BottomNavFragment;
import com.example.criengine.Fragments.MyBooksListFragment;
import com.example.criengine.Fragments.MyProfileFragment;
import com.example.criengine.Fragments.NotificationFragment;
import com.example.criengine.Fragments.RequestedBooksFragment;
import com.example.criengine.Fragments.SearchBooksFragment;

import com.example.criengine.Interfaces.IOnBackPressed;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The central navigation activity. Handles creating the following fragments:
 * - Search fragment
 * - Notifications fragment
 * - Outgoing Requests fragment
 * - My Books fragment
 * - My Profile fragment
 */
public class RootActivity extends AppCompatActivity {
    private static final int PAGE_COUNT = 5;
    private ViewPager2 viewPager;
    private BottomNavigationView navigation;
    private DatabaseWrapper dbw;
    private RootPagerFragmentAdapter mPagerAdapter;

    public enum PAGE {
        SEARCH(0),
        NOTIFICATIONS(1),
        REQUESTS(2),
        MY_BOOKS(3),
        PROFILE(4);

        public final int value;

        PAGE(int value) {
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

        dbw = DatabaseWrapper.getWrapper();

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new onNavItemSelect());

        viewPager = findViewById(R.id.view_pager);
        mPagerAdapter = new RootPagerFragmentAdapter(this);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.registerOnPageChangeCallback(new onPageChange());

        dbw.addOnChangeListener(new DatabaseWrapper.OnChangeListener () {
            @Override
            public void onChange() {
                updateNotificationBadge();
            }
        });

        // If returning from another activity, this can control which screen to navigate to.
        if (getIntent().getExtras() != null) {
            int index = ((PAGE) getIntent().getSerializableExtra("Index")).getValue();
            viewPager.setCurrentItem(index, false);
        } else {
            // Returns to my books. This will be the home screen.
            viewPager.setCurrentItem(PAGE.MY_BOOKS.getValue(), false);
        }
    }

    /**
     * FragmentStateAdapter for the ViewPager
     */
    private static class RootPagerFragmentAdapter extends FragmentStateAdapter {
        public RootPagerFragmentAdapter(@NonNull FragmentActivity fa) {
            super(fa);
        }

        @Override
        public int getItemCount() {
            return PAGE_COUNT;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SearchBooksFragment();
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

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }


    /**
     * Reloads the activity on the current page
     */
    public void refresh() {
        Intent intent = new Intent(this, RootActivity.class);
        intent.putExtra("Index", PAGE.values()[viewPager.getCurrentItem()]);
        startActivity(intent);
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
            // TODO: More refresh stuff goes here.
            updateNotificationBadge();
        }
    }

    /**
     * Updates the Notification menu item badge to show the number of notifications
     */
    private void updateNotificationBadge() {
        dbw.getProfile(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        int notificationCount = profile.getNotifications().size();
                        BadgeDrawable badge = navigation
                                .getOrCreateBadge(R.id.bottom_navigation_item_notifications);
                        badge.setVisible(notificationCount > 0);
                        badge.setNumber(notificationCount);
                    }
                }
        );
    }

    /**
     * OnItemSelectedListener for the BottomNavigationView
     * sets the ViewPager page to the index of the menu item selected
     * ie. selecting the 2nd menu item will open the 2nd ViewPager page
     */
    private class onNavItemSelect implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem selectedItem) {
            BottomNavFragment.setInitialSelectedItemId(selectedItem.getItemId());
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

    public void refresh(RootActivity.PAGE page) {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        intent.putExtra("Index", page);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}