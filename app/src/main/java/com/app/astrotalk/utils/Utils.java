package com.app.astrotalk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class Utils {
    public static String searchTextValue;

    //Add Your Sample Phone Number
    public static String vedicAstroPhoneNumberDummy = "8320577653";
    public static String loveAstroPhoneNumberDummy = "8320577653";
    public static String careerAstroPhoneNumberDummy = "8320577653";
    public static String marriageAstroPhoneNumberDummy = "8320577653";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

            return networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }

        return false;
    }
}
