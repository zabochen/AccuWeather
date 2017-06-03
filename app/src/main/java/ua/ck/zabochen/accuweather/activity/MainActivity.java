package ua.ck.zabochen.accuweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.RealmResults;
import ua.ck.zabochen.accuweather.R;
import ua.ck.zabochen.accuweather.adapter.MainAdapter;
import ua.ck.zabochen.accuweather.database.Database;
import ua.ck.zabochen.accuweather.event.AdapterClick;
import ua.ck.zabochen.accuweather.model.realm.City;
import ua.ck.zabochen.accuweather.model.singleton.MainCityList;
import ua.ck.zabochen.accuweather.utils.Utils;

public class MainActivity extends BaseActivity
        implements AdapterClick {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setUi();
    }

    private void loadData() {
        int databaseSize = Database.getInstance().getCities().size();
        if (databaseSize > 0) {
            RealmResults<City> cities = Database.getInstance().getCities();
            MainCityList.getInstance().getMainList().clear();
            for (City c : cities) {
                MainCityList.getInstance().addToMainList(c);
            }
        }
    }

    private void setUi() {
        // Layout
        setContentView(R.layout.activity_main);

        // Coordinator Layout
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main_coordinator_layout);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_title));
        setSupportActionBar(toolbar);

        // Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new MainAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data & recyclerView
        loadData();
        mRecyclerView.setAdapter(new MainAdapter(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_main_item_search:
                if (Utils.isOnline(this)) {
                    startActivity(new Intent(this, SearchActivity.class));
                } else {
                    Snackbar.make(mCoordinatorLayout,
                            getString(R.string.snackbar_message_internet_disable),
                            Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getAdapterClickPosition(int position) {
        if (Utils.isOnline(this)) {
            Intent intent = new Intent(this, ConditionActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            Snackbar.make(mCoordinatorLayout,
                    getString(R.string.snackbar_message_internet_disable),
                    Snackbar.LENGTH_LONG)
                    .show();
        }

    }
}
