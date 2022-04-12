package ca.senecacollege.dps924.assignment4_forexapp;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNav;
    BottomNavigationItemView exchangeNavItem;
    BottomNavigationItemView historyNavItem;
    BottomNavigationItemView newsNavItem;

    FragmentManager fm = getSupportFragmentManager();
    ExchangeFragment exchangeFragment;
    HistoryFragment historyFragment;
    NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.navigation);
        exchangeNavItem = findViewById(R.id.home);
        historyNavItem = findViewById(R.id.history);
        newsNavItem = findViewById(R.id.news);
        bottomNav.setOnItemSelectedListener(this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.home:
                Log.e("Clicked", "Exchange");
                if (!item.isChecked()) {
                    item.setChecked(true);
                    this.exchangeFragment = new ExchangeFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.exchangeFragment).commit();
                }
                break;
            case R.id.history:
                Log.e("Clicked", "History");
                if (!item.isChecked()) {
                    item.setChecked(true);
                    this.historyFragment = new HistoryFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.historyFragment).commit();
                }
                break;
            case R.id.news:
                Log.e("Clicked", "News");
                if (!item.isChecked()) {
                    item.setChecked(true);
                    this.newsFragment = new NewsFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.newsFragment).commit();
                }
                break;
        }
        return false;
    }
}