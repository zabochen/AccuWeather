package ua.ck.zabochen.accuweather.model.singleton;

import java.util.ArrayList;

import ua.ck.zabochen.accuweather.model.realm.City;

public class SearchCityList {

    private static SearchCityList searchCityList;

    private static ArrayList<City> cities = new ArrayList<>();

    private SearchCityList() {
    }

    public static SearchCityList getInstance() {
        if (searchCityList == null) {
            searchCityList = new SearchCityList();
        }
        return searchCityList;
    }

    public void addToSearchList(City city) {
        cities.add(city);
    }

    public ArrayList<City> getSearchList() {
        return cities;
    }

    public void clearSearchList() {
        if (cities != null) {
            cities.clear();
        }
    }
}
