/*
Team 6
RegisterViewModel.java
Spring 2020
 */

//packages
package edu.uw.tcss450.team6_client.ui.auth.register;

//import statements
import android.app.Application;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.tcss450.team6_client.R;

/**
 * The RegisterViewModel class enables client authentication
 * by sending HTTP connection requests
 */
public class RegisterViewModel extends AndroidViewModel {
    /** The mutable live data class object for json objects */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * The default class constructor
     * @param application
     */
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Add an observer to the jsonobject super type connected to the web service.
     * @param owner the lifecycle owner
     * @param observer the observer object
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Handle volley errors
     * @param error the Volley error
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Sends an HTTP Post request using JSONObject class
     * @param first
     * @param last
     * @param email
     * @param password
     */
    public void connect(final String first,
                        final String last,
                        final String username,
                        final String email,
                        final String password) {

        //String url = getApplication().getResources().getString(R.string.local_host_5000) + "auth";
        String url = getApplication().getResources().getString(R.string.base_url) + "auth";

        JSONObject body = new JSONObject();
        try {
            body.put("first", first);
            body.put("last", last);
            body.put("username", username);
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);

    }
}
