package ua.ck.zabochen.accuweather.network;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ua.ck.zabochen.accuweather.API;
import ua.ck.zabochen.accuweather.event.Completed;
import ua.ck.zabochen.accuweather.model.jackson.condition.Condition;
import ua.ck.zabochen.accuweather.model.jackson.location.Location;
import ua.ck.zabochen.accuweather.model.realm.City;
import ua.ck.zabochen.accuweather.model.singleton.ConditionCity;
import ua.ck.zabochen.accuweather.model.singleton.SearchCityList;

public class Network {

    public static final String TAG = Network.class.getSimpleName();

    private static Network network;

    private Network() {
    }

    public static Network getInstance() {
        if (network == null) {
            network = new Network();
        }
        return network;
    }

    public void requestLocation(final Completed activity, String city) {

        String cityLocation = API.URL + "locations/v1/search?q=" + city + "&apikey=" + API.KEY;

        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(cityLocation)
                .build();

        Observable.fromCallable(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return okHttpClient.newCall(request).execute();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<Response, Location[]>() {
                    @Override
                    public Location[] apply(@NonNull Response response) throws Exception {
                        return new ObjectMapper().readValue(response.body().byteStream(), Location[].class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Location[]>() {
                    @Override
                    public void accept(@NonNull Location[] location) throws Exception {

                        SearchCityList.getInstance().getSearchList().clear();

                        activity.updateUi();

                        for (int i = 0; i < location.length; i++) {
                            City city = new City();
                            city.setKey(location[i].getKey());
                            city.setCityName(location[i].getEnglishName());
                            city.setCountryName(location[i].getCountry().getEnglishName());
                            city.setRegionName(location[i].getRegion().getEnglishName());

                            SearchCityList.getInstance().addToSearchList(city);
                        }

                        activity.updateUi();
                    }
                });
    }

    public void requestCondition(final Completed activity, String key) {

        String cityCondition = API.URL + "currentconditions/v1/" + key + ".json?apikey=" + API.KEY;

        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(cityCondition)
                .build();

        Observable.fromCallable(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return okHttpClient.newCall(request).execute();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<Response, Condition[]>() {
                    @Override
                    public Condition[] apply(@NonNull Response response) throws Exception {
                        return new ObjectMapper().readValue(response.body().byteStream(), Condition[].class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Condition[]>() {
                    @Override
                    public void accept(@NonNull Condition[] condition) throws Exception {
                        ConditionCity.getInstance().setCondition(condition[0]);
                        activity.updateUi();
                    }
                });
    }


}
