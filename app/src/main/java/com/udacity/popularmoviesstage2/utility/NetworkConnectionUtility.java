package com.udacity.popularmoviesstage2.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.udacity.popularmoviesstage2.R;

/**
 * Created by hansend on 6/8/2018.
 */

public class NetworkConnectionUtility {
    public static boolean haveActiveNetworkConnection(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(connectivityManager);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void displayNoNetworkConnection(Context context) {
        Toast.makeText(context, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
    }

    private static NetworkInfo getActiveNetworkInfo(ConnectivityManager connectivityManager) {
        return connectivityManager.getActiveNetworkInfo();
    }
}
