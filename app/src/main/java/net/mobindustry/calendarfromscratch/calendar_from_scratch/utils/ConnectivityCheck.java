package net.mobindustry.calendarfromscratch.calendar_from_scratch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Edward on 11/4/14.
 * helper class that provides connectivity check
 */
public class ConnectivityCheck {

    public static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager conMngr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMngr.getActiveNetworkInfo();

        return i != null && i.isAvailable() && i.isConnected();
    }


}
