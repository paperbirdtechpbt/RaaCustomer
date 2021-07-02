package com.hopetechno.raadarbar.Notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hopetechno.raadarbar.Activity.CustHomePageActivity;
import com.hopetechno.raadarbar.Activity.CutomerWaitingActivity;
import com.hopetechno.raadarbar.Activity.NewCustomerWaitingActivity;
import com.hopetechno.raadarbar.Activity.NotificationDisplayActivity;
import com.hopetechno.raadarbar.Activity.OtpVerificationActivity;
import com.hopetechno.raadarbar.Activity.PaymentActivity;
import com.hopetechno.raadarbar.Activity.SplashScreen;
import com.hopetechno.raadarbar.Activity.ThanksScreenActivity;
import com.hopetechno.raadarbar.Dialog.CustomDialogClass;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService implements AppConstant {

    private static final String TAG = "MyFirebaseMsgService";

    private NotificationUtils notificationUtils;
    String title, message, imageUrl = "", timestamp;
    JSONObject payload;
    boolean isBackground;
    int driver;
    static boolean launch = false;

    public static Activity activity;


    public static MediaPlayer mPlayer;
    public static CountDownTimer countDownTimer;

    String logtokan = "";

    static Context context;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        OtpVerificationActivity.fireregId = s;

        System.out.println("Firebase Token Update onNewToken -->>>> " + s);

        String refreshedToken = "";
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

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

        if (!token.isEmpty())
            editor.putString("regId", token);
        else
            editor.putString("regId", "105MyfirebaseEMPTY");
        editor.apply();

        SharedPreferences FireBaseToken = getSharedPreferences("FireBaseToken", MODE_PRIVATE);
        SharedPreferences.Editor editorFireBaseToken = FireBaseToken.edit();
        if (!token.isEmpty())
            editorFireBaseToken.putString("regId", token);
        else
            editorFireBaseToken.putString("regId", "113MyfirebaseEMPTY");
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
                            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
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
                if (token != null && !token.isEmpty())
                    params.put("fireregId", token);
                else
                    params.put("fireregId", prefsFireBase.getString("regId", MYFIREBASE));


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
        if (!token.isEmpty())
            editor.putString("regId", token);
        else
            editor.putString("regId", "179MyfirebaseEMPTY");
        editor.apply();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {


            context = getApplicationContext();

            Log.e(TAG, "From: " + remoteMessage.getFrom().toString());
            Log.d(TAG, " onMessageReceived : " + remoteMessage.getFrom().toString());


            if (remoteMessage == null)
                return;


            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                // Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                // handleNotification(remoteMessage.getNotification().getBody());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());

                }
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());

        }
    }

    JSONObject data;

    private void handleDataMessage(final JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            data = json.getJSONObject("data");
            try {
                //driver = 1;
                title = data.getString("title");
                message = data.getString("message");
                driver = data.getInt("is_driver");
                timestamp = data.getString("timestamp");
                if (data.getJSONObject("payload") != null)
                    payload = data.getJSONObject("payload");

                Log.e(TAG, " Title  =====  ---->>" + title);
                isBackground = false;
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());

            }

            Log.e(TAG, "L: " + String.valueOf(driver));

            try {

                if (title.trim().equals("Service not available for SUV")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CustHomePageActivity.class);
                    resultIntent.putExtra("message", message);
                    resultIntent.putExtra("payload", String.valueOf(payload));
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);
                }
                if (!title.trim().equals("Driver Is coming")) {
                    Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("payload", String.valueOf(payload));
                    pushNotification.putExtra("datafromnoti", String.valueOf(json));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                } else {
                    Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("payload", String.valueOf(payload));
                    pushNotification.putExtra("datafromnoti", String.valueOf(json));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                }

                if (title.equals("Thank You for Ride!!!")) {
                    if (driver == 0) {
                        Intent dialogIntent = new Intent(this, ThanksScreenActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                if (title.trim().equals("Driver Is coming")) {

                    Log.e(TAG, "CutomerWaitingActivity 282");
                    Intent resultIntent = new Intent(getApplicationContext(), CutomerWaitingActivity.class);
                    resultIntent.putExtra("message", message);
                    resultIntent.putExtra("payload", String.valueOf(payload));
                    resultIntent.putExtra("datafromnoti", String.valueOf(json));
//                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(resultIntent);

                    showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);

                } else {
                    //@Anil send user Payload when app is open
                    Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("payload", String.valueOf(payload));
                    pushNotification.putExtra("datafromnoti", String.valueOf(json));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                }

                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound(driver);

                //@Anil User Block Popup
                if (title.equals("You are blocked") && payload != null) {
                    JSONObject main = new JSONObject(String.valueOf(payload));
                    if (main != null) {
                        if ("blocked".equals(main.getString("trip_status"))) {

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (MyApplication.currentActivity() != null) {
                                        CustomDialogClass cdd = new CustomDialogClass(MyApplication.currentActivity(), "", "");
                                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        cdd.show();
                                    }
                                }
                            });
                        }
                    }
                }

            } else {

                try {
                    if (driver == 0 && title != null && title.trim().equals("Driver Is coming")) {
                        Log.e(TAG, "CutomerWaitingActivity 330");
                        Intent resultIntent = new Intent(getApplicationContext(), CutomerWaitingActivity.class);
                        resultIntent.putExtra("message", message);
                        resultIntent.putExtra("payload", String.valueOf(payload));
                        resultIntent.putExtra("datafromnoti", String.valueOf(json));
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(resultIntent);
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                }
            }
            if (driver == 0) {
                try {

                    Log.e("CheckNotification", " Payload" + payload);
                    JSONObject main = new JSONObject(String.valueOf(payload));
                    if (main.has("endotp")) {
                        Log.e("endotp", "endotp");
                        String endotpnot = main.getString("endotp");
                        // String payable_amount = main.optString("payable_amount");
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        Log.e("message12", message);
                        editor.putString("page", "customer");
                        editor.putString("message", message);
                        editor.putString("payload", String.valueOf(payload));
                        editor.putString("payload", String.valueOf(payload));
                        editor.putString("endotpnot", endotpnot);
                        editor.putString("messagenoti", message);
                        editor.apply();

                        Log.e(TAG, "CutomerWaitingActivity 363");
                        Log.e("endotpnot", endotpnot);
                        Intent resultIntent = new Intent(getApplicationContext(), CutomerWaitingActivity.class);
                        resultIntent.putExtra("message", message);
                        resultIntent.putExtra("payload", String.valueOf(payload));
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);
                    } else if (main.has("trip_status")) {


                        String status = payload.getString("trip_status");
                        Log.e("CheckNotification", " status " + status);
                        if (status.equalsIgnoreCase("car_not_available"))
                            Log.e("CheckNotification", " if ");
                        else
                            Log.e("CheckNotification", " else ");


                        if (status.equalsIgnoreCase("car_not_available")) {

                            Log.e("CheckNotification", "car_not_available");

                            Intent resultIntent = new Intent(getApplicationContext(), NewCustomerWaitingActivity.class);
                            resultIntent.putExtra("message", message);
                            resultIntent.putExtra("payload", String.valueOf(payload));
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);

                            return;
                        } else if (status.equalsIgnoreCase("car_is_available")) {

                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("page", "customer_waiting");
                            editor.putString("message", message);
                            editor.putString("payload", String.valueOf(payload));
                            editor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis());
                            editor.apply();

                            Log.e(TAG, "CutomerWaitingActivity 403");
                            CutomerWaitingActivity.i = 1;
                            Intent resultIntent = new Intent(getApplicationContext(), CutomerWaitingActivity.class);
                            resultIntent.putExtra("message", message);
                            resultIntent.putExtra("payload", String.valueOf(payload));
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);

                            return;
                        }
                    } else if (main.has("payable_amount")) {
                        //get Value of payable_amount
                        Log.e("payable_amount", "payable_amount");
                        Log.e("HIHIH ", "we are in");
                        String payable_amount = main.optString("payable_amount");
                        //String endotpnot = main.getString("endotp");
                        // Clean for new Ride

                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("payable_amount", payable_amount);
                        editor.putString("start", String.valueOf(main));
                        editor.putString("page", "payment");
                        editor.putString("message", message);
                        editor.putString("start_lat", "");
                        editor.putString("start_lng", "");
                        editor.putString("Mstart_lat", "");
                        editor.putString("Mstart_lng", "");

                        editor.putString("mtrip_end_lat", "");
                        editor.putString("mtrip_end_long", "");
                        editor.putString("mtrip_driver_lat", "");
                        editor.putString("mtrip_driver_lng", "");

                        editor.putString("end_lat", "");
                        editor.putString("end_lng", "");
                        editor.putString("Mend_lat", "");
                        editor.putString("Mend_lng", "");
                        editor.putString("payload", String.valueOf(payload));
                        editor.apply();

                        Log.d("MYFIREBASE", "Payment Activity CAll From firebase  --->> ");
                        Intent resultIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                        resultIntent.putExtra("message", message);
                        resultIntent.putExtra("payload", String.valueOf(payload));
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);
                    } else if (main.has("status")) {
                        Log.e("status", "status");
                        if (main.getString("status").equalsIgnoreCase("user_trip_canceled")) {
                            Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);

                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
                            editor.putString("Driver_namecws", "");
                            editor.putString("phonecws", "");
                            editor.putString("cash123", "");
                            editor.putString("driver_latcws", "");
                            editor.putString("driver_lngcws", "");
                            editor.putString("trip_start_latcws", "");
                            editor.putString("trip_end_latcws", "");
                            editor.putString("trip_start_otpcws", "");
                            editor.putString("carnumbercws", "");
                            editor.putString("start_lat", "");
                            editor.putString("start_lng", "");
                            editor.putString("Mstart_lat", "");
                            editor.putString("Mstart_lng", "");

                            editor.putString("mtrip_end_lat", "");
                            editor.putString("mtrip_end_long", "");
                            editor.putString("mtrip_driver_lat", "");
                            editor.putString("mtrip_driver_lng", "");

                            editor.putString("end_lat", "");
                            editor.putString("end_lng", "");
                            editor.putString("Mend_lat", "");
                            editor.putString("Mend_lng", "");
                            editor.putString("driver_photocws", "");
                            editor.putString("car_namecws", "");
                            editor.putString("car_image_textcws", "");
                            editor.putString("finalstartlatcws", "");
                            editor.putString("finalstartlongcws", "");
                            editor.putString("finalendlatcws", "");
                            editor.putString("finalendlongcws", "");
                            editor.remove("finalcws");
                            editor.putString("message1cws", "");
                            editor.remove("isridestart");
                            editor.putString("finalBill", "");
                            editor.putString("endotpnot", "");
                            editor.putString("messagenoti", "");
                            editor.apply();
                        } else if (main.getString("status").equalsIgnoreCase("trip_canceled_from_admin")) {
                            Intent resultIntent = new Intent(getApplicationContext(), CustHomePageActivity.class);
                            resultIntent.putExtra("message", message);
                            resultIntent.putExtra("payload", String.valueOf(payload));
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);

                            Log.e(TAG, "app splash");
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
                            editor.putString("page", "cancel");
                            editor.putString("Driver_namecws", "");
                            editor.putString("phonecws", "");
                            editor.putString("cash123", "");
                            editor.putString("driver_latcws", "");
                            editor.putString("driver_lngcws", "");
                            editor.putString("trip_start_latcws", "");
                            editor.putString("trip_end_latcws", "");
                            editor.putString("trip_start_otpcws", "");
                            editor.putString("carnumbercws", "");
                            editor.putString("start_lat", "");
                            editor.putString("start_lng", "");
                            editor.putString("Mstart_lat", "");
                            editor.putString("Mstart_lng", "");

                            editor.putString("mtrip_end_lat", "");
                            editor.putString("mtrip_end_long", "");
                            editor.putString("mtrip_driver_lat", "");
                            editor.putString("mtrip_driver_lng", "");

                            editor.putString("end_lat", "");
                            editor.putString("end_lng", "");
                            editor.putString("Mend_lat", "");
                            editor.putString("Mend_lng", "");
                            editor.putString("driver_photocws", "");
                            editor.putString("car_namecws", "");
                            editor.putString("car_image_textcws", "");
                            editor.putString("finalstartlatcws", "");
                            editor.putString("finalstartlongcws", "");
                            editor.putString("finalendlatcws", "");
                            editor.putString("finalendlongcws", "");
                            editor.remove("finalcws");
                            editor.putString("message1cws", "");
                            editor.remove("isridestart");
                            editor.putString("finalBill", "no");
                            editor.putString("endotpnot", "");
                            editor.putString("messagenoti", "");
                            editor.apply();
                        } else {
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("page", "customer");
                            editor.putString("message", message);
                            editor.putString("payload", String.valueOf(payload));
                            editor.apply();

                            Log.e(TAG, "CutomerWaitingActivity 524");
                            Intent resultIntent = new Intent(getApplicationContext(), CutomerWaitingActivity.class);
                            resultIntent.putExtra("message", message);
                            resultIntent.putExtra("payload", String.valueOf(payload));
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);
                        }
                    }
                    if (title.equals("Raa cab service") || payload.getString("offer").equals("New Offer")) {
                        Log.e("else", "else");
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("page", "customer");
                        editor.putString("message", message);
                        editor.putString("payload", String.valueOf(payload));
                        editor.apply();

                        Log.e(TAG, "CutomerWaitingActivity 540");

                        Intent resultIntent = new Intent(getApplicationContext(), NotificationDisplayActivity.class);
                        resultIntent.putExtra("message", message);
                        resultIntent.putExtra("payload", String.valueOf(payload));
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);
                    } else {

                        Log.e("else", "else");
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("page", "customer");
                        editor.putString("message", message);
                        editor.putString("payload", String.valueOf(payload));
                        editor.apply();

                        Log.e(TAG, "CutomerWaitingActivity 559");

                        Intent resultIntent = new Intent(getApplicationContext(), CutomerWaitingActivity.class);
                        resultIntent.putExtra("message", message);
                        resultIntent.putExtra("payload", String.valueOf(payload));
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, driver, String.valueOf(payload), resultIntent);
                    }

                } catch (Exception e) {
                    Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                }
            }

        } catch (Exception e) {
            Toast.makeText(activity, TAG + " Exception :" + e.getMessage() + "  line : " + e.getStackTrace()[0].getLineNumber(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, int driver, String payload, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, driver, payload, intent);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, int driver, String payload, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, driver, payload, intent, imageUrl);
    }
}
