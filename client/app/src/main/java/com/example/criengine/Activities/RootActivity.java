package com.example.criengine.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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
    }

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
                    return new RequestedBooksFragment();
                case 4:
                    return new RequestedBooksFragment();
            }

            return null;
        }
    }

    private class onPageChange extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            int id = navigation.getMenu().getItem(position).getItemId();
            navigation.setSelectedItemId(id);
        }
    }

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