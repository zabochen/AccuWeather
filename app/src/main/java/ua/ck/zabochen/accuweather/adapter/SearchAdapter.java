package ua.ck.zabochen.accuweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        Log.i(TAG, "updateUi: ====> Adapter 1");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_search_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "updateUi: ====> Adapter 2");
        holder.key.setText(SearchCityList.getInstance().getSearchList().get(position).getKey());
        holder.cityName.setText(SearchCityList.getInstance().getSearchList().get(position).getCityName());
        holder.regionName.setText(SearchCityList.getInstance().getSearchList().get(position).getRegionName());
        holder.countryName.setText(SearchCityList.getInstance().getSearchList().get(position).getCountryName());
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "updateUi: ====> Adapter 3 ====> " + SearchCityList.getInstance().getSearchList().size());
        return SearchCityList.getInstance().getSearchList().size() > 0
                ? SearchCityList.getInstance().getSearchList().size()
                : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView key;
        private TextView cityName;
        private TextView regionName;
        private TextView countryName;

        public MyViewHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.adapter_search_key);
            cityName = (TextView) itemView.findViewById(R.id.adapter_search_city_name);
            regionName = (TextView) itemView.findViewById(R.id.adapter_search_region_name);
            countryName = (TextView) itemView.findViewById(R.id.adapter_search_country_name);
        }
    }

}
