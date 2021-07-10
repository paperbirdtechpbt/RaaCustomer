package com.hopetechno.raadarbar.Activity;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CEditText;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Modal.BaseResponseProvider;
import com.hopetechno.raadarbar.Modal.HeaderModel;
import com.hopetechno.raadarbar.Modal.Provider;
import com.hopetechno.raadarbar.Notification.NotificationUtils;
import com.hopetechno.raadarbar.Other.ExpandableNavigationListView;
import com.hopetechno.raadarbar.Other.GeocodingLocation;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Service.LocationTrack;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.ConnectivityReceiver;
import com.hopetechno.raadarbar.Utils.CustomExceptionHandler;
import com.hopetechno.raadarbar.Utils.DataParser;
import com.hopetechno.raadarbar.Utils.DateUtils;
import com.hopetechno.raadarbar.Utils.MyApplication;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;
import static java.lang.System.exit;

public class CustHomePageActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AppConstant, NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    public static Location mLastLocation1;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    CButton ridenow, prebooking, selectvhical, ridenowrentel, prebookingrental;
    CTextView Cust_name, monumber, tv1, tv2, outcitytext, rentaltext, incitytext;
    String placeto = "", placefrom = "", citysource = "", citydest = "", flegincityoroutcity = "0";
    LinearLayout relativeLayout1;
    CEditText editText1, editText2;
    TextView txtEstamated;
    TextView txtTagline2;
    String locationAddress1 = "", locationAddress2 = "", cartype = "sedan", fueltype = "cng";
    double Latitudedefoult, Longitudedefoult;
    LatLng origin, dest;
    LinearLayout testlinear, newrental, destlayout, sourcelayout, linOffer;
    RelativeLayout suvlayout, sedanlayout, microlayout, buslayout, sharelayout, incitylayout, rentallayout, outcitylayout;
    ImageView suv, sedan, micro, bus, share, favicon, favicon1;
    CircleImageView imageView, selectvnew;
    private TextView txtTagline, txtTagline1;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final String TAG = "HomePage";

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    SupportMapFragment mapFragment;

    private boolean canReRoute = true, canCarAnim = true;

    CTextView appversion, microtext, sedantext, suvtext;

    Context context;
    boolean repet = false, rentalpreebooking = false;

    private Calendar todayCalendar, selectedCalendar;
    double distance;
    double end_dis;
   /* String center_lat = "23.022490";
    String center_lan = "72.556152";*/

    Boolean isRental;

    String center_lat = "23.0356514";
    String center_lan = "72.5780539";

    String condition = "Please Select Car ";
    String estimatedprice = "";

    private Boolean network = Boolean.TRUE;
    private ConnectivityReceiver connectivityReceiver;

    private Boolean isOutofAhemedaBad = false;

    private HashMap<Integer, Marker> providersMarker;

    protected PowerManager.WakeLock mWakeLock;
    LocationTrack locationTrack;
    private ExpandableNavigationListView navigationExpandableListView;


    public static final int FILE_PERMISSION_REQUEST_CODE = 112;

    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean isBookAvailable = true;


    @SuppressLint({"InvalidWakeLockTag", "ClickableViewAccessibility"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_home_page);

        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.addListener(this);
        this.registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/RaadarbarCrash", "", getApplicationContext()));
        }

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        MyApplication.setCurrentActivity(this);

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version_name = packageInfo.versionName;

        appversion = findViewById(R.id.appversion);
        appversion.setText("Version : " + version_name);

        todayCalendar = Calendar.getInstance();
        selectedCalendar = Calendar.getInstance();

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.title_activity_cust_home_page));
        navigationExpandableListView = (ExpandableNavigationListView) findViewById(R.id.expandable_navigation);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initview();

        SharedPreferences prefs1 = getSharedPreferences("Login", MODE_PRIVATE);

        String custname = prefs1.getString("first_name", "");
        String mo_number = prefs1.getString("phone", "");
        String image_pref = prefs1.getString("image", "");
        String selectedtokan = prefs1.getString("tokan", "");

        custuserid = prefs.getString("userid", "");

        try {
            String isridestart = prefs.getString("isridestart", "");
            if (isridestart.equalsIgnoreCase("yes")) {
                CutomerWaitingActivity.i = 0;
                Intent i = new Intent(CustHomePageActivity.this, CutomerWaitingActivity.class);
                startActivity(i);
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cust_name.setText(custname);
        monumber.setText(mo_number);

        UrlImageViewHelper.setUrlDrawable(imageView, image + "users/thumbnail/" + image_pref, R.drawable.raalogo_white);

        progstart(CustHomePageActivity.this, "Loading...", "Loading Please wait while finding your current location...");

        //for map
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
            displayLocationSettingsRequest(CustHomePageActivity.this);
        }

        // Initializing
        MarkerPoints = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be lused.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tv2 = (CTextView) findViewById(R.id.textView2);
        relativeLayout1 = (LinearLayout) findViewById(R.id.relativeLayout1);
        ridenow = (CButton) findViewById(R.id.ridenow);


        editText1 = (CEditText) findViewById(R.id.editText1);
        editText2 = (CEditText) findViewById(R.id.editText2);
        testlinear = findViewById(R.id.testlinear);
        microlayout = findViewById(R.id.microlayout);
        sedanlayout = findViewById(R.id.sedanlayout);
        suvlayout = findViewById(R.id.suvlayout);
        buslayout = findViewById(R.id.buslayout);
        newrental = findViewById(R.id.newrental);
        sharelayout = findViewById(R.id.sharelayout);
        share = findViewById(R.id.share);
        micro = findViewById(R.id.micro);
        sedan = findViewById(R.id.sedan);
        suv = findViewById(R.id.suv);
        microtext = findViewById(R.id.microtext);
        sedantext = findViewById(R.id.sedantext);
        suvtext = findViewById(R.id.suvtext);
        bus = findViewById(R.id.bus);
        prebooking = findViewById(R.id.prebooking);
//        selectvhical = findViewById(R.id.selectvhical);
        selectvnew = findViewById(R.id.selectvnew);
        incitylayout = findViewById(R.id.incitylayout);
        outcitylayout = findViewById(R.id.outcitylayout);
        rentallayout = findViewById(R.id.rentallayout);
        incitytext = findViewById(R.id.incitytext);
        outcitytext = findViewById(R.id.outcitytext);
        rentaltext = findViewById(R.id.rentaltext);
        ridenowrentel = findViewById(R.id.ridenowrentel);
        prebookingrental = findViewById(R.id.prebookingrental);
        favicon = findViewById(R.id.favicon);
        favicon1 = findViewById(R.id.favicon1);
        destlayout = findViewById(R.id.destlayout);
        sourcelayout = findViewById(R.id.sourcelayout);

        txtTagline = findViewById(R.id.txtTagline);
        txtTagline1 = findViewById(R.id.txtTagline1);
        txtTagline.setSelected(true);
        txtTagline1.setSelected(true);
        linOffer = findViewById(R.id.linOffer);


        txtEstamated = findViewById(R.id.txtEstamated);

        getOffers();

        String favriteiconchangesorce = prefs1.getString("favriteiconchangesorce", "");
        String favriteiconchangedest = prefs1.getString("favriteiconchangedest", "");
        if (favriteiconchangesorce.equalsIgnoreCase("1")) {
            favicon1.setImageDrawable(getResources().getDrawable(R.drawable.selected_favorite));
        }
        if (favriteiconchangedest.equalsIgnoreCase("1")) {
            favicon.setImageDrawable(getResources().getDrawable(R.drawable.selected_favorite));
        }


        String endlat = prefs1.getString("start_lat", "");
        String end_lng = prefs1.getString("start_lng", "");

//        distance = getDistanceBetweenTwoPoint(center_lat, center_lan, endlat, end_lng);

        favicon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText1.getText().toString().isEmpty()) {
                    SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                    String favplace = prefs.getString("favplace", "");

                    String[] favarray = favplace.split("<mrrhope>");
                    favicon1.setImageDrawable(getResources().getDrawable(R.drawable.selected_favorite));
                    if (!Arrays.asList(favarray).contains(editText1.getText().toString().trim())) {
                        // true

                        if (!favplace.equalsIgnoreCase("")) {
                            Toast.makeText(CustHomePageActivity.this, "Added as Favorite", Toast.LENGTH_LONG).show();
                            favplace = favplace + "<mrrhope>" + editText1.getText().toString().trim();
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("favplace", favplace);
                            editor.apply();
                            ;
                        } else {

                            Toast.makeText(CustHomePageActivity.this, "Added as Favorite", Toast.LENGTH_LONG).show();
                            favplace = editText1.getText().toString().trim();
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("favplace", favplace);
                            editor.apply();
                            ;
                        }
                    } else {
                        Toast.makeText(CustHomePageActivity.this, "Already Added as Favorite", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        favicon.setImageDrawable(getResources().getDrawable(R.drawable.favorite));
        favicon1.setImageDrawable(getResources().getDrawable(R.drawable.favorite));
        favicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText2.getText().toString().isEmpty()) {
                    SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                    String favplace = prefs.getString("favplace", "");

                    favicon.setImageDrawable(getResources().getDrawable(R.drawable.selected_favorite));
                    String[] favarray = favplace.split("<mrrhope>");

                    if (!Arrays.asList(favarray).contains(editText2.getText().toString().trim())) {

                        if (!favplace.equalsIgnoreCase("")) {
                            Toast.makeText(CustHomePageActivity.this, "Added as Favorite", Toast.LENGTH_LONG).show();
                            favplace = favplace + "<mrrhope>" + editText2.getText().toString().trim();
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("favplace", favplace);
                            editor.apply();
                            ;
                        } else {

                            Toast.makeText(CustHomePageActivity.this, "Added as Favorite", Toast.LENGTH_LONG).show();
                            favplace = editText2.getText().toString().trim();
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("favplace", favplace);
                            editor.apply();
                            ;
                        }
                    } else {
                        Toast.makeText(CustHomePageActivity.this, "Already Added as Favorite", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        if (editText1.getText().toString().isEmpty() && editText2.getText().toString().isEmpty()) {
            SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Mstart_lat", "");
            editor.putString("Mstart_lng", "");
            editor.putString("Mend_lat", "");
            editor.putString("Mend_lng", "");
            editor.commit();
        }


        outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
        outcitytext.setTextColor(getResources().getColor(R.color.white));
        rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
        rentaltext.setTextColor(getResources().getColor(R.color.white));
        outcitylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRental = false;


                rentalInVisible();

                rentalpreebooking = false;

                if (!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {
                    if (!checkout()) {

                        flegincityoroutcity = "1";
//                        newrental.setVisibility(View.GONE);
//                        testlinear.setVisibility(View.VISIBLE);
//                        relativeLayout1.setVisibility(View.VISIBLE);
//                        destlayout.setVisibility(View.VISIBLE);

                        rentalpreebooking = false;
                        incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                        outcitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                        rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                        incitytext.setTextColor(getResources().getColor(R.color.white));
                        rentaltext.setTextColor(getResources().getColor(R.color.white));
                        outcitytext.setTextColor(getResources().getColor(R.color.bleck));
                        getOffers();

                        if (!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {
//                            Ridenowclass(false);
                            GeocodingLocation locationAddress = new GeocodingLocation();
                            locationAddress.getAddressFromLocation(placefrom, getApplicationContext(), new GeocoderHandler());
                        }
                    } else {
                        Toast.makeText(CustHomePageActivity.this, getString(R.string.incityselected), Toast.LENGTH_LONG).show();
                    }
                } else {

                    flegincityoroutcity = "1";

                    rentalpreebooking = false;
                    incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    outcitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                    rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    incitytext.setTextColor(getResources().getColor(R.color.white));
                    rentaltext.setTextColor(getResources().getColor(R.color.white));
                    outcitytext.setTextColor(getResources().getColor(R.color.bleck));
                    getOffers();
                }
            }
        });
        incitylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isRental = false;

                rentalInVisible();

                rentalpreebooking = false;

                if (!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {

                    if (checkIncity()) {

                        flegincityoroutcity = "0";


                        outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                        incitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                        rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                        outcitytext.setTextColor(getResources().getColor(R.color.white));
                        rentaltext.setTextColor(getResources().getColor(R.color.white));
                        incitytext.setTextColor(getResources().getColor(R.color.bleck));
                        getOffers();

                        if (!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {
                            GeocodingLocation locationAddress = new GeocodingLocation();
                            locationAddress.getAddressFromLocation(placefrom, getApplicationContext(), new GeocoderHandler());
                        }
                    } else {
                        Toast.makeText(CustHomePageActivity.this, getString(R.string.outcityselected), Toast.LENGTH_LONG).show();
                    }
                } else {

                    flegincityoroutcity = "0";

                    rentalpreebooking = false;
                    outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    incitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                    rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    outcitytext.setTextColor(getResources().getColor(R.color.white));
                    rentaltext.setTextColor(getResources().getColor(R.color.white));
                    incitytext.setTextColor(getResources().getColor(R.color.bleck));
                    getOffers();
                }
            }
        });
        rentallayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRental = true;
                flegincityoroutcity = "2";

                rentalVisible();

                rentallayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                rentaltext.setTextColor(getResources().getColor(R.color.bleck));
                outcitytext.setTextColor(getResources().getColor(R.color.white));
                incitytext.setTextColor(getResources().getColor(R.color.white));
                txtTagline.setVisibility(View.GONE);
                linOffer.setVisibility(View.GONE);

                rentalpreebooking = true;

                GeocodingLocation locationAddress1 = new GeocodingLocation();
                locationAddress1.getAddressFromLocation(editText1.getText().toString().trim(),
                        getApplicationContext(), new rentalHandler());

            }
        });


        // For RENTAL Ride Now
        ridenowrentel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustHomePageActivity.this, ListPakegesActivity.class);
                i.putExtra("Sorce", editText1.getText().toString().trim());
                i.putExtra("dest", editText2.getText().toString().trim());
                startActivity(i);
                finish();

            }
        });

        prebookingrental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                return;
            }
        });

        sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedanselected));
        sedantext.setTextSize(12);
        sedantext.setTextColor(getResources().getColor(R.color.appred));
        microtext.setTextColor(getResources().getColor(R.color.grey));
        suvtext.setTextColor(getResources().getColor(R.color.grey));

        microlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "micromini";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.microselected));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));

                microtext.setTextSize(12);
                microtext.setTextColor(getResources().getColor(R.color.appred));
                sedantext.setTextColor(getResources().getColor(R.color.grey));
                suvtext.setTextColor(getResources().getColor(R.color.grey));
            }
        });
        suvlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "suv";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suvselected));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));

                suvtext.setTextSize(12);
                suvtext.setTextColor(getResources().getColor(R.color.appred));
                microtext.setTextColor(getResources().getColor(R.color.grey));
                sedantext.setTextColor(getResources().getColor(R.color.grey));

                if (!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty())
                    Ridenowclass(false);
            }
        });
        sedanlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "sedan";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedanselected));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));

                sedantext.setTextSize(12);
                sedantext.setTextColor(getResources().getColor(R.color.appred));
                microtext.setTextColor(getResources().getColor(R.color.grey));
                suvtext.setTextColor(getResources().getColor(R.color.grey));

                if (!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty())
                    Ridenowclass(false);
            }
        });
        buslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ridenow.setVisibility(View.INVISIBLE);
                ridenow.setEnabled(false);
                cartype = "bus";
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.busselected));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));
            }
        });
        sharelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "share";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.shareselected));
            }
        });


        testlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText2.getText().toString().trim().equalsIgnoreCase("")) {

                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        selectvnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        if (flegincityoroutcity.equalsIgnoreCase("0")) {
                            if (citysource.equalsIgnoreCase(citydest)) {
                                if (editText2.getText().toString().trim().equalsIgnoreCase("")) {

                                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    editText2.setError(getString(R.string.destinationprovaid));

                                } else {

                                    SharedPreferences sharedpreferences;
                                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("cartype", cartype);
                                    editor.putString("fueltype", fueltype);
                                    editor.putString("flegincityoroutcity", flegincityoroutcity);
                                    editor.putString("incity", flegincityoroutcity);
                                    editor.putString("triptype", jarneyselected);
                                    editor.remove("date");
                                    editor.apply();

                                    Intent i = new Intent(CustHomePageActivity.this, SelectCarTypeActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
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

                                    SharedPreferences sharedpreferences;
                                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("cartype", cartype);
                                    editor.putString("fueltype", fueltype);
                                    editor.putString("flegincityoroutcity", flegincityoroutcity);
                                    editor.putString("incity", flegincityoroutcity);
                                    editor.putString("triptype", jarneyselected);
                                    editor.remove("date");
                                    editor.apply();
                                    ;

                                    Intent i = new Intent(CustHomePageActivity.this, SelectCarTypeActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception" + e.toString());
                }
            }
        });
        prebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {

                        SharedPreferences prefs1 = getSharedPreferences("Login", MODE_PRIVATE);
                        String endlat = prefs1.getString("start_lat", "");
                        String end_lng = prefs1.getString("start_lng", "");

                        String elat = prefs1.getString("end_lat", "");
                        String elng = prefs1.getString("end_lng", "");

                        String latitude = prefs1.getString("temp_lat", "");
                        String longitude = prefs1.getString("temp_long", "");

                        double dist_source = 0;
                        double dist_dest = 0;
                        double dist_dest1 = 0;
                        double theta = 0;

                        Boolean isincity = false;


                        if (flegincityoroutcity.equalsIgnoreCase("0")) {

                            if (endlat != null && endlat.trim().length() > 0 && end_lng != null && end_lng.trim().length() > 0) {
                                distance = getDistanceBetweenTwoPoint(center_lat, center_lan, endlat, end_lng);

                                if (isIncity(citysource, editText1.getText().toString())) {
                                    theta = Double.parseDouble(end_lng) - Double.parseDouble(elng);
                                    dist_dest = Math.sin(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                                    dist_dest = Math.acos(dist_dest);
                                    dist_dest = Math.toDegrees(dist_dest);
                                    dist_dest = dist_dest * 60 * 1.1515 * 1.609344;
                                    isincity = true;
                                } else if (isIncity(citydest, editText2.getText().toString())) {
                                    theta = Double.parseDouble(center_lan) - Double.parseDouble(end_lng);
                                    dist_source = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(endlat))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(theta));
                                    dist_source = Math.acos(dist_source);
                                    dist_source = Math.toDegrees(dist_source);
                                    dist_source = dist_source * 60 * 1.1515 * 1.609344;
                                    isincity = true;
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
                            }

                            if (flegincityoroutcity.equals("0")) {
                                if (distance > 15 && flegincityoroutcity.equals("0")) {
                                    flegincityoroutcity = "1";
                                } else {
                                    flegincityoroutcity = "0";
                                }
                            }

                            if (isDistnceOutOfAhemedabad(latitude, longitude) || isIncity(citysource, editText1.getText().toString())) {
                                if (isDistnceOutOfAhemedabad(elat, elng)) {
                                    Log.e("TAG", "941 outcity  ");
                                    rideLater(dist_dest, dist_dest1, dist_source);
                                } else {
                                    Log.e("TAG", " 944  incity  ");
                                    rideLater(dist_dest, dist_dest1, dist_source);
                                }
                            } else {
                                Log.e("TAG", " 1172 outcity  " + isincity);
                                Log.e("TAG", " select only Ahmedabad locations 949");
                                Toast.makeText(CustHomePageActivity.this, "select only Ahmedabad locations", Toast.LENGTH_SHORT).show();
                            }


                        } else {

//                                Log.e("TAG", " else flegincityoroutcity not zero   "+flegincityoroutcity);
//
//                                if (editText1.getText().toString().equalsIgnoreCase(editText2.getText().toString())) {
//                                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.incityselected), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                } else {
                            if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                editText2.setError(getString(R.string.destinationprovaid));
                            } else {

                                if (isDistnceOutOfAhemedabad(latitude, longitude) || isIncity(citysource, editText1.getText().toString())) {
                                    if (isDistnceOutOfAhemedabad(elat, elng)) {
                                        Log.e("TAG", "973 outcity  ");
                                        openDialog();
                                    } else {
                                        Log.e("TAG", " 975  incity  ");
                                        openDialog();
                                    }
                                } else {
                                    Toast.makeText(CustHomePageActivity.this, "select only Ahmedabad locations", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    Log.e(TAG, "Exception" + e.toString());
                }
            }

        });

        ridenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (editText1.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.sourcenprovaid), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {

                    SharedPreferences prefs1 = getSharedPreferences("Login", MODE_PRIVATE);
                    String endlat = prefs1.getString("start_lat", "");
                    String end_lng = prefs1.getString("start_lng", "");
                    Log.e(TAG, "RideLater Start Lat Long : " + endlat + "," + end_lng);
                    String elat = prefs1.getString("end_lat", "");
                    String elng = prefs1.getString("end_lng", "");
                    String latitude = prefs1.getString("temp_lat", "");
                    String longitude = prefs1.getString("temp_long", "");
                    double dist_source = 0;
                    double dist_dest = 0;
                    double dist_dest1 = 0;
                    double theta = 0;

                    Boolean isincity = false;

                    if (endlat != null && endlat.trim().length() > 0 && end_lng != null && end_lng.trim().length() > 0) {
                        if (isIncity(citysource, editText1.getText().toString())) {
//                            Toast.makeText(CustHomePageActivity.this, "source", Toast.LENGTH_SHORT).show();
                            theta = Double.parseDouble(end_lng) - Double.parseDouble(elng);
                            dist_dest = Math.sin(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                            dist_dest = Math.acos(dist_dest);
                            dist_dest = Math.toDegrees(dist_dest);
                            dist_dest = dist_dest * 60 * 1.1515 * 1.609344;
                            isincity = true;
                        } else if (isIncity(citydest, editText2.getText().toString())) {
                            theta = Double.parseDouble(center_lan) - Double.parseDouble(end_lng);
                            dist_source = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(endlat))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(theta));
                            dist_source = Math.acos(dist_source);
                            dist_source = Math.toDegrees(dist_source);
                            dist_source = dist_source * 60 * 1.1515 * 1.609344;
                            isincity = true;
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

                    }

                    if (flegincityoroutcity.equals("0")) {
                        if (distance > 15 && flegincityoroutcity.equals("0")) {
                            flegincityoroutcity = "1";
                        } else {
                            flegincityoroutcity = "0";
                        }
                    }

                    if (isDistnceOutOfAhemedabad(latitude, longitude) || isIncity(citysource, editText1.getText().toString())) {
                        if (isDistnceOutOfAhemedabad(elat, elng)) {
                            Log.e("TAG", "1165 outcity  ");
                            ridenow(dist_dest, dist_source, isincity);
                        } else {
                            Log.e("TAG", " 1168  incity  ");
                            ridenow(dist_dest, dist_source, isincity);
                        }
                    } else {
                        Log.e("TAG", " 1172 outcity  " + isincity);
                        Log.e("TAG", " select only Ahmedabad locations 1080");
                        Toast.makeText(CustHomePageActivity.this, "select only Ahmedabad locations", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });


        try {
            Intent i = getIntent();
            placefrom = i.getStringExtra("placefrom");

            Log.i("placefrom", "onCreate: -- " + placefrom);

            //for set current location in edittext @Anil

            if (flegincityoroutcity.equalsIgnoreCase("2")) {
                GeocodingLocation locationAddress1 = new GeocodingLocation();
                locationAddress1.getAddressFromLocation(editText1.getText().toString().trim(),
                        getApplicationContext(), new rentalHandler());
            }

            Log.i("zxcz", placefrom);

            GeocodingLocation locationAddress = new GeocodingLocation();
            locationAddress.getAddressFromLocation(placefrom, getApplicationContext(), new GeocoderHandler());
            SharedPreferences sharedpreferences;
            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("start_address", placefrom);
            editor.apply();
            repet = true;
        } catch (Exception e) {
            Log.i("Exception ", e.toString());
        }
        try {
            Intent i = getIntent();
            placeto = i.getStringExtra("placeto");
            Log.i("placetoplaceto", placeto);
            editText2.setText(placeto);


            String selected = prefs.getString("start_address", "");
            placefrom = selected;
            Log.i("selected", selected);
            editText1.setText(selected);

            GeocodingLocation locationAddress1 = new GeocodingLocation();
            locationAddress1.getAddressFromLocation(placefrom,
                    getApplicationContext(), new GeocoderHandler());
            SharedPreferences sharedpreferences;
            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("start_address", editText1.getText().toString().trim());
            editor.putString("end_address", editText2.getText().toString().trim());
            editor.apply();
            repet = true;


        } catch (Exception e) {
            Log.e("Exception  to", e.toString());
        }


        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(CustHomePageActivity.this, SourceSearchActivity.class);
                startActivity(i);
//                finish();
                return false;
            }
        });


        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("start_address", editText1.getText().toString().trim());
                    editor.apply();
                    Intent i = new Intent(CustHomePageActivity.this, DestinationSearchActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                    Log.e(TAG, "Exception" + e.toString());
                }
                return false;
            }
        });


        SharedPreferences prefs2 = getSharedPreferences("Login", MODE_PRIVATE);

        String end_address = prefs2.getString("end_address", "");
        String start_address = prefs2.getString("start_address", "");

        Log.i(TAG, "onCreate: " + end_address + " start_address : " + start_address);


        Log.i("TEST 123", "zjhcvjhxcvxzc   " + end_address + "    start_address  " + start_address);
        if (!start_address.equalsIgnoreCase("")) {
            Log.i("TEST 123", end_address);
            editText2.setText(end_address);
            editText1.setText(start_address);
            String flegincityoroutcitypau = prefs2.getString("flegincityoroutcitypau", "");

            Log.e(TAG, "Rental Visigone recreate " + flegincityoroutcitypau);

            if (flegincityoroutcitypau.equalsIgnoreCase("2")) {
                isRental = true;
            } else {
                isRental = false;
            }

            String cartypepau = prefs2.getString("cartypepau", "");
            if (flegincityoroutcitypau.equalsIgnoreCase("0")) {
                flegincityoroutcity = "0";
                Log.e(TAG, "Rental Visigone 1287 ");
                newrental.setVisibility(View.GONE);
                testlinear.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.VISIBLE);
                outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                incitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                outcitytext.setTextColor(getResources().getColor(R.color.white));
                rentaltext.setTextColor(getResources().getColor(R.color.white));
                incitytext.setTextColor(getResources().getColor(R.color.bleck));
                rentalpreebooking = false;
            } else if (flegincityoroutcitypau.equalsIgnoreCase("1")) {
                rentalpreebooking = false;
                flegincityoroutcity = "1";
                Log.e(TAG, "Rental Visigone 1301 ");
                newrental.setVisibility(View.GONE);
                testlinear.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.VISIBLE);
                incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                outcitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                incitytext.setTextColor(getResources().getColor(R.color.white));
                rentaltext.setTextColor(getResources().getColor(R.color.white));
                outcitytext.setTextColor(getResources().getColor(R.color.bleck));
            } else if (flegincityoroutcitypau.equalsIgnoreCase("2")) {
                testlinear.setVisibility(View.GONE);
                relativeLayout1.setVisibility(View.VISIBLE);
                newrental.setVisibility(View.VISIBLE);
                destlayout.setVisibility(View.VISIBLE);
                sourcelayout.setVisibility(View.VISIBLE);
                rentalpreebooking = true;
                flegincityoroutcity = "2";
                rentallayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                rentaltext.setTextColor(getResources().getColor(R.color.bleck));
                outcitytext.setTextColor(getResources().getColor(R.color.white));
                incitytext.setTextColor(getResources().getColor(R.color.white));

                GeocodingLocation locationAddress1 = new GeocodingLocation();
                locationAddress1.getAddressFromLocation(editText1.getText().toString().trim(),
                        getApplicationContext(), new rentalHandler());

            }

            if (cartypepau.equalsIgnoreCase("micromini")) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "micromini";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.microselected));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));
                microtext.setTextSize(12);
                microtext.setTextColor(getResources().getColor(R.color.appred));
                sedantext.setTextColor(getResources().getColor(R.color.grey));
                suvtext.setTextColor(getResources().getColor(R.color.grey));
            } else if (cartypepau.equalsIgnoreCase("suv")) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "suv";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suvselected));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));
                suvtext.setTextSize(12);
                suvtext.setTextColor(getResources().getColor(R.color.appred));
                microtext.setTextColor(getResources().getColor(R.color.grey));
                suvtext.setTextColor(getResources().getColor(R.color.grey));
            } else if (cartypepau.equalsIgnoreCase("sedan")) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "sedan";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedanselected));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));
                sedantext.setTextSize(12);
                sedantext.setTextColor(getResources().getColor(R.color.appred));
                microtext.setTextColor(getResources().getColor(R.color.grey));
                suvtext.setTextColor(getResources().getColor(R.color.grey));
            } else if (cartypepau.equalsIgnoreCase("bus")) {
                ridenow.setVisibility(View.INVISIBLE);
                ridenow.setEnabled(false);
                cartype = "bus";
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.busselected));
                share.setImageDrawable(getResources().getDrawable(R.drawable.share));
            } else if (cartypepau.equalsIgnoreCase("share")) {
                ridenow.setVisibility(View.VISIBLE);
                cartype = "share";
                ridenow.setEnabled(true);
                micro.setImageDrawable(getResources().getDrawable(R.drawable.micro));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.suv));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.sedan));
                bus.setImageDrawable(getResources().getDrawable(R.drawable.bus));
                share.setImageDrawable(getResources().getDrawable(R.drawable.shareselected));
            }
        }

        if (editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty())
            getAllCars();

        if (!editText2.getText().toString().isEmpty()) {
            progstop();
        } else {
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Check Storage permission Granted ===>> ");

            String extStorageDirectory = null;
            File file;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                file = new File(getApplication().getFilesDir(), "RaadarbarCrash");
            } else {
                extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                file = new File(extStorageDirectory, "RaadarbarCrash");
            }

            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            Log.i(TAG, "Check Storage Not Granted ===>> ");

            ActivityCompat.requestPermissions(this, PERMISSIONS, FILE_PERMISSION_REQUEST_CODE);
        }

    }

    public void ridenow(double dist_dest, double dist_source, Boolean isincity) {
        if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {

            if (flegincityoroutcity.equalsIgnoreCase("0")) {

                if (citydest != null && citydest != null && citysource.equalsIgnoreCase(citydest) || isIncity(citysource, editText1.getText().toString()) || isIncity(citydest, editText2.getText().toString())) {

                    if (editText2.getText().toString().toString().trim().equalsIgnoreCase("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        editText2.setError(getString(R.string.destinationprovaid));
                    } else {

                        if (dist_dest < 15 || dist_source < 15 || isincity) {
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("cartype", cartype);
                            editor.putString("fueltype", fueltype);
                            editor.apply();

                            if (isBookAvailable) {
                                Ridenowclass(true);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                } else {
                    Ridenowclass(true);
                }
            } else {

                if (citysource != null && citydest != null && citysource.equalsIgnoreCase(citydest) && dist_source < 15 && dist_dest < 15) {

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
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("cartype", cartype);
                        editor.putString("fueltype", fueltype);
                        editor.apply();
                        if (citysource != null && citydest != null && isIncity(citysource, editText1.getText().toString()) || isIncity(citydest, editText2.getText().toString())) {
                            flegincityoroutcity = "0";
                        }

                        if (isBookAvailable) {
                            Ridenowclass(true);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }
            }
        }
    }

    public void rideLater(double dist_dest, double dist_dest1, double dist_source) {

        if (editText2.getText().toString().trim().equalsIgnoreCase("")) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            if (flegincityoroutcity.equalsIgnoreCase("0")) {
                if (isIncity(citysource, editText1.getText().toString()) || isIncity(citydest, editText2.getText().toString())) {
                    if (editText2.getText().toString().toString().trim().equalsIgnoreCase("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        editText2.setError(getString(R.string.destinationprovaid));
                    } else {
                        if ((dist_dest < 15 || dist_dest1 < 15) && dist_source < 15) {
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("cartype", cartype);
                            editor.putString("fueltype", fueltype);
                            editor.apply();
                            openDialog();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    }
                } else {

                    if ((dist_dest < 15 || dist_dest1 < 15) && dist_source < 15) {
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("cartype", cartype);
                        editor.putString("fueltype", fueltype);
                        editor.apply();
                        openDialog();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.outcityselected), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            } else {
                if (citysource.equalsIgnoreCase(citydest) && dist_source < 15 && dist_dest < 15) {

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
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("cartype", cartype);
                        editor.putString("fueltype", fueltype);
                        editor.apply();
                        if (isIncity(citysource, editText1.getText().toString()) || isIncity(citydest, editText2.getText().toString())) {
                            flegincityoroutcity = "0";
                        }

                        if (editText2 != null && editText2.getText().toString().trim().equalsIgnoreCase("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.destinationprovaid), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            editText2.setError(getString(R.string.destinationprovaid));
                        } else {
                            openDialog();
                        }
                    }
                }
            }
        }

    }

    public void rentalVisible() {

        testlinear.setVisibility(View.GONE);
        relativeLayout1.setVisibility(View.VISIBLE);
        newrental.setVisibility(View.VISIBLE);
        destlayout.setVisibility(View.VISIBLE);
        sourcelayout.setVisibility(View.VISIBLE);

    }

    public void rentalInVisible() {

        testlinear.setVisibility(View.VISIBLE);
        relativeLayout1.setVisibility(View.VISIBLE);
        destlayout.setVisibility(View.VISIBLE);
        newrental.setVisibility(View.GONE);

    }


    private void initview() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationExpandableListView
                .init(this)
                .addHeaderModel(new HeaderModel(getResources().getString(R.string.offer), R.drawable.ic_tag))
                .addHeaderModel(new HeaderModel(getResources().getString(R.string.goodfeature), R.drawable.ic_star))
                .addHeaderModel(new HeaderModel(getResources().getString(R.string.ridehi), R.drawable.ic_history_new))
                .addHeaderModel(new HeaderModel(getResources().getString(R.string.help), R.drawable.help))
                .addHeaderModel(new HeaderModel(getResources().getString(R.string.raa_wallet), R.drawable.ic_wallet))
                .addHeaderModel(new HeaderModel(getResources().getString(R.string.myqr_code), R.drawable.qr_code))
                .addHeaderModel(new HeaderModel(getResources().getString(R.string.log_out), R.drawable.logout))
                .build()
                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition);

                        if (id == 0) {
                            startActivity(new Intent(CustHomePageActivity.this, WhyRaadarbarActivity.class));
                            finish();
                        } else if (id == 1) {
                            //Orders Menu
                            startActivity(new Intent(CustHomePageActivity.this, FeaturesActivity.class));
                            finish();//                            drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 2) {
                            //Orders Menu
                            startActivity(new Intent(CustHomePageActivity.this, RideHistoryActivity.class));
                            finish();//                            drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 3) {
                            startActivity(new Intent(CustHomePageActivity.this, HelpActivity.class));
                            finish();

                        } else if (id == 4) {
                            startActivity(new Intent(CustHomePageActivity.this, WalletActivity.class));
                            finish();//                            drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 5) {
                            Intent i = new Intent(CustHomePageActivity.this, RaaQRCodeActivity.class);
                            i.putExtra("iscust", 1);
                            startActivity(i);
                        }else if (id == 6) {
                                logoutclass();
                            }
                            return false;
                        }
                    })
                            .

                    addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick (ExpandableListView parent, View v,
                        int groupPosition, int childPosition, long id){
                            navigationExpandableListView.setSelected(groupPosition, childPosition);

                            if (id == 0) {
                                startActivity(new Intent(CustHomePageActivity.this, ProfileActivity.class));
                                finish();//                            drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 1) {
                                startActivity(new Intent(CustHomePageActivity.this, SelectLanguageActivity.class));
                                finish();//                            drawer.closeDrawer(GravityCompat.START);
//                            Common.showToast(context, "Woman's Fashion");
                            }
                            return false;
                        }
                    });
        navigationExpandableListView.expandGroup(2);

                    View headerView = navigationView.getHeaderView(0);

                    Cust_name =headerView.findViewById(R.id.cust_name);
                    monumber =headerView.findViewById(R.id.monumber);
                    imageView =headerView.findViewById(R.id.imageView);
                }

        private double deg2rad ( double deg){
            return (deg * Math.PI / 180.0);
        }

        private double rad2deg ( double rad){
            return (rad * 180.0 / Math.PI);
        }


        public void customToast (String msg){
            //Creating the LayoutInflater instance
            LayoutInflater li = getLayoutInflater();
            //Getting the View object as defined in the customtoast.xml file
            View layout = li.inflate(R.layout.dialog_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));

            //Creating the Toast object
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(layout);
            toast.setText(msg);//setting the view of custom toast layout
            toast.show();
        }

        Dialog dialog;
        RadioGroup selectjourny;
        RadioButton radioselectrd;
        String jarneyselected = "0";

        public void showPopUp () {

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_popup_dialog);
            dialog.setTitle(getResources().getString(R.string.selectfuel));

            RelativeLayout diesel = dialog.findViewById(R.id.diesellayout);
            RelativeLayout petrol = dialog.findViewById(R.id.petrollayout);
            RelativeLayout cng = dialog.findViewById(R.id.cnglayout);

            SharedPreferences prefs2 = getSharedPreferences("Login", MODE_PRIVATE);
            String cartypepau = prefs2.getString("cartype", "");
            if (cartypepau != null && "suv".equalsIgnoreCase(cartypepau)) {
                cng.setVisibility(View.GONE);
            }


            selectjourny = dialog.findViewById(R.id.selectjourny);

            diesel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //builder.cancel();
                    try {
                        int selectedId = selectjourny.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        radioselectrd = (RadioButton) dialog.findViewById(selectedId);

                        if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single trip")) {
                            jarneyselected = "0";
                        } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return trip")) {
                            jarneyselected = "1";
                        }

                        fueltype = "diesel";
                        dialog.dismiss();
                        Ridenowclass(true);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception : " + e.toString());
                    }
                }
            });
            petrol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = selectjourny.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioselectrd = (RadioButton) dialog.findViewById(selectedId);

                    if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single trip")) {
                        jarneyselected = "0";
                    } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return trip")) {
                        jarneyselected = "1";
                    }

                    fueltype = "petrol";
                    dialog.dismiss();
                    Ridenowclass(true);
                }
            });
            cng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = selectjourny.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioselectrd = (RadioButton) dialog.findViewById(selectedId);

                    if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single trip")) {
                        jarneyselected = "0";
                    } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return trip")) {
                        jarneyselected = "1";
                    }

                    fueltype = "cng";
                    dialog.dismiss();
                    Ridenowclass(true);
                }
            });
            dialog.setCanceledOnTouchOutside(false);

            dialog.show();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            // The absolute width of the available display size in pixels.
            int displayWidth = displayMetrics.widthPixels;
            // The absolute height of the available display size in pixels.
            int displayHeight = displayMetrics.heightPixels;

            // Initialize a new window manager layout parameters
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

            // Copy the alert dialog window attributes to new layout parameter instance
            layoutParams.copyFrom(dialog.getWindow().getAttributes());

            // Set the alert dialog window width and height
            // Set alert dialog width equal to screen width 90%
            // int dialogWindowWidth = (int) (displayWidth * 0.9f);
            // Set alert dialog height equal to screen height 90%
            // int dialogWindowHeight = (int) (displayHeight * 0.9f);

            // Set alert dialog width equal to screen width 70%
            int dialogWindowWidth = (int) (displayWidth * 0.95f);
            // Set alert dialog height equal to screen height 70%
            //int dialogWindowHeight = (int) (displayHeight * 0.7f);

            // Set the width and height for the layout parameters
            // This will bet the width and height of alert dialog
            layoutParams.width = dialogWindowWidth;
            //layoutParams.height = dialogWindowHeight;
            // Apply the newly created layout parameters to the alert dialog window
            dialog.getWindow().setAttributes(layoutParams);

        }

        public void openDialog () {

            final TextView tvShowNumbers;
            final TimePicker simpleTimePicker;
            final DatePicker simpleDatePicker;

            dialog = new Dialog(CustHomePageActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_date_time);

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
                        dialog.cancel();
//                    showDialog(DIALOG_MENU);
                        return true;
                    }
                    return false;
                }
            });

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

                    if (String.valueOf(simpleDatePicker.getYear()).equals(currentYear)) {

                        String format = "";

                        int Hour = simpleTimePicker.getCurrentHour();
                        final int Minute = simpleTimePicker.getCurrentMinute();
                        if (Hour == 0) {
                            Hour += 12;
                            format = "AM";
                        } else if (Hour == 12) {
                            format = "PM";
                        } else if (Hour > 12) {
                            Hour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        Date date = null;
//                "08:48: AM";
                        String dtStart = Integer.valueOf(simpleDatePicker.getMonth() + 1) + "/" + simpleDatePicker.getDayOfMonth() + "/" + simpleDatePicker.getYear() + " " + Hour + ":" + Minute + " " + format;
                        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                        try {
                            date = formatTime.parse(dtStart);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());
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
                                Toast.makeText(CustHomePageActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
                            } else {

                                if (rentalpreebooking) {
                                    Intent intent = new Intent(CustHomePageActivity.this, ListPakegesActivity.class);
                                    intent.putExtra("Sorce", editText1.getText().toString().trim());
                                    intent.putExtra("dest", editText2.getText().toString().trim());
                                    intent.putExtra("date", DateUtils.convertDateToString(date));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    SharedPreferences sharedpreferences;
                                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("cartype", cartype);
                                    editor.putString("fueltype", fueltype);
                                    editor.putString("date", DateUtils.convertDateToString(date));
                                    editor.putString("incity", flegincityoroutcity);
                                    editor.putString("triptype", jarneyselected);
                                    editor.apply();
                                    Intent intent = new Intent(CustHomePageActivity.this, SelectCarTypeActivity.class);
                                    intent.putExtra("date", DateUtils.convertDateToString(date));
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(CustHomePageActivity.this, " Please Select Current Year ", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
            simpleTimePicker.setIs24HourView(false); // used to display AM/PM mode
            // perform set on time changed listener event

            String format = "";
            int Hour = simpleTimePicker.getCurrentHour();
            final int Minute = simpleTimePicker.getCurrentMinute();
            if (Hour == 0) {
                Hour += 12;
                format = "AM";
            } else if (Hour == 12) {
                format = "PM";
            } else if (Hour > 12) {
                Hour -= 12;
                format = "PM";
            } else {
                format = "AM";
            }

            String DateTime1 = simpleDatePicker.getDayOfMonth() + "-" + Integer.valueOf(simpleDatePicker.getMonth() + 1) + "-" + simpleDatePicker.getYear() + " " + Hour + ":" + Minute + " " + format;
            tvShowNumbers.setText(DateTime1);

            final int finalHour = Hour;
            simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                    String format = "";
                    int Hour = hourOfDay;
                    final int Minute = minute;
                    if (Hour == 0) {
                        Hour += 12;
                        format = "AM";
                    } else if (Hour == 12) {
                        format = "PM";
                    } else if (Hour > 12) {
                        Hour -= 12;
                        format = "PM";
                    } else {
                        format = "AM";
                    }

                    String DateTime1 = simpleDatePicker.getDayOfMonth() + "-" + Integer.valueOf(simpleDatePicker.getMonth() + 1) + "-" + simpleDatePicker.getYear() + " " + Hour + ":" + Minute + " " + format;
                    tvShowNumbers.setText(DateTime1);
                }
            });


//        simpleDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                String format = "";
//                int Hour = simpleTimePicker.getHour();
//                final int Minute = simpleTimePicker.getMinute();
//                if (Hour == 0) {
//                    Hour += 12;
//                    format = "AM";
//                } else if (Hour == 12) {
//                    format = "PM";
//                } else if (Hour > 12) {
//                    Hour -= 12;
//                    format = "PM";
//                } else {
//                    format = "AM";
//                }
//
//                String DateTime1 = dayOfMonth + "-" + (monthOfYear +1)+ "-" + year + " " + Hour + ":" + Minute + " " + format;
//                tvShowNumbers.setText(DateTime1);
//            }
//        });

//        simpleDatePicker = new DatePickerDialog(MainActivity.this,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    }
//                }, year, month, day);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            simpleDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//                @Override
//                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                    String format = "";
//                    int Hour = simpleTimePicker.getCurrentHour();
//                    final int Minute = simpleTimePicker.getCurrentMinute();
//                    if (Hour == 0) {
//                        Hour += 12;
//                        format = "AM";
//                    } else if (Hour == 12) {
//                        format = "PM";
//                    } else if (Hour > 12) {
//                        Hour -= 12;
//                        format = "PM";
//                    } else {
//                        format = "AM";
//                    }
//
//                    String DateTime1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " " + Hour + ":" + Minute + " " + format;
//                    tvShowNumbers.setText(DateTime1);
//                }
//            });
//        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            simpleDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);

                    String format = "";
                    int Hour = simpleTimePicker.getCurrentHour();
                    final int Minute = simpleTimePicker.getCurrentMinute();
                    if (Hour == 0) {
                        Hour += 12;
                        format = "AM";
                    } else if (Hour == 12) {
                        format = "PM";
                    } else if (Hour > 12) {
                        Hour -= 12;
                        format = "PM";
                    } else {
                        format = "AM";
                    }

                    String DateTime1 = dayOfMonth + "-" + (month + 1) + "-" + year + " " + Hour + ":" + Minute + " " + format;
                    tvShowNumbers.setText(DateTime1);

                }
            });
//        }

            dialog.show();

        }

        private void displayLocationSettingsRequest (Context context){
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
                            Log.i(TAG, "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(CustHomePageActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });


        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
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

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        mMap.setMyLocationEnabled(true);
                                    }
                                } else {
                                    mMap.setMyLocationEnabled(true);
                                }

                            }
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            if (mMap != null)
                                mMap.setMyLocationEnabled(true);
                            break;
                        }
                        case Activity.RESULT_CANCELED: {
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
            }
        }

        public boolean checkLocationPermission () {
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

        @Override
        public void networkAvailable () {
            network = Boolean.TRUE;
        }

        @Override
        public void networkUnavailable () {
            network = Boolean.FALSE;
        }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            this.mWakeLock.release();
            connectivityReceiver.removeListener(this);
            this.unregisterReceiver(connectivityReceiver);
        }

        private class GeocoderHandler extends Handler {

            @Override
            public void handleMessage(Message message) {

                switch (message.what) {

                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress1 = bundle.getString("address");
                        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                        String selected = prefs.getString("start_address", "");

                        try {
                            String[] latlong1 = locationAddress1.split(",");

                            SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("temp_lat", latlong1[0]);
                            editor.putString("temp_long", latlong1[1]);
                            editor.apply();

                            getcars_latlong(Double.parseDouble(latlong1[0]), Double.parseDouble(latlong1[1]));
                        } catch (Exception e) {
                        }
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
                            Log.e(TAG, " List Of Address " + new Gson().toJson(message.getData()));
                            locationAddress2 = bundle.getString("address");
                            //      Log.i("locationAddress2",locationAddress1+"  Testing    "+locationAddress2);
                            SharedPreferences sharedpreferences2 = getSharedPreferences("currLatLng", Context.MODE_PRIVATE);
                            Log.e(TAG, "Current Lat Long is : " + sharedpreferences2.getString("currLat", "") + " " + sharedpreferences2.getString("currLong", ""));

                            if (sharedpreferences2.getString("currLat", "").isEmpty() && sharedpreferences2.getString("currLong", "").isEmpty()) {
                                if (!editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                    placefrom = editText1.getText().toString().trim();
                                    String[] latlong1 = locationAddress1.split(",");
                                    String latitude1 = latlong1[0];
                                    String longitude1 = latlong1[1];

                                    Log.e(TAG, " Location  : " + latitude1 + "," + longitude1);
                                    if (isDistnceOutOfAhemedabad(latitude1, longitude1)) {
                                        settingmap(locationAddress1, locationAddress2);
                                    } else {
                                        Log.e("TAG", " select only Ahmedabad locations 2327");
                                        flegincityoroutcity = "1";
                                        newrental.setVisibility(View.GONE);
                                        testlinear.setVisibility(View.VISIBLE);
                                        relativeLayout1.setVisibility(View.VISIBLE);
                                        destlayout.setVisibility(View.VISIBLE);

                                        rentalpreebooking = false;
                                        incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                                        outcitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                                        rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                                        incitytext.setTextColor(getResources().getColor(R.color.white));
                                        rentaltext.setTextColor(getResources().getColor(R.color.white));
                                        outcitytext.setTextColor(getResources().getColor(R.color.bleck));
                                        Toast.makeText(CustHomePageActivity.this, "select only Ahmedabad locations", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (!editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                    placefrom = editText1.getText().toString().trim();
                                    settingmap(sharedpreferences2.getString("currLat", "") + "," + sharedpreferences2.getString("currLong", ""), locationAddress2);
                                }
                            }
                        } catch (Exception e) {
                            Log.i("Exception sdsdsdsdsd", e.toString());
                        }
                        break;
                    default:
                        locationAddress2 = null;
                }
            }
        }

        private class rentalHandler extends Handler {
            @Override
            public void handleMessage(Message message) {

                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        String rentalAddress = bundle.getString("address");
                        rental(rentalAddress);
                        break;
                    default:
                        locationAddress1 = null;
                }

            }
        }

        // Rental Map Show
        public void rental (String from){

            try {
                MarkerPoints.clear();
                mMap.clear();

                String[] latlong1 = from.split(",");
                double latitude1 = Double.parseDouble(latlong1[0]);
                double longitude1 = Double.parseDouble(latlong1[1]);
                origin = new LatLng(latitude1, longitude1);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.start);

                mMap.addMarker(new MarkerOptions().position(origin)
                        .title("Start Ride ").icon(icon));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latlong1[0]), Double.parseDouble(latlong1[1])), 12.0f));
            } catch (Exception e) {
            }
        }

        public static LatLng midPoint ( double lat1, double lon1, double lat2, double lon2){

            double dLon = Math.toRadians(lon2 - lon1);

            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);
            lon1 = Math.toRadians(lon1);

            double Bx = Math.cos(lat2) * Math.cos(dLon);
            double By = Math.cos(lat2) * Math.sin(dLon);
            double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
            double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

            return new LatLng(lat3, lon3);

        }

        public void settingmap (String latlon1, String latlon2){


            Log.i("checkincity ", " settingmap ==>> latlon1 " + locationAddress1 + " elng ==>> " + latlon2);

            MarkerPoints.clear();
            mMap.clear();

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.start);
            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.end);

            try {

                String[] latlong1 = latlon1.split(",");
                double latitude1 = 0;
                double longitude1 = 0;

                double latitude2 = 0;
                double longitude2 = 0;

                if (placefrom.equalsIgnoreCase("")) {
                    origin = new LatLng(Latitudedefoult, Longitudedefoult);
                    Geocoder gcd = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(Latitudedefoult, Longitudedefoult, 1);
                    if (addresses.size() > 0)
                        citysource = addresses.get(0).getLocality();
                } else {

                    latitude1 = Double.parseDouble(latlong1[0]);
                    longitude1 = Double.parseDouble(latlong1[1]);

                    origin = new LatLng(latitude1, longitude1);
                    Geocoder gcd = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses1 = gcd.getFromLocation(Double.parseDouble(latlong1[0]), Double.parseDouble(latlong1[1]), 1);
                    if (addresses1.size() > 0) {
                        if (addresses1.get(0).getLocality() == null)
                            citysource = addresses1.get(0).getSubAdminArea();
                        else
                            citysource = addresses1.get(0).getLocality();
                    }
                }

                mMap.addMarker(new MarkerOptions().position(origin)
                        .title("Start Ride ").icon(icon));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(9f));
                String[] latlong2 = latlon2.split(",");
                latitude2 = Double.parseDouble(latlong2[0]);
                longitude2 = Double.parseDouble(latlong2[1]);
                dest = new LatLng(latitude2, longitude2);
                mMap.addMarker(new MarkerOptions().position(dest)
                        .title("End Ride").icon(icon1));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(9f));

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
                    Log.e(TAG, "Exception" + e.toString());
                }

                String url = getUrl(origin, dest);
                FetchUrl FetchUrl = new FetchUrl();
                FetchUrl.execute(url);

                ridenow.setEnabled(true);
                selectvnew.setEnabled(true);
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latlong1[0]), Double.parseDouble(latlong1[1])), 11f));

                if (flegincityoroutcity.equalsIgnoreCase("0"))
                    getAllCars();
//                apicallingpois(latitude1, longitude1,latitude2, longitude2);

                if (mMap != null) {
                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(latlong1[0]),
                                    Double.valueOf(latlong1[1])));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(11f);

                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.toString());
            }
        }


        public void getAllCars () {

            StringRequest postRequest = new StringRequest(Request.Method.GET, getcarsnearest,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                BaseResponseProvider baseResponseProvider = new Gson().fromJson(response, BaseResponseProvider.class);

                                if (baseResponseProvider != null && baseResponseProvider.getResponseProvider().getResponseProvider() != null) {

                                    int i = 0;


                                    for (Provider provider : baseResponseProvider.getResponseProvider().getResponseProvider()) {

                                        i++;

                                        mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.valueOf(provider.getLast_lat()), Double.valueOf(provider.getLast_long())))
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_new)));

//
//                                    LatLng latLng = new LatLng(Double.valueOf(provider.getLast_lat()), Double.valueOf(provider.getLast_long()));
//                                    MarkerOptions markerOptions = new MarkerOptions();
//                                    markerOptions.position(latLng);
//                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_new));
//                                    markerOptions.getPosition();
//                                    MarkerPoints.add(latLng);
//                                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                                    }
                                }


                            } catch (Exception e) {
                                Log.e(TAG, " Exception Error Allcars : " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, " Exception Volley  Error Allcars : " + error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);

        }

        private interface LatLngInterface {
            LatLng interpolate(float fraction, LatLng a, LatLng b);

            class LinearFixed implements LatLngInterface {
                @Override
                public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                    double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                    double lngDelta = b.longitude - a.longitude;
                    if (Math.abs(lngDelta) > 180) lngDelta -= Math.signum(lngDelta) * 360;
                    double lng = lngDelta * fraction + a.longitude;
                    return new LatLng(lat, lng);
                }
            }
        }

        private void carAnim ( final Marker marker, final LatLng start, final LatLng end){


            try {
                Log.d(TAG, "RRR MainActivity.carAnim");
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1900);
                final LatLngInterface latLngInterpolator = new LatLngInterface.LinearFixed();
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        canCarAnim = false;
                        float v = animation.getAnimatedFraction();
                        LatLng newPos = latLngInterpolator.interpolate(v, start, end);
                        marker.setPosition(newPos);
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setRotation(bearingBetweenLocations(start, end));
                    }

                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        canCarAnim = true;
                    }
                });
                animator.start();

            } catch (Exception e) {
            }
        }

        protected float bearingBetweenLocations (LatLng latLng1, LatLng latLng2){
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
            return (float) brng;
        }

        public void getcars_latlong ( final double latitudedefoult1, final double longitudedefoult1)
        {

            SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
            logtokan = prefs.getString("tokan", "");
            StringRequest postRequest = new StringRequest(Request.Method.POST, getcars_duration,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("asdf carduration", "response" + response);
                            try {
                                JSONObject responce = new JSONObject(response);
                                JSONObject success = responce.getJSONObject("success");
                                String status = success.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    JSONArray cars = success.getJSONArray("cars");
                                    for (int i = 0; i < cars.length(); i++) {
                                        JSONObject object = cars.getJSONObject(i);
                                        if (i == 0) {
                                            microtext.setText(object.getString("Duration") + " mins");
                                        }
                                        if (i == 1) {
                                            sedantext.setText(object.getString("Duration") + " mins");
                                        }
                                        if (i == 2) {
                                            suvtext.setText(object.getString("Duration") + " mins");
                                        }

                                        Log.d("asdf duration", object.getString("Duration"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("start_lat", String.valueOf(latitudedefoult1));
                    params.put("start_lng", String.valueOf(longitudedefoult1));

                    Log.i("start_lat", String.valueOf(latitudedefoult1));
                    Log.i("start_lng", String.valueOf(longitudedefoult1));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + logtokan);
                    return params;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(this);
            //requestQueue.add(jsonObjReq);
            queue.add(postRequest);
        }

        private String getUrl (LatLng origin, LatLng dest){

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

                // For storing data from web service
                String data = "";

                try {
                    // Fetching the data from web service
                    data = downloadUrl(url[0]);
                    Log.d("Background Task data", data.toString());
                } catch (Exception e) {
                    Log.d("Background Task", e.toString());
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

        private String downloadUrl (String strUrl) throws IOException {
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
                Log.d("Exception", e.toString());
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

                    Log.i("custhome", " Map data Response   : " + jObject);

//                if(jObject.getString("status") == null ||  !jObject.getString("status").equals("OVER_QUERY_LIMIT")) {

                    DataParser parser = new DataParser();
                    //Log.d("ParserTask", parser.toString());

                    // Starts parsing data

                    Log.i("custhome", "Custhome get :  ---->> Raout Response  : " + jObject.toString());


                    routes = parser.parse(jObject);
                    //Log.d("ParserTask ","Executing routes");
                    ///Log.d("ParserTask",routes.toString());
//                jObject = new JSONObject(jsonData[1]);

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


//                if (!sharedpreferences2.getString("currLat", "").isEmpty() && !sharedpreferences2.getString("currLong", "").isEmpty()) {
                    String start_lat = start_location.getString("lat");
                    String start_lng = start_location.getString("lng");
//                }

                    Log.i("checkincity", " param  totalhr " + durationvalue + " totalkm : " + distancevalue);


                    String distanceText = distance.getString("text");


                    String distancevalue = distance.getString("value");


                    String durationText = duration.getString("text");
                    String durationvalue = duration.getString("value");

                    Log.e("custhome", "Custhome get :  ---->> Raout distancevalue  : " + durationText + " ====>> Duration === >> " + distanceText);

                    //   Log.e("distanceText",distancevalue+"   "+durationvalue);
                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("jsonData", legs.toString());
                    editor.putString("durationvalue", durationvalue);
                    editor.putString("distanceText", distanceText);
                    editor.putString("durationText", durationText);
                    editor.putString("distancevalue", distancevalue.toString());
                    editor.putString("end_address", editText2.getText().toString().trim());
                    editor.putString("start_address", editText1.getText().toString().trim());
                    editor.putString("end_lat", end_lat);
                    editor.putString("end_lng", end_lng);

                    SharedPreferences sharedpreferences2 = getSharedPreferences("currLatLng", Context.MODE_PRIVATE);

                    Log.e(TAG, "Current Lat Long is : " + sharedpreferences2.getString("currLat", "") + " " + sharedpreferences2.getString("currLong", ""));


                    editor.putString("start_lat", start_lat);
                    editor.putString("start_lng", start_lng);

                    editor.apply();


                    try {


                        Log.i("checkincity ", " GeocoderHandler1 ==>> locationAddress1 " + locationAddress1 + " elng ==>> " + locationAddress2);

                        double latitude1 = 0;
                        double longitude1 = 0;

                        double latitude2 = 0;
                        double longitude2 = 0;

                        SharedPreferences prefs1 = getSharedPreferences("Login", MODE_PRIVATE);

                        String endlat, elat, elng;

                        elng = end_lat;
                        elng = end_lng;

                        endlat = prefs1.getString("start_lat", "");
                        end_lng = prefs1.getString("start_lng", "");

                        elat = prefs1.getString("end_lat", "");
                        elng = prefs1.getString("end_lng", "");


                        editor.putString("Mstart_lat", endlat);
                        editor.putString("Mstart_lng", end_lng);
                        editor.putString("Mend_lat", elat);
                        editor.putString("Mend_lng", elng);
                        editor.commit();


                        double dist_source = 0;
                        double dist_dest = 0;
                        double dist_dest1 = 0;
                        double theta = 0;

                        if (!isRental) {

                            if (endlat != null && endlat.trim().length() > 0 && end_lng != null && end_lng.trim().length() > 0) {
                                // distance = getDistanceBetweenTwoPoint(center_lat, center_lan, endlat, end_lng);


                                //  getDistance(center_lat, center_lan, endlat, end_lng);
//                            if (editText1.getText().toString().equals("Paldi Kankaj, Gujarat, India") || citysource.contains("Gandhinagar") || citysource.contains("Hansol") || citysource.contains("Bopal") || citysource.contains("Sarkhej") || citysource.contains("Chharodi") || citysource.contains("Changodar") || citysource.contains("Morasar")) {
                                if (isIncity(citysource, editText1.getText().toString())) {
//                            Toast.makeText(CustHomePageActivity.this, "source", Toast.LENGTH_SHORT).show();
                                    theta = Double.parseDouble(end_lng) - Double.parseDouble(elng);
                                    dist_dest = Math.sin(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                                    dist_dest = Math.acos(dist_dest);
                                    dist_dest = Math.toDegrees(dist_dest);
                                    dist_dest = dist_dest * 60 * 1.1515 * 1.609344;
//                            } else if (editText2.getText().toString().equals("Paldi Kankaj, Gujarat, India") || citydest.contains("Gandhinagar") || citydest.contains("Hansol") || citydest.contains("Bopal") || citydest.contains("Sarkhej") || citydest.contains("Chharodi") || citydest.contains("Changodar") || citydest.contains("Morasar")) {
                                } else if (isIncity(citydest, editText2.getText().toString())) {
//                            Toast.makeText(CustHomePageActivity.this, "dest", Toast.LENGTH_SHORT).show();
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
                            }

                            Boolean isincity = false;


                            if (editText1.getText().toString().equals("Paldi Kankaj, Gujarat, India") || editText2.getText().toString().equals("Paldi Kankaj, Gujarat, India")) {
                                selectIncityAndOutCity(true);
                            } else {

                                if (isIncity(citysource, editText1.getText().toString()) || isIncity(citydest, editText2.getText().toString()))
                                    isincity = true;

                                if ((isincity || isDistnceOutOfAhemedabad(endlat, end_lng))) {
                                    if (!isDistnceOutOfAhemedabad(elat, elng)) {
                                        Log.e("TAG", "3160 outcity  ");
                                        selectIncityAndOutCity(false);
                                    } else {
                                        Log.e("TAG", " 3163  incity  ");
                                        selectIncityAndOutCity(true);
                                    }
                                } else {
                                    Log.e("TAG", " 3166 outcity  " + isincity);
                                    selectIncityAndOutCity(false);
                                }

                            }

                            Ridenowclass(false);
                            isincity = false;
                        }


                    } catch (Exception e) {
                        Log.i(":Exception ", ":Exception :Exception " + e.toString());
                    }

                } catch (Exception e) {
                    Log.d("ParserTask", e.toString());
                    e.printStackTrace();
                }

                return routes;
            }

            public void selectIncityAndOutCity(Boolean aBoolean) {

                if (aBoolean) {
                    newrental.setVisibility(View.GONE);
                    testlinear.setVisibility(View.VISIBLE);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    destlayout.setVisibility(View.VISIBLE);
                    rentalpreebooking = false;
                    outcitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    incitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                    rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    outcitytext.setTextColor(getResources().getColor(R.color.white));
                    rentaltext.setTextColor(getResources().getColor(R.color.white));
                    incitytext.setTextColor(getResources().getColor(R.color.bleck));
                    flegincityoroutcity = "0";
                } else {

                    flegincityoroutcity = "1";
                    newrental.setVisibility(View.GONE);
                    testlinear.setVisibility(View.VISIBLE);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    destlayout.setVisibility(View.VISIBLE);

                    rentalpreebooking = false;
                    incitylayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    outcitylayout.setBackground(getResources().getDrawable(R.drawable.rounded));
                    rentallayout.setBackgroundColor(getResources().getColor(R.color.appred));
                    incitytext.setTextColor(getResources().getColor(R.color.white));
                    rentaltext.setTextColor(getResources().getColor(R.color.white));
                    outcitytext.setTextColor(getResources().getColor(R.color.bleck));
                }

            }


            // Executes in UI thread, after the parsing process
            @SuppressLint("ResourceType")
            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;

                if (result != null) {
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

                    // Drawing polyline in the Google Map for the i-th route
                    if (lineOptions != null) {
                        mMap.addPolyline(lineOptions);
                    }
                }
            }

        }


        public Boolean isDistnceOutOfAhemedabad (String latitude, String longitude){

            Log.e(TAG, " Location  : isMatchLocation " + latitude + "," + longitude);

            double theta = 0;
            double dist_source = 0;
            if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {

                theta = Double.parseDouble(center_lan) - Double.parseDouble(longitude);
                dist_source = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(latitude))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(latitude))) * Math.cos(Math.toRadians(theta));
                dist_source = Math.acos(dist_source);
                dist_source = Math.toDegrees(dist_source);
                dist_source = dist_source * 60 * 1.1515 * 1.609344;

                Log.e("TAG", " Location  :  Total distance : " + dist_source);
            }


            if (dist_source < 100) {
                isOutofAhemedaBad = true;
                return true;
            } else {
                isOutofAhemedaBad = false;
                return false;
            }


        }

        boolean doubleBackToExitPressedOnce = false;

        @Override
        protected Dialog onCreateDialog ( int id){
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
                        showDialog(TIME_PICKER_ID);
                    }
                };


        private TimePickerDialog.OnTimeSetListener myTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                selectedCalendar.set(Calendar.HOUR_OF_DAY, i);
                selectedCalendar.set(Calendar.MINUTE, i1);

                long tenBefore = System.currentTimeMillis() + PRE_BOOKING_DELAY_TIME;
                if (selectedCalendar.getTimeInMillis() < tenBefore) {
                    Toast.makeText(CustHomePageActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
                } else {
                    if (rentalpreebooking) {

                        Log.d("asdf", "11");
                        Intent intent = new Intent(CustHomePageActivity.this, ListPakegesActivity.class);
                        intent.putExtra("Sorce", editText1.getText().toString().trim());
                        intent.putExtra("dest", editText2.getText().toString().trim());
                        intent.putExtra("date", DateUtils.convertDateToString(selectedCalendar.getTime()));
                        startActivity(intent);
                        finish();

                    } else {
                        Log.d("asdf", "12");
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("cartype", cartype);
                        editor.putString("fueltype", fueltype);
                        editor.putString("date", DateUtils.convertDateToString(selectedCalendar.getTime()));
                        editor.putString("incity", flegincityoroutcity);
                        editor.putString("triptype", jarneyselected);
                        editor.apply();
                        Intent intent = new Intent(CustHomePageActivity.this, SelectCarTypeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };

        @Override
        protected void onPause () {

            SharedPreferences sharedpreferences;
            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("flegincityoroutcitypau", flegincityoroutcity);
            editor.putString("cartypepau", cartype);
            editor.apply();
            ;
            super.onPause();
        }

        @Override
        public void onBackPressed () {


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if (doubleBackToExitPressedOnce) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    super.onBackPressed();
                    super.setResult(0);
                    finish();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
                // super.onBackPressed();
            }

            // super.onBackPressed();
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.bookride) {
                // Handle the camera action

            } else if (id == R.id.ridehistry) {
                startActivity(new Intent(CustHomePageActivity.this, RideHistoryActivity.class));
                finish();
                //drawer.closeDrawers();
            } else if (id == R.id.settingride) {
                startActivity(new Intent(CustHomePageActivity.this, SelectLanguageActivity.class));
                finish();
            } else if (id == R.id.profile) {
                startActivity(new Intent(CustHomePageActivity.this, ProfileActivity.class));
                finish();
            } else if (id == R.id.help) {
                startActivity(new Intent(CustHomePageActivity.this, HelpActivity.class));
                finish();
            } else if (id == R.id.logout) {
                logoutclass();
            } else if (id == R.id.features) {
                startActivity(new Intent(CustHomePageActivity.this, FeaturesActivity.class));
                finish();
            } else if (id == R.id.raawallet) {
                startActivity(new Intent(CustHomePageActivity.this, WalletActivity.class));
                finish();
            } else if (id == R.id.offers) {
                startActivity(new Intent(CustHomePageActivity.this, OffersActivity.class));
                finish();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        String durationvalue, distancevalue, custuserid;

        void Ridenowclass ( final Boolean isIntent){

            SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
            logtokan = prefs.getString("tokan", "");
//        cartype = prefs.getString("cartype", "");
            // jsonData = prefs.getString("jsonData","");
            durationvalue = prefs.getString("durationvalue", "");
            distancevalue = prefs.getString("distancevalue", "");

            custuserid = prefs.getString("userid", "");

            Log.i("logtokan", logtokan);
            progstart(CustHomePageActivity.this, "Loading...", "Loading...");

            SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);


            if (distancevalue != null && !distancevalue.isEmpty()) {

                StringRequest request = new StringRequest(Request.Method.POST, getridenowpackagesapi, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("custhome", "Custhome res : " + new Gson().toJson(response.toString()));

                        try {

                            JSONObject jobject = new JSONObject(response);

                            if (jobject.has("success")) {

                                JSONObject mainobject = jobject.getJSONObject("success");
                                JSONObject packageobj = mainobject.getJSONObject("package");
                                String packegename = packageobj.getString("name");
                                //String cartype = packageobj.getString("cartype");
                                String kilometer = packageobj.getString("kilometer");
                                String hours = packageobj.getString("hours");
                                String price = packageobj.getString("price");
                                condition = packageobj.getString("condition");
                                estimatedprice = mainobject.getString("estimatedprice");
                                String car_rate_per_kl = mainobject.getString("car_rate_per_kl");
                                String car_rate_per_hr = mainobject.getString("car_rate_per_hr");
                                String wallet_amount = mainobject.getString("wallet_amount");

                                SharedPreferences sharedpreferences;
                                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("packegename", packegename);
                                // editor.putString("cartype", cartype);
                                editor.putString("kilometer", kilometer);
                                editor.putString("hours", hours);
                                editor.putString("price", price);
                                editor.putString("condition", condition);
                                editor.putString("fueltype", fueltype);
                                editor.putString("estimatedprice", estimatedprice);
                                editor.putString("car_rate_per_kl", car_rate_per_kl);
                                editor.putString("car_rate_per_hr", car_rate_per_hr);
                                editor.putString("incity", flegincityoroutcity);
                                editor.putString("triptype", jarneyselected);
                                editor.putString("triptype", jarneyselected);
                                editor.putString("wallet_amount", wallet_amount);
                                editor.putString("distancevalue", distancevalue);
                                editor.putString("end_address", editText2.getText().toString());
                                editor.putString("start_address", editText1.getText().toString());
                                editor.remove("date");
                                editor.apply();


                                if (estimatedprice != null && !estimatedprice.isEmpty()) {
                                    int price2 = Integer.parseInt(estimatedprice) + 10;
                                    if (editText1.getText().toString().toLowerCase().contains(AIRPORT)) {
                                        txtEstamated.setText(ESTIMATED + RUPEE + price2 + PARKING_CHARGE);
                                    } else if (editText1.getText().toString().toLowerCase().contains(KALUPUR) || editText2.getText().toString().toLowerCase().contains(KALUPUR)) {
                                        if (Double.valueOf(estimatedprice) < 197)
                                            txtEstamated.setText(ESTIMATED + RUPEE + PRICE);
                                        else
                                            txtEstamated.setText(ESTIMATED + RUPEE + price2);
                                    } else
                                        txtEstamated.setText(ESTIMATED + RUPEE + price2);
                                }

                                if (isIntent) {
                                    Intent i = new Intent(CustHomePageActivity.this, SelectedPakegeShowActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                                isBookAvailable = true;

                            } else if (jobject.has("error")) {
                                txtEstamated.setText(jobject.getString("error"));
                                txtEstamated.setSelected(true);
                                txtTagline2.setSelected(true);
                                txtTagline2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                isBookAvailable = false;
                            }

                        } catch (Exception e) {
                            Log.i("Exception  ", "Ridenow getridenowpackagesapi Exception --->>> " + e.toString());
                        }
                        //Common.image+'cars/primary/thumbnail/'+carimage;
                        progstop();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error is ", "" + error.getMessage());
                        progstop();
                        Toast.makeText(CustHomePageActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                    }
                }) {
                    //This is for Headers If You Needed
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + logtokan);
                        // Log.i("Authorization", "Bearer "+logtokan);
                        return params;
                    }

                    //Pass Your Parameters here
                    @Override
                    protected Map<String, String> getParams() {


                        if (cartype.equals("suv")) {
                            fueltype = "diesel";
                        } else if (cartype.equals("sedan")) {
                            fueltype = "cng";
                        }

                        SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("start_lat", sharedpreferences.getString("Mstart_lat", ""));
                        params.put("start_lng", sharedpreferences.getString("Mstart_lng", ""));
                        params.put("end_lat", sharedpreferences.getString("Mend_lat", ""));
                        params.put("end_lng", sharedpreferences.getString("Mend_lng", ""));
                        params.put("totalkm", distancevalue);
                        params.put("totalhr", durationvalue);
                        params.put("cartype", cartype);
                        params.put("userid", custuserid);
                        params.put("incity", flegincityoroutcity);
                        params.put("fueltype", fueltype);
                        params.put("triptype", "0");
                        params.put("days", "0");

                        Log.i("checkincity", " param  totalhr " + durationvalue + " totalkm : " + distancevalue);

                        Log.i("checkincity", "Custhome get : GetRideNowPackagesApi ---->> param" + new Gson().toJson(params));

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }
        }

        String logtokan;

        public void logoutclass () {

            progstart(CustHomePageActivity.this, "Loading... ", "Loading...");

            SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
            logtokan = prefs.getString("tokan", "");
            StringRequest request = new StringRequest(Request.Method.POST, logout, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Your Array Response", response);

                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("tokan");
                    editor.remove("role");
                    editor.putBoolean("hasLoggedIn", false);
                    editor.apply();
                    ;

                /*String filePath = getApplicationContext().getFilesDir().getParent()+"/shared_prefs/Login.xml";
                File deletePrefFile = new File(filePath );
                deletePrefFile.delete();*/
                    progstop();

                    NotificationUtils.clearNotifications(CustHomePageActivity.this);
                    Intent i = new Intent(CustHomePageActivity.this, PhoneNumberVerifyActivity.class);
                    startActivity(i);
                    finish();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Exception : " + error);
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

                //Pass Your Parameters here
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    // params.put("User", UserName);
                    // params.put("Pass", PassWord);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);

        }


        @Override
        public void onMapReady (GoogleMap googleMap){

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
//        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
//            @Override
//            public boolean onMyLocationButtonClick()
//            {
//               Log.e("CheckPermis","Click lisner call  : ");
//                return false;
//            }
//        });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else
                mMap.setMyLocationEnabled(true);
            // Toast.makeText(Drivar_map.this, "hihiihhi", Toast.LENGTH_LONG).show();
            //Initialize Google Play Services
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("HH");
            String datetime = dateformat.format(c.getTime());
            Log.d("asdf", "datetime" + datetime);
//        if( Integer.parseInt(datetime) < 19 && Integer.parseInt(datetime) > 6)
//        {
          /*  boolean success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }*/
//        }
//        else
//        {
          /*  boolean success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.night));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }*/
//        }

            locationTrack = new LocationTrack(CustHomePageActivity.this);
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
            }
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(0, 400, 180, 0);
            // Setting onclick event listener for the map

        }

        protected synchronized void buildGoogleApiClient () {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnected (@Nullable Bundle bundle){

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
        public void onConnectionSuspended ( int i){
        }

        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
        }


        protected String getAddress (LatLng currentLocation){
            String address = null;
            try {

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                if ((addresses != null) && !addresses.isEmpty()) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder();
                    if (returnedAddress.getMaxAddressLineIndex() > 0)
                        for (int j = 0; j < returnedAddress.getMaxAddressLineIndex(); j++)
                            strReturnedAddress.append(returnedAddress.getAddressLine(j)).append("");
                    else strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("");
                    address = strReturnedAddress.toString();
                } else {

                }
            } catch (Exception e) {
                Log.e(TAG, "line -->> " + e.getStackTrace()[0].getLineNumber() + " --->> " + e.getMessage());
            }
            return address;
        }


        @Override
        public void onLocationChanged (Location location){

            mLastLocation = location;
            mLastLocation1 = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);

//        Marker placeMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            if (!isOutofAhemedaBad) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
            }

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            //Place current location marker

            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0);
                if (editText1.getText().toString().trim().equalsIgnoreCase("")) {

                    latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    //move map camera
                    if (!isOutofAhemedaBad) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.2f));
                    }

                    Latitudedefoult = location.getLatitude();
                    Longitudedefoult = location.getLongitude();

//for set current location in edittext @Anil

                    getcars_latlong(Latitudedefoult, Longitudedefoult);

                    SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                    String favplace = prefs.getString("favplace", "");


                    String[] favarray = favplace.split("<mrrhope>");

//                if (Arrays.asList(favarray).contains(editText1.getText().toString().trim())) {
//                    favicon1.setImageDrawable(getResources().getDrawable(R.drawable.selected_favorite));
//                }
                    Log.i("zXHcjhzc", address + " - addresses " + addresses);
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
                            //Toast.makeText(CustHomePageActivity.this, "hihihi", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Log.e(TAG, "Exception " + e.toString());
                        }
                    }
                }

                progstop();

//            if(mLastLocation!= null)
//                Log.e(TAG,"OnMap Ready CAll : Latitude "+mLastLocation.getLatitude()+"  : Longitude "+mLastLocation.getLongitude());

//            try {
//                if (editText1.getText().toString().isEmpty()) {
//
//                    LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
//                    if(getAddress(latLng1) != null) {
//                        String currentAddress = getAddress(latLng1);
//                        setAddress(currentAddress,location);
//                    }else{
//                    String currentAddress =  getAddressFromLatLong(latLng1,location);
//                    }
//                    repet = true;
//                }
//            }catch (Exception e){
//                Toast.makeText(CustHomePageActivity.this, "Error: "+e.getMessage()+" Line "+e.getStackTrace()[0].getLineNumber(), Toast.LENGTH_SHORT).show();
//            }

            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.toString());
            }
            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }

        public Boolean isIncity (String city, String editText){

//                || start_point.includes('Adani')//Adani Shantigram Entrance, Gandhinagar, Gujarat, India
            if (editText.contains("Paldi Kankaj")
                    || editText.contains("Gandhinagar")
                    || editText.contains("Nana Chiloda")
                    || editText.contains("Kalol")
                    || editText.contains("Hansol")
                    || editText.contains("South Bopal")
                    || editText.contains("Sarkhej")
                    || editText.contains("Chharodi")
                    || editText.contains("Changodar")
                    || editText.contains("Sanand")
                    || editText.contains("Vatva")
                    || editText.contains("Vinzol")
                    || editText.contains("Shela")
                    || editText.contains("Kathwada")
                    || editText.contains("Gamdi")
                    || editText.contains("Santej")
                    || editText.contains("Adani")
                    || editText.contains("Morasar")) {
                return true;
            } else
                return false;
        }

        public void setAddress (String currentAddress, Location location){

            editText1.setText(currentAddress);

            GeocodingLocation locationAddress1 = new GeocodingLocation();
            locationAddress1.getAddressFromLocation(currentAddress,
                    getApplicationContext(), new GeocoderHandler());
            SharedPreferences sharedpreferences;

            SharedPreferences sharedpreferences2 = getSharedPreferences("currLatLng", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedpreferences2.edit();
            editor2.putString("currLat", String.valueOf(location.getLatitude()));
            editor2.putString("currLong", String.valueOf(location.getLongitude()));
            editor2.apply();

            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("start_address", editText1.getText().toString().trim());
            editor.apply();
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode,
        String permissions[], int[] grantResults){
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

                                mMap.setMyLocationEnabled(true);
                            }

                            ActivityCompat.requestPermissions(this, PERMISSIONS, FILE_PERMISSION_REQUEST_CODE);
                        }

                    } else {

                        // Permission denied, Disable the functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

                        finish();
                        exit(0);
                    }
                    return;
                }
                case FILE_PERMISSION_REQUEST_CODE: {

                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        String extStorageDirectory = null;
                        File file;

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            file = new File(getApplication().getFilesDir(), "RaadarbarCrash");
                        } else {
                            extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                            file = new File(extStorageDirectory, "RaadarbarCrash");
                        }

                        if (!file.exists()) {
                            file.mkdirs();
                        }

                    }
                }

                // other 'case' lines to check for other permissions this app might request.
                // You can add here other case statements according to your requirement.
            }
        }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

        @Override
        protected void onResume () {
            super.onResume();

            if (editText1 != null && editText2 != null && !editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {

                SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String latitude =  sharedpreferences.getString("temp_lat", "");
                String longitude = sharedpreferences.getString("temp_long", "");
                if (isNumeric(latitude) && isNumeric(longitude) ) {
                    if (isDistnceOutOfAhemedabad(latitude, longitude)) {
                        isOutofAhemedaBad = true;
                    }
                }

            }

            if (network) {
                callApi();
            }


            SharedPreferences sharedpreferences;
            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            String newCHeck = sharedpreferences.getString("locationTrack", "");
            if (newCHeck.equalsIgnoreCase("1")) {
                SharedPreferences.Editor editor123 = sharedpreferences.edit();
                editor123.putString("locationTrack", "");
                editor123.apply();

                Intent i = new Intent(CustHomePageActivity.this, CustHomePageActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }


        }

        public double distance (String start_lat, String start_lng, String endlat, String end_lng){
            double theta = Double.parseDouble(start_lng) - Double.parseDouble(end_lng);
            double dist = Math.sin(deg2rad(Double.parseDouble(start_lat)))
                    * Math.sin(deg2rad(Double.parseDouble(endlat))
                    + Math.cos(deg2rad(Double.parseDouble(start_lat)))
                    * Math.cos(deg2rad(Double.parseDouble(endlat)))
                    * Math.cos(deg2rad(theta)));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1.609344 * 0.001;
//        dist = dist * 60 * 1.1515;
//        dist = dist* 1.609344;
            return (dist);
        }

        private double getDistanceBetweenTwoPoint (String start_lat, String start_lng, String
        endlat, String end_lng){
//        int Radius = 6371;
            double theta = Double.parseDouble(start_lng) - Double.parseDouble(end_lng);
            double dist = Math.sin(deg2rad(Double.parseDouble(start_lat)))
                    * Math.sin(deg2rad(Double.parseDouble(endlat))
                    + Math.cos(deg2rad(Double.parseDouble(start_lat)))
                    * Math.cos(deg2rad(Double.parseDouble(endlat)))
                    * Math.cos(deg2rad(theta)));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1.609344 * 0.001;
//        dist = dist * 60 * 1.1515;
//        dist = dist* 1.609344;
            return (dist);

        /*double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(Double.parseDouble(endlat) - Double.parseDouble(start_lng));
        double dLng = Math.toRadians(Double.parseDouble(end_lng) - Double.parseDouble(start_lng));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(Double.parseDouble(start_lat))) * Math.cos(Math.toRadians(Double.parseDouble(endlat))) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (float) (earthRadius * c);*/




        /*theta = $lng - $request->longitude;
        $dist = sin(deg2rad($lat)) * sin(deg2rad($request->latitude)) +  cos(deg2rad($lat)) * cos(deg2rad($request->latitude)) * cos(deg2rad($theta));
        $dist = acos($dist);
        $dist = rad2deg($dist);
        $miles = $dist * 60 * 1.1515;
        $distance= ($miles * 1.609344);

        double dLat = Math.toRadians(Double.parseDouble(endlat) - Double.parseDouble(start_lat));
        double dLon = Math.toRadians(Double.parseDouble(end_lng) - Double.parseDouble(start_lng));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(Double.parseDouble(start_lat)))
                * Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;


        *//*Location loc1 = new Location("");
        loc1.setLatitude(Double.parseDouble(start_lat));
        loc1.setLongitude(Double.parseDouble(start_lng));

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(endlat));
        loc2.setLongitude(Double.parseDouble(end_lng));

        float distanceInMeters = loc1.distanceTo(loc2);*/


        }


        void postRequest (String postBody) throws IOException {

            Intent i = new Intent(CustHomePageActivity.this, NewCustomerWaitingActivity.class);
            startActivity(i);

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, postBody);
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .addHeader("Authorization", "Bearer " + logtokan)
                    .url(confirmride)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    try {
                        call.cancel();

                    } catch (Exception e1) {
                        Log.e(TAG, "Exception : " + e1.toString());
                    }
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {


                    try {
                        JSONObject mainobject = new JSONObject(String.valueOf(response));
                        JSONObject succ = mainobject.getJSONObject("success");
                        String status = succ.getString("status");
                        if (status.equalsIgnoreCase("empty_records")) {

                            Toast.makeText(CustHomePageActivity.this, getResources().getString(R.string.tripnotaveleble), Toast.LENGTH_LONG).show();

                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Exception " + e.toString());
                    }

                }
            });
        }


        private void callApi () {

            StringRequest postRequest = new StringRequest(Request.Method.POST, lastlogin,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Exception : " + error.getMessage());
                            Toast.makeText(CustHomePageActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            txtTagline.setVisibility(View.GONE);
                            linOffer.setVisibility(View.GONE);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + logtokan);
                    return params;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);
        }

        void getOffers () {
            txtTagline.setVisibility(View.GONE);
            linOffer.setVisibility(View.GONE);

            SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
            logtokan = prefs.getString("tokan", "");
            String role = prefs.getString("role", "");
            // progstart(CustHomePageActivity.this,"Loading...","Loading...");

            Log.i("role", role);

            StringRequest postRequest = new StringRequest(Request.Method.GET, offers,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            try {
                                JSONObject main = new JSONObject(response);
                                JSONObject submain = main.getJSONObject("success");
                                JSONObject customer_data = submain.getJSONObject("data");
                                String velue = customer_data.getString("value");

                                Log.i("velue", velue);
                                if (customer_data.isNull("value")) {
                                    txtTagline.setVisibility(View.GONE);
                                    linOffer.setVisibility(View.GONE);
                                } else {
                                    txtTagline.setText(Html.fromHtml(velue));
                                    txtTagline.setVisibility(View.VISIBLE);
                                    linOffer.setVisibility(View.VISIBLE);
                                }

                            /*if(velue != null && !velue.isEmpty() && !velue.equals("null")){
                                txtTagline.setText(Html.fromHtml(velue));
                                txtTagline.setVisibility(View.VISIBLE);
                                linOffer.setVisibility(View.VISIBLE);
                            }else{
                                txtTagline.setVisibility(View.GONE);
                                linOffer.setVisibility(View.GONE);
                            }*/
                            /*if (velue!= null && !"".equals(velue)) {
                                txtTagline.setText(Html.fromHtml(velue));
                                txtTagline.setVisibility(View.VISIBLE);
                                linOffer.setVisibility(View.VISIBLE);
                            } else {
                                txtTagline.setVisibility(View.GONE);
                                linOffer.setVisibility(View.GONE);
                            }*/


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progstop();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                            //progstop();
                            Toast.makeText(CustHomePageActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            txtTagline.setVisibility(View.GONE);
                            linOffer.setVisibility(View.GONE);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + logtokan);
                    return params;
                }
            /*@Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("msg", msg.getText().toString().trim());
                //params.put("password", pass.getText().toString().trim());

                return params;
            }*/
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);
        }

        public Boolean checkIncity () {


            SharedPreferences prefs1 = getSharedPreferences("Login", MODE_PRIVATE);
            String endlat = prefs1.getString("start_lat", "");
            String end_lng = prefs1.getString("start_lng", "");

            String elat = prefs1.getString("end_lat", "");
            String elng = prefs1.getString("end_lng", "");

            double dist_source = 0;
            double dist_dest = 0;
            double dist_dest1 = 0;
            double theta = 0;


            if (endlat != null && endlat.trim().length() > 0 && end_lng != null && end_lng.trim().length() > 0) {

//            if (editText1.getText().toString().equals("Paldi Kankaj, Gujarat, India") || citysource.contains("Gandhinagar") || citysource.contains("Hansol") || citysource.contains("Bopal") || citysource.contains("Sarkhej") || citysource.contains("Chharodi") || citysource.contains("Changodar") || citysource.contains("Morasar")) {
                if (isIncity(citysource, editText1.getText().toString())) {
//                            Toast.makeText(CustHomePageActivity.this, "source", Toast.LENGTH_SHORT).show();
                    theta = Double.parseDouble(end_lng) - Double.parseDouble(elng);
                    dist_dest = Math.sin(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                    dist_dest = Math.acos(dist_dest);
                    dist_dest = Math.toDegrees(dist_dest);
                    dist_dest = dist_dest * 60 * 1.1515 * 1.609344;

                    return true;

//            } else if (editText2.getText().toString().equals("Paldi Kankaj, Gujarat, India") || citydest.contains("Gandhinagar") || citydest.contains("Hansol") || citydest.contains("Bopal") || citydest.contains("Sarkhej") || citydest.contains("Chharodi") || citydest.contains("Changodar") || citydest.contains("Morasar")) {
                } else if (isIncity(citydest, editText2.getText().toString())) {
//                            Toast.makeText(CustHomePageActivity.this, "dest", Toast.LENGTH_SHORT).show();
                    theta = Double.parseDouble(center_lan) - Double.parseDouble(end_lng);
                    dist_source = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(endlat))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(theta));
                    dist_source = Math.acos(dist_source);
                    dist_source = Math.toDegrees(dist_source);
                    dist_source = dist_source * 60 * 1.1515 * 1.609344;

                    return true;

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

                    if (isIncity(citysource, editText1.getText().toString()) || isIncity(citydest, editText2.getText().toString())) {
                        return true;
                    } else {
                        if ((dist_dest < 15 || dist_dest1 < 15) && dist_source < 15) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }

            }


            return false;
        }

        public Boolean checkout () {


            SharedPreferences prefs1 = getSharedPreferences("Login", MODE_PRIVATE);
            String endlat = prefs1.getString("start_lat", "");
            String end_lng = prefs1.getString("start_lng", "");

            String elat = prefs1.getString("end_lat", "");
            String elng = prefs1.getString("end_lng", "");

            double dist_source = 0;
            double dist_dest = 0;
            double dist_dest1 = 0;
            double theta = 0;

            Boolean isIncity = false;


            if (endlat != null && endlat.trim().length() > 0 && end_lng != null && end_lng.trim().length() > 0) {

                if (isIncity(citysource, editText1.getText().toString())) {
                    theta = Double.parseDouble(end_lng) - Double.parseDouble(elng);
                    dist_dest = Math.sin(Math.toRadians(Double.parseDouble(endlat))) * Math.sin(Math.toRadians(Double.parseDouble(elat))) + Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(Double.parseDouble(elat))) * Math.cos(Math.toRadians(theta));
                    dist_dest = Math.acos(dist_dest);
                    dist_dest = Math.toDegrees(dist_dest);
                    dist_dest = dist_dest * 60 * 1.1515 * 1.609344;
                    isIncity = true;
                    return true;

                } else if (isIncity(citydest, editText2.getText().toString())) {
                    theta = Double.parseDouble(center_lan) - Double.parseDouble(end_lng);
                    dist_source = Math.sin(Math.toRadians(Double.parseDouble(center_lat))) * Math.sin(Math.toRadians(Double.parseDouble(endlat))) + Math.cos(Math.toRadians(Double.parseDouble(center_lat))) * Math.cos(Math.toRadians(Double.parseDouble(endlat))) * Math.cos(Math.toRadians(theta));
                    dist_source = Math.acos(dist_source);
                    dist_source = Math.toDegrees(dist_source);
                    dist_source = dist_source * 60 * 1.1515 * 1.609344;
                    isIncity = true;
                    return true;

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

                    if (isIncity(citysource, editText1.getText().toString()) || isIncity(citydest, editText2.getText().toString())) {
                        return true;
                    } else if (((dist_dest > 15 || dist_dest1 < 15) || dist_source > 15) && isIncity) {
                        return true;
                    } else {
                        if ((dist_dest < 15 || dist_dest1 < 15) && dist_source < 15 || isIncity) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }

            }


            return false;
        }
    }
