package ua.ck.zabochen.accuweather.database;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.ck.zabochen.accuweather.model.realm.City;


public class Database {

    private static Database database;
    private Realm realm;

    private Database() {
        realm = Realm.getDefaultInstance();
    }

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public Realm getRealm() {
        return realm;
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.delete(City.class);
        realm.commitTransaction();
    }

    public RealmResults<City> getCities() {
        return realm.where(City.class).findAll();
    }

    public void addCity(City city) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(city);
        realm.beginTransaction();
    }

    public void addCities(ArrayList<City> cityList) {
        for (City city : cityList) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(city);
            realm.commitTransaction();
        }
    }

    public void removeCity(String key) {
        final RealmResults<City> city = realm
                .where(City.class)
                .equalTo("key", key)
                .findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                city.deleteAllFromRealm();
            }
        });

    }


}
