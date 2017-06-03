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

    private TextView mCityName;
    private TextView mWeatherText;
    private TextView mTemperature;
    private ImageView mWeatherIcon;
    private String mMobileLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Selected item
        this.position = getIntent().getIntExtra("position", 0);

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

        // TextView Find
        mCityName = (TextView) findViewById(R.id.activity_condition_city_name);
        mWeatherText = (TextView) findViewById(R.id.activity_condition_weather_text);
        mTemperature = (TextView) findViewById(R.id.activity_condition_temperature);

        // ImageView
        mWeatherIcon = (ImageView) findViewById(R.id.activity_condition_weather_icon);

        // Button
        Button mobileLink = (Button) findViewById(R.id.activity_condition_button_mobile_link);
        mobileLink.setOnClickListener(this);
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mMobileLink));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateUi() {
        setUi();

        mCityName.setText(MainCityList.getInstance().getMainList().get(position).getCityName());

        String imageUrl;
        if (ConditionCity.getInstance().getCondition().getWeatherIcon() < 10) {
            imageUrl = API.URL + "developers/Media/Default/WeatherIcons/0"
                    + ConditionCity.getInstance().getCondition().getWeatherIcon() + "-s.png";
        } else {
            imageUrl = API.URL + "developers/Media/Default/WeatherIcons/"
                    + ConditionCity.getInstance().getCondition().getWeatherIcon() + "-s.png";
        }

        Picasso
                .with(this)
                .load(imageUrl)
                .into(mWeatherIcon);

        mWeatherText.setText(ConditionCity.getInstance().getCondition().getWeatherText());
        mTemperature.setText(String.valueOf(ConditionCity.getInstance().getCondition().getTemperature().getMetric().getValue()));
        mMobileLink = ConditionCity.getInstance().getCondition().getMobileLink();
    }

}
