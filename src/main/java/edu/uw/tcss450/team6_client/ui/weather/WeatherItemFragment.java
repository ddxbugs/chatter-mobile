package edu.uw.tcss450.team6_client.ui.weather;

/**
 * A simple class.
 */

public class WeatherItemFragment {


    private String mAddress;
    private String mDate;
    private String mStatus;
    private String mTemp;
    private String mTempMorn;
    private String mTempDay;
    private String mTempEve;



    private String mTempNight;

    public String getAddress() {
        return mAddress;
    }

    public String getDate() {
        return mDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getTemp() {
        return mTemp;
    }

    public String getTempMorn() {
        return mTempMorn;
    }

    public String getTempDay() {
        return mTempDay;
    }

    public String getTempEve() {
        return mTempEve;
    }

    public String getTempNight() {
        return mTempNight;
    }



    public WeatherItemFragment() {
    }


    public WeatherItemFragment(String address,
                               String date,
                               String status,
                               String temp,
                               String tempMorn,
                               String tempDay,
                               String tempEve,
                               String tempNight) {
        this.mAddress = address;
        this.mDate = date;
        this.mStatus = status;
        this.mTemp = temp;
        this.mTempMorn = tempMorn;
        this.mTempDay = tempDay;
        this.mTempEve = tempEve;
        this.mTempNight = tempNight;
    }

}

