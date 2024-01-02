package edu.uw.tcss450.team6_client.ui.contact;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactInfoViewModel extends AndroidViewModel {

    private Map<Integer, MutableLiveData<List<ContactCard>>> mContacts;

    public ContactInfoViewModel(@NonNull Application application) {
        super(application);
        mContacts = new HashMap<>();
    }

    public void addContactObserver(int tContactId,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<ContactCard>> observer) {

    }

    // TODO: Fix me, returns null;
    public List<ContactCard> getContactListByContactId(final int tContactId) {
        return null;
    }
}
