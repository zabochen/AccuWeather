package ua.ck.zabochen.accuweather.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.ck.zabochen.accuweather.R;
import ua.ck.zabochen.accuweather.event.AdapterClick;
import ua.ck.zabochen.accuweather.model.singleton.MainCityList;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private static final String TAG = MainAdapter.class.getSimpleName();

    private AdapterClick mActivity;

    public MainAdapter(AdapterClick activity) {
        this.mActivity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_main_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.key.setText("Key: " + MainCityList.getInstance().getMainList().get(position).getKey());
        holder.cityName.setText(MainCityList.getInstance().getMainList().get(position).getCityName());
        holder.countryRegionName.setText(MainCityList.getInstance().getMainList().get(position).getCountryName()
                + ", " + MainCityList.getInstance().getMainList().get(position).getRegionName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getAdapterClickPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainCityList.getInstance().getMainList().size() > 0
                ? MainCityList.getInstance().getMainList().size()
                : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView key;
        private TextView cityName;
        private TextView countryRegionName;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.adapter_main_key);
            cityName = (TextView) itemView.findViewById(R.id.adapter_main_city_name);
            countryRegionName = (TextView) itemView.findViewById(R.id.adapter_main_country_region_name);
            cardView = (CardView) itemView.findViewById(R.id.adapter_main_card_view);
        }
    }

}
