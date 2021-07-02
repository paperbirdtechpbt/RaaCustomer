package com.hopetechno.raadarbar.Notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hopetechno.raadarbar.Utils.AppConstant;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements AppConstant {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    String logtokan = "";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        System.out.println("Firebase Token Update onTokenRefresh -->>>> "+refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);


        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);



        logtokan = prefs.getString("tokan", "");

        if (logtokan.equalsIgnoreCase("")) {

        } else {
            // sending reg id to my server
            callingapiforupdatefirebasetokan(refreshedToken);
        }


        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        SharedPreferences pref;
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();

        SharedPreferences  FireBaseToken = getSharedPreferences("FireBaseToken", MODE_PRIVATE);
        SharedPreferences.Editor editorFireBaseToken = FireBaseToken.edit();
        editorFireBaseToken.putString("regId", token);
        editorFireBaseToken.apply();

        callingapiforupdatefirebasetokan(token);
    }

    public void callingapiforupdatefirebasetokan(final String token) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, updatefcm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response My FirebaseID", response);

                        try {
                            JSONObject responce = new JSONObject(response);
                            JSONObject success = responce.getJSONObject("success");
                            String trip_status = success.getString("trip_status");

                        } catch (Exception e) {
                            Log.e("Exception ", e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences prefsFireBase = getSharedPreferences("FireBaseToken", MODE_PRIVATE);

                if(token!= null && !token.isEmpty())
                params.put("fireregId", token);
                else
                params.put("fireregId", prefsFireBase.getString("regId",MYFIREBASE ));



                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref;
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
        ;
    }
}

