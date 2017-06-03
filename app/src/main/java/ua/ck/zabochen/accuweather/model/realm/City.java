package ua.ck.zabochen.accuweather.model.realm;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {

    @PrimaryKey
    private String key;
    private String cityName;
    private String regionName;
    private String countryName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
