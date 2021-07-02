package com.hopetechno.raadarbar.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.*;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CEditText;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Modal.CancelRideModel;
import com.hopetechno.raadarbar.Notification.NotificationUtils;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Service.LocationTrack;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.DataParser;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class CutomerWaitingActivity extends FragmentActivity implements AppConstant, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    LocationTrack locationTrack;
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LatLng origin, dest;
    double Latitudedefoult = 0, Longitudedefoult = 0;
    float getbearing;
    CTextView time, check, carnumbertext, distancetextview, txtTagline, Driver_name_text, driver_phone, otp, carname;
    String carnumber = "", Driver_id = "", payload = "", message = "", Driver_name = "", startkm = "", phone = "", driver_lat = "", driver_lng = "", trip_start_lat = "", trip_end_lat = "", trip_start_otp = "", driver_photo = "", car_name = "", car_image_text = "";
    CircleImageView drivar_image, car_image;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    LinearLayout cancel_layout, carcoming;
    boolean frompayload = false, finalcws = false;
    String finalstartlat = "", finalstartlong = "", finalendlat = "", finalendlong = "", message1cws = "", endotpnot = "";
    CButton cancel_button;
    CountDownTimer timer;
    ImageView big_image;
    CountDownTimer countDownTimer;
    CEditText edtremark;
    ArrayList<CancelRideModel> cancelridereasonlist;
    Dialog dialog, subdialog;
    String reasonid, reasontext = "", logtokan = "", durationText;
    public static int i = 0;
    Marker car_marker;
    boolean isMarkerRotating = false;
    BitmapDescriptor icon;
    BitmapDescriptor icon1;
    Timer t;

    String TAG = "CustomerWatingSC";

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_waiting);
        MapsInitializer.initialize(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        /*cancelridereasonlist = new ArrayList<>();

        cancelridereasonlist.add(new CancelRideModel("1", "reason1"));
        cancelridereasonlist.add(new CancelRideModel("2", "reason2"));
        cancelridereasonlist.add(new CancelRideModel("3", "reason3"));
        cancelridereasonlist.add(new CancelRideModel("4", "other"));*/


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        carcoming = findViewById(R.id.carcoming);
        txtTagline = findViewById(R.id.txtTagline);
        distancetextview = findViewById(R.id.textViewwait);
        Driver_name_text = findViewById(R.id.textView2);
        driver_phone = findViewById(R.id.textView3);
        otp = findViewById(R.id.textView4);
        big_image = findViewById(R.id.big_image);
        cancel_layout = findViewById(R.id.cancel_layout);
        drivar_image = findViewById(R.id.drivar_image);
        carnumbertext = findViewById(R.id.textView5);
        carname = findViewById(R.id.textView6);
        cancel_button = findViewById(R.id.cancel_button);
        time = findViewById(R.id.time);
        car_image = findViewById(R.id.car_image);
        check = findViewById(R.id.check);

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);

        SharedPreferences sharedpreferences1;
        sharedpreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
        i = 0;
        try {
            String isridestart = prefs.getString("isridestart", "");
            if (isridestart.equalsIgnoreCase("yes")) {
                Driver_name = sharedpreferences1.getString("Driver_namecws", Driver_name);
                Driver_id = sharedpreferences1.getString("Driver_id", Driver_id);
                phone = sharedpreferences1.getString("phonecws", phone);
                driver_lat = sharedpreferences1.getString("driver_latcws", driver_lat);
                driver_lng = sharedpreferences1.getString("driver_lngcws", driver_lng);
                trip_start_lat = sharedpreferences1.getString("trip_start_latcws", trip_start_lat);
                trip_end_lat = sharedpreferences1.getString("trip_end_latcws", trip_end_lat);
                trip_start_otp = sharedpreferences1.getString("trip_start_otpcws", trip_start_otp);
                carnumber = sharedpreferences1.getString("carnumbercws", carnumber);


                driver_photo = sharedpreferences1.getString("driver_photocws", driver_photo);
                car_name = sharedpreferences1.getString("car_namecws", car_name);
                car_image_text = sharedpreferences1.getString("car_image_textcws", car_image_text);
                finalstartlat = sharedpreferences1.getString("finalstartlatcws", finalstartlat);
                finalstartlong = sharedpreferences1.getString("finalstartlongcws", finalstartlong);
                finalendlat = sharedpreferences1.getString("finalendlatcws", finalendlat);
                finalendlong = sharedpreferences1.getString("finalendlongcws", finalendlong);
//                finalcws = sharedpreferences1.getBoolean("finalcws", finalcws);
                message1cws = sharedpreferences1.getString("message1cws", message1cws);
                startkm = sharedpreferences1.getString("STRATKM", startkm);

                String status = sharedpreferences1.getString("trip_status", "");

                Driver_name_text.setText(Html.fromHtml("Driver: <b>" + Driver_name));
                driver_phone.setText(Html.fromHtml("Phone: <b>" + phone));
                carnumbertext.setText(Html.fromHtml("Car number: <b>" + carnumber));
                carname.setText(Html.fromHtml("<b>" + car_name));

                Log.e("DriverPhoto","  Driver Photo  : "+driver_photo);
                UrlImageViewHelper.setUrlDrawable(drivar_image, driver_photo, R.drawable.raalogo);
                UrlImageViewHelper.setUrlDrawable(car_image, car_image_text, R.drawable.raalogo);

                Log.d("asdf c1", status + "   " + startkm);

                SharedPreferences loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);

                String mtrip_end_lat = loginPref.getString("mtrip_end_lat", "");
                String mtrip_end_long = loginPref.getString("mtrip_end_long", "");
                String mtrip_driver_lat = loginPref.getString("mtrip_driver_lat","");
                String mtrip_driver_lng = loginPref.getString("mtrip_driver_lng","");

                Log.e(TAG, " settingmap -->>> 195 " + mtrip_end_lat + " " + mtrip_end_long);
                settingmap(mtrip_end_lat + "," + mtrip_end_long,mtrip_driver_lat+","+mtrip_driver_lng);

            } else {
                Log.e("cust else", "cust else");
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }

        t = new Timer();
        // Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
//                                      Toast.makeText( CutomerWaitingActivity.this, "", Toast.LENGTH_SHORT).show();
                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                      Driver_id = getSharedPreferences("Login", MODE_PRIVATE).getString("Driver_id", "");
                                      if (i == 0) {
                                          getcarlatlong(Driver_id);
                                          get_trip_status();
                                      }
                                  }

                              },
                // Set how long before to start calling the TimerTask(in milliseconds)
                1000,
                // Set the amount of time between each execution(in milliseconds)
                20000);

        try {

            Intent i = getIntent();
            payload = i.getStringExtra("payload");
            message = i.getStringExtra("message");
            Log.e(TAG, "Message  : " + message);
            Log.e(TAG, "payload  : asdf " + payload);

            try {
                JSONObject main = new JSONObject(payload);
                // For forcefully finished from Admiministrator

                Log.e(TAG, "Message Json : " + main.toString());


                if (main.has("trip_status") && main.getString("trip_status").equals("car_is_available")) {
                    SharedPreferences loginPref2 = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor loginEditor2 = loginPref2.edit();

                    loginEditor2.putString("mtrip_end_lat", main.getString("trip_start_lat"));
                    loginEditor2.putString("mtrip_end_long", main.getString("trip_end_long"));
                    loginEditor2.putString("mtrip_driver_lat", main.getString("driver_lat"));
                    loginEditor2.putString("mtrip_driver_lng", main.getString("driver_lng"));

                    Log.e(TAG, "BroadCast Reciever Fire : =====>>> " +main.getString("trip_start_lat") + " " + main.getString("trip_end_long")+ " " + main.getString("driver_lat") + " " +  main.getString("driver_lng"));
                    loginEditor2.apply();
                }
            }catch (Exception e ) {
                Log.d(TAG, "Exception -->>  test " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }

            startTimer();

        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }

        try {
            if (message.contains("Driver not Accepted this Ride")) {
                Intent i = new Intent( CutomerWaitingActivity.this, SelectedPakegeShowActivity.class);
                startActivity(i);
                finish();
                return;
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }

        check.setVisibility(View.GONE);

        try {
            JSONObject payload1 = new JSONObject(payload);
            Driver_id = payload1.getString("driver_id");
            Driver_name = payload1.getString("driver_name");
            phone = payload1.getString("phone");
            driver_lat = payload1.getString("driver_lat");
            driver_lng = payload1.getString("driver_lng");
            trip_start_lat = payload1.getString("trip_start_lat");
            trip_end_lat = payload1.getString("trip_end_long");
            trip_start_otp = payload1.getString("trip_start_otp");
            carnumber = payload1.getString("car_number");
            driver_photo = payload1.getString("driver_photo");
            car_name = payload1.getString("car_name");
            car_image_text = payload1.getString("car_photo");

            SharedPreferences loginPref;
            loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor loginEditor = loginPref.edit();
            loginEditor.putString(KEY_PREF_TRIP_ID, payload1.getString("trip_id"));
            //loginEditor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis());

            loginEditor.putString("trip_start_lat", trip_start_lat);
            loginEditor.putString("trip_start_long", trip_end_lat);

            loginEditor.apply();
            if (payload1.has("trip_status")) {
                String ride_is_started = payload1.getString("trip_status");
                if (ride_is_started.equalsIgnoreCase("ride_is_started")) {
                    loginEditor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
                    loginEditor.apply();
                    //startTimer();
                    cancel_layout.setVisibility(View.GONE);
                    Log.d(TAG, "Layout invisible ---->>>> 328");
                }
            }
            try {
                if (payload1.has("trip_status")) {
                    String status = payload1.getString("trip_status");
                    if (status.equalsIgnoreCase("car_not_available")) {
                        Intent i = new Intent( CutomerWaitingActivity.this, CustHomePageActivity.class);
                        startActivity(i);
                        finish();
                        return;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }


        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        // for Cancel Ride from user side
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelridereasonlist = new ArrayList<>();

                resonForCancelride();

                dialog = new Dialog( CutomerWaitingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                dialog.setContentView(R.layout.cancelridedialog);

                edtremark = dialog.findViewById(R.id.edtremark);
                edtremark.setFocusableInTouchMode(false);
                CTextView textyes, textno;

                textyes = dialog.findViewById(R.id.textyes);
                textno = dialog.findViewById(R.id.textno);

                edtremark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (cancelridereasonlist != null && cancelridereasonlist.size() > 0) {

                            if (reasontext.trim().length() > 0 && reasontext.equalsIgnoreCase("Other")) {
                                edtremark.setFocusableInTouchMode(true);
                            } else {
                                edtremark.setFocusableInTouchMode(false);
                                showCustomPopup();
                            }
                        } else {
                            edtremark.setFocusableInTouchMode(true);
                        }

                    }
                });

                textyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String remark = edtremark.getText().toString().trim();

                        if (reasontext.trim().length() > 0 && reasontext.equalsIgnoreCase("Other")) {
                            reasontext = edtremark.getText().toString().trim();
                        } else {
                            reasontext = "";
                        }

                        if (remark.length() == 0) {
                            edtremark.setError("Please add remark");
                            edtremark.setFocusable(true);
                        } else {
                            callCancelApi();
                        }
                    }
                });
                textno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });
                dialog.show();
                /*AlertDialog.Builder builder = new AlertDialog.Builder( CutomerWaitingActivity.this);

                builder.setTitle(getApplicationContext().getString(R.string.sure));
                builder.setMessage(getApplicationContext().getString(R.string.cancel_ride_confrim));
                //Setting message manually and performing action on button click
                builder.setCancelable(true)
                        .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                callCancelApi();
                            }
                        })
                        .setNegativeButton(getApplicationContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/
            }
        });


        //for the map
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        MarkerPoints = new ArrayList<>();

        //for Confirm KM with Driver
        //check.setText("sdsdsd");
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                cancel_layout.setVisibility(View.GONE);

                SharedPreferences loginPref;
                loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor loginEditor = loginPref.edit();
                loginEditor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
                loginEditor.apply();

                String message1 = intent.getStringExtra("message");
                String payload = intent.getStringExtra("payload");

                Log.d(TAG, "  ---->>>> message1 " + message1);
                Log.d(TAG, "  ---->>>> payload " + payload);

                try {
                    JSONObject main = new JSONObject(payload);
                    // For forcefully finished from Admiministrator

                    SharedPreferences loginPref2 = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor loginEditor2 = loginPref2.edit();

                    if(main.has("trip_status") && main.getString("trip_status").equals("car_is_available")) {

                        loginEditor2.putString("mtrip_end_lat", main.getString("trip_end_lat"));
                        loginEditor2.putString("mtrip_end_long", main.getString("trip_end_long"));
                        loginEditor2.putString("mtrip_driver_lat", main.getString("driver_lat"));
                        loginEditor2.putString("mtrip_driver_lng", main.getString("driver_lng"));
                        Log.e(TAG, "BroadCast Reciever Fire : =====>>> " + main.getString("trip_end_lat") + " " + main.getString("trip_end_long") + " " + main.getString("driver_lat") + " " + main.getString("driver_lng"));
                        loginEditor2.apply();
                    }

                    if (main.has("status")) {
                        String status = main.getString("status");

                        if (status.equalsIgnoreCase("just_booked")) {
                            i = 0;
                        }

                        if (status.equalsIgnoreCase("trip_canceled_from_admin")) {

                            progstart( CutomerWaitingActivity.this, "Remove Data", "Forcefully finished from Administrator please wait...");
                            String message321 = main.getString("message");

                            Log.d(TAG, " Remove Data  " + message321);
                            distancetextview.setText(message321);

                            loginEditor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
                            loginEditor.putString("Driver_namecws", "");
                            loginEditor.putString("phonecws", "");
                            loginEditor.putString("cash123", "");
                            loginEditor.putString("driver_latcws", "");
                            loginEditor.putString("driver_lngcws", "");
                            loginEditor.putString("trip_start_latcws", "");
                            loginEditor.putString("trip_end_latcws", "");
                            loginEditor.putString("trip_start_otpcws", "");
                            loginEditor.putString("carnumbercws", "");
                            loginEditor.putString("driver_photocws", "");
                            loginEditor.putString("car_namecws", "");
                            loginEditor.putString("car_image_textcws", "");
                            loginEditor.putString("finalstartlatcws", "");
                            loginEditor.putString("finalstartlongcws", "");
                            loginEditor.putString("finalendlatcws", "");
                            loginEditor.putString("finalendlongcws", "");
                            loginEditor.remove("finalcws");
                            loginEditor.putString("message1cws", "");
                            loginEditor.remove("isridestart");
                            loginEditor.putString("finalBill", "");
                            loginEditor.putString("endotpnot", "");
                            loginEditor.putString("messagenoti", "");
                            loginEditor.putString("page", "");
                            loginEditor.putString("message", "");
                            loginEditor.putString("payload", "");
                            loginEditor.apply();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progstop();
                                    Intent i = new Intent( CutomerWaitingActivity.this, CustHomePageActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }, 3000);

                            return;
                        }
                    }
                    else if (main.has("trip_status")) {




                        if (main.getString("trip_status").equals("ride_is_started")) {

                            i = 1;

                            Log.e(TAG, "BroadCast Reciever Fire : =====>>> " + main.getString("trip_end_lat") + " : " + main.getString("trip_end_long"));

                            loginEditor2.putString("mtrip_end_lat", main.getString("trip_end_lat"));
                            loginEditor2.putString("mtrip_end_long", main.getString("trip_end_long"));
                            loginEditor2.putString("mtrip_driver_lat", main.getString("driver_lat"));
                            loginEditor2.putString("mtrip_driver_lng", main.getString("driver_lng"));

                            loginEditor2.apply();

                            Log.e(TAG, "BroadCast Reciever Fire : =====>>> " + loginPref2.getString("mtrip_end_lat", "") + " : " + loginPref2.getString("mtrip_end_long", ""));


                        }else if(main.getString("trip_status").equals("car_is_available")){



                        }

                    }
                } catch (
                        Exception e) {
                    Log.d(TAG, "Exception -->> 557 " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                }
                try {
                    newpopup(message1, payload);
                } catch (
                        Exception e) {
                    Log.d(TAG, "Exception -->>  " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                }

            }
        }

        ;


        sharedpreferences =

                getSharedPreferences("Login", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("end_address");
        editor.remove("start_address");
        editor.remove("flegincityoroutcitypau");
        editor.remove("cartypepau");
        editor.putString("isridestart", "yes");
        editor.putString("Driver_id", Driver_id);
        editor.putString("favriteiconchangesorce", "");
        editor.putString("favriteiconchangedest", "");
        editor.apply();

        Driver_name_text.setText(Html.fromHtml("Driver: <b>" + Driver_name));
        driver_phone.setText(Html.fromHtml("Phone: <b>" + phone));

        Driver_name_text.setText(Html.fromHtml("Driver: <b>" + Driver_name));
        driver_phone.setText(Html.fromHtml("Phone: <b>" + phone));

        driver_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(callIntent);
                }
            }
        });
        try {
            otp.setText("OTP: " + trip_start_otp);
            carnumbertext.setText(Html.fromHtml("Car number: <b>" + carnumber));
            carname.setText(Html.fromHtml("<b>" + car_name));
            UrlImageViewHelper.setUrlDrawable(drivar_image, driver_photo, R.drawable.raalogo);
            UrlImageViewHelper.setUrlDrawable(car_image, car_image_text, R.drawable.raalogo);
        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }

        if (finalcws) {
            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    //here you can have your logic to set text to edittext

                }

                public void onFinish() {
                    try {
                        //otp.setVisibility(View.GONE);
                        distancetextview.setText(message1cws);
                        frompayload = true;
                        SharedPreferences loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);

                        String mtrip_end_lat = loginPref.getString("mtrip_end_lat", "");
                        String mtrip_end_long = loginPref.getString("mtrip_end_long", "");
                        String mtrip_driver_lat = loginPref.getString("mtrip_driver_lat","");
                        String mtrip_driver_lng = loginPref.getString("mtrip_driver_lng","");

                        Log.e(TAG, " settingmap -->>> 646 " + mtrip_end_lat + " " + mtrip_end_long);
                        settingmap(mtrip_end_lat + "," + mtrip_end_long,mtrip_driver_lat+","+mtrip_driver_lng);
                    } catch (Exception e) {
                        Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                    }
                }
            }.start();
        }
        try {
            String endnotifrom = sharedpreferences.getString("endotpnot", "");
            String messagenotifrom = sharedpreferences.getString("messagenoti", message);
            Log.e(TAG, " endnotifrom   endnotifrom " + endnotifrom);

            if (!endnotifrom.equalsIgnoreCase("")) {
                otp.setVisibility(View.GONE);
                otp.setText("End OTP :" + endnotifrom);
                distancetextview.setText(messagenotifrom);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }


        timer = new

                CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        big_image.setVisibility(View.GONE);
                    }

                }

        ;
        car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                big_image.setVisibility(View.VISIBLE);
                car_image_text = car_image_text.replace("thumbnail/", "");
                UrlImageViewHelper.setUrlDrawable(big_image, car_image_text, R.drawable.raalogo);

                timer.cancel();
                timer.start();
            }
        });
        drivar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driver_photo = driver_photo.replace("thumbnail/", "");
                big_image.setVisibility(View.VISIBLE);
                UrlImageViewHelper.setUrlDrawable(big_image, driver_photo, R.drawable.raalogo);
                timer.cancel();
                timer.start();

            }
        });


    }

    private void get_trip_status() {
        final SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        StringRequest request = new StringRequest(Request.Method.POST, tripstatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "trip status " + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject object1 = object.getJSONObject("success");
                        if (object1.getString("trip_status").equalsIgnoreCase("canceled")) {
                            Intent intent1 = new Intent( CutomerWaitingActivity.this, CustHomePageActivity.class);
                            startActivity(intent1);
                            finish();
                            if (t != null) {
                                t.cancel();
                                t = null;
                            }
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                    }
                    progstop();
                } catch (Exception e) {
                    Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", " error is  " + error);
                progstop();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("trip_id", prefs.getString(KEY_PREF_TRIP_ID, "0"));

                Log.e("trip_id", prefs.getString(KEY_PREF_TRIP_ID, "0"));

                return params;
            }
        };
        if (queue == null) {
            queue = Volley.newRequestQueue( CutomerWaitingActivity.this);
            queue.add(request);
        } else if (queue != null) {
            queue.add(request);
        }
    }

    private void resonForCancelride() {

        final SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, resonForCancelride, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("cancelride", "response: " + new Gson().toJson(response));

                try {
                    JSONObject responsJson = new JSONObject(response);
                    JSONObject success = responsJson.getJSONObject("success");
                    JSONArray array = success.getJSONArray("reason");


                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        String id = object.getString("id");
                        String reason = object.getString("reason");

                        cancelridereasonlist.add(new CancelRideModel(id, reason));

                    }


                } catch (JSONException e) {
                    Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                    Toast.makeText(getApplicationContext(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
                }// Do something with response string
                progstop();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                        Log.e("Error", error.toString());
                        error.printStackTrace();
                        progstop();
                    }
                }
        ) {
            /*@Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trip_id", sharedpreferences.getString(KEY_PREF_TRIP_ID, "0"));
                params.put("reason", reasonid);
                params.put("other", reasontext);
                Log.e("Parm", params.toString());

                Log.e("cancelride", "getParams: " + new Gson().toJson(params));
                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("Authorization", "Bearer " + sharedpreferences.getString("tokan", "0"));
                params.put("Authorization", "Bearer " + sharedpreferences.getString("tokan", "0"));
                return params;
            }
        };
        if (queue == null) {
            queue = Volley.newRequestQueue( CutomerWaitingActivity.this);
            queue.add(stringRequest);
        } else if (queue != null) {
            queue.add(stringRequest);
        }

    }

    private void showCustomPopup() {

       /* LayoutInflater layoutInflater = (LayoutInflater)  CutomerWaitingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.cancelride_popup, null);*/

        subdialog = new Dialog( CutomerWaitingActivity.this);
        subdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        subdialog.setCancelable(true);
        subdialog.setContentView(R.layout.cancelride_popup);

        /*View customView = LayoutInflater.from( CutomerWaitingActivity.this).inflate(R.layout.cancelride_popup, null);
        PopupWindow popupView = new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);*/

        RecyclerView recyclerView = subdialog.findViewById(R.id.rvCancelrideReason);
        recyclerView.setLayoutManager(new LinearLayoutManager( CutomerWaitingActivity.this, LinearLayoutManager.VERTICAL, false));

        CancelRideReasonAdaptor adapter = new CancelRideReasonAdaptor( CutomerWaitingActivity.this, cancelridereasonlist);
        recyclerView.setAdapter(adapter);
        subdialog.show();

    }

    @Override
    protected void onPause() {

        super.onPause();
        try {
            SharedPreferences sharedpreferences;
            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Driver_namecws", Driver_name);
            editor.putString("phonecws", phone);
            editor.putString("driver_latcws", driver_lat);
            editor.putString("driver_lngcws", driver_lng);
            editor.putString("trip_start_latcws", trip_start_lat);
            editor.putString("trip_end_latcws", trip_end_lat);
            editor.putString("trip_start_otpcws", trip_start_otp);
            editor.putString("carnumbercws", carnumber);
            editor.putString("driver_photocws", driver_photo);
            editor.putString("car_namecws", car_name);
            editor.putString("car_image_textcws", car_image_text);
            editor.putString("finalstartlatcws", finalstartlat);
            editor.putString("finalstartlongcws", finalstartlong);
            editor.putString("finalendlatcws", finalendlat);
            editor.putString("finalendlongcws", finalendlong);
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        startTimer();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String page = sharedpreferences.getString("page", "");
        String message = sharedpreferences.getString("message", "");
        String payload = sharedpreferences.getString("payload", "");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("page", "");
        editor.putString("message", "");
        editor.putString("payload", "");
        editor.apply();

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        String cash123 = prefs.getString("cash123", "");
        if (cash123.equalsIgnoreCase("yes")) {

            Log.e("cash flag", cash123 + "payment");
//            Toast.makeText( CutomerWaitingActivity.this, "Payment 1", Toast.LENGTH_SHORT).show();
            Intent i = new Intent( CutomerWaitingActivity.this, PaymentActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        try {
            Log.e("payload", "payload     cancel  " + payload);
            JSONObject main = new JSONObject(payload);
            if (main.has("message")) {
                String status = main.getString("message");
                if (status.equalsIgnoreCase("Trip finished Success")) {

                    Log.e("FINAL NOTIFICATION", payload);


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

//                    startActivity(new Intent( CutomerWaitingActivity.this, thanksscreen.class));
//                    finish();
                    return;
                }
                if (status.equalsIgnoreCase("trip_canceled_from_admin")) {

                    progstart( CutomerWaitingActivity.this, "Remove Data", "Forcefully finished from Administrator please wait...");
                    String message321 = main.getString("message");

                    Log.d("asdf 55  ", message321);
                    distancetextview.setText(message321);


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

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progstop();
                            Intent i = new Intent( CutomerWaitingActivity.this, CustHomePageActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 3000);

                    return;
                }


            }/* else {
                Log.e("Data","Got it");
                Intent i = new Intent( CutomerWaitingActivity.this, PaymentActivity.class);
                startActivity(i);
                finish();
            }*/

        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }


        try {
            Log.e(message, "RESUME  " + payload);

//            newpopup(message, payload);
        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        try {
//            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
//            if (!success) {
//                Log.i("Map:Style", "Style parsing failed.");
//            } else {
//                Log.i("Map:Style", "Style Applied.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.i("Map:Style", "Can't find style. Error: ");
//        }
        mMap = googleMap;

        locationTrack = new LocationTrack( CutomerWaitingActivity.this);
        if (locationTrack.canGetLocation()) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
//            locationTrack.showSettingsAlert();
        }
// position on right bottom

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        /*locationChecker(mGoogleApiClient, );*/
        mGoogleApiClient.connect();

    }

    public void settingmap(String latlon1, String latlon2) {

        if (!latlon1.isEmpty() && !latlon2.isEmpty() && !latlon1.equals("0.0,0.0") && !latlon2.equals("0.0,0.0") && !latlon1.equals("00.00,00.00") && !latlon2.equals("00.00,00.00")) {


            MarkerPoints.clear();
            mMap.clear();

            BitmapDescriptor icon;
            BitmapDescriptor icon1;

            Log.e(TAG, "Strin is Start : " + i);

//            if ( CutomerWaitingActivity.i == 0)
//                icon1 = BitmapDescriptorFactory.fromResource(R.drawable.start);
//            else
                icon1 = BitmapDescriptorFactory.fromResource(R.drawable.end);

//            if ( CutomerWaitingActivity.i == 0)
//                icon = BitmapDescriptorFactory.fromResource(R.drawable.car_map);
//            else
                icon = BitmapDescriptorFactory.fromResource(R.drawable.start);

            SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("cust_start_driver", latlon1);
            editor.putString("cust_end_driver", latlon2);
            editor.apply();


            String[] latlong1 = latlon1.split(",");
            final double latitude1 = Double.parseDouble(latlong1[0]);
            final double longitude1 = Double.parseDouble(latlong1[1]);
            origin = new LatLng(latitude1, longitude1);

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            String address = "Start Ride";
            try {
                addresses = geocoder.getFromLocation(latitude1, longitude1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                address = addresses.get(0).getAddressLine(0);
//                mMap.addMarker(new MarkerOptions().position(origin).title(address).icon(icon));

                if (addresses != null && addresses.get(0) != null && addresses.get(0).getAddressLine(0) != null) {
                    address = addresses.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(origin).title(address).icon(icon));
                } else {
                    mMap.addMarker(new MarkerOptions().position(origin).title("Start Ride ").icon(icon));
                }

                //.title("Start Ride ").icon(icon));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.2f));
                String[] latlong2 = latlon2.split(",");
                final double latitude2 = Double.parseDouble(latlong2[0]);
                final double longitude2 = Double.parseDouble(latlong2[1]);
                dest = new LatLng(latitude2, longitude2);
                mMap.addMarker(new MarkerOptions().position(dest)
                        .title("End Ride").icon(icon1));
                String destingation = mMap.getCameraPosition().toString();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
                MarkerPoints.add(origin);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(dest);
                LatLngBounds bounds = builder.build();
                int padding = 200; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);

            } catch (IOException e) {
                Log.e(TAG, " IOException  SettingMap : " + e.getMessage() + " Line : " + e.getStackTrace()[0].getLineNumber());
            }
        }

//
//
////        if (i == 0 && i == 1) {
////            mMap.getUiSettings().setZoomControlsEnabled(false);
////        }
//        try {
//            MarkerPoints.clear();
//            mMap.clear();
//        } catch (Exception e) {
//            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
//        }
////        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.you);
//        try {
////            if (frompayload) {
//
//            String[] latlong1 = latlon1.split(",");
//            double latitude1 = Double.parseDouble(latlong1[0]);
//            double longitude1 = Double.parseDouble(latlong1[1]);
//            origin = new LatLng(latitude1, longitude1);
//
////            } else {
////
////                origin = new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_lng));
////            }
//        } catch (Exception e) {
//            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
//        }
//
//        if (i == 1) {
//            icon1 = BitmapDescriptorFactory.fromResource(R.drawable.start);
//            mMap.addMarker(new MarkerOptions().position(origin)
//                    .title("Start Ride ").icon(icon1));
//        } else if (i == 0) {
//            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.start);
//            car_marker = mMap.addMarker(new MarkerOptions().position(origin)
//                    .title("Start Ride ").icon(icon));
//        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//
//        String[] latlong2 = latlon2.split(",");
//        double latitude2 = Double.parseDouble(latlong2[0]);
//        double longitude2 = Double.parseDouble(latlong2[1]);
//        dest = new LatLng(latitude2, longitude2);
//
//        if (i == 1) {
//            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.end);
//            car_marker = mMap.addMarker(new MarkerOptions().position(dest)
//                    .title("End Ride").icon(icon));
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(dest));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//        } else if (i == 0) {
//            icon1 = BitmapDescriptorFactory.fromResource(R.drawable.end);
//            mMap.addMarker(new MarkerOptions().position(dest)
//                    .title("End Ride").icon(icon1));
//        }
//        CameraUpdate center = null;
//        if (i == 0) {
//            center = CameraUpdateFactory.newLatLng(origin);
//        } else if (i == 1) {
//            center = CameraUpdateFactory.newLatLng(dest);
//        }
//
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
//
//        mMap.moveCamera(center);
//        mMap.animateCamera(zoom);
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//        MarkerPoints.add(origin);
//        MarkerAnimation.animateMarkerToHC(car_marker, dest, new LatLngInterpolator.Spherical());
//        car_marker.setRotation(getBearing(dest, origin));
//        double rot = 0;
//        if (i == 1) {
//            rot = bearingBetweenLocations(dest, origin);
//        } else if (i == 0) {
//            rot = bearingBetweenLocations(origin, dest);
//        }
//
//        rotateMarker(car_marker, (float) rot);
//        Log.e("locationAddress2", String.valueOf(origin) + "  Testing    " + String.valueOf(dest));

        // Getting URL to the Google Directions API
//        String url = getUrl(origin, dest);
//        Log.d("onMapClick", url.toString());
//        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
//        FetchUrl.execute(url);
        //move map camera


        //for shwoing path
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        builder.include(dest);
//        builder.include(dest);
//
//        LatLngBounds bounds = builder.build();
//
//        int padding = 50; // offset from edges of the map in pixels
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //mMap.moveCamera(cu);
//        mMap.animateCamera(cu);

    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

//                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

//    private String getUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//
//        // Sensor enabled
//        String sensor = "sensor=true&key=" + getResources().getString(R.string.google_maps_key);
//
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
////        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
////        Log.e("UR$L T$EST", url);
//
//        return url;
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0);


            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.2f));

            Latitudedefoult = location.getLatitude();
            Longitudedefoult = location.getLongitude();

            getbearing = mLastLocation.getBearing();
            /* editText1.setText(address);*/
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            // BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.you));
            //  mCurrLocationMarker = mMap.addMarker(markerOptions);
            try {

                SharedPreferences loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);

                String mtrip_end_lat = loginPref.getString("mtrip_end_lat", "");
                String mtrip_end_long = loginPref.getString("mtrip_end_long", "");
                String mtrip_driver_lat = loginPref.getString("mtrip_driver_lat","");
                String mtrip_driver_lng = loginPref.getString("mtrip_driver_lng","");

                Log.e(TAG, " settingmap -->>> 1489 " + mtrip_end_lat + " " + mtrip_end_long);
                settingmap(mtrip_end_lat + "," + mtrip_end_long,mtrip_driver_lat+","+mtrip_driver_lng);
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
//                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
//            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
//        @SuppressLint("WrongThread")
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask ", "Executing routes");
                Log.d("ParserTask", routes.toString());
//                jObject = new JSONObject(jsonData[1]);

                JSONObject jsonObject = new JSONObject(jsonData[0]);

                JSONArray routes2 = jsonObject.getJSONArray("routes");

                JSONObject routes1 = routes2.getJSONObject(0);

                JSONArray legs = routes1.getJSONArray("legs");
                Log.e("legs", legs.toString());

                JSONObject legs1 = legs.getJSONObject(0);

                JSONObject distance = legs1.getJSONObject("distance");

                JSONObject duration = legs1.getJSONObject("duration");

                String distanceText = distance.getString("text");
                String distancevalue = distance.getString("value");

                durationText = duration.getString("text");
                String durationvalue = duration.getString("value");
                //+  Toast.makeText(CustHomePage.this, distanceText+"   "+durationText, Toast.LENGTH_LONG).show();
                Log.e("distanceText", distancevalue + "   " + durationvalue);

                // distancetextview.setText(distanceText);
//                if (!frompayload) {

//                }
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            try {
                // Traversing through all the routes
                if (i == 0) {
                    Log.d("asdf", "    3");
//                    distancetextview.setText("Car is Coming in " + durationText);
                    txtTagline.setVisibility(View.GONE);
                } else if (i == 1) {
                    Log.d("asdf", "    4   " + startkm);

                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    String message = sharedpreferences.getString("message1cws", "");

                    Log.d("asdff", message);
//          distancetextview.setHeight(60);
                    txtTagline.setVisibility(View.VISIBLE);
                    //   txtTagline.setText("start kilometer is: 2020202020");
                    txtTagline.setText(message);
//                    txtTagline.setText("Start kilometer is: " + startkm + " km.confirm with Driver.");
//                    txtTagline.setText("Start kilometer is: 1234567 km. Confirm with Driver.");
//                    distancetextview.setText("Coming time is : " + durationText);
//                    distancetextview.setSingleLine(false);

                }
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(6);
                    lineOptions.color(Color.parseColor(getResources().getString(R.color.appred)));

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    mMap.addPolyline(lineOptions);

                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }
        }
    }

    public void startTimer() {
        SharedPreferences loginPref;
        loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final SharedPreferences.Editor loginEditor = loginPref.edit();

        long savedTimeStamp = loginPref.getLong(KEY_PREF_CANCEL_TIME_STAMP, 0);
        long twoAgo = System.currentTimeMillis() - COUNT_DOWN_TIME;

        Log.e("savedTimeStamp", String.valueOf(savedTimeStamp) + "   " + String.valueOf(twoAgo));
//        if (savedTimeStamp < twoAgo) {
//            time.setText("0");
//            Log.d(TAG,"Layout invisible ---->>>> 1744");
////            cancel_layout.setVisibility(View.GONE);
//        } else {
        cancel_layout.setVisibility(View.VISIBLE);
        long goneTime = System.currentTimeMillis() - savedTimeStamp;
        countDownTimer = new CountDownTimer(COUNT_DOWN_TIME - goneTime, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                time.setText(minutes + ":" + seconds);
            }

            public void onFinish() {
                time.setText("0");
                Log.d(TAG, "Layout invisible ---->>>> 1760");
                cancel_layout.setVisibility(View.GONE);
                if (dialog != null) {
                    dialog.dismiss();
                }
                loginEditor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
                loginEditor.apply();
            }
        }.start();
//        }
    }

    private void callCancelApi() {
        final SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        progstart( CutomerWaitingActivity.this, "Please wait...", "Canceling Ride");

        Log.e("Url", cancelride);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, cancelride, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("cancelride", "response: " + new Gson().toJson(response));

                try {
                    JSONObject responsJson = new JSONObject(response);
                    JSONObject success = responsJson.getJSONObject("success");
                    if (success.getString("status").equals("success")) {

                        dialog.dismiss();
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
                        startActivity(new Intent( CutomerWaitingActivity.this, CustHomePageActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                    Toast.makeText(getApplicationContext(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
                }// Do something with response string
                progstop();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                        Log.e("Error", error.toString());
                        error.printStackTrace();
                        progstop();
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trip_id", sharedpreferences.getString(KEY_PREF_TRIP_ID, "0"));
                params.put("reason", reasonid);
                params.put("other", reasontext);
                Log.e("Parm", params.toString());

                Log.e("cancelride", "getParams: " + new Gson().toJson(params));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("Authori   zation", "Bearer " + sharedpreferences.getString("tokan", "0"));
                params.put("Authorization", "Bearer " + sharedpreferences.getString("tokan", "0"));
                return params;
            }
        };
        if (queue == null) {
            queue = Volley.newRequestQueue( CutomerWaitingActivity.this);
            queue.add(stringRequest);
        } else if (queue != null) {
            queue.add(stringRequest);
        }
    }


    void newpopup(final String message, final String start) {

        try {
            JSONObject startobj = new JSONObject(start);
            Log.e("startobj", startobj.toString());
            try {

                endotpnot = startobj.getString("endotp");


                otp.setVisibility(View.GONE);
                String newendotp = "End OTP : " + endotpnot;
                otp.setText(newendotp);

                Log.e("newendotp", newendotp);
                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("endotpnot", endotpnot);
                editor.apply();

            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }
            try {
                if (startobj.has("trip_status")) {
                    String status = startobj.getString("trip_status");
                    if (status.equalsIgnoreCase("car_not_available")) {
                        Intent i = new Intent( CutomerWaitingActivity.this, SelectedPakegeShowActivity.class);
                        startActivity(i);
                        finish();
                        return;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
            }
            if (startobj.has("payable_amount")) {
                //get Value of payable_amount

                Log.e("HIHIH ", "we are in " + startobj);
                String payable_amount = startobj.optString("payable_amount");

                // Clean for new Ride

                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("payable_amount", payable_amount);
                //editor.putString("endotpnot",endotpnot);
                editor.putString("start", start);
                editor.apply();
                ;
                Intent i = new Intent( CutomerWaitingActivity.this, PaymentActivity.class);
                i.putExtra("start", start);
                startActivity(i);
                finish();
                Log.e("payable_amount ", "PAY nPAy:  " + payable_amount);
                return;
            }


            String message1 = message.replace("<br>", "  ");
            Log.d("asdf 5  ", message1);
//            distancetextview.setText(message1);
            i = 1;

            SharedPreferences sharedpreferences;
            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            // editor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis()- COUNT_DOWN_TIME);
            editor.putBoolean("finalcws", true);
            editor.putString("message1cws", message1);
            editor.apply();


            if (i == 0) {
                txtTagline.setVisibility(View.GONE);
            } else if (i == 1) {
                txtTagline.setVisibility(View.VISIBLE);
                txtTagline.setText(message);

            }
            ;

            new CountDownTimer(4000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    //here you can have your logic to set text to edittext

                }

                public void onFinish() {

                    try {
                        JSONObject main = new JSONObject(start);
                        if (main.has("trip_start_long")) {
                            SharedPreferences loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);

                            String mtrip_end_lat = loginPref.getString("mtrip_end_lat", "");
                            String mtrip_end_long = loginPref.getString("mtrip_end_long", "");
                            String mtrip_driver_lat = loginPref.getString("mtrip_driver_lat","");
                            String mtrip_driver_lng = loginPref.getString("mtrip_driver_lng","");

                            Log.e(TAG, " settingmap -->>> 1934 " + mtrip_end_lat + " " + mtrip_end_long);
                            settingmap(mtrip_end_lat + "," + mtrip_end_long,mtrip_driver_lat+","+mtrip_driver_lng);
                        }

                    } catch (Exception e) {
                        Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                    }

                }

            }.start();


                SharedPreferences loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);

                String mtrip_end_lat = loginPref.getString("mtrip_end_lat", "");
                String mtrip_end_long = loginPref.getString("mtrip_end_long", "");
                String mtrip_driver_lat = loginPref.getString("mtrip_driver_lat","");
                String mtrip_driver_lng = loginPref.getString("mtrip_driver_lng","");

                Log.e(TAG, " settingmap -->>> 1955 " + mtrip_end_lat + " " + mtrip_end_long);
                settingmap(mtrip_end_lat + "," + mtrip_end_long,mtrip_driver_lat+","+mtrip_driver_lng);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
        }
    }

    private void getcarlatlong(final String driverid) {
        Log.d("asdf", driverid);
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        Log.d("asdf", logtokan);
        StringRequest request = new StringRequest(Request.Method.POST, carlatlong, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response ", "start Ride " + response);

                    Log.d("asdf", "hello");
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            Log.d("asdf lat long car", String.valueOf(Latitudedefoult) + "," + String.valueOf(Longitudedefoult) + " , " + object.getString("last_lat") + "," + object.getString("last_long"));
//                            Toast.makeText( CutomerWaitingActivity.this, "Heloo", Toast.LENGTH_SHORT).show();
                            Log.d("eta time", finalendlat + "," + finalendlong + "      " + Latitudedefoult + "," + Longitudedefoult);
                            SharedPreferences loginPref = getSharedPreferences("Login", Context.MODE_PRIVATE);

                            String mtrip_end_lat = loginPref.getString("mtrip_end_lat", "");
                            String mtrip_end_long = loginPref.getString("mtrip_end_long", "");
                            String mtrip_driver_lat = loginPref.getString("mtrip_driver_lat","");
                            String mtrip_driver_lng = loginPref.getString("mtrip_driver_lng","");

                            Log.e(TAG, " settingmap -->>> 1991 " + mtrip_end_lat + " " + mtrip_end_long);
                            settingmap(mtrip_end_lat + "," + mtrip_end_long,mtrip_driver_lat+","+mtrip_driver_lng);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                    }
                    progstop();
                } catch (Exception e) {
                    Log.d(TAG, "Exception -->> " + e.getMessage() + " Line:" + e.getStackTrace()[0].getLineNumber());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("driver_id", driverid);

                Log.e("driver_id", driverid);

                return params;
            }
        };
        if (queue == null) {
            queue = Volley.newRequestQueue( CutomerWaitingActivity.this);
            queue.add(request);
        } else if (queue != null) {
            queue.add(request);
        }

    }

    private class CancelRideReasonAdaptor extends RecyclerView.Adapter<CancelRideReasonAdaptor.MyViewHolder> {

        Activity activity;
        ArrayList<CancelRideModel> cancelridereasonlist;

        public CancelRideReasonAdaptor(Activity activity, ArrayList<CancelRideModel> cancelridereasonlist) {
            this.activity = activity;
            this.cancelridereasonlist = cancelridereasonlist;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.row_cancel_ride, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.textCancelRide.setText(cancelridereasonlist.get(position).getReason());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edtremark.setText(cancelridereasonlist.get(position).getReason());
                    reasontext = cancelridereasonlist.get(position).getReason();
                    reasonid = cancelridereasonlist.get(position).getId();

                    subdialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return cancelridereasonlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            CTextView textCancelRide;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textCancelRide = itemView.findViewById(R.id.textCancelRide);


            }
        }
    }
}
