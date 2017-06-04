package ua.ck.zabochen.accuweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.ck.zabochen.accuweather.R;
import ua.ck.zabochen.accuweather.model.singleton.SearchCityList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private static final String TAG = SearchAdapter.class.getSimpleName();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_search_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.key.setText("Key: " + SearchCityList.getInstance().getSearchList().get(position).getKey());
        holder.cityName.setText(SearchCityList.getInstance().getSearchList().get(position).getCityName());
        holder.countryRegionName.setText(SearchCityList.getInstance().getSearchList().get(position).getCountryName()
                + ", " + SearchCityList.getInstance().getSearchList().get(position).getRegionName());
    }

    @Override
    public int getItemCount() {
        return SearchCityList.getInstance().getSearchList().size() > 0
                ? SearchCityList.getInstance().getSearchList().size()
                : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView key;
        private TextView cityName;
        private TextView countryRegionName;

        public MyViewHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.adapter_search_key);
            cityName = (TextView) itemView.findViewById(R.id.adapter_search_city_name);
            countryRegionName = (TextView) itemView.findViewById(R.id.adapter_search_country_region_name);
        }
    }
}