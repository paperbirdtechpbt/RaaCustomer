package com.hopetechno.raadarbar.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Adapter.CarListingAdapter;
import com.hopetechno.raadarbar.Dialog.InfoWindowDialog;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Other.GeocodingLocation;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.DateUtils;
import com.hopetechno.raadarbar.Utils.MyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class SelectCarTypeActivity extends AppCompatActivity implements AppConstant {
    ImageView cngcar, petrolcar, dieselcar;
    RelativeLayout cnglayout, petrollayout, diesellayout;
    //    RadioButton sway;/**/
    Spinner dropdown;
    CButton next, backbutton;
    //
    TextView farerate, price, fuleselected, date1, textdate, selectedcar, endtextdate;
    CarListingAdapter mAdapter;
    //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
    String selectedcarid, condition, jarneyselected = "0", durationvalue, distancevalue, flegincityoroutcity = "0", userid = "", end_address = "",
            start_address = "", end_lat = "", end_lng = "";
    String[] id, carname, carnumber, carimage, special_rate, distance, Duration;
    ListView list1;

    String cartype, distanceText, estimatedprice, logtokan, jsonData, fueltype = "diesel", date = "", special_ratestring, carnamestring, start_lat, start_lng, locationAddress1 = "";
    RadioGroup selectjourny;
    RadioButton radioselectrd;
    //SwitchDateTimeDialogFragment dateTimeFragment;

    private Calendar todayCalendar, selectedCalendar;
    ExpandableListView expandableListView;
    LinearLayout datelayout, enddatelayout;
    CButton prebooking;
    boolean prebookingbutton = false;
    private String TAG = "SelectCarType";
    RadioGroup radioGroupFuelType;
    long diffDays;
    RadioGroup radioGroupTripType;
    Dialog dialog;

    TextView jarny, pricetext, conditionone, conditionone1;

    RadioButton radioButtonDisel, rbtnCng, rbtnrStrip, rbtnrRtrip;


    @Override
    public void onBackPressed() {
        Intent i = new Intent(SelectCarTypeActivity.this, CustHomePageActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car_type);

        MyApplication.setCurrentActivity(this);

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);

        todayCalendar = Calendar.getInstance();
        selectedCalendar = Calendar.getInstance();

        logtokan = prefs.getString("tokan", "");
        cartype = prefs.getString("cartype", "");
        jsonData = prefs.getString("jsonData", "");
        durationvalue = prefs.getString("durationvalue", "");
        distancevalue = prefs.getString("distancevalue", "");
        date = prefs.getString("date", "");
        flegincityoroutcity = prefs.getString("incity", flegincityoroutcity);
        start_lat = prefs.getString("start_lat", start_lat);
        start_lng = prefs.getString("start_lng", start_lng);
        userid = prefs.getString("userid", userid);
        estimatedprice = prefs.getString("estimatedprice", "");
        end_address = prefs.getString("end_address", end_address);
        start_address = prefs.getString("start_address", start_address);
        end_lat = prefs.getString("end_lat", end_lat);
        end_lng = prefs.getString("end_lng", end_lng);
        distanceText = prefs.getString("distanceText", "");
        condition = prefs.getString("condition", condition);
        Log.e("flegincityoroutcity", flegincityoroutcity);
        Log.e("logtokan", distanceText);
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        //editor.remove("date");
        editor.remove("jsonData");
        editor.apply();
        ;


        try {
            Intent i = getIntent();
            String Sorce = i.getStringExtra("Sorce");

            //date =  i.getStringExtra("date");
            GeocodingLocation locationAddress = new GeocodingLocation();
            locationAddress.getAddressFromLocation(Sorce,
                    getApplicationContext(), new GeocoderHandler());

            if (Sorce != null && !Sorce.isEmpty() && !Sorce.equals("null")) {
                start_address = Sorce;
            }
        } catch (Exception e) {
            Log.e("Exeption", "List Pakeges" + e.toString());
        }


        prebooking = (CButton) findViewById(R.id.prebooking);
        textdate = (TextView) findViewById(R.id.textdate);
        date1 = (TextView) findViewById(R.id.date);
        cngcar = (ImageView) findViewById(R.id.cngcar);
        petrolcar = (ImageView) findViewById(R.id.petrolcar);
        dieselcar = (ImageView) findViewById(R.id.dieselcar);
        dropdown = findViewById(R.id.spinner1);
        list1 = findViewById(R.id.list1);
        cnglayout = (RelativeLayout) findViewById(R.id.cnglayout);
        petrollayout = (RelativeLayout) findViewById(R.id.petrollayout);
        diesellayout = (RelativeLayout) findViewById(R.id.diesellayout);
        selectedcar = findViewById(R.id.selectedcar);
        radioGroupTripType = findViewById(R.id.selectjourny);
        radioGroupFuelType = findViewById(R.id.radioGroupFuelType);
        radioButtonDisel = findViewById(R.id.rbtnDiesel);
        rbtnCng = findViewById(R.id.rbtnCng);
        rbtnrStrip = findViewById(R.id.rsingletrip);
        rbtnrRtrip = findViewById(R.id.rreturntrip);
//        sway = findViewById(R.id.sway);
        backbutton = findViewById(R.id.backbutton);
        next = findViewById(R.id.next);
        fuleselected = findViewById(R.id.fuleselected);
        datelayout = findViewById(R.id.datelayout);
        endtextdate = findViewById(R.id.endtextdate);
        enddatelayout = findViewById(R.id.enddatelayout);
        radioGroupFuelType = findViewById(R.id.radioGroupFuelType);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        conditionone = findViewById(R.id.conditionone);
        jarny = findViewById(R.id.jarny);
        pricetext = findViewById(R.id.pricetext);
        conditionone1 = findViewById(R.id.conditionone1);
//        sway.setChecked(true);

//        radioGroupFuelType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
//
//                if (checkedRadioButton.getText().equals("CNG")) {
//                    fueltype = "cng";
//                } else {
//                    fueltype = "diesel";
//                }
//            }
//        });
//
//
//
//
//
//        int radioButtonID = radioGroupFuelType.getCheckedRadioButtonId();
//        RadioButton radioButton = (RadioButton) radioGroupFuelType.findViewById(radioButtonID);
//////        String selectedtext = (String) radioButton.getText();
////        if (selectedtext.equals("CNG")) {
////            fueltype = "cng";
////        } else {
////            fueltype = "diesel";
////        }


        if (estimatedprice != null && !estimatedprice.isEmpty()) {
            int price2 = Integer.parseInt(estimatedprice) + 20;
            if (start_address.toLowerCase().contains(AIRPORT)) {
                pricetext.setText(ESTIMATED + RUPEE + estimatedprice + DASH + price2 + PARKING_CHARGE);
            } else if (start_address.toLowerCase().contains(KALUPUR) || end_address.toLowerCase().contains(KALUPUR)) {
                if (Double.valueOf(estimatedprice) < 197)
                    pricetext.setText(ESTIMATED + RUPEE + PRICE);
                else
                    pricetext.setText(ESTIMATED + RUPEE + estimatedprice + DASH + price2);
            } else
                pricetext.setText(ESTIMATED + RUPEE + estimatedprice + DASH + price2);
        }

//        if (estimatedprice != null && !estimatedprice.isEmpty()) {
//            int price = Integer.parseInt(estimatedprice) + 20;
//            String priceto = "₹ " + estimatedprice + " - " + price;
//            pricetext.setText(priceto);
//        }

        if (cartype.equals("suv")) {
            rbtnCng.setVisibility(View.GONE);
            radioButtonDisel.setChecked(true);
            rbtnCng.setChecked(false);
            Log.i(TAG, " Car Type  : " + cartype);
        } else if (cartype.equals("sedan")) {
//            rbtnCng.setVisibility(View.GONE);
            radioButtonDisel.setChecked(false);
            rbtnCng.setChecked(true);
            Log.i(TAG, " Car Type  : " + cartype);
        } else {
            radioButtonDisel.setChecked(true);
            rbtnCng.setChecked(false);
            rbtnCng.setVisibility(View.VISIBLE);
        }

        radioButtonDisel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fueltype = "diesel";
                radioButtonDisel.setChecked(true);
                rbtnCng.setChecked(false);

                Log.e("Radio", "  rbtnDisel ==>>> " + fueltype);
                CallBudgetApi();
            }
        });

        rbtnCng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fueltype = "cng";
                radioButtonDisel.setChecked(false);
                rbtnCng.setChecked(true);
                Log.e("Radio", "  rbtnCng  ==>>> " + fueltype);
                CallBudgetApi();
            }
        });

        rbtnrStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarneyselected = "0";

                Log.e("Radio", "  rbtnrsingletrip  ==>>> ");
                rbtnrStrip.setChecked(true);
                rbtnrRtrip.setChecked(false);
                CallBudgetApi();
            }
        });

        rbtnrRtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Radio", "  rbtnrReturntrip  ==>>> ");
                jarneyselected = "1";
                rbtnrStrip.setChecked(false);
                rbtnrRtrip.setChecked(true);


                CallBudgetApi();
            }
        });

        textdate.setVisibility(View.GONE);
        if (!date.equalsIgnoreCase("")) {
            selectedCalendar.setTime(DateUtils.convertStringToDate(date));
            date1.setText(date);
            endtextdate.setText(date);
            selectedcar.setVisibility(View.GONE);
            dropdown.setVisibility(View.GONE);
            fuleselected.setTypeface(null, Typeface.NORMAL);
            fuleselected.setText(Html.fromHtml("<b>Notes:</b> Car will be available before 15 min at Start Ride time"));
            fuleselected.setTextSize(16);
        } else {
            datelayout.setVisibility(View.GONE);
            date1.setVisibility(View.GONE);
            enddatelayout.setVisibility(View.GONE);
        }

        if (condition != null && !condition.isEmpty()) {

            Log.e("condition", "  condition ==>>> " + condition);
            final String[] x = condition.split("\r\n*");
//            String y= condition.substring(211,condition.length()-1);


            if (x.length == 3) {
                conditionone.setText(Html.fromHtml(x[0] + x[1] + x[2]));
            } else if (x.length == 2) {
                conditionone.setText(Html.fromHtml(x[0] + x[1]));
            } else if (x.length == 1) {
                conditionone.setText(Html.fromHtml(x[0]));
            }


            String tearms = "";

            for (int i = 0; i < x.length; i++) {

                if (i > 2)
                    tearms = tearms + x[i];
            }

//        conditionone3.setText(Html.fromHtml(tearms));

            final String finalTearms = tearms;
            conditionone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InfoWindowDialog cdd = new InfoWindowDialog(SelectCarTypeActivity.this, finalTearms);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    cdd.show();
                }
            });
        }
//        String cartypepau = sharedpreferences.getString("cartype", "");
//        if (cartypepau != null && "suv".equalsIgnoreCase(cartypepau)) {
//            cnglayout.setVisibility(View.GONE);
//        }

        enddatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prebookingbutton = true;

//                Intent i = getIntent(); i.getStringExtra("date");

                //date =  i.getStringExtra("date");
//                selectedCalendar.setTime(DateUtils.convertStringToDate(endtextdate.getText().toString()));
//                showDialog(DATE_PICKER_ID);

            }
        });

        datelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectedCalendar.setTime(DateUtils.convertStringToDate(date));
                prebookingbutton = false;
                openDialog();
//                showDialog(DATE_PICKER_ID);

            }
        });

        prebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCalendar.setTime(DateUtils.convertStringToDate(date));
                prebookingbutton = false;
                showDialog(DATE_PICKER_ID);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id1) {
                selectedcarid = id[position];

                special_ratestring = special_rate[position];
                carnamestring = carname[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


//        cnglayout.setBackground(getResources().getDrawable(R.drawable.layout_border_selected));
//        petrollayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//        diesellayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//        cngcar.setImageDrawable(getResources().getDrawable(R.drawable.cngselected));
//        petrolcar.setImageDrawable(getResources().getDrawable(R.drawable.petrol));
//        dieselcar.setImageDrawable(getResources().getDrawable(R.drawable.diesel));
        if (date.equalsIgnoreCase("")) {
            getcarlist();
        }

//        cnglayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fueltype = "cng";
//                cnglayout.setBackground(getResources().getDrawable(R.drawable.layout_border_selected));
//                petrollayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//                diesellayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//                cngcar.setImageDrawable(getResources().getDrawable(R.drawable.cngselected));
//                petrolcar.setImageDrawable(getResources().getDrawable(R.drawable.petrol));
//                dieselcar.setImageDrawable(getResources().getDrawable(R.drawable.diesel));
//                if (date.equalsIgnoreCase("")) {
//                    getcarlist();
//                }
//
//            }
//        });
//        petrollayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fueltype = "petrol";
//                cnglayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//                petrollayout.setBackground(getResources().getDrawable(R.drawable.layout_border_selected));
//                diesellayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//                cngcar.setImageDrawable(getResources().getDrawable(R.drawable.cng));
//                petrolcar.setImageDrawable(getResources().getDrawable(R.drawable.petrolselected));
//                dieselcar.setImageDrawable(getResources().getDrawable(R.drawable.diesel));
//                if (date.equalsIgnoreCase("")) {
//                    getcarlist();
//                }
//
//            }
//        });
//        diesellayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fueltype = "diesel";
//                cnglayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//                petrollayout.setBackground(getResources().getDrawable(R.drawable.layout_border));
//                diesellayout.setBackground(getResources().getDrawable(R.drawable.layout_border_selected));
//                cngcar.setImageDrawable(getResources().getDrawable(R.drawable.cng));
//                petrolcar.setImageDrawable(getResources().getDrawable(R.drawable.petrol));
//                dieselcar.setImageDrawable(getResources().getDrawable(R.drawable.dieselselected));
//
//                if (date.equalsIgnoreCase("")) {
//                    getcarlist();
//                }
//            }
//        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectCarTypeActivity.this, CustHomePageActivity.class);
                startActivity(i);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onClick: " + date);
                Log.e(TAG, "onClick: " + date);
                if (!date.equalsIgnoreCase("")) {


                    try {
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        String dateold = sharedpreferences.getString("date", "");

                        Log.e("Dates ", dateold + "   currant   " + endtextdate.getText().toString());

                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                            Date startdate = dateFormat.parse(dateold);
                            Date enddate = dateFormat.parse(endtextdate.getText().toString());
                            long diff = enddate.getTime() - startdate.getTime();


                            diffDays = diff / (24 * 60 * 60 * 1000);

                            Log.e("DIIFE", "onClick: diffDays " + diffDays);

//                        Toast.makeText(activity, "" + diffDays, Toast.LENGTH_SHORT).show();


//                            if (enddate.after(startdate)) {
                            DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");

                            Date currentTime = Calendar.getInstance().getTime();

                            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                            // for check current time beetween 08:00 AM to 10:00 PM or trip time is same

//                            if (isCurrentTimeBeetweenTwoTime(outputFormat.format(currentTime))) {
//                                ConfirmationAdvanceBooking();
//                            } else if (!isCurrentTimeBeetweenTwoTime(outputFormat.format(currentTime)) && !isCurrentTimeBeetweenTwoTime(outputFormat.format(dateFormat2.parse(dateold)))) {
//                                startActivity(new Intent(getApplicationContext(), ServiceNotAvailable.class));
//                            } else {
                            ConfirmationAdvanceBooking();
//                            }
//                            } else {
//                                Toast.makeText(SelectCarTypeActivity.this, "Please select proper end date and time", Toast.LENGTH_SHORT).show();
//                            }
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }
                     /*   if (endtextdate.getText().toString().compareTo(dateold) <= 0) {
                            Toast.makeText(SelectCarTypeActivity.this, "Please select proper end date and time", Toast.LENGTH_SHORT).show();
                        } else {
                            //Log.e("zXJhckz",":zjhgxfcj");
                            advancebooking();
                        }*/
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }

                    //Toast.makeText(SelectCarTypeActivity.this, "Hi this is test", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        int selectedId = selectjourny.getCheckedRadioButtonId();


                        radioselectrd = (RadioButton) findViewById(selectedId);

                        if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single Way")) {
                            jarneyselected = "0";
                        } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return Trip")) {
                            jarneyselected = "1";
                        }
                        if (date.equalsIgnoreCase("")) {
                            if (special_ratestring.equalsIgnoreCase("0")) {
                                nextbutton();
                            } else {
                                popview(v);
                            }
                            //*nextbutton();
                        } else {
                            if (special_ratestring.equalsIgnoreCase("0")) {
                                nextbutton();
                            } else {
                                popview(v);
                            }

                            //  Toast.makeText(SelectCarTypeActivity.this, "Your PreeBooking is Confirm", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }
                }

            }
        });
    }

    public void openDialog() {

        Log.i(TAG, "Open openDialog()");

        final TextView tvShowNumbers;
        final TimePicker simpleTimePicker;
        final DatePicker simpleDatePicker;

        dialog = new Dialog(SelectCarTypeActivity.this);
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
                SimpleDateFormat currentYearFormate = new SimpleDateFormat("yyyy", Locale.getDefault());
                String currentYear = currentYearFormate.format(new Date());
                Log.i(TAG, "Current Year : " + currentYear);
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
                    String dtStart = Integer.valueOf(simpleDatePicker.getMonth() + 1) + "/" + simpleDatePicker.getDayOfMonth() + "/" + simpleDatePicker.getYear() + " " + Hour + ":" + Minute + " " + format;
                    SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    try {
                        date = formatTime.parse(dtStart);
                        Log.i(TAG, "Date Pasing Successfuly :: " + date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.i(TAG, "Date Pasing Exception ->" + e.getMessage());
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    String currentSelcted = sdf.format(date);

                    Date dateCurrent = null;
                    try {
                        dateCurrent = sdf.parse(currentDateandTime);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (dialog != null && dialog.isShowing()) {
                        long tenBefore = dateCurrent.getTime() + PRE_BOOKING_DELAY_TIME;
                        if (date.getTime() < tenBefore) {
                            Toast.makeText(SelectCarTypeActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
                        } else {
                            if (prebookingbutton) {
                                // Toast.makeText(SelectCarTypeActivity.this, date, Toast.LENGTH_SHORT).show();
                                //advancebooking();
                                endtextdate.setText(DateUtils.convertDateToString(selectedCalendar.getTime()));
                                date1.setText(DateUtils.convertDateToString(date));
                            } else {
                                date1.setText(DateUtils.convertDateToString(selectedCalendar.getTime()));
                                SharedPreferences sharedpreferences;
                                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("date", DateUtils.convertDateToString(date));
                                editor.apply();
                                date1.setText(DateUtils.convertDateToString(date));
                            }
                        }
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(SelectCarTypeActivity.this, " Please Select Current Year ", Toast.LENGTH_SHORT).show();
                }
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
        date1.setText(DateTime1);

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
                date1.setText(DateTime1);
            }
        });
//        simpleDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                String format = "";
//                int Hour = simpleTimePicker.getCurrentHour();
//                final int Minute = simpleTimePicker.getCurrentMinute();
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
//                date1.setText(DateTime1);
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            simpleDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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

                    String DateTime1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " " + Hour + ":" + Minute + " " + format;
                    tvShowNumbers.setText(DateTime1);
                    date1.setText(DateTime1);
                }
            });
        } else {
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
                    date1.setText(DateTime1);

                }
            });
        }


        dialog.show();

    }

    public Boolean isCurrentTimeBeetweenTwoTime(String time) {
        try {

            String string1 = "08:00:00";
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            String string2 = "22:00:00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);


            Date d = new SimpleDateFormat("HH:mm:ss").parse(time);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                //checkes whether the current time is between 08:00:00 and 10:00:00.
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
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
                Toast.makeText(SelectCarTypeActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
            } else {

                if (prebookingbutton) {
                    // Toast.makeText(SelectCarTypeActivity.this, date, Toast.LENGTH_SHORT).show();
                    //advancebooking();
                    endtextdate.setText(DateUtils.convertDateToString(selectedCalendar.getTime()));

                } else {
                    date1.setText(DateUtils.convertDateToString(selectedCalendar.getTime()));
                    date = DateUtils.convertDateToString(selectedCalendar.getTime());

                    Log.e("Test Test", date);
                    Log.e("Test Test DAT", String.valueOf(selectedCalendar.getTime()));
                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("date", date);
                    editor.apply();
                }
            }

        }
    };


    CButton conride, canride;

    void popview(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        farerate = popupView.findViewById(R.id.farerate);
        price = popupView.findViewById(R.id.price);
        conride = popupView.findViewById(R.id.conride);
        canride = popupView.findViewById(R.id.canride);


        farerate.setText(Html.fromHtml(getResources().getString(R.string.fare) + " <b>" + carnamestring + "</b> " + getResources().getString(R.string.wiilbe)));

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        price.setText(special_ratestring + " Extra per KM ");
        conride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                /*Intent i = new Intent(SelectCarTypeActivity.this,SelectedPakegeShowActivity.class);
                startActivity(i);
                finish();*/
                nextbutton();
            }
        });
        canride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // dismiss the popup window when touched
        /*popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });*/
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress1 = bundle.getString("address");


                    break;
                default:
                    locationAddress1 = null;
            }
            //  Toast.makeText(Drivar_map.this, locationAddress.toString(), Toast.LENGTH_LONG).show();

        }
    }

    void advancebooking() {


        CallBudgetApi();

        /*

        int selectedId = selectjourny.getCheckedRadioButtonId();
        radioselectrd = (RadioButton) findViewById(selectedId);
        if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single Way")) {
            jarneyselected = "0";
        } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return Trip")) {
            jarneyselected = "1";
        }

        Log.e("dis", "advancebooking: distancevalue" + distancevalue);
        Log.e("dis", "advancebooking: durationvalue" + durationvalue);


        *//*

        int selectedId = selectjourny.getCheckedRadioButtonId();
        radioselectrd = (RadioButton) findViewById(selectedId);
        if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single Way")) {
            jarneyselected = "0";
        } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return Trip")) {
            jarneyselected = "1";
        }

        *//*

        final String[] latlong = locationAddress1.split(",");


        progstart(SelectCarTypeActivity.this, "Loading...", "Loading...");

        StringRequest request = new StringRequest(Request.Method.POST, advancebooking, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("Your Array Response", response.toString());
                Log.e("advance", "Select car type response:" + new Gson().toJson(response));

                try {
                    JSONObject main = new JSONObject(response);
                    JSONObject submian = main.getJSONObject("success");
                    String succ = submian.getString("status");
                    if (succ.equalsIgnoreCase("success")) {
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove("date");
                        editor.remove("jsonData");
                        editor.remove("end_address");
                        editor.remove("start_address");
                        editor.remove("flegincityoroutcitypau");
                        editor.remove("cartypepau");
                        editor.apply();

                        Intent i = new Intent(SelectCarTypeActivity.this, AdvanceBookingThanks.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
                Toast.makeText(SelectCarTypeActivity.this, "Please try again", Toast.LENGTH_LONG).show();
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

                String DateandTime[] = DateUtils.convertDateToString(selectedCalendar.getTime()).split(" ");
                //DateUtils.convertDateToString(selectedCalendar.getTime());


                params.put("start_date_time", date);
                params.put("user_id", userid);
                params.put("end_point", end_address);
                params.put("start_point", start_address);
                params.put("end_longitude", end_lng);
                params.put("end_latitude", end_lat);
                params.put("start_latitude", start_lat);
                params.put("start_longitude", start_lng);
                params.put("car_type", cartype);
                params.put("fueltype", fueltype);
                params.put("incity", flegincityoroutcity);
                params.put("roundtrip", jarneyselected);
                params.put("end_date", DateandTime[0]);
                params.put("end_time", DateandTime[1] + " " + DateandTime[2]);

                Log.e("advance", "Select car type :" + new Gson().toJson(params));

                Log.e("start_date_time", date);
                Log.e("user_id", userid);
                Log.e("end_point", end_address);
                Log.e("start_point", start_address);
                Log.e("end_longitude", end_lng);
                Log.e("end_latitude", end_lat);
                Log.e("start_latitude", start_lat);
                Log.e("start_longitude", start_lng);
                Log.e("car_type", cartype);
                Log.e("fueltype", fueltype);
                Log.e("incity", flegincityoroutcity);
                Log.e("roundtrip", jarneyselected);
                Log.e("end_date", DateandTime[0]);
                Log.e("end_time", DateandTime[1] + " " + DateandTime[2]);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);*/
    }

    private void CallBudgetApi() {

//        int selectedId = selectjourny.getCheckedRadioButtonId();
//        radioselectrd = (RadioButton) findViewById(selectedId);
//        if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single")) {
//            jarneyselected = "0";
//        } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return")) {
//            jarneyselected = "1";
//        }

        StringRequest request = new StringRequest(Request.Method.POST, getridenowpackagesapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, " Select eadvance res Get : " + response);
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
                    condition = packageobj.getString("condition");
                    String estimatedprice = mainobject.getString("estimatedprice");
                    String car_rate_per_kl = mainobject.getString("car_rate_per_kl");
                    String car_rate_per_hr = mainobject.getString("car_rate_per_hr");
                    String wallet_amount = mainobject.getString("wallet_amount");
                    String id = packageobj.getString("id");

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
                    editor.putString("end_date", DateandTime[0]);
                    editor.putString("end_time", DateandTime[1] + " " + DateandTime[2]);
                    editor.putString("wallet_amount", wallet_amount);
                    editor.putString("package_id", id);
                    editor.apply();

                    /*int selectedId = selectjourny.getCheckedRadioButtonId();
                    radioselectrd = (RadioButton) findViewById(selectedId);
                    if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Single Way")) {
                        jarneyselected = "0";
                    } else if (radioselectrd.getText().toString().trim().equalsIgnoreCase("Return Trip")) {
                        jarneyselected = "1";
                    }*/

//                    Intent i = new Intent(SelectCarTypeActivity.this, AdvancebknSelectedPakegeShow.class);
//                    startActivity(i);

                    try {
                        if (flegincityoroutcity.equalsIgnoreCase("0") && jarneyselected.equalsIgnoreCase("0")) {
                            jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + "</b> "));

                        } else {
                            String[] dist = distanceText.split(" ");

                            float newtotalkm = Float.valueOf(dist[0]);
                            newtotalkm = newtotalkm * 2;
                            Log.e("newtotalkm", dist[0]);
                            Log.e("newtotalkm", String.valueOf(newtotalkm));
                            //
//                            jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + " x 2 = " + String.valueOf(newtotalkm) + " km</b> "));
                        }
                    } catch (Exception e) {
                        Log.e("distanceText", "" + e.getMessage() + " Line : " + e.getStackTrace()[0].getLineNumber());
                    }

                    if (mainobject.has("estimatedprice") && !mainobject.getString("estimatedprice").isEmpty()) {
                        int price2 = Integer.parseInt(mainobject.getString("estimatedprice")) + 20;
                        String priceto = "₹ " + mainobject.getString("estimatedprice") + " - " + price2;
                        pricetext.setText(priceto);
                    }

                } catch (Exception e) {
                    Log.e("Exception  ", "Ridenow class" + e.toString());
                }
                //image+'cars/primary/thumbnail/'+carimage;

                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
                Toast.makeText(SelectCarTypeActivity.this, "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                Log.e("Authorization", "Bearer " + logtokan);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("start_lat", start_lat);
                params.put("start_lng", start_lng);
                params.put("end_lat", end_lat);
                params.put("end_lng", end_lng);
                params.put("totalkm", distancevalue);
                params.put("totalhr", durationvalue);
                params.put("cartype", cartype);
                params.put("userid", userid);
                params.put("incity", flegincityoroutcity);
                params.put("fueltype", fueltype);
                params.put("triptype", jarneyselected);
                params.put("days", "0");

                Log.e("SelectCarType", " selectcar getridenowPackages param ---->>  " + new Gson().toJson(params));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }

    private void ConfirmationAdvanceBooking() {
        progstart(SelectCarTypeActivity.this, "Loading...", "Loading...");

        StringRequest request = new StringRequest(Request.Method.POST, advancebooking, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Select car type response:" + new Gson().toJson(response));
                try {
                    JSONObject main = new JSONObject(response);
                    JSONObject submian = main.getJSONObject("success");
                    String succ = submian.getString("status");
                    if (succ.equalsIgnoreCase("success")) {
                        SharedPreferences sharedpreferences;
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove("date");
                        editor.remove("jsonData");
                        editor.remove("end_address");
                        editor.remove("start_address");
                        editor.remove("flegincityoroutcitypau");
                        editor.remove("cartypepau");
                        editor.apply();

                        Intent i = new Intent(SelectCarTypeActivity.this, AdvanceBookingThanksActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
                Toast.makeText(SelectCarTypeActivity.this, "Please try again", Toast.LENGTH_LONG).show();
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

                SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                params.put("start_date_time", prefs.getString("date", ""));
                params.put("user_id", userid);
                params.put("end_point", end_address);
                params.put("start_point", start_address);
                params.put("end_longitude", end_lng);
                params.put("end_latitude", end_lat);
                params.put("start_latitude", start_lat);
                params.put("start_longitude", start_lng);
                params.put("car_type", cartype);
                params.put("fueltype", fueltype);
                params.put("incity", flegincityoroutcity);
                params.put("roundtrip", jarneyselected);
                params.put("totalhr_value", durationvalue);
                params.put("totalkm_value", distancevalue);
                params.put("end_date", prefs.getString("end_date", ""));
                params.put("end_time", prefs.getString("end_time", ""));
                params.put("package_id", prefs.getString("package_id", ""));


                if (estimatedprice != null && !estimatedprice.isEmpty()) {
                    int price2 = Integer.parseInt(estimatedprice) + 20;
                    if (start_address.toLowerCase().contains(AIRPORT)) {
                        params.put("estimatedprice", String.valueOf(Integer.parseInt(estimatedprice)+90));
                    } else if (start_address.toLowerCase().contains(KALUPUR) || end_address.toLowerCase().contains(KALUPUR)) {
                        if (Double.valueOf(estimatedprice) < 197)
                            params.put("estimatedprice", "197");
                        else
                            params.put("estimatedprice", estimatedprice);
                    } else
                        params.put("estimatedprice", estimatedprice);
                }


                /*Log.e("start_date_time", date);
                Log.e("user_id", userid);
                Log.e("end_point", end_address);
                Log.e("start_point", start_address);
                Log.e("end_longitude", end_lng);
                Log.e("end_latitude", end_lat);
                Log.e("start_latitude", start_lat);
                Log.e("start_longitude", start_lng);
                Log.e("car_type", cartype);
                Log.e("fueltype", fueltype);
                Log.e("incity", flegincityoroutcity);
                Log.e("roundtrip", jarneyselected);
                Log.e("end_date", DateandTime[0]);
                Log.e("end_time", DateandTime[1] + " " + DateandTime[2]);*/

                Log.e("AdvanceBknPkgShow", " param --->> " + new Gson().toJson(params));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    void getcarlist() {

        progstart(SelectCarTypeActivity.this, "Loading...", "Loading...");

        StringRequest request = new StringRequest(Request.Method.POST, getcardetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("carlist", "carlidt response-->" + new Gson().toJson(response));
                try {
                    JSONObject jobject = new JSONObject(response);
                    JSONObject mainobject = jobject.getJSONObject("success");
                    String status = mainobject.getString("status");
                    if (status.equalsIgnoreCase("empty_records")) {
                        fuleselected.setText(getResources().getString(R.string.nocar));
                        fuleselected.setVisibility(View.VISIBLE);
                        dropdown.setVisibility(View.GONE);
                    } else {
                        dropdown.setVisibility(View.VISIBLE);

                        JSONArray cars = mainobject.getJSONArray("cars");

                        id = new String[cars.length()];
                        carname = new String[cars.length()];
                        carimage = new String[cars.length()];
                        carnumber = new String[cars.length()];
                        special_rate = new String[cars.length()];
                        distance = new String[cars.length()];
                        Duration = new String[cars.length()];
                        for (int i = 0; i < cars.length(); i++) {
                            id[i] = cars.getJSONObject(i).getString("id");
                            carname[i] = cars.getJSONObject(i).getString("name");
                            carimage[i] = cars.getJSONObject(i).getString("carimage");
                            carnumber[i] = cars.getJSONObject(i).getString("carnumber");
                            special_rate[i] = cars.getJSONObject(i).getString("special_rate");
                            distance[i] = cars.getJSONObject(i).getString("distance");
                            Duration[i] = cars.getJSONObject(i).getString("Duration");

                        }

                        //  dropdown.setVisibility(View.GONE);
                        //(new CarListingAdapter(SelectCarTypeActivity.this,id,carname,carimage,carnumber));
                        mAdapter = new CarListingAdapter(SelectCarTypeActivity.this, id, carname, carimage, carnumber, distance, Duration);
                        dropdown.setAdapter(mAdapter);
                        fuleselected.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                //image+'cars/primary/thumbnail/'+carimage;
                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
                Toast.makeText(SelectCarTypeActivity.this, "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                Log.e("token", "getHeaders: " + logtokan);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("start_lat", start_lat);
                params.put("start_lng", start_lng);
                params.put("cartype", cartype);
                params.put("fueltype", fueltype);

                Log.e("carlist", "carlidt req-->" + new Gson().toJson(params));


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    void nextbutton() {

        progstart(SelectCarTypeActivity.this, "Loading...", "Loading...");

        StringRequest request = new StringRequest(Request.Method.POST, getpackages, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Your Array Response", response);

                try {
                    JSONObject jobject = new JSONObject(response);
                    JSONObject mainobject = jobject.getJSONObject("success");
                    JSONObject packageobj = mainobject.getJSONObject("package");
                    String packegename = packageobj.getString("name");
                    String cartype = packageobj.getString("car_type");
                    String kilometer = packageobj.getString("kilometer");
                    String hours = packageobj.getString("hours");
                    String price = packageobj.getString("price");
                    String condition = packageobj.getString("condition");
                    String estimatedprice = mainobject.getString("estimatedprice");
                    String car_rate_per_kl = mainobject.getString("car_rate_per_kl");
                    String car_rate_per_hr = mainobject.getString("car_rate_per_hr");

                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("packegename", packegename);
                    editor.putString("car_type", cartype);
                    editor.putString("kilometer", kilometer);
                    editor.putString("hours", hours);
                    editor.putString("price", price);
                    editor.putString("condition", condition);
                    editor.putString("estimatedprice", estimatedprice);
                    editor.putString("car_rate_per_kl", car_rate_per_kl);
                    editor.putString("car_rate_per_hr", car_rate_per_hr);
                    editor.putString("special_ratestring", special_ratestring);
                    editor.putString("selectedcarid", selectedcarid);
                    editor.apply();
                    ;

                    Intent i = new Intent(SelectCarTypeActivity.this, SelectedPakegeShowActivity.class);
                    startActivity(i);
                    finish();

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                //image+'cars/primary/thumbnail/'+carimage;
                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
                Toast.makeText(SelectCarTypeActivity.this, "Please try again", Toast.LENGTH_LONG).show();
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

                params.put("totalkm", distancevalue);
                params.put("totalhr", durationvalue);
                params.put("carid", selectedcarid);
                params.put("triptype", jarneyselected);
                params.put("incity", flegincityoroutcity);
                params.put("cartype", cartype);
                params.put("fueltype", fueltype);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}
