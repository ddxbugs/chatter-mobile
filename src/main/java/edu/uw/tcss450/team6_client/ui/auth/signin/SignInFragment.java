/*
Team 6
SignInFragment.java
Spring 2020
 */

//packages
package edu.uw.tcss450.team6_client.ui.auth.signin;

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

import edu.uw.tcss450.team6_client.model.PushyTokenViewModel;
import edu.uw.tcss450.team6_client.model.UserInfoViewModel;
import edu.uw.tcss450.team6_client.databinding.FragmentSignInBinding;

import edu.uw.tcss450.team6_client.utils.PasswordValidator;
import static edu.uw.tcss450.team6_client.utils.PasswordValidator.*;

/**
 * SignInFragment class ui for client-side authentication activity
 * Sends a HTTP connection method GET request to the listening server
 * to perform server-side client validation
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    /** The sign in fragment binding */
    private FragmentSignInBinding binding;

    /** The view model for SigninFragment */
    private SignInViewModel mSignInModel;
    /** The pushy token view model */
    private PushyTokenViewModel mPushyTokenViewModel;
    /** The user info view model */
    private UserInfoViewModel mUserViewModel;

    /** The email password validator */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));
    /** The password validator */
    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    /** The default class constructor */
    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

        mPushyTokenViewModel = new ViewModelProvider(getActivity()).get(PushyTokenViewModel.class);
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
        binding = FragmentSignInBinding.inflate(inflater);
        // Inflate the layout for this fragment
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

        binding.buttonToRegister.setOnClickListener(button ->
            Navigation.findNavController(getView()).navigate(
                    SignInFragmentDirections.actionLoginFragmentToRegisterFragment()
            ));

        binding.buttonSignIn.setOnClickListener(this::attemptSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSignInResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
         binding.editEmail.setText(args.getEmail().equals("default") ? "minh1212@uw.edu" : args.getEmail());
         binding.editPassword.setText(args.getPassword().equals("default") ? "Bigbang322$" : args.getPassword());
//        binding.editEmail.setText(args.getEmail().equals("default") ? "kkinoc@yahoo.fr" : args.getEmail());
//        binding.editPassword.setText(args.getPassword().equals("default") ? "Lagrace1$" : args.getPassword());

//        binding.editEmail.setText(args.getEmail().equals("default") ? "joejoo@uw.edu" : args.getEmail());
//        binding.editPassword.setText(args.getPassword().equals("default") ? "PASSword!@#$1234" : args.getPassword());

        //don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token -> binding.buttonSignIn.setEnabled(!token.isEmpty()));

        mPushyTokenViewModel.addResponseObserver( getViewLifecycleOwner(),
                this::observePushyPutResponse);

        //Add onClick listener to the text view when the user clicks to reset pw
        binding.textReset.setOnClickListener(this::attemptResetPW);
    }

    /**
     *
     * @param view
     */
    private void attemptResetPW(View view) {
        Navigation.findNavController(getView()).navigate(SignInFragmentDirections
                .actionSignInFragmentToResetFragment());
    }

    /**
     * Attempt sign in verification process when user clicks the login button.
     * @param button the button view
     */
    private void attemptSignIn(final View button) {
        validateEmail();
    }

    /**
     * Validate the email and process the result
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validate the password and call the verify authentication with server
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword.setError("Please enter a valid Password."));
    }

    /**
     * Call the HTTP connection GET method and verify client credentials
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.editEmail.getText().toString(),
                binding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionLoginFragmentToMainActivity(email, jwt));
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeSignInResponse(final JSONObject response) {
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
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory( binding.editEmail.getText().toString(),
                                    response.getString("token") )).get(UserInfoViewModel.class);
                    sendPushyToken();
//                    navigateToSuccess(
//                            binding.editEmail.getText().toString(),
//                            response.getString("token")
//                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getmJwt());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                binding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        binding.editEmail.getText().toString(),
                        mUserViewModel.getmJwt()

                );
            }

        }
    }
}
