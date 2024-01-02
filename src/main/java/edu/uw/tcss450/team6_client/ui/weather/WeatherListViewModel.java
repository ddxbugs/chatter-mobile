package edu.uw.tcss450.team6_client.ui.weather;

import android.app.Application;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.uw.tcss450.team6_client.R;

public class WeatherListViewModel extends AndroidViewModel {
    private MutableLiveData<List<WeatherItemFragment>> mWeatherList;

    public WeatherListViewModel(@NonNull Application application) {
        super(application);
        mWeatherList = new MutableLiveData<>();
        mWeatherList.setValue(new ArrayList<>());
    }

    public void addWeatherListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<WeatherItemFragment>> observer) {
        mWeatherList.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    //method to handle a non-error response
    private void handleResult(final JSONObject response) {
//        IntFunction<String> getString =
//                getApplication().getResources()::getString;
        if (response.length() > 0) {
            try {

                JSONObject root = response;
                JSONArray data = response.getJSONArray("daily");
                Log.e("data= ", String.valueOf(data));

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonWeather = data.getJSONObject(i);
                    WeatherItemFragment weatherItem = new WeatherItemFragment(
                            "Tacoma",
                            "Date: " + new SimpleDateFormat("dd/MM/yyyy",
                                    Locale.ENGLISH).format(new Date(jsonWeather.getLong("dt") * 1000)),
                            (jsonWeather.getJSONArray("weather").getJSONObject(0)).getString("description"),
                            (jsonWeather.getJSONObject("temp")).getString("day"),
                            "Morn Temp: " +(jsonWeather.getJSONObject("temp")).getString("morn"),
                            "Day Temp: " +(jsonWeather.getJSONObject("temp")).getString("day"),
                            "Eve Temp: " +(jsonWeather.getJSONObject("temp")).getString("eve"),
                            "Night Temp: " +(jsonWeather.getJSONObject("temp")).getString("night")
                            );
                    if (!mWeatherList.getValue().contains(weatherItem)) {
                        mWeatherList.getValue().add(weatherItem);
                    }
                }


            } catch (JSONException e) {
                Log.e("JSON Parse Error", e.getMessage());
            }

        } else {
            Log.d("JSON Response", "No Response");
        }
        mWeatherList.setValue(mWeatherList.getValue());
}

        //


        public void connectGet(String jwt) {
        String url =
                getApplication().getResources().getString(R.string.base_url) + "weather/onecall/lon=-122.44&lat=47.25&daily";;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                //headers.put("Authorization", "");
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }


}
