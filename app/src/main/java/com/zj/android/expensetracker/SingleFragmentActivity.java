package com.zj.android.expensetracker;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zj.android.expensetracker.add_item.AddItemActivity;
import com.zj.android.expensetracker.dashboard.DashboardActivity;
import com.zj.android.expensetracker.transaction.TransactionActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

    }

    public void startDashboard(MenuItem item){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void startAddItem(MenuItem item){
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    public void startTransaction(MenuItem item){
        Intent intent = new Intent(this, TransactionActivity.class);

        startActivity(intent);
    }

}
