package ua.ck.zabochen.accuweather.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import ua.ck.zabochen.accuweather.R;
import ua.ck.zabochen.accuweather.adapter.SearchAdapter;
import ua.ck.zabochen.accuweather.database.Database;
import ua.ck.zabochen.accuweather.event.Completed;
import ua.ck.zabochen.accuweather.model.singleton.SearchCityList;
import ua.ck.zabochen.accuweather.network.Network;

public class SearchActivity extends BaseActivity
        implements View.OnClickListener, Completed {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private CoordinatorLayout mCoordinatorLayout;
    private ProgressBar mProgressBar;
    private EditText mSearchEditText;
    private RecyclerView mRecyclerView;
    private Button mButtonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUi();
    }

    private void setUi() {
        // Layout
        setContentView(R.layout.activity_search);

        // Coordinator Layout
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_search_coordinator_layout);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_search_toolbar);
        toolbar.setTitle(getString(R.string.activity_search_toolbar_title));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_title));
        setSupportActionBar(toolbar);

        // Edit Text
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.activity_search_text_input_layout);
        textInputLayout.setHint(getString(R.string.activity_search_edit_text_hint));
        mSearchEditText = textInputLayout.getEditText();

        // Button Search
        Button buttonSearch = (Button) findViewById(R.id.activity_search_button_search);
        buttonSearch.setOnClickListener(this);

        // Button Add
        mButtonAdd = (Button) findViewById(R.id.activity_search_button_add);
        mButtonAdd.setVisibility(View.GONE);
        mButtonAdd.setOnClickListener(this);

        // ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.activity_search_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_search_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_search_button_search:
                searchCity();
                break;
            case R.id.activity_search_button_add:
                saveCity();
                break;
            default:
                break;
        }
    }

    private void searchCity() {
        // Hide Button Add
        mButtonAdd.setVisibility(View.GONE);

        // Hide Keyboard
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (!TextUtils.isEmpty(mSearchEditText.getText().toString().trim())) {
            // Show ProgressBar
            mProgressBar.setVisibility(View.VISIBLE);
            // Request
            Network.getInstance().requestLocation(this, mSearchEditText.getText().toString().trim());
        } else {
            Snackbar.make(mCoordinatorLayout,
                    getString(R.string.snackbar_message_city_valid),
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void saveCity() {
        if (SearchCityList.getInstance().getSearchList().size() > 0) {
            Database.getInstance().addCities(SearchCityList.getInstance().getSearchList());
            mButtonAdd.setVisibility(View.GONE);
            Snackbar.make(mCoordinatorLayout,
                    getString(R.string.snackbar_message_city_save),
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void updateUi() {
        // Hide ProgressBar
        mProgressBar.setVisibility(View.GONE);

        // Show Button Add
        if (SearchCityList.getInstance().getSearchList().size() > 0) {
            mButtonAdd.setVisibility(View.VISIBLE);
        } else {
            mButtonAdd.setVisibility(View.GONE);
        }

        // Update UI
        mRecyclerView.setAdapter(new SearchAdapter());
    }
}