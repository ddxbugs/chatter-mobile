package edu.uw.tcss450.team6_client.ui.contact;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ContactCard class
 */
public class ContactCard {

    /** The serialized id number */
    private int mMemberId;

    /** The contact's name */
    private String mName;

    /** The contact's phone number */
    private String mPhone;

    /** The contact's photo */
    private String mPhoto;

    //TODO: this information already exists in the database, how can we save space and res?
    // private final String mFirstName;
    // private final String mLastName;

    /**
     * Dummy constructor
     */
    public ContactCard() {
        //
    }
    /**
     * Default public constructor
     * @param tMemberId the contact id number
     * @param tName the contact's name
     * @param tPhone the contact's phone number
     */
    //TODO: add more vars to contact ie email, first, last...
    public ContactCard(final int tMemberId, final String tName, final String tPhone) {
        mMemberId = tMemberId;
        mName = tName;
        mPhone = tPhone;
        //TODO: Implement photo feature
    }

    public ContactCard(final String tName) {
        mName = tName;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ContactCard object
     * @param contactAsJson the String to be parsed into a ContactCard object
     * @return a ContactCard object with the details contained in a JSON String
     * @throws JSONException when contactAsJson cannot be parsed into a ContactCard
     */
    public static ContactCard createFromJsonString(final String contactAsJson) throws JSONException {
        final JSONObject contact = new JSONObject(contactAsJson);
        return null; //TODO fix me!
    }

    /**
     * Return the contact's name
     * @return mContactName the name of the contact
     */
    public String getName() {return mName;}

    /**
     * Return the contact's phone number
     * @return The phone number
     */
    public String getPhone() { return mPhone; }

    /**
     * Return the contact serial id
     * @return mContactId the contact's id number
     */
    public int getId() {return mMemberId;}

    /**
     * Equals method compares member id
     * @param other The other member id
     * @return result
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ContactCard) {
            result = mMemberId == ((ContactCard) other).mMemberId;
        }
        return result;
    }
}
