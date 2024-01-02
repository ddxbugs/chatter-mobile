/*
Team 6
Spring 2020
 */
//packages
package edu.uw.tcss450.team6_client.ui.contact;

//import statements
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.databinding.FragmentContactCardBinding;

/**
 *
 */
public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {
    /** The contact list displayed */
    private final List<ContactCard> mContactList;
    /** The unique contact id */
    //private final int mContactId;

    /**
     * Default public constructor
     * @param tContactList
     * @param //tContactId //TODO Fix me!
     */
    public ContactRecyclerViewAdapter(final List<ContactCard> tContactList) {
        mContactList = tContactList;
        //mContactId = tContactId;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.fragment_contact_card, parent, false));
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContactList.get(position));
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() { return mContactList.size(); }

    /**
     *
     */
    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentContactCardBinding binding;

        public ContactViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);
            //TODO: setOnClickListener for edit/view contact card binding detail
        }

        void setContact(final ContactCard tContact) {
            binding.textContactEmail.setText(tContact.getName());
            binding.textContactPhone.setText(tContact.getPhone());
        }
    }
}
