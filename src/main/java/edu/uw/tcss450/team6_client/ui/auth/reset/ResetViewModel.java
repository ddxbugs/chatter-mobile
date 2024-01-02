/*
Team 6
Spring 2020
 */

//packages
package edu.uw.tcss450.team6_client.ui.auth.reset;

//import statements
import android.app.Application;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.io.RequestQueueSingleton;

/**
 * View model for reset fragment to send HTTP
 * request and observe JSON response objects
 * returned from the Heroku server end point
 */
public class ResetViewModel extends AndroidViewModel {

    /** The mutable live data json response object */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Default public constructor
     * @param application
     */
    public ResetViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Add a JSON response observer to this class for the reset password HTTP request
     * sent to the Heroku server end point
     * @param owner This lifecycle owner
     * @param observer The observer observing the HTTP response object
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Handle the Volley error for JSON object
     * @param error The volley error
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
    }

    /**
     * Connect to the backend web service using a POST
     * to send the client's email for database validation
     * and verification
     *
     * @param email the client's email
     */
    public void connectPost(final String email) {

        //Team backend end point for authorization
        String url = getApplication().getResources().getString(R.string.base_url) + "reset";
        //String url = getApplication().getResources().getString(R.string.local_host_5000) + "reset"; //TODO Remove me
        // The JSONObject for sending the String email to the POST endpoint
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Initialize and declare a POST request object
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

    /**
     * HTTP Get method sends an HTTP GET request to the Heroku server
     * for client validation and verification that the HTML link
     * was clicked to begin the reset password process. This request
     * sends the temporary password assigned to the client for
     * reset password activity.
     * @param email the client email\
     */
    public void connectGet(final String email, final String password) {
        String url = getApplication().getResources().getString(R.string.base_url) + "reset";
        //String url = getApplication().getResources().getString(R.string.local_host_5000) + "reset"; //TODO Debug, remove me

        Log.i("URL: ", url);

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                String credentials = email + ":" + password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }
}
