/*
Team 6
RegisterFragment.java
Spring 2020
 */

//packages
package edu.uw.tcss450.team6_client.ui.auth.register;
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

import edu.uw.tcss450.team6_client.ui.auth.register.RegisterFragmentDirections;
import edu.uw.tcss450.team6_client.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.team6_client.utils.*;

import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.checkPwdUpperCase;



/**
 * This fragment implements the registration ui. Handles
 * HTTP connection POST method for server-side verification
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    /** The Register fragment binding */
    private FragmentRegisterBinding binding;
    /** The Register view model */
    private RegisterViewModel mRegisterModel;
    /** The string name validator */
    private PasswordValidator mNameValidator = checkPwdLength(1);
    /** The string nick name validator */
    private PasswordValidator mNicknameValidator = checkPwdLength(4)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase().or(checkPwdUpperCase()));
    /** The string email validator */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));
    /** The password validator */
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().and(checkPwdUpperCase()));

    /**
     * Default public constructor
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
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

        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Attempt to register the new user using server web auth
     * @param button The button clicked.
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * Validate the first name.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validate the last name.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateNickName,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validate the nick name.
     */
    private void validateNickName() {
        mNameValidator.processResult(
                mNicknameValidator.apply(binding.editNickname.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validate the email.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validate the password.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password."));
    }

    /**
     * Connect to the backend database and verify client registration
     * Sends a JWT token for client-server auth using HTTPS encrypt.
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editNickname.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }


    /**
     * This Method help navigate to the VerificationProcess fragment to tell the user
     * the verification process that is taking place after registration
     */
    private void navigateToVerificationProcess() {
        RegisterFragmentDirections.ActionRegisterFragmentToVerificationProcessFragment directions =
            RegisterFragmentDirections.actionRegisterFragmentToVerificationProcessFragment();

        directions.setFirstName(binding.editFirst.getText().toString());
        directions.setLastName(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);
    }
    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
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
                navigateToVerificationProcess();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}
