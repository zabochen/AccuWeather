package ua.ck.zabochen.accuweather.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import ua.ck.zabochen.accuweather.R;
import ua.ck.zabochen.accuweather.adapter.SearchAdapter;
import ua.ck.zabochen.accuweather.database.Database;
import ua.ck.zabochen.accuweather.event.Completed;
import ua.ck.zabochen.accuweather.model.singleton.SearchCityList;
import ua.ck.zabochen.accuweather.network.Network;


public class SearchActivity extends BaseActivity
        implements View.OnClickListener,
        Completed {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private EditText mSearchEditText;
    private Button mButtonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUi();
    }

    private void setUi() {
        // Layout
        setContentView(R.layout.activity_search);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_search_toolbar);
        toolbar.setTitle(getString(R.string.activity_search_toolbar_title));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_title));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
                if (SearchCityList.getInstance().getSearchList().size() > 0) {
                    Database.getInstance().addCities(SearchCityList.getInstance().getSearchList());
                }
                break;
            default:
                break;
        }
    }

    private void searchCity() {
        Network.getInstance().requestLocation(this, mSearchEditText.getText().toString());

        // Hide keyboard
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void updateUi() {
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
