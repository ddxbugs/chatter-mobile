/*
Team 6
VerificationProcessFragment.java
Spring 2020
 */

//packages
package edu.uw.tcss450.team6_client.ui.auth.register;

//import statements
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.databinding.FragmentVerificationProcessBinding;
import edu.uw.tcss450.team6_client.model.UserInfoViewModel;

/**
 * VerificationProcessFragment handles client side registration requests
 * by navigating them to this class first before performing authentication.
 * A simple {@link Fragment} subclass.
 */
public class VerificationProcessFragment extends Fragment {
    /**
     * The default class constructor.
     */
    public VerificationProcessFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification_process, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentVerificationProcessBinding binding = FragmentVerificationProcessBinding.bind(getView());

        //Note argument sent to the ViewModelProvider constructor. It is the Activity that
        //hols this fragment.
//        UserInfoViewModel model = new ViewModelProvider(requireActivity())
//                .get(UserInfoViewModel.class);
        binding.displayVerificationProcess.setText( String.format(" "
                + " We are verifying your information!" +
                "\n Please check your email to validate your email address.") );

    }
}
