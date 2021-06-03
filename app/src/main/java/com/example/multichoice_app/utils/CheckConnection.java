package com.example.multichoice_app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {

    public static boolean checkCon(Context context){
        boolean haveConnectedWifi  = false;
        boolean haveConnectedMobile  = false;
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for(NetworkInfo info : networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if(info.isConnected())
                    haveConnectedWifi = true;
            if(info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                    haveConnectedMobile = true;
        }


        return haveConnectedMobile || haveConnectedWifi;
    }



}
