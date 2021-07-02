package com.hopetechno.raadarbar.Utils;

import android.util.Log;

import com.hopetechno.raadarbar.BuildConfig;

public class DebugerLog {
    public static Boolean DEBUG = BuildConfig.DEBUG ;

    public static void printError(String tag,String message){
        if(DEBUG)
        Log.e(tag,message);
    }

    public static void pringInfo (String tag,String message){
        if(DEBUG)
        Log.i(tag,message);
    }

    public static void pringDebug (String tag,String message){
        if(DEBUG)
        Log.d(tag,message);
    }
}
