/*
Team 6
UserInfoViewModel.java
Spring 2020
 */

//packages
package edu.uw.tcss450.team6_client.model;

//import statements

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * View model for auth and main activity cycle
 * Stores client registration info
 */
public class UserInfoViewModel extends ViewModel {

    /**
     * Return the user's email address.
     *
     * @return mEmail
     */
    public String getEmail() {

        return mEmail;
    }

    /**
     * Return the user's JWT.
     *
     * @return mJWT
     */
    public String getmJwt() {

        return mJWT;
    }
//    public void setEmail(String mEmail) {
//        this.mEmail = mEmail;
//    }

    /**
     * The user's email
     */
    private String mEmail;
    /**
     * The user's jwt
     */
    private String mJWT;


    /**
     * The default class constructor
     * Creates a user view model
     *
     * @param email The user email
     * @param jwt   The user jwt
     */
    private UserInfoViewModel(String email, String jwt) {

        mEmail = email;
        mJWT = jwt;
    }

    /**
     * Default view model factory class constructor
     * Implements ViewModelProvider.Factor
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {
        /**
         * The user's email
         */
        private final String email;
        /**
         * The user's jwt
         */
        private final String jwt;

        /**
         * The default factory class constructor.
         *
         * @param email The user's email
         * @param jwt   The user's jwt
         */
        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;

        }

        /**
         * Returns a UserInfoViewModel
         *
         * @param modelClass The class type
         * @param <T>        T extends ViewModel
         * @return new UserInfoViewModel(email, jwt)
         */
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {

                return (T) new UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }

    }

}
