package com.example.criengine.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Fragments.MyBooksListFragment;
import com.example.criengine.Fragments.NotificationFragment;
import com.example.criengine.Fragments.RequestedBooksFragment;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RootActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView navigation;
    private DatabaseWrapper dbw;

    public enum PAGE {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        dbw = DatabaseWrapper.getWrapper();

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new onNavItemSelect());

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new RootPagerFragmentAdapter(this));
        viewPager.registerOnPageChangeCallback(new onPageChange());

        // If returning from another activity, this can control which screen to navigate to.
        if (getIntent().getExtras() != null) {
            int index = ((PAGE) getIntent().getSerializableExtra("Index")).getValue();
            viewPager.setCurrentItem(index, false);
        } else {
            // Returns to my books. This will be the home screen.
            viewPager.setCurrentItem(PAGE.MY_BOOKS.getValue());
        }
    }

    /**
     * Changes the viewPager page to the given page
     *
     * @ param id: page index
     */
    public void goToPage(int index) {
        viewPager.setCurrentItem(index);
    }

    /**
     * Overrides the back button so it does not return to the previous screen.
     */
    @Override
    public void onBackPressed() {
    }

    /**
     * FragmentStateAdapter for the ViewPager
     */
    private class RootPagerFragmentAdapter extends FragmentStateAdapter {
        public RootPagerFragmentAdapter(@NonNull FragmentActivity fa) {
            super(fa);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

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
                    return new RequestedBooksFragment();
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
                        // TODO: uncomment the next line when notifications work
                        // badge.setVisible(notificationCount > 0);
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
            for (int i = 0; i < navigation.getMenu().size(); i++) {
                if (navigation.getMenu().getItem(i) == selectedItem) {
                    viewPager.setCurrentItem(i);
                    return true;
                }
            }
            return false;
        }
    }
}