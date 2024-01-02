package edu.uw.tcss450.team6_client.ui.chat.chatlist;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.io.RequestQueueSingleton;

public class ChatListViewModel extends AndroidViewModel {

    /**
     * A Map of Lists of Chat Messages.
     * The Key represents the Chat ID
     * The value represents the List of (known) messages for that that room.
     */
    private MutableLiveData<List<ChatRoom>> mChatRooms;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatRooms = new MutableLiveData<>();
        mChatRooms.setValue(new ArrayList<>());

    }

    /**
     * Register as an observer to listen to a specific chat room's list of messages.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<ChatRoom>> observer) {
        mChatRooms.observe(owner, observer);
    }

    public void getChatRooms(final String jwt, final String email) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats/rooms";
        Log.i("JWT", jwt);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
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

        //code here will run
    }

    private void handelSuccess(final JSONObject response) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        if (response.has("exist")) {
            try {

                JSONArray data = response.getJSONArray(getString.apply(R.string.keys_json_exist_message));
                for (int i = 0; i < data.length(); i++) {
                    JSONObject chatroom = data.getJSONObject(i);
                    ChatRoom cRoom = new ChatRoom(
                            chatroom.getInt(getString.apply(R.string.keys_json_chat_id)),
                            chatroom.getString(getString.apply(R.string.keys_json_chatroom_name)),
                            chatroom.getString(getString.apply(R.string.keys_json_members)),
                            chatroom.getString(getString.apply(R.string.keys_json_message_preview)),
                            chatroom.getString(getString.apply(R.string.keys_json_timestamp))
                    );

                    if (!mChatRooms.getValue().contains(cRoom)) {
                        mChatRooms.getValue().add(cRoom);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR!", e.getMessage());
            }
        } else {
            try {
                JSONArray data = response.getJSONArray(getString.apply(R.string.keys_json_non_exist_message));
                for (int i = 0; i < data.length(); i++) {
                    JSONObject chatroom = data.getJSONObject(i);
//                    for (ChatRoom cr : mChatRooms.getValue()) {}
                    ChatRoom cRoom = new ChatRoom(
                            chatroom.getInt(getString.apply(R.string.keys_json_chat_id)),
                            chatroom.getString(getString.apply(R.string.keys_json_chatroom_name)),
                            chatroom.getString(getString.apply(R.string.keys_json_members)),
                            "None",
                            "None"
                    );

                    if (!mChatRooms.getValue().contains(cRoom)) {
                        mChatRooms.getValue().add(cRoom);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR!", e.getMessage());
            }
        }



        mChatRooms.setValue(mChatRooms.getValue());

    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }
}

