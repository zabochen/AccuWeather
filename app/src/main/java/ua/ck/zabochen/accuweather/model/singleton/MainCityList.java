package ua.ck.zabochen.accuweather.model.singleton;

import java.util.ArrayList;

import ua.ck.zabochen.accuweather.model.realm.City;

public class MainCityList {

    private static MainCityList mainCityList;

    private static ArrayList<City> cities = new ArrayList<>();

    private MainCityList() {
    }

    public static MainCityList getInstance() {
        if (mainCityList == null) {
            mainCityList = new MainCityList();
        }
        return mainCityList;
    }

    public void addToMainList(City city) {
        cities.add(city);
    }

    public ArrayList<City> getMainList() {
        return cities;
    }
}
