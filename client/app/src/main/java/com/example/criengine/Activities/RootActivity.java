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
import com.example.criengine.Fragments.RequestedBooksFragment;
import com.example.criengine.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RootActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView navigation;

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
            int index = (int) getIntent().getSerializableExtra("Index");
            viewPager.setCurrentItem(index);
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
                    return new RequestedBooksFragment();
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
     * Overrides the back button so it does not return to the previous screen.
     */
    @Override
    public void onBackPressed() {}
}