package edu.uw.tcss450.team6_client.ui.chat.chatlist;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Encapsulate chat message details.
 */
public final class ChatRoom implements Serializable {

    private final int mChatId;
    private final String mRoomName;
    private final String mMembers;
    private final String mMessagePreview;
    private final String mTimestamp;

    public ChatRoom(int chatId, String roomName, String members, String messagePreview, String timestamp) {
        mChatId = chatId;
        mRoomName = roomName;
        mMembers = members;
        mMessagePreview = messagePreview;
        mTimestamp = timestamp;

    }

    public int getChatId() {

        return mChatId;
    }

    public String getRoomName() {

        return mRoomName;
    }

    public String getMembers() {
        return mMembers;
    }

    public String getMessagePreview() {
        return mMessagePreview;
    }

    public String getTimestamp() {
        return mTimestamp;
    }


    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatRoom) {
            result = mChatId == ((ChatRoom) other).mChatId;
        }
        return result;
    }
}
