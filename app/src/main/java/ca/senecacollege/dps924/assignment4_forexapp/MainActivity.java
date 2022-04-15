package ca.senecacollege.dps924.assignment4_forexapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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

    NetworkingService networkingService;
    JsonService JsonService;
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.networkingService = ((MyApp)getApplication()).getNetworkingService();
        this.JsonService = ((MyApp)getApplication()).getJsonService();
        this.dataManager = ((MyApp)getApplication()).getDataManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.navigation);
        exchangeNavItem = findViewById(R.id.home);
        historyNavItem = findViewById(R.id.history);
        newsNavItem = findViewById(R.id.news);
        bottomNav.setOnItemSelectedListener(this);

        exchangeFragment = new ExchangeFragment();

        if (!this.dataManager.menu_selection.equals("")) {
            if (this.dataManager.menu_selection.equals("Exchange")) {
                bottomNav.setSelectedItemId(R.id.home);
            }
            else if (this.dataManager.menu_selection.equals("History")) {
                bottomNav.setSelectedItemId(R.id.history);
            }
            else if (this.dataManager.menu_selection.equals("News")) {
                bottomNav.setSelectedItemId(R.id.news);
            }
        }
        else {
            fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.exchangeFragment).commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.elipse_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.quit:
                finish();
                break;
            case R.id.reset_database:
                dataManager.dbManager.removeAllConversions();
                Toast.makeText(this, "Cleared database", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.home:
                Log.d("Clicked", "Exchange");
                this.dataManager.menu_selection = "Exchange";
                if (!item.isChecked()) {
                    item.setChecked(true);
                    exchangeFragment = new ExchangeFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.exchangeFragment).commit();
                }
                break;
            case R.id.history:
                Log.d("Clicked", "History");
                this.dataManager.menu_selection = "History";
                if (!item.isChecked()) {
                    item.setChecked(true);
                    historyFragment = new HistoryFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.historyFragment).commit();
                }
                break;
            case R.id.news:
                Log.d("Clicked", "News");
                this.dataManager.menu_selection = "News";
                if (!item.isChecked()) {
                    item.setChecked(true);
                    newsFragment = new NewsFragment();
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragmentContainerView, this.newsFragment).commit();
                }
                break;
        }
        return false;
    }
}