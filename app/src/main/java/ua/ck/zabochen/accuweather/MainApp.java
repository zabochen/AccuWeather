package ua.ck.zabochen.accuweather;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setRealm();
    }

    private void setRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("accuWeather.db")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}