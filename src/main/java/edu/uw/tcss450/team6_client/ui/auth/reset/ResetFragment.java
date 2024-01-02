/*
Team 6
Spring 2020
 */

//packages
package edu.uw.tcss450.team6_client.ui.auth.reset;

//import statements
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.team6_client.databinding.FragmentResetBinding;
import edu.uw.tcss450.team6_client.utils.PasswordValidator;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkPwdSpecialChar;

/**
 * Reset fragment displays an edit text view for
 * clients to send their email to the Heroku
 * backend for resetting their password
 */
public class ResetFragment extends Fragment {

    /** The fragment binding */
    private FragmentResetBinding binding;

    /** The reset password view model */
    private ResetViewModel mResetModel;

    /** Email validator */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Default public constructor
     *
     */
    public ResetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResetModel = new ViewModelProvider(getActivity())
                .get(ResetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonReset.setOnClickListener(this::attemptResetPW);

        mResetModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Helper method to call validate email
     * @param view
     */
    private void attemptResetPW(View view) { validateEmail(); }

    /**
     * Validate the edit text view field
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::verifyAuthWithServer,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Connect to the backend database and verify client registration
     * by sending a HTTP Post
     */
    private void verifyAuthWithServer() {
        mResetModel.connectPost(binding.editEmail.getText().toString());
        //Asynchronous call, no statements after should rely on result of connect
    }


    /**
     * This Method help navigate to the VerificationProcess fragment to tell the user
     * the verification process that is taking place after registration
     */
    private void navigateToResetVerificationProcess() {
        ResetFragmentDirections.ActionResetFragmentToResetVerficationProcessFragment directions =
                ResetFragmentDirections.actionResetFragmentToResetVerficationProcessFragment();
        Navigation.findNavController(getView()).navigate(directions);
    }

    /**
     * Creates a JSON object observer for an HTTP response object. This observer should be
     * attached to ResetViewModel.
     *
     * @param response the response sent from the Heroku server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToResetVerificationProcess();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}
