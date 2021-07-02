package com.hopetechno.raadarbar.Activity;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.BuildConfig;
import com.hopetechno.raadarbar.Dialog.CustomDialogClass;
import com.hopetechno.raadarbar.Dialog.EnterMailDialog;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Receiver.SmsReceiver;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.ConnectivityReceiver;
import com.hopetechno.raadarbar.Utils.MyApplication;
import me.philio.pinentry.PinEntryView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class OtpVerificationActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AppConstant {

    private static final String TAG = "verification";
    PinEntryView pinEntryView;
    CButton btn_verify;
    CTextView tv_resend, tv_timer, tv_mob, txtSendMail;
    String MobileNumber = "";
    private FirebaseAuth mAuth;
    private String verificationId;
    String fname = "", trip_id, logtokan, role = "", mname = "", lname = "", email = "", password;
    public static String fireregId = "";
    SharedPreferences sharedpreferences, sharedpreferences1;
    private static final int REQ_USER_CONSENT = 200;

//    private SmsOtpReceiver smsOtpReceiver;
    private Boolean network = Boolean.TRUE;
    private ConnectivityReceiver connectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp_varification);

//        smsOtpReceiver = new SmsOtpReceiver();
//        IntentFilter updateAlertIntentFilter = new IntentFilter();
//        updateAlertIntentFilter.addAction(SmsReceiver.SMSRECEIVE);
//        registerReceiver(smsOtpReceiver, updateAlertIntentFilter);

        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.addListener(this);
        this.registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        MyApplication.setCurrentActivity(this);

        mAuth = FirebaseAuth.getInstance();

        pinEntryView = findViewById(R.id.otp_value1);
        tv_resend = findViewById(R.id.tv_resend);
        tv_mob = findViewById(R.id.tv_mob);
        tv_timer = findViewById(R.id.tv_timer);
        btn_verify = findViewById(R.id.btn_verify);
        txtSendMail = findViewById(R.id.txtSendMail);

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        fireregId = prefs.getString("regId", VEFICATION);
        if (fireregId.equals("") && fireregId.trim().length() > 0) {

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.e("AAAAA", "getInstanceId failed", task.getException());
                                return;
                            }

                            fireregId = task.getResult().getToken();
                        }
                    });
        } else {
            fireregId = prefs.getString("regId", VEFICATION);
        }


        Log.e("para", "fcm :" + fireregId);

        if (!TextUtils.isEmpty(fireregId))
            sharedpreferences = getSharedPreferences("REGISTER", Context.MODE_PRIVATE);
//        if (sharedpreferences.getString("fname", "") != null && sharedpreferences.getString("fname", "").trim().length() > 0) {
//            fname = sharedpreferences.getString("fname", "");
//        }
//
//        if (sharedpreferences.getString("mname", "") != null && sharedpreferences.getString("mname", "").trim().length() > 0) {
//            mname = sharedpreferences.getString("mname", "");
//        }
//        if (sharedpreferences.getString("lname", "") != null && sharedpreferences.getString("lname", "").trim().length() > 0) {
//            lname = sharedpreferences.getString("lname", "");
//        }
//
//        email = sharedpreferences.getString("emailedit", "");
//        password = sharedpreferences.getString("pass", "");
        if (sharedpreferences != null && sharedpreferences.getString("mobile", "") != null)
            MobileNumber = sharedpreferences.getString("mobile", "");

        if (!MobileNumber.equals("")) {
            tv_mob.setText(" +91" + MobileNumber);
            btn_verify.setEnabled(true);
        } else {
            if (getIntent() != null && getIntent().getStringExtra("mobile") != null) {
                tv_mob.setText(" +91" + getIntent().getStringExtra("mobile"));
                MobileNumber = getIntent().getStringExtra("mobile");
            }
        }

        Log.e(TAG, "onCreate: fname" + fname + " mname " + mname + " lname "
                + lname + " email " + email + " password " + password + " MobileNumber " + MobileNumber);


//        tv_mob.setText(" +91" + MobileNumber);
        btn_verify.setEnabled(true);

        setCountDonw();
        startSmsUserConsent();
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(OtpVerificationActivity.this,  pinEntryView.getText().toString(), Toast.LENGTH_SHORT).show();
//
                String code = pinEntryView.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(OtpVerificationActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    btn_verify.setEnabled(false);
                    progstart(OtpVerificationActivity.this, "Loading...", "Loading...");
                    verfiyOTP(code, MobileNumber);
            }
//                apicallingchecknum();
            }
        });

        txtSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterMailDialog cdd = new EnterMailDialog(OtpVerificationActivity.this, MobileNumber);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();
            }
        });


        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String code = pinEntryView.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    //.setError("Enter code...");
                    //CEditText.requestFocus();
                    return;
                }
                verifyCode(code);*/

                setCountDonw();
                progstart(OtpVerificationActivity.this, "Loading...", "Loading...");
                call_api("+91" + MobileNumber);
//                sendVerificationCode("+91" + MobileNumber);

            }
        });
    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent("BP-Raacab").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                Toast.makeText(getApplicationContext(), message.substring(0, 5), Toast.LENGTH_LONG).show();
                pinEntryView.setText(message.substring(0, 6));
                Log.d("asdf", message.substring(0, 6));
//                textViewMessage.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));
//                getOtpFromMessage(message);
            }
        }
    }

    //    private void getOtpFromMessage(String message) {
//        // This will match any 6 digit number in the message
//        Pattern pattern = Pattern.compile("(|^)\\d{6}");
//        Matcher matcher = pattern.matcher(message);
//        if (matcher.find()) {
//            otpText.setText(matcher.group(0));
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void call_api(final String mob) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, enterphone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("asdf", "response" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                Toast.makeText(OtpVerificationActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OtpVerificationActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            progstop();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progstop();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progstop();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", mob);

                Log.e("phone", mob);

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);

    }

    private void verfiyOTP(final String code, String mobileNumber) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, phoneverify,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("asdf", "response" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
//                                Toast.makeText(OtpVerificationActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                apicallingchecknum();
                            } else {
                                Toast.makeText(OtpVerificationActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            btn_verify.setEnabled(true);

                        } catch (Exception e) {
                            progstop();
                            e.printStackTrace();
                        }
                        progstop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progstop();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", MobileNumber);
                params.put("otp", code);
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);

    }

    private void setCountDonw() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_timer.setText("Wait for OTP : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                tv_resend.setEnabled(false);
            }

            public void onFinish() {
                tv_timer.setText("Time Out");
                tv_resend.setEnabled(true);
            }

        }.start();

    }

    private void verifyCode(String code) {

        Log.e("AAAAA", "verifyCode: " + verificationId + "code" + code);
        try {

            progstart(OtpVerificationActivity.this, "Loading...", "Loading...");
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            progstop();
            Toast toast = Toast.makeText(this, "OtpVerificationActivity Code is wrong", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OtpVerificationActivity.this, "Verified", Toast.LENGTH_LONG).show();
                    try {
//                                apicallingpois();

//                        progstart(OtpVerificationActivity.this, "Loading...", "Loading...");
                        apicallingchecknum();
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }

                } else {
                    Toast.makeText(OtpVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void apicallingchecknum() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, checkphone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            progstop();
                            btn_verify.setEnabled(true);
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("success")) {

                                JSONObject success = jsonObject.getJSONObject("success");

                                if (jsonObject.getJSONObject("success").getString("msg").equalsIgnoreCase("Success")) {

                                    logtokan = success.getString("token");
                                    Log.e("para", "vriertoken : " + logtokan);
                                    role = success.getString("role");
                                    JSONObject user = success.getJSONObject("user");
                                    String id, first_name, last_name, email, email_verified_at, image, license, phone, address, pincode, city, state, birthdate, created_at, updated_at, photo_proof, driver_mode;
                                    id = user.getString("id");
                                    first_name = user.getString("first_name");
                                    last_name = user.getString("last_name");
                                    email = user.getString("email");
                                    email_verified_at = user.getString("email_verified_at");
                                    image = user.getString("image");
                                    license = user.getString("license");
                                    photo_proof = user.getString("photo_proof");
                                    phone = user.getString("phone");
                                    //driver_mode = user.getString("driver_mode");
                                    SharedPreferences sharedpreferences;
                                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tokan", logtokan);
                                    editor.putString("userid", id);
                                    editor.putString("role", role);
                                    editor.putString("first_name", first_name);
                                    editor.putString("last_name", last_name);
                                    editor.putString("email", email);
                                    editor.putString("email_verified_at", email_verified_at);
                                    editor.putString("image", image);
                                    editor.putString("license", license);
                                    editor.putString("phone", phone);
                                    editor.putBoolean("hasLoggedIn", true);
                                    editor.putString("photo_proof", photo_proof);
                                    if (role.equalsIgnoreCase("driver")) {
                                        JSONObject driver = success.getJSONObject("cars");
                                        editor.putString("car_id", driver.getString("id"));
                                        editor.putString("car_number", driver.getString("carnumber"));
                                        editor.putString("car_name", driver.getString("name"));
                                        editor.putString("fueltype", driver.getString("fueltype"));
                                        editor.putString("car_type", driver.getString("car_type"));
                                        editor.putString("raadarbar_car", driver.getString("raadarbar_car"));
                                        editor.putInt("dutymode", success.getInt("driver_mode"));
                                        Log.e("driver", driver.getString("id"));

                                    }
                                    editor.apply();

//                                main();
                                    if (logtokan.equalsIgnoreCase("")) {
                                        if ("driver".equalsIgnoreCase(role)) {
                                            trip_id = sharedpreferences.getString("trip_id", "");
                                        } else {
                                            trip_id = sharedpreferences.getString("KEY_PREF_TRIP_ID", "");
                                        }
                                        if (!trip_id.equalsIgnoreCase("")) {
                                            //for Cheking application or trip stetus
                                            if (network) {
                                                callapiforgettingstatus();
                                                Log.e(TAG," CheckStatus CAll -->>  ");

                                            } else {
                                            }
                                        } else {
                                            main();
                                        }
                                    } else {
                                        Log.e(TAG," CheckStatus CAll -->>  ");
//                                    call_api_firsttrip();
                                        new CheckStatus(OtpVerificationActivity.this, "", role).execute();
                                    }
                                } else {
                                    Intent i2 = new Intent(OtpVerificationActivity.this, RegistrationActivity.class);
                                    i2.putExtra("mobile", MobileNumber);
                                    startActivity(i2);
                                    finish();
                                }
                            } else if (jsonObject.has("error")) {

//                                if (jsonObject.has("error")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(OtpVerificationActivity.this, PhoneNumberVerifyActivity.class);
                                startActivity(i);
                                finishAffinity();
//                                }


                            }


                        } catch (Exception e) {
                            Toast.makeText(OtpVerificationActivity.this, "Exception --->> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Exception", e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());

                        progstop();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences prefsFireBase = getSharedPreferences("FireBaseToken", MODE_PRIVATE);
                if (fireregId != null && !fireregId.isEmpty())
                    params.put("fireregId", fireregId);
                else
                    params.put("fireregId", prefsFireBase.getString("regId", VEFICATION));


                params.put("phone", MobileNumber);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(OtpVerificationActivity.this);
        queue.add(postRequest);
    }

    private void call_api_firsttrip() {
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        StringRequest postRequest = new StringRequest(Request.Method.POST, firsttrip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        Log.d("asdf", "response" + response);

                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() == 0) {
                                main();
                            } else {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    int result = object.getInt("id");
                                    trip_id = String.valueOf(result);

                                    if (!trip_id.equalsIgnoreCase("")) {
                                        //for Cheking application or trip stetus
                                        if (network) {
                                            callapiforgettingstatus();
                                        } else {
                                        }
                                    } else {
                                        main();
                                    }
//                               Log.d("asdf","id" + String.valueOf(result));
//                               Log.d("asdf","trip_id" + trip_id);
                                }
                            }
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
                params.put("fireregId", prefsFireBase.getString("regId", VEFICATION));

                Log.d("Authorization", prefsFireBase.getString("regId", VEFICATION));
                Log.d("FireBaseToken", "Firebase Token -->> " + prefsFireBase.getString("regId", ""));

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);

    }


    public void callapiforgettingstatus() {

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        progstart(OtpVerificationActivity.this, "Loading...", "Please wait while fetching your trip details...");

        StringRequest postRequest = new StringRequest(Request.Method.POST, gettripstatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);

                        try {
                            JSONObject responce = new JSONObject(response);
                            JSONObject success = responce.getJSONObject("success");
                            String trip_status = success.getString("trip_status");

                            Log.e("jhzXbcvj", String.valueOf(trip_status.equalsIgnoreCase("waiting_for_driver")));
                            if (trip_status.equalsIgnoreCase("just_booked") || trip_status.equalsIgnoreCase("waiting_for_driver") || trip_status.equalsIgnoreCase("started")) {

                                SharedPreferences sharedpreferences;
                                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("isridestart", "yes");

                                String start = success.getString("start_lat") + "," + success.getString("start_long");
                                String end = success.getString("end_lat") + "," + success.getString("end_long");

                                editor.putString("startaddss", start);
                                editor.putString("endaddss", end);
                                editor.putString("trip_id", trip_id);


                                editor.putString("Driver_id", success.getString("driver_id"));
                                editor.putString("Driver_namecws", success.getString("driver_first_name"));
                                editor.putString("phonecws", success.getString("driver_phone"));
                                editor.putString("driver_latcws", success.getString("last_lat"));
                                editor.putString("driver_lngcws", success.getString("last_long"));
                                editor.putString("trip_start_latcws", success.getString("start_lat"));
                                editor.putString("trip_end_latcws", success.getString("start_long"));
                                editor.putString("trip_start_otpcws", success.getString("start_otp"));
                                editor.putString("carnumbercws", success.getString("carnumber"));
                                editor.putString("driver_photocws", success.getString("image"));
                                editor.putString("car_namecws", success.getString("carname"));
                                editor.putString("car_image_textcws", success.getString("carimage"));
                                editor.putString("trip_status", success.getString("trip_status"));
                                editor.putString("start_point", success.getString("start_point"));
                                editor.putString("end_point", success.getString("end_point"));
                                editor.putString("cust_address", success.getString("start_point"));
                                editor.putString("STRATKM", success.getString("start_km"));

                                editor.putString("cust_name_driver", success.getString("customer_first_name"));
                                editor.putString("cust_number_driver", success.getString("customer_phone"));
                                editor.putString("incity", success.getString("incity"));
                                editor.putString("roundtrip", success.getString("roundtrip"));

                                editor.apply();


                                editor.putString("startaddss", start);
                                editor.putString("endaddss", end);


                                Log.d("asdf", "Driver_namecws " + success.getString("driver_first_name"));
                                Log.d("asdf", "phonecws " + success.getString("driver_phone"));
                                Log.d("asdf", "cust_name_driver " + success.getString("customer_first_name"));
                                Log.d("asdf", "cust_number_driver " + success.getString("customer_phone"));
                                Log.d("asdf", "driver_latcws " + success.getString("last_lat"));
                                Log.d("asdf", "driver_lngcws " + success.getString("last_long"));
                                Log.d("asdf", "trip_start_latcws " + success.getString("start_lat"));
                                Log.d("asdf", "trip_end_latcws " + success.getString("start_long"));
                                Log.d("asdf", "trip_start_otpcws " + success.getString("start_otp"));
                                Log.d("asdf", "carnumbercws " + success.getString("carnumber"));
                                Log.d("asdf", "driver_photocws  " + success.getString("image"));
                                Log.d("asdf", "car_namecws  " + success.getString("carname"));
                                Log.d("asdf", "car_image_textcws   " + success.getString("carimage"));

                            } else {
                                SharedPreferences sharedpreferences;
                                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("finalBill", "");
                                editor.putString("trip_status", "");
                                editor.putString("isridestart", "");
                                editor.putString("startaddss", "");
                                editor.putString("endaddss", "");
                                editor.putString("whaitingaddress_one", "");
                                editor.putString("whaitingcustpayload", "");
                                editor.putString("cust_name_driver", "");
                                editor.putString("cust_number_driver", "");
                                editor.putString("page", "");
                                editor.putString("is_autop_tripfrompayload", "");
                                editor.putString("start_otp1frompayload", "");
                                editor.putString("message", "");
                                editor.putString("payload", "");
                                editor.putString("is_auto_endotp", "");
                                editor.remove("KEY_PREF_TRIP_ID");

                                editor.remove("trip_id");
                                editor.putString("Driver_namecws", "");
                                editor.putString("phonecws", "");
                                editor.putString("cash123", "");
                                editor.putString("driver_latcws", "");
                                editor.putString("driver_lngcws", "");
                                editor.putString("trip_start_latcws", "");
                                editor.putString("trip_end_latcws", "");
                                editor.putString("trip_start_otpcws", "");
                                editor.putString("carnumbercws", "");
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
                                editor.putString("page", "");
                                editor.putString("message", "");
                                editor.putString("payload", "");

                                editor.apply();
                            }

                            // Calling main task of Splesh screen
                            main();
                            // Progressbar stoping
                            progstop();
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
                        progstop();
                        // Calling main task of Splesh screen
                        main();
                        Toast.makeText(OtpVerificationActivity.this, getResources().getString(R.string.errormsg), Toast.LENGTH_LONG).show();
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
                params.put("trip_id", trip_id);
                Log.e("trip_id", trip_id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);
    }


    private void main() {
        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        role = sharedpreferences.getString("role", "");
        if (role.equalsIgnoreCase("user")) {

            Toast.makeText(OtpVerificationActivity.this, "Successfully Verify ", Toast.LENGTH_LONG).show();

            Log.e(TAG," main CAll -->>  ");
            Intent i = new Intent(OtpVerificationActivity.this, CustHomePageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return;
        }else{
            Toast.makeText(OtpVerificationActivity.this, "Sorry this is not driver Application", Toast.LENGTH_LONG).show();

            Log.e(TAG," main CAll not user  -->>  ");
        }
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progstop();
            verificationId = s;
            btn_verify.setEnabled(true);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            Log.e("para", "onVerificationCompleted : " + code);

            //btn_verify.setEnabled(true);

            progstop();
//            btn_verify.setEnabled(true);
            if (code != null) {
//                progstart(OtpVerificationActivity.this, "Loading...", "Loading...");
                pinEntryView.setText(code);
                btn_verify.setText("Next");

//                verifyCode(code);
            } else {

            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progstop();
            Toast.makeText(OtpVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void networkAvailable() {
        network = Boolean.TRUE;
    }

    @Override
    public void networkUnavailable() {
        network = Boolean.FALSE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityReceiver.removeListener(this);
        this.unregisterReceiver(connectivityReceiver);

//        if (smsOtpReceiver != null) {
//            unregisterReceiver(smsOtpReceiver);
//        }
    }

//    void apicallingpois() {
//
//        progstart(OtpVerificationActivity.this, "Loading...", "Loading...");
//        mname = "";
//        StringRequest postRequest = new StringRequest(Request.Method.POST, register,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            try {
//                                JSONObject success = jsonObject.getJSONObject("error");
//                                if (success.has("email")) {
//                                    JSONArray Token = success.getJSONArray("email");
//                                    Toast.makeText(OtpVerificationActivity.this, Token.get(0).toString(), Toast.LENGTH_LONG).show();
//                                }
//                                if (success.has("phone")) {
//                                    JSONArray Token1 = success.getJSONArray("phone");
//                                    Toast.makeText(OtpVerificationActivity.this, Token1.get(0).toString(), Toast.LENGTH_LONG).show();
//                                }
//                                progstop();
//                                return;
//                            } catch (Exception e) {
//                                Log.e("Exception", e.toString());
//                            }
//
//                            JSONObject success = jsonObject.getJSONObject("success");
//                            String Token = success.getString("token");
//                            String role = success.getString("role");
//                            JSONObject user = success.getJSONObject("user");
//                            String first_name, last_name, email, email_verified_at, image, license, phone, address, pincode, city, state, birthdate, created_at, updated_at, id, photo_proof;
//
//                            id = user.getString("id");
//                            first_name = user.getString("first_name");
//                            last_name = user.getString("last_name");
//                            email = user.getString("email");
//                            email_verified_at = user.getString("email_verified_at");
//                            image = user.getString("image");
//                            license = user.getString("license");
//                            photo_proof = user.getString("photo_proof");
//                            phone = user.getString("phone");
//                            SharedPreferences sharedpreferences;
//                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedpreferences.edit();
//
//                            editor.putString("tokan", Token);
//                            editor.putString("userid", id);
//                            editor.putString("role", role);
//                            editor.putString("first_name", first_name);
//                            editor.putString("last_name", last_name);
//                            editor.putString("email", email);
//                            editor.putString("email_verified_at", email_verified_at);
//                            editor.putString("image", image);
//                            editor.putString("license", license);
//                            editor.putString("phone", phone);
//                            editor.putBoolean("hasLoggedIn", true);
//                            editor.putString("photo_proof", photo_proof);
//                            editor.apply();
//
//                            Toast.makeText(OtpVerificationActivity.this, getString(R.string.succreg), Toast.LENGTH_LONG).show();
//
//                            Intent i = new Intent(OtpVerificationActivity.this, CustHomePage.class);
//                            startActivity(i);
//                            finish();
//                            progstop();
//
//                        } catch (Exception e) {
//                            Log.e("Exception", e.toString());
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("Error.Response", error.toString());
//
//                        progstop();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                //params.put("first_name", fname.getText().toString().trim() + " " + mname.getText().toString().trim() + " " + lname.getText().toString().trim());
//                //params.put("last_name", mname.getText().toString().trim() + " " + lname.getText().toString().trim());
//                params.put("first_name", fname + " " + mname + " " + lname);
//                params.put("last_name", mname + " " + lname);
//                params.put("phone", MobileNumber);
//                params.put("email", email);
//                params.put("password", password);
//                params.put("c_password", password);
//                params.put("fireregId", fireregId);
//
//                return params;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(this);
//        //requestQueue.add(jsonObjReq);
//        queue.add(postRequest);
//    }

    public class CheckStatus extends AsyncTask<String, String, String> {
        Activity activity;
        String tripId;
        String role;

        public CheckStatus(Activity activity, String tripId, String role) {
            this.activity = activity;
            this.tripId = tripId;
            this.role = role;
        }

        @Override
        protected String doInBackground(String... strings) {

            StringRequest postRequest = new StringRequest(Request.Method.POST, checkblock,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            try {
                                if (!response.isEmpty() && response.trim().equals("0")) {
                                    call_api_firsttrip();

                                } else {
                                    CustomDialogClass cdd = new CustomDialogClass(activity, "", "");
                                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    cdd.show();
                                }


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
                    Log.d("Authorization", "Bearer " + logtokan);

                    return params;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    String versionCode = BuildConfig.VERSION_NAME;
                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;
                    String modelname = manufacturer + " " + model;
                    String osversion = Build.VERSION.RELEASE;

                    PackageInfo packageInfo = null;
                    try {
                        packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    String version_name = packageInfo.versionName;


                    params.put("m_model", modelname);
                    params.put("m_os", osversion);
                    params.put("m_version", versionCode);

                    Log.e(TAG, "CheckStatus  param --->>>> " + new Gson().toJson(params));

                    return params;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
            queue.add(postRequest);
            return null;
        }
    }

//    public class SmsOtpReceiver extends BroadcastReceiver {
//        private final String TAG = SmsReceiver.class.getSimpleName();
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Log.e(TAG, " SmsOtpReceiver --->>>> " + intent.getExtras().getString("SMSRECEIVE"));
//
//
//            if(intent != null && intent.getStringExtra("SMSRECEIVE")!= null){
//                pinEntryView.setText(intent.getStringExtra("SMSRECEIVE").substring(0, 6));
//            }
//
//        }
//    }
}
