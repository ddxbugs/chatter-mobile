package edu.uw.tcss450.team6_client.ui.contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.databinding.FragmentContactListBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment {

    private FragmentContactListBinding binding;
    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    /*
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        if (view instanceof RecyclerView) {

            ((RecyclerView) view).setLayoutManager(new GridLayoutManager(getContext(), 1));
            ((LinearLayoutManager)((RecyclerView) view).getLayoutManager())
                  .setOrientation(LinearLayoutManager.VERTICAL);

            ((RecyclerView) view).setAdapter(
                    new ContactRecyclerViewAdapter(ContactGenerator.getContactList()));
        }
        return view;
     */
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());
        binding.listRoot.setAdapter(new ContactRecyclerViewAdapter(ContactGenerator.getContactList()));
    }
}
