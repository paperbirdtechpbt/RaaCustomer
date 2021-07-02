package com.hopetechno.raadarbar.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CEditText;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Other.GeocodingLocation;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Service.LocationTrack;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.DataParser;
import com.hopetechno.raadarbar.Utils.DateUtils;
import com.hopetechno.raadarbar.Utils.MyApplication;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;
import static java.lang.System.exit;

public class EditAdvancedTripActivity extends AppCompatActivity implements AppConstant, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    CEditText editText1, editText2;
    CTextView startDate, incitytext, outcitytext, rentaltext;

    //    ImageView  endtime_icon;
    String id, start_date, end_date, start_time = "", start_address, end_address, end_time, end_point, start_point, citysource, citydest;
    String placeto = "", placefrom = "", locationAddress1 = "", locationAddress2 = "";
    boolean repet = false;
    SharedPreferences prefs, sharedPreferences;
    ArrayList<LatLng> MarkerPoints;
    SupportMapFragment mapFragment;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 109;
    protected static final int REQUEST_CHECK_SETTINGS = 0x2;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location mLastLocation;
    public static Location mLastLocation1;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    LocationTrack locationTrack;
    double Latitudedefoult, Longitudedefoult;
    LatLng origin, dest;
    private Calendar todayCalendar, selectedCalendar;
    int choice = 0;
    CButton getprice;
    String incity, roundtrip, car_type, fuel_type, logtokan, userid, distancevalue, durationvalue;
    RelativeLayout incitylayout, rentallayout, outcitylayout;
    long diffDays, diff;
    String flegincityoroutcity = "0";

    Date date;

    private static final String TAG = "EditAdvanceTrip";

    String center_lat = "23.0356514";
    String center_lan = "72.5780539";

    static final int initZoom = 8;
    //steps the zoom
    int stepZoom = 0;
    // number of steps in zoom, be careful with this number!
    int stepZoomMax = 5;
    //number of .zoom steps in a step
    int stepZoomDetent = (18 - initZoom) / stepZoomMax;
    //when topause zoom for spin
    int stepToSpin = 4;
    //steps the spin
    int stepSpin = 0;
    //number of steps in spin (factor of 360)
    int stepSpinMax = 4;
    //number of degrees in stepSpin
    int stepSpinDetent = 360 / stepSpinMax;

    Intent detailIntent;
    Intent intent;
    Marker marker;
    final int mapHopDelay = 2000;
    double distance;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advanced_trip);

        MyApplication.setCurrentActivity(this);
        prefs = getSharedPreferences("UpdateData", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);

        todayCalendar = Calendar.getInstance();
        selectedCalendar = Calendar.getInstance();

        id = prefs.getString("update_id", "");
        start_date = prefs.getString("update_sdate", "");
        start_time = prefs.getString("update_stime", "");
        end_time = prefs.getString("update_etime", "");
        end_date = prefs.getString("update_edate", "");
        start_point = prefs.getString("update_spoint", "");
        end_point = prefs.getString("update_epoint", "");
        incity = prefs.getString("update_incity", "0");
        roundtrip = prefs.getString("update_roundtrip", "");
        car_type = prefs.getString("update_car_type", "");
        fuel_type = prefs.getString("update_fueltype", "");

        Log.d("asdfg", "  " + incity + "  " + roundtrip + "  " + car_type + "  " + fuel_type + "   " + id + "   " +
                start_date + "  " + end_date + "  " + start_point + "  " + end_point + "  " + start_time + "  " + end_time);

        logtokan = sharedPreferences.getString("tokan", "");
        userid = sharedPreferences.getString("userid", "");

        Log.d("asdfg", "  " + distancevalue + "  " + durationvalue + "  " + logtokan + "  " + userid);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
            displayLocationSettingsRequest(EditAdvancedTripActivity.this);
        }


        // Initializing
        MarkerPoints = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be lused.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        startDate = findViewById(R.id.textdate);
        getprice = findViewById(R.id.updatedata);
        incitylayout = findViewById(R.id.incitylayout);
        outcitylayout = findViewById(R.id.outcitylayout);
        rentallayout = findViewById(R.id.rentallayout);
        incitytext = findViewById(R.id.incitytext);
        outcitytext = findViewById(R.id.outcitytext);
        rentaltext = findViewById(R.id.rentaltext);

        if (incity.equalsIgnoreCase("null") && roundtrip.equalsIgnoreCase("null")) {
            rentallayout.setBackground(getResources().getDrawable(R.drawable.rounded));
            incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
            outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
            rentaltext.setTextColor(getResources().getColor(R.color.bleck));
            outcitytext.setTextColor(getResources().getColor(R.color.white));
            incitytext.setTextColor(getResources().getColor(R.color.white));
        } else if (incity.equalsIgnoreCase("0")) {
            outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
            incitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
            rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
            outcitytext.setTextColor(getResources().getColor(R.color.white));
            rentaltext.setTextColor(getResources().getColor(R.color.white));
            incitytext.setTextColor(getResources().getColor(R.color.bleck));
        } else if (incity.equalsIgnoreCase("1")) {
            incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
            outcitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
            rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
            incitytext.setTextColor(getResources().getColor(R.color.white));
            rentaltext.setTextColor(getResources().getColor(R.color.white));
            outcitytext.setTextColor(getResources().getColor(R.color.bleck));

        }
        editText1.setText(start_point);
        editText2.setText(end_point);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Log.e(TAG, "StartDate : " + start_date + "Start Time : " + start_time);
            date = dateFormat.parse(start_date + " " + start_time);
        } catch (ParseException e) {
            Log.e(TAG, " Exception " + e.getMessage());
        }

        startDate.setText(dateFormat.format(date));

        outcitylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flegincityoroutcity = "1";

                incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                outcitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                incitytext.setTextColor(getResources().getColor(R.color.white));
                rentaltext.setTextColor(getResources().getColor(R.color.white));
                outcitytext.setTextColor(getResources().getColor(R.color.bleck));
            }
        });
        incitylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flegincityoroutcity = "0";

                outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                incitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                outcitytext.setTextColor(getResources().getColor(R.color.white));
                rentaltext.setTextColor(getResources().getColor(R.color.white));
                incitytext.setTextColor(getResources().getColor(R.color.bleck));
            }
        });
        rentallayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rentallayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                rentaltext.setTextColor(getResources().getColor(R.color.bleck));
                outcitytext.setTextColor(getResources().getColor(R.color.white));
                incitytext.setTextColor(getResources().getColor(R.color.white));

            }
        });


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        try {
            Intent i1 = getIntent();
            placefrom = i1.getStringExtra("placefrom");

            Log.e("placefrom", "onCreate: -- " + placefrom);
            editText1.setText(placefrom);

            Log.e("zxcz", placefrom);

            GeocodingLocation locationAddress = new GeocodingLocation();
            locationAddress.getAddressFromLocation(placefrom, getApplicationContext(), new GeocoderHandler());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("update_spoint", placefrom);
            editor.apply();
            repet = true;
        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
        }
        try {
            Intent i1 = getIntent();
            placeto = i1.getStringExtra("placeto");
            Log.e("placetoplaceto", placeto);
            editText2.setText(placeto);


            String selected = prefs.getString("update_spoint", "");
            placefrom = selected;
            Log.e("selected", selected);
            editText1.setText(selected);

            GeocodingLocation locationAddress1 = new GeocodingLocation();
            locationAddress1.getAddressFromLocation(placefrom,
                    getApplicationContext(), new GeocoderHandler());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("update_spoint", editText1.getText().toString().trim());
            editor.putString("update_epoint", editText2.getText().toString().trim());
            editor.apply();
            repet = true;
        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
        }

        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(EditAdvancedTripActivity.this, SourceSearchActivity.class);
                i.putExtra("edit", 1);
                startActivity(i);
                finish();
                return false;
            }
        });


        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("update_spoint", editText1.getText().toString().trim());
                    editor.apply();

                    Intent i = new Intent(EditAdvancedTripActivity.this, DestinationSearchActivity.class);
                    i.putExtra("edit", 1);
                    startActivity(i);
                    finish();

                } catch (Exception e) {
                    Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
                }
                return false;
            }
        });

        end_address = prefs.getString("update_epoint", "");
        start_address = prefs.getString("update_spoint", "");

        Log.e("asdf", "onCreate: " + end_address + " start_address : " + start_address);


        Log.e("TEST 123", "zjhcvjhxcvxzc   " + end_address + "    start_address  " + start_address);
        if (!start_address.equalsIgnoreCase("")) {
            Log.e("TEST 123", end_address);
            editText2.setText(end_address);
            editText1.setText(start_address);
        }

        getprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (editText1.getText().toString().trim().equalsIgnoreCase("") || editText2.getText().toString().trim().equalsIgnoreCase("")) {

                    if (editText1.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.sourcenprovaid), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                } else {

                    if (flegincityoroutcity.equalsIgnoreCase("0")) {

                        SharedPreferences prefs1 = getSharedPreferences("UpdateData", MODE_PRIVATE);
                        String endlat = prefs1.getString("update_start_latitude", "");
                        String end_lng = prefs1.getString("update_start_longitude", "");

                        String elat = prefs1.getString("update_end_latitude", "");
                        String elng = prefs1.getString("update_end_longitude", "");

                        double dist_source = 0;
                        double dist_dest = 0;
                        double dist_dest1 = 0;
                        double theta = 0;
                        if (citysource != null && endlat != null && endlat.trim().length() > 0 && end_lng != null && end_lng.trim().length() > 0) {
                            // distance = getDistanceBetweenTwoPoint(center_lat, center_lan, endlat, end_lng);

                            //  getDistance(center_lat, center_lan, endlat, end_lng);
                            if (citysource.contains("Gandhinagar") || citysource.contains("Hansol") || citysource.contains("Bopal") || citysource.contains("Sarkhej") || citysource.contains("Chharodi") || citysource.contains("Changodar") || citysource.contains("Morasar")) {
//                            Toast.makeText(CustHomePage.this, "source", Toast.LENGTH_SHORT).show();
                                theta = Double.parseDouble(end_lng) - Double.parseDouble(elng);
                                dist_dest = Math.sin(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                                dist_dest = Math.acos(dist_dest);
                                dist_dest = Math.toDegrees(dist_dest);
                                dist_dest = dist_dest * 60 * 1.1515 * 1.609344;
                            } else if (citydest.contains("Gandhinagar") || citydest.contains("Hansol") || citydest.contains("Bopal") || citydest.contains("Sarkhej") || citydest.contains("Chharodi") || citydest.contains("Changodar") || citydest.contains("Morasar")) {
//                            Toast.makeText(CustHomePage.this, "dest", Toast.LENGTH_SHORT).show();
                                theta = Double.parseDouble(center_lan) - Double.parseDouble(end_lng);
                                dist_source = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(endlat))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(theta));
                                dist_source = Math.acos(dist_source);
                                dist_source = Math.toDegrees(dist_source);
                                dist_source = dist_source * 60 * 1.1515 * 1.609344;
                            } else {


                                theta = Double.parseDouble(center_lan) - Double.parseDouble(end_lng);
                                dist_source = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(endlat))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(theta));
                                dist_source = Math.acos(dist_source);
                                dist_source = Math.toDegrees(dist_source);
                                dist_source = dist_source * 60 * 1.1515 * 1.609344;

                                theta = Double.parseDouble(end_lng) - Double.parseDouble(elng);
                                dist_dest1 = Math.sin(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                                dist_dest1 = Math.acos(dist_dest1);
                                dist_dest1 = Math.toDegrees(dist_dest1);
                                dist_dest1 = dist_dest1 * 60 * 1.1515 * 1.609344;

                                theta = Double.parseDouble(center_lan) - Double.parseDouble(elng);
                                dist_dest = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                                dist_dest = Math.acos(dist_dest);
                                dist_dest = Math.toDegrees(dist_dest);
                                dist_dest = dist_dest * 60 * 1.1515 * 1.609344;

                            }
                            //   distance2 = getDistanceOnRoad(Double.parseDouble(caaaaaenter_lat), Double.parseDouble(center_lan), Double.parseDouble(endlat), Double.parseDouble(end_lng));
                        }

                        if (flegincityoroutcity.equals("0")) {
                            if (distance > 15 && flegincityoroutcity.equals("0")) {
                                flegincityoroutcity = "1";
                            } else {
                                flegincityoroutcity = "0";
                            }
                        }

                        if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            if (flegincityoroutcity.equalsIgnoreCase("0") && citysource != null) {


                                if (citysource != null &&  citysource.equalsIgnoreCase(citydest) || citysource.contains("Gandhinagar") || citydest.contains("Gandhinagar") || citysource.contains("Ahmedabad") || citydest.contains("Ahmedabad") || citysource.contains("Sarkhej") || citydest.contains("Sarkhej") || citysource.contains("Bopal") || citydest.contains("Bopal") || citysource.contains("Hansol") || citydest.contains("Hansol") || citysource.contains("Chharodi") || citydest.contains("Chharodi") || citysource.contains("Changodar") || citydest.contains("Changodar") || citysource.contains("Morasar") || citydest.contains("Morasar")) {
                                    if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        editText2.setError(getString(R.string.destinationprovaid));
                                    } else {
                                        if ((dist_dest < 15 || dist_dest1 < 15) && dist_source < 15) {
                                            SharedPreferences sharedpreferences;
                                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                            checkValidation();
                                        } else {
                                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            } else {
                                if (citysource != null && citysource.equalsIgnoreCase(citydest) && dist_source < 15 && dist_dest < 15) {

                                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.incityselected), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                } else {
                                    if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        editText2.setError(getString(R.string.destinationprovaid));
                                    } else {

                                        if (citydest != null && citydest.equalsIgnoreCase("Gandhinagar") || citysource.equalsIgnoreCase("Gandhinagar") || citysource.contains("Sarkhej") || citydest.equalsIgnoreCase("Sarkhej") || citysource.contains("Chharodi") || citysource.contains("Changodar") || citysource.contains("Morasar")) {
                                            flegincityoroutcity = "0";
                                        }

                                        if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            editText2.setError(getString(R.string.destinationprovaid));
                                        } else
                                            checkValidation();
                                    }
                                }
                            }
                        }


//                        if (citysource.equalsIgnoreCase(citydest)) {
//                            if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
//                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                                editText2.setError(getString(R.string.destinationprovaid));
//                            } else {
//                                //add code here
//                                checkValidation();
//                            }
//                        } else {
//                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
//                        }
                    } else {
                        if (citysource.equalsIgnoreCase(citydest)) {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.incityselected), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                editText2.setError(getString(R.string.destinationprovaid));
                            } else {
                                //add code here
                                checkValidation();
                            }
                        }
                    }
                }


//
            }
        });
    }

    public void openDialog() {

        Log.i(TAG, "Open openDialog()");

        final TextView tvShowNumbers;
        final TimePicker simpleTimePicker;
        final DatePicker simpleDatePicker;

        final Dialog  dialog = new Dialog(EditAdvancedTripActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_date_time);
        tvShowNumbers = (TextView) dialog.findViewById(R.id.txtdate);
        simpleTimePicker = (TimePicker) dialog.findViewById(R.id.simpleTimePicker);
        simpleDatePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        Button btnConfirmDate = (Button) dialog.findViewById(R.id.btnConfirmDate);
        btnConfirmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //30-10-2020 09:35 PM

                SimpleDateFormat currentYearFormate = new SimpleDateFormat("yyyy", Locale.getDefault());
                String currentYear = currentYearFormate.format(new Date());

                Log.i(TAG, "Current Year : " + currentYear);

                if (String.valueOf(simpleDatePicker.getYear()).equals(currentYear)) {


                    int Hour = simpleTimePicker.getCurrentHour();
                    final int Minute = simpleTimePicker.getCurrentMinute();
                    if (Hour == 0) {
                        Hour += 12;
                    } else if (Hour == 12) {
                    } else if (Hour > 12) {
                        Hour -= 12;
                    }
//                "08:48: AM";

                    String dtStart = Integer.valueOf(simpleDatePicker.getMonth() + 1) + "-" + simpleDatePicker.getDayOfMonth() + "-" + simpleDatePicker.getYear() + " " + Hour + ":" + Minute;
                    SimpleDateFormat formatTime = new SimpleDateFormat("MM-dd-yyyy HH:mm");


                    try {
                        date = formatTime.parse(dtStart);
                        Log.i(TAG, "Date Pasing Successfuly :: " + date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.i(TAG, "Date Pasing Exception ->" + e.getMessage());
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    String currentSelcted = sdf.format(date);

                    Date date1 = null;
                    try {
                        date1 = sdf.parse(currentDateandTime);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (dialog != null && dialog.isShowing()) {
                        long tenBefore = date1.getTime() + PRE_BOOKING_DELAY_TIME;
                        if (date.getTime() < tenBefore) {
                            Toast.makeText(EditAdvancedTripActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
                        } else {

                        }
                    }
                    dialog.dismiss();
                } else {

                    Toast.makeText(EditAdvancedTripActivity.this, " Please Select Current Year ", Toast.LENGTH_SHORT).show();
                }
                SimpleDateFormat MMDDYYYYY = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat HHMM = new SimpleDateFormat("hh:mm");

                String strDate = MMDDYYYYY.format(date);
                String strTime = HHMM.format(date);

                Log.e(TAG, " DAte :" + date);

                Log.e(TAG, "OpenDialog StrDAte " + strDate + " strTime :" + strTime);

                start_point = prefs.getString("update_spoint", "");
                end_point = prefs.getString("update_epoint", "");

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("update_spoint", editText1.getText().toString().trim());
                editor.putString("update_epoint", editText2.getText().toString().trim());
                editor.putString("update_sdate", strDate);
                editor.putString("update_edate", strDate);
                editor.putString("update_stime", strTime);
                editor.putString("update_etime", strTime);

                startDate.setText(strDate + " " + strTime);

                dialog.dismiss();
            }
        });
        simpleTimePicker.setIs24HourView(false); // used to display AM/PM mode
        // perform set on time changed listener event

        String format = "";
        int Hour = simpleTimePicker.getCurrentHour();
        final int Minute = simpleTimePicker.getCurrentHour();
        if (Hour == 0) {
            Hour += 12;
        } else if (Hour == 12) {
            format = "PM";
        } else if (Hour > 12) {
            Hour -= 12;
        }

        String DateTime1 = simpleDatePicker.getDayOfMonth() + "-" + Integer.valueOf(simpleDatePicker.getMonth() + 1) + "-" + simpleDatePicker.getYear() + " " + Hour + ":" + Minute;
        tvShowNumbers.setText(DateTime1);

        final int finalHour = Hour;
        final int finalHour2 = Hour;
        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                String format = "";
                int Hour = hourOfDay;
                final int Minute = minute;
                if (Hour == 0) {
                    Hour += 12;
                } else if (Hour == 12) {
                    format = "PM";
                } else if (Hour > 12) {
                    Hour -= 12;
                }

                String DateTime1 = simpleDatePicker.getDayOfMonth() + "-" + (simpleDatePicker.getMonth() + 1) + "-" + simpleDatePicker.getYear() + " " + Hour + ":" + Minute;
                Log.e(TAG, "Time Change : " + DateTime1);
                tvShowNumbers.setText(DateTime1);
            }
        });
        final int finalHour1 = Hour;
//        simpleDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                String DateTime1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " " + finalHour1 + ":" + Minute;
//                tvShowNumbers.setText(DateTime1);
//            }
//        });


        dialog.show();

    }

    public void checkValidation() {

        SimpleDateFormat MMDDYYYYY = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat HHMM = new SimpleDateFormat("hh:mm");

        String startDate = MMDDYYYYY.format(date);
        String EndTime = HHMM.format(date);

        Log.e(TAG, " checkValidation  date : " + date);
        Log.e(TAG, " checkValidation  StrDAte : " + startDate + " strTime :" + EndTime);

        start_point = prefs.getString("update_spoint", "");
        end_point = prefs.getString("update_epoint", "");

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("update_spoint", editText1.getText().toString().trim());
        editor.putString("update_epoint", editText2.getText().toString().trim());
        editor.putString("update_sdate", startDate);
        editor.putString("update_edate", startDate);
        editor.putString("update_stime", EndTime);
        editor.putString("update_etime", EndTime);
        editor.putString("update_incity", flegincityoroutcity);
        editor.apply();

        if (!start_date.equalsIgnoreCase("") && !end_date.equalsIgnoreCase("") && !start_time.equalsIgnoreCase("") && !end_time.equalsIgnoreCase("") && !start_point.equalsIgnoreCase("") && !end_point.equalsIgnoreCase("")) {
            try {
                CallBudgetApi();
            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
            }
        } else {
            Toast.makeText(EditAdvancedTripActivity.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
        }
    }

    private void CallBudgetApi() {

        StringRequest request = new StringRequest(Request.Method.POST, getridenowpackagesapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String DateandTime[] = DateUtils.convertDateToString(selectedCalendar.getTime()).split(" ");
                try {

                    JSONObject jobject = new JSONObject(response);
                    JSONObject mainobject = jobject.getJSONObject("success");
                    JSONObject packageobj = mainobject.getJSONObject("package");
                    String packegename = packageobj.getString("name");
                    //String cartype = packageobj.getString("cartype");
                    String kilometer = packageobj.getString("kilometer");
                    String hours = packageobj.getString("hours");
                    String price = packageobj.getString("price");
                    String condition = packageobj.getString("condition");
                    String estimatedprice = mainobject.getString("estimatedprice");
                    String car_rate_per_kl = mainobject.getString("car_rate_per_kl");
                    String car_rate_per_hr = mainobject.getString("car_rate_per_hr");
                    String wallet_amount = mainobject.getString("wallet_amount");
                    String id = packageobj.getString("id");

                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("UpdateData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("packegename", packegename);
                    // editor.putString("cartype", cartype);
                    editor.putString("kilometer", kilometer);
                    editor.putString("hours", hours);
                    editor.putString("price", price);
                    editor.putString("condition", condition);
                    editor.putString("fueltype", fuel_type);
                    editor.putString("estimatedprice", estimatedprice);
                    editor.putString("car_rate_per_kl", car_rate_per_kl);
                    editor.putString("car_rate_per_hr", car_rate_per_hr);
                    editor.putString("end_date", DateandTime[0]);
                    editor.putString("end_time", DateandTime[1] + " " + DateandTime[2]);
                    editor.putString("wallet_amount", wallet_amount);
                    editor.putString("package_id", id);
                    editor.apply();
                    ;
                    Intent i = new Intent(EditAdvancedTripActivity.this, UpdateAdvancebknSelectedPakegeShow.class);
                    startActivity(i);

                } catch (Exception e) {
                    Toast.makeText(EditAdvancedTripActivity.this, "catch", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
                }
                //Common.image+'cars/primary/thumbnail/'+carimage;

                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
                Toast.makeText(EditAdvancedTripActivity.this, "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                // Log.e("Authorization", "Bearer "+logtokan);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                SharedPreferences prefs1 = getSharedPreferences("UpdateData", MODE_PRIVATE);
                String slat = prefs1.getString("update_start_latitude", "");
                String slng = prefs1.getString("update_start_longitude", "");

                String elat = prefs1.getString("update_end_latitude", "");
                String elng = prefs1.getString("update_end_longitude", "");

                params.put("start_lat", slat);
                params.put("start_lng", slng);
                params.put("end_lat", elat);
                params.put("end_lng", elng);
                params.put("totalkm", distancevalue);
                params.put("totalhr", durationvalue);
                params.put("cartype", car_type);
                params.put("userid", userid);
                params.put("incity", incity);
                params.put("fueltype", fuel_type);
                params.put("triptype", roundtrip);
                params.put("days", "0");

                Log.e("EditAdvancetrip", " EditAdvancetrip  param -->>>  : " + new Gson().toJson(params));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }

    private void callapi() {
        final String logtokan;
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        Log.e("asdf", logtokan);
        progstart(EditAdvancedTripActivity.this, "Loading...", "Loading...");

        StringRequest request = new StringRequest(Request.Method.POST, updateride, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("Your Array Response", response.toString());
                Log.e("advance", "Select car type response:" + response);

                try {
                    JSONObject main = new JSONObject(response);
                    JSONObject submian = main.getJSONObject("success");
                    String succ = submian.getString("status");
                    if (succ.equalsIgnoreCase("success")) {
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("UpdateData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
//
                        Intent i = new Intent(EditAdvancedTripActivity.this, RideHistoryActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
                }
                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Exception : " + error.getMessage() + " Line :" + error.getStackTrace()[0].getLineNumber());
                progstop();
                Toast.makeText(EditAdvancedTripActivity.this, "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //DateUtils.convertDateToString(selectedCalendar.getTime());
                params.put("start_date", start_date);
                params.put("trip_id", id);
                params.put("end_point", end_point);
                params.put("start_point", start_point);
                params.put("start_time", start_time);
                params.put("end_date", end_date);
                params.put("end_time", end_time);

                Log.e("advance", "Update trip ------>>> :" + new Gson().toJson(params));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == DATE_PICKER_ID) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    myDateListener, todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        } else if (id == TIME_PICKER_ID) {
            return new TimePickerDialog(this, myTimeSetListener, todayCalendar.get(Calendar.HOUR_OF_DAY), todayCalendar.get(Calendar.MINUTE), false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int year, int month, int day) {
                    // TODO Auto-generated method stub
                    selectedCalendar.set(year, month, day);
                    dismissDialog(DATE_PICKER_ID);
                    if (choice == 1) {
//                        startdate_txt.setText(DateUtils.convertDatetimeToString(selectedCalendar.getTime()));
                    } else if (choice == 3) {
//                        enddate_txt.setText(DateUtils.convertDatetimeToString(selectedCalendar.getTime()));
                    }
                }
            };


    private TimePickerDialog.OnTimeSetListener myTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            selectedCalendar.set(Calendar.HOUR_OF_DAY, i);
            selectedCalendar.set(Calendar.MINUTE, i1);

            long tenBefore = System.currentTimeMillis() + PRE_BOOKING_DELAY_TIME;
            if (selectedCalendar.getTimeInMillis() < tenBefore) {
                Toast.makeText(EditAdvancedTripActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
            } else {
                if (choice == 2) {
                    startDate.setText(DateUtils.convertDateToString(date));
                }
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // This log is never called
        Log.d("onActivityResult()", Integer.toString(resultCode));

        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions

                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //  int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

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
        // Toast.makeText(Drivar_map.this, "hihiihhi", Toast.LENGTH_LONG).show();
        //Initialize Google Play Services
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("HH");
        String datetime = dateformat.format(c.getTime());
        Log.d("asdf", "datetime" + datetime);

        locationTrack = new LocationTrack(EditAdvancedTripActivity.this);
        if (locationTrack.canGetLocation()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);

                    googleMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);

            }
        } else {

//            locationTrack.showSettingsAlert();
        }
        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 400, 180, 0);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("asdf", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("asdf", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(EditAdvancedTripActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("asdf", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        mLastLocation1 = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //Place current location marker
       /* LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));*/
        //Place current location marker

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0);
            //editText1.setText(address);
            if (editText1.getText().toString().trim().equalsIgnoreCase("")) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.2f));

                Latitudedefoult = location.getLatitude();
                Longitudedefoult = location.getLongitude();

//                if (start_address.equalsIgnoreCase("")) {
//                    editText1.setText(address);
//                }
//
                /*MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");*/
                // BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
                //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.you));
                // mCurrLocationMarker = mMap.addMarker(markerOptions);
            }


            if (!editText2.getText().toString().trim().equalsIgnoreCase("")) {
                if (repet == false) {
                    try {
                        GeocodingLocation locationAddress1 = new GeocodingLocation();
                        locationAddress1.getAddressFromLocation(editText1.getText().toString().trim(),
                                getApplicationContext(), new GeocoderHandler());
                        //Toast.makeText(CustHomePage.this, "hihihi", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
                    }
                }
            }

            progstop();
        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
        }
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

                    finish();
                    exit(0);
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress1 = bundle.getString("address");

                    Log.e(TAG,"locationAddress1 "+locationAddress1);

                    String selected = prefs.getString("update_spoint", "");
                    if (!selected.isEmpty() || !editText2.getText().toString().trim().equalsIgnoreCase("")) {

                        GeocodingLocation locationAddress = new GeocodingLocation();
                        locationAddress.getAddressFromLocation(editText2.getText().toString().trim(),
                                getApplicationContext(), new GeocoderHandler1());

                    }
                    break;
                default:
                    locationAddress1 = null;
            }

        }
    }

    private class GeocoderHandler1 extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:
                    try {
                        Bundle bundle = message.getData();
                        locationAddress2 = bundle.getString("address");
                              Log.e(TAG,locationAddress1+"  Testing    "+locationAddress2);

                        if (!editText2.getText().toString().trim().equalsIgnoreCase("")) {

                            placefrom = editText1.getText().toString().trim();
                            try {
                                settingmap(locationAddress1, locationAddress2, mLastLocation1);
                            } catch (Exception e) {
                                Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
                    }
                    break;
                default:
                    locationAddress2 = null;
            }

        }
    }

    public GoogleMap.CancelableCallback cameraAnimation = new GoogleMap.CancelableCallback() {

        @Override
        public void onFinish() {
            if (stepZoom < stepZoomMax && stepZoom != stepToSpin) {
                stepZoom++;
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                        .target(origin)
                        .zoom(initZoom + (stepZoomDetent * (stepZoom - 1)))
                        //   .bearing(40*aniStep)
                        //   .tilt(60)
                        .build()), mapHopDelay, cameraAnimation);

            } else if (stepZoom >= stepZoomMax)// ending position hard coded for this application
            {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                        .target(origin)
                        .zoom(18)
                        //  .bearing(0)
                        .tilt(0)
                        .build()));
            } else {
                if (stepSpin <= stepSpinMax) {
                    stepSpin++;
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                            .target(origin)
                            .zoom(initZoom + stepZoomDetent * stepZoom)
                            .bearing(stepSpinDetent * (stepSpin - 1))
                            .tilt(60)
                            .build()), mapHopDelay, cameraAnimation);
                } else {
                    stepZoom++;
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                            .target(origin)
                            .zoom(initZoom + stepZoomDetent * stepZoom)
                            .bearing(0)
                            .tilt(0)
                            .build()), mapHopDelay, cameraAnimation);
                }
            }
        }

        @Override
        public void onCancel() {
        }

    };

    public void settingmap(String latlon1, String latlon2, Location mLastLocation1) {

        MarkerPoints.clear();
        mMap.clear();


        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.start);
        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.end);

        try {
            if (placefrom.equalsIgnoreCase("")) {
                origin = new LatLng(Latitudedefoult, Longitudedefoult);
                //origin = new LatLng(lat, lon);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());

                List<Address> addresses = gcd.getFromLocation(Latitudedefoult, Longitudedefoult, 1);
                if (addresses.size() > 0) {
                    citysource = addresses.get(0).getLocality();
                }
            } else {
                String[] latlong1 = latlon1.split(",");
                double latitude1 = Double.parseDouble(latlong1[0]);
                double longitude1 = Double.parseDouble(latlong1[1]);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("update_slat", String.valueOf(latitude1));
                editor.putString("update_slang", String.valueOf(longitude1));
                editor.putString("update_start_latitude", String.valueOf(latitude1));
                editor.putString("update_start_longitude", String.valueOf(longitude1));
                editor.apply();

                origin = new LatLng(latitude1, longitude1);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latlong1[0]), Double.parseDouble(latlong1[1])), 12.0f));
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses1 = gcd.getFromLocation(Double.parseDouble(latlong1[0]), Double.parseDouble(latlong1[1]), 1);
                if (addresses1.size() > 0) {
                    if (addresses1.size() > 0) {
                        if (addresses1.get(0).getLocality() == null) {
                            citysource = addresses1.get(0).getSubAdminArea();
                        } else {
                            citysource = addresses1.get(0).getLocality();
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
        }
        mMap.addMarker(new MarkerOptions().position(origin)
                .title("Start Ride ").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(origin));


        mMap.addMarker(new MarkerOptions().position(origin)
                .title("Start Ride ").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
//        mMap.animateCamera(CameraUpdateFactory.newLa/tLng(origin));
        String[] latlong2 = latlon2.split(",");
        double latitude2 = Double.parseDouble(latlong2[0]);
        double longitude2 = Double.parseDouble(latlong2[1]);
        dest = new LatLng(latitude2, longitude2);
        mMap.addMarker(new MarkerOptions().position(dest)
                .title("End Ride").icon(icon1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                        .target(dest)
                        .zoom(initZoom - 1)
                        .build())
                , mapHopDelay
                , cameraAnimation
        );


        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("update_elat", String.valueOf(latitude2));
        editor.putString("update_elang", String.valueOf(longitude2));
        editor.putString("update_end_latitude", String.valueOf(latitude2));
        editor.putString("update_end_longitude", String.valueOf(longitude2));

        editor.apply();

        dest = new LatLng(latitude2, longitude2);
        mMap.addMarker(new MarkerOptions().position(dest)
                .title("End Ride").icon(icon1));
        progstop();
        try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(Double.parseDouble(latlong2[0]), Double.parseDouble(latlong2[1]), 1);
            if (addresses.size() > 0) {
                if (addresses.get(0).getLocality() == null) {
                    citydest = addresses.get(0).getSubAdminArea();
                } else {
                    citydest = addresses.get(0).getLocality();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
        }


        String url = getUrl(origin, dest);
        FetchUrl FetchUrl = new FetchUrl();

        FetchUrl.execute(url);
        //move map camera
        //for shwoing path
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(origin);
        builder.include(dest);

        LatLngBounds bounds = builder.build();


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        Log.e("diagonalInches", String.valueOf(diagonalInches));
        if (diagonalInches >= 5.0) {
            // 5inch device or bigger

            int padding = 430; // offset from edges of the map in pixels

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            //mMap.moveCamera(cu);
//            mMap.animateCamera(cu);

        } else {

            int padding = 320; // offset from edges of the map in pixels

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            //mMap.moveCamera(cu);
//            mMap.animateCamera(cu);

            // smaller device
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(latlon1), Double.valueOf(latlon2)), 12.0f));

    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false" + "&key=" + getResources().getString(R.string.google_maps_key);

        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    public class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {


            Log.e(TAG, " Direction Api call =====>>>> ");

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

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
            Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                DataParser parser = new DataParser();
                routes = parser.parse(jObject);


                JSONObject jsonObject = new JSONObject(jsonData[0]);

                JSONArray routes2 = jsonObject.getJSONArray("routes");

                JSONObject routes1 = routes2.getJSONObject(0);

                JSONArray legs = routes1.getJSONArray("legs");
                //  Log.e("legs",legs.toString());

                JSONObject legs1 = legs.getJSONObject(0);

                JSONObject distance = legs1.getJSONObject("distance");

                JSONObject duration = legs1.getJSONObject("duration");

                String end_address = legs1.getString("end_address");
                String start_address = legs1.getString("start_address");
                JSONObject end_location = legs1.getJSONObject("end_location");
                String end_lat = end_location.getString("lat");
                String end_lng = end_location.getString("lng");
                JSONObject start_location = legs1.getJSONObject("start_location");
                String start_lat = start_location.getString("lat");
                String start_lng = start_location.getString("lng");


                String distanceText = distance.getString("text");
                distancevalue = distance.getString("value");


                String durationText = duration.getString("text");
                durationvalue = duration.getString("value");

                Log.e("distanceText", distancevalue + "   " + durationvalue);
                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences("UpdateData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("update_jsonData", legs.toString());
                editor.putString("update_durationvalue", durationvalue);
                editor.putString("update_distanceText", distanceText);
                editor.putString("update_distancevalue", distancevalue);
                editor.putString("update_epoint", end_address);
                editor.putString("update_spoint", start_address);
                editor.putString("update_start_longitude", start_lng);
                editor.putString("update_start_latitude", start_lat);
                editor.putString("update_end_longitude", end_lng);
                editor.putString("update_end_latitude", end_lat);
                editor.apply();

            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.getMessage() + " Line :" + e.getStackTrace()[0].getLineNumber());
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
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
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
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
}
