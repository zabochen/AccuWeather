package ua.ck.zabochen.accuweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ua.ck.zabochen.accuweather.API;
import ua.ck.zabochen.accuweather.R;
import ua.ck.zabochen.accuweather.database.Database;
import ua.ck.zabochen.accuweather.event.Completed;
import ua.ck.zabochen.accuweather.model.singleton.ConditionCity;
import ua.ck.zabochen.accuweather.model.singleton.MainCityList;
import ua.ck.zabochen.accuweather.network.Network;

public class ConditionActivity extends BaseActivity
        implements View.OnClickListener, Completed {

    private int position;
    private String mobileLink;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Selected item from MainAdapter/MainActivity
        // Intent key - "position"
        this.position = getIntent().getIntExtra("position", 0);

        // UI
        setUi();

        // Network Request
        Network.getInstance().requestCondition(this, MainCityList.getInstance().getMainList().get(position).getKey());
    }

    private void setUi() {
        // Layout
        setContentView(R.layout.activity_condition);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_condition_toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_title));
        toolbar.setTitle(R.string.activity_condition_toolbar_title);
        setSupportActionBar(toolbar);

        // ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.activity_condition_progress_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_condition, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_condition_item_delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog
                        .setMessage(getString(R.string.alert_dialog_message_delete))
                        .setPositiveButton(getString(R.string.alert_dialog_button_yes_title),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeItem(position);
                                    }
                                })
                        .setNegativeButton(getString(R.string.alert_dialog_button_no_title),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeItem(int position) {
        Database.getInstance().removeCity(MainCityList.getInstance().getMainList().get(position).getKey());
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_condition_button_mobile_link:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mobileLink));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateUi() {
        // TextView's
        TextView cityName = (TextView) findViewById(R.id.activity_condition_city_name);
        cityName.setText(MainCityList.getInstance().getMainList().get(position).getCityName());

        TextView countryRegionName = (TextView) findViewById(R.id.activity_condition_country_region_name);
        countryRegionName.setText(MainCityList.getInstance().getMainList().get(position).getCountryName()
                + ", " + MainCityList.getInstance().getMainList().get(position).getRegionName());

        TextView weatherText = (TextView) findViewById(R.id.activity_condition_weather_text);
        weatherText.setText(ConditionCity.getInstance().getCondition().getWeatherText());

        TextView temperature = (TextView) findViewById(R.id.activity_condition_temperature);
        temperature.setText(String.valueOf(ConditionCity.getInstance().getCondition().getTemperature().getMetric().getValue()
                + " " + ConditionCity.getInstance().getCondition().getTemperature().getMetric().getUnit()
                + " / " + ConditionCity.getInstance().getCondition().getTemperature().getImperial().getValue()
                + " " + ConditionCity.getInstance().getCondition().getTemperature().getImperial().getUnit()));

        // ImageView
        ImageView weatherIcon = (ImageView) findViewById(R.id.activity_condition_weather_icon);

        String imageUrl;
        if (ConditionCity.getInstance().getCondition().getWeatherIcon() < 10) {
            imageUrl = API.URL + "developers/Media/Default/WeatherIcons/0"
                    + ConditionCity.getInstance().getCondition().getWeatherIcon() + "-s.png";
        } else {
            imageUrl = API.URL + "developers/Media/Default/WeatherIcons/"
                    + ConditionCity.getInstance().getCondition().getWeatherIcon() + "-s.png";
        }

        Picasso.with(this).load(imageUrl).into(weatherIcon);

        // Button
        mobileLink = ConditionCity.getInstance().getCondition().getMobileLink();
        Button mobileLink = (Button) findViewById(R.id.activity_condition_button_mobile_link);
        mobileLink.setOnClickListener(this);

        // Hide ProgressBar
        mProgressBar.setVisibility(View.GONE);
    }

}
