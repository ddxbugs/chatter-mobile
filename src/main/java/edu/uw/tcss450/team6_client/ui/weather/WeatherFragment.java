package edu.uw.tcss450.team6_client.ui.weather;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Locale;

import edu.uw.tcss450.team6_client.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.team6_client.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    private WeatherViewModel mWeatherModel;

    public WeatherFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        //mWeatherModel.connectGet(model.getmJwt());
        mWeatherModel.connectDailyGet(model.getmJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());
        binding.forcast.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        WeatherFragmentDirections.actionNavigationWeatherToNavigationWeatherList()));

        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

    }
    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        //if (response.length() > 0) {
//            if (!response.has("coord")) {
//            throw new IllegalStateException("Unexpected response in WeatherViewModel: " + response);
//            }
        if (response.length() > 0) {
            try {

                JSONObject root = response;
                JSONObject current = root.getJSONObject("current");
                //StringBuilder currentWeather = new StringBuilder(current.)
                JSONArray data = root.getJSONArray("hourly");
                //Log.e("data= ", String.valueOf(data));
                String temp = current.getString("temp") + "°F";
                Long sunrise = current.getLong("sunrise");
                Long sunset = current.getLong("sunset");
                Long updatedAt = current.getLong("dt");
                String pressure = current.getString("pressure");
                String humidity = current.getString("humidity");
                String windSpeed = current.getString("wind_speed");
                JSONObject weather = current.getJSONArray("weather").getJSONObject(0);
                String weatherDescription = weather.getString("description");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                StringBuilder myOutput = new StringBuilder("Hourly Forecast\n");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonWeather = data.getJSONObject(i);
                    StringBuilder weatherItem = new StringBuilder(
                                    "Tacoma\n" +
                                    "Date and time: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a" ,
                                        Locale.ENGLISH).format(new Date(jsonWeather.getLong("dt") * 1000)) +
                                    "\nTemp: "+jsonWeather.getString("temp") + "°F"+
                                    "\nPressure: "+jsonWeather.getString("pressure") +
                                    "\nHumidity: "+jsonWeather.getString("humidity") +
                                    "\nWind Speed: "+jsonWeather.getString("wind_speed") +
                                    "\nDescription: "+(jsonWeather.getJSONArray("weather").getJSONObject(0)).getString("description") +"\n"
                    );

                    myOutput.append("\n" + weatherItem);

                }
                binding.weatherInfoTxt.setText(myOutput);
                binding.status.setText(weatherDescription);
                binding.temp.setText(temp);
                binding.sunrise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                binding.sunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                binding.wind.setText(windSpeed);
                binding.pressure.setText(pressure);
                binding.humidity.setText(humidity);
                binding.updatedAt.setText(updatedAtText);
                binding.about.setText("Team6");


            } catch (JSONException e) {
                Log.e("JSON Parse Error", e.getMessage());
            }

//        try {
//            JSONObject root = response;
//            //Log.e("root= ", String.valueOf(root));
//
//
//            JSONObject main = root.getJSONObject("main");
//            JSONObject sys = root.getJSONObject("sys");
//            JSONObject wind = root.getJSONObject("wind");
//            JSONObject weather = root.getJSONArray("weather").getJSONObject(0);
//
//            Long sunrise = sys.getLong("sunrise");
//            Long sunset = sys.getLong("sunset");
//            Long updatedAt = root.getLong("dt");
//            String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
//            String address = root.getString("name") + ", " + sys.getString("country");
//            String temp = main.getString("temp") + "°F";
//            String tempMin = "Min Temp: " + main.getString("temp_min") + "°F";
//            String tempMax = "Max Temp: " + main.getString("temp_max") + "°F";
//            String pressure = main.getString("pressure");
//            String humidity = main.getString("humidity");
//            String windSpeed = wind.getString("speed");
//            String weatherDescription = weather.getString("description");
//
//            //JSONArray weatherInformation = root.getJSONArray("coord");
//            //Log.e("weather= ", String.valueOf(weather));
//            binding.status.setText(weatherDescription);
//            binding.temp.setText(temp);
//            binding.tempMin.setText(tempMin);
//            binding.tempMax.setText(tempMax);
//            binding.sunrise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
//            binding.sunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
//            binding.wind.setText(windSpeed);
//            binding.pressure.setText(pressure);
//            binding.humidity.setText(humidity);
//            binding.updatedAt.setText(updatedAtText);
//            binding.address.setText(address);
//            binding.about.setText("Team6");
//        } catch (JSONException e) {
//                    Log.e("JSON Parse Error", e.getMessage());
//            }

            } else{
                Log.d("JSON Response", "No Response");
            }

        }
    }
