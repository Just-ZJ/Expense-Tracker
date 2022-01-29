package com.zj.android.expensetracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zj.android.expensetracker.add_item.AddItemFragment;
import com.zj.android.expensetracker.dashboard.DashboardFragment;
import com.zj.android.expensetracker.transaction.TransactionFragment;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    FragmentPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);


        mViewPager = findViewById(R.id.fragment_container);
        mPagerAdapter = new CustomFragmentStateAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        // on click events for bottom app bar
        BottomNavigationItemView menu_dashboard = findViewById(R.id.bottom_appbar_dashboard);
        menu_dashboard.setOnClickListener(view -> mViewPager.setCurrentItem(0, true));
        FloatingActionButton menu_fab = findViewById(R.id.floating_action_button);
        menu_fab.setOnClickListener(view -> mViewPager.setCurrentItem(1, true));
        BottomNavigationItemView menu_transaction = findViewById(R.id.bottom_appbar_transaction);
        menu_transaction.setOnClickListener(view -> mViewPager.setCurrentItem(2, true));
    }

    public static class CustomFragmentStateAdapter extends FragmentPagerAdapter {

        public CustomFragmentStateAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DashboardFragment();
                case 1:
                    return new AddItemFragment();
                case 2:
                    return new TransactionFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
