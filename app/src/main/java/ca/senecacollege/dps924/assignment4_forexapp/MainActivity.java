package ca.senecacollege.dps924.assignment4_forexapp;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, RecentConversionsAdapter.saveClickListener,
NewsAdapter.ArticleClickListener{

    BottomNavigationView bottomNav;
    BottomNavigationItemView exchangeNavItem;
    BottomNavigationItemView historyNavItem;
    BottomNavigationItemView newsNavItem;

    FragmentManager fm = getSupportFragmentManager();
    ExchangeFragment exchangeFragment;
    HistoryFragment historyFragment;
    NewsFragment newsFragment;

    NetworkingService networkingService;
    JsonService JsonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.networkingService = ((MyApp)getApplication()).getNetworkingService();
        this.JsonService = ((MyApp)getApplication()).getJsonService();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.navigation);
        exchangeNavItem = findViewById(R.id.home);
        historyNavItem = findViewById(R.id.history);
        newsNavItem = findViewById(R.id.news);
        bottomNav.setOnItemSelectedListener(this);

        exchangeFragment = new ExchangeFragment();

        fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.exchangeFragment).commit();
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
                    Bundle bundle = new Bundle();
                    ArrayList<String> short_form = new ArrayList<>();
                    ArrayList<String> long_form = new ArrayList<>();

                    bundle.putStringArrayList("short_form", short_form);
                    bundle.putStringArrayList("long_form", long_form);
                    this.exchangeFragment.setArguments(bundle);

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.exchangeFragment).commit();
                }
                break;
            case R.id.history:
                Log.e("Clicked", "History");
                if (!item.isChecked()) {
                    item.setChecked(true);

                    historyFragment = new HistoryFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.historyFragment).commit();
                }
                break;
            case R.id.news:
                Log.e("Clicked", "News");
                if (!item.isChecked()) {
                    item.setChecked(true);
                    newsFragment = new NewsFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.newsFragment).commit();
                }
                break;
        }
        return false;
    }

    @Override
    public void onSaveClicked() {
        Log.e("Save Clicked", "Yes");
    }

    @Override
    public void onArticleClicked() {
        Log.e("Article clicked", "yes");
    }
}