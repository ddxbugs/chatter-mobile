package edu.uw.tcss450.team6_client.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.databinding.FragmentWeatherItemBinding;

public class WeatherListRecyclerViewAdapter extends RecyclerView.Adapter<WeatherListRecyclerViewAdapter.weatherViewHolder>  {

    Context mContext;
    List<WeatherItemFragment> mWeatherData;
    //List<WeatherItemFragment> mDataFiltered ;
    boolean isDark = false;

//    public WeatherListRecyclerViewAdapter(Context mContext, List<WeatherItemFragment> mWeatherData, boolean isDark) {
//        this.mContext = mContext;
//        this.mWeatherData = mWeatherData;
//        this.isDark = isDark;
//        //this.mDataFiltered = mWeatherData;
//    }
//
    public WeatherListRecyclerViewAdapter(Context mContext, List<WeatherItemFragment> mWeatherData) {
        this.mContext = mContext;
        this.mWeatherData = mWeatherData;
        //this.mDataFiltered = mWeatherData;

    }

    public WeatherListRecyclerViewAdapter(List<WeatherItemFragment> weatherList) {

        this.mWeatherData = weatherList;
    }

    @NonNull
    @Override
    public weatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

//        View layout;
//        layout = LayoutInflater..inflate(R.layout.fragment_weather_item,viewGroup,false);
//        return new weatherViewHolder(layout);
        return new WeatherListRecyclerViewAdapter.weatherViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull weatherViewHolder weatherViewHolder, int position) {
        weatherViewHolder.setWeather(mWeatherData.get(position));
    }

    @Override
    public int getItemCount() {
        return mWeatherData.size();
    }



    public class weatherViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentWeatherItemBinding binding;

        public weatherViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentWeatherItemBinding.bind(itemView);
            if (isDark) {
                setDarkTheme();
            }
        }

        void setWeather(final WeatherItemFragment weather) {
            //binding.container.setAnimation(AnimationUtils.loadAnimation(,R.anim.fade_scale_animation));
            binding.container.setBackgroundResource(R.drawable.background_grad);
            binding.address.setText(weather.getAddress());
            binding.updatedAt.setText(weather.getDate());
            binding.status.setText(weather.getStatus());
            binding.temp.setText(weather.getTemp());
            binding.tempDay.setText(weather.getTempDay());
            binding.tempMorn.setText(weather.getTempMorn());
            binding.tempEve.setText(weather.getTempEve());
            binding.tempNight.setText(weather.getTempNight());

        }

        private void setDarkTheme() {
            binding.container.setBackgroundResource(R.drawable.background_grad);
        }

    }
}
