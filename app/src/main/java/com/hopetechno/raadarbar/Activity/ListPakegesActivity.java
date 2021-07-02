package com.hopetechno.raadarbar.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Adapter.CustomExpandableListAdapter;
import com.hopetechno.raadarbar.Adapter.PakegesAdeptor;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Other.GeocodingLocation;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.DateUtils;
import com.hopetechno.raadarbar.Utils.MyApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class ListPakegesActivity extends AppCompatActivity implements AppConstant {


    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    ListView listone;
    String carpakeges = "", feultype = "", locationAddress1 = "", locationAddress2 = "", Sorce = "", dest = "", userid = "", date = "";
    String[] id, name, kilometer, hours, tax1, tax2, price, condition;
    LinearLayout datelayout;
    CTextView date1;
    Calendar todayCalendar, selectedCalendar;
    List<String> fultype = new ArrayList<>();

    Dialog dialog;

    public String TAG = "ListPackages";


    @Override
    public void onBackPressed() {
        Intent i = new Intent(ListPakegesActivity.this, CustHomePageActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    CTextView selectedvelue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pakeges);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MyApplication.setCurrentActivity(this);

        todayCalendar = Calendar.getInstance();
        selectedCalendar = Calendar.getInstance();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listone = findViewById(R.id.listone);
        selectedvelue = findViewById(R.id.selectedvelue);
        datelayout = (LinearLayout) findViewById(R.id.datelayout);
        date1 = (CTextView) findViewById(R.id.date);


        try {
            Intent i = getIntent();
            Sorce = i.getStringExtra("Sorce");
            dest = i.getStringExtra("dest");
            date = i.getStringExtra("date");
            GeocodingLocation locationAddress = new GeocodingLocation();
            locationAddress.getAddressFromLocation(Sorce,
                    getApplicationContext(), new GeocoderHandler());


            GeocodingLocation locationAddress1 = new GeocodingLocation();
            locationAddress1.getAddressFromLocation(dest,
                    getApplicationContext(), new GeocoderHandler1());

        } catch (Exception e) {
            Log.e("Exeption", "List Pakeges" + e.toString());
        }

        try {
            if (!date.equalsIgnoreCase("")) {
                selectedCalendar.setTime(DateUtils.convertStringToDate(date));
                date1.setText(date);

            } else {
                datelayout.setVisibility(View.GONE);
                date1.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            datelayout.setVisibility(View.GONE);
            date1.setVisibility(View.GONE);
        }

        List<String> cricket = new ArrayList<String>();
        cricket.add(getResources().getString(R.string.selectcar));
        cricket.add(getResources().getString(R.string.selectfuel));

        expandableListTitle = new ArrayList<String>(cricket);


        expandableListDetail = getData();
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                expandableListView.collapseGroup(groupPosition);
                if (groupPosition == 0) {
                    ///cartype = "micromini";
                    carpakeges = expandableListDetail.get(
                            expandableListTitle.get(groupPosition)).get(
                            childPosition);

                    selectedvelue.setVisibility(View.VISIBLE);
                    selectedvelue.setText(Html.fromHtml(getResources().getString(R.string.selectcar) + ":  <b>" + carpakeges));

                    if (carpakeges.equalsIgnoreCase("suv")) {
                        if(fultype != null)
                        fultype.remove(0);
                        //  expandableListAdapter.notify();
                    } else {
                        fultype.clear();
                        fultype.add(getResources().getString(R.string.cng));
                        fultype.add(getResources().getString(R.string.diesel));
                    }
                    expandableListView.collapseGroup(1);

                } else {

                    feultype = expandableListDetail.get(
                            expandableListTitle.get(groupPosition)).get(
                            childPosition);

                    //Toast.makeText(ListPakegesActivity.this, String.valueOf(groupPosition), Toast.LENGTH_LONG).show();

                    if (carpakeges.equalsIgnoreCase("")) {
                        Toast.makeText(ListPakegesActivity.this, getResources().getString(R.string.selectcar), Toast.LENGTH_LONG).show();
                    } else {
                        selectedvelue.setText(Html.fromHtml(getResources().getString(R.string.selectcar) + ":  <b>" + carpakeges + "</b> <br>"
                                + getResources().getString(R.string.selectfuel) + ":  <b>" + feultype));
                        if (carpakeges.equalsIgnoreCase("Micro/Mini"))
                            carpakeges = "micromini";
                        callapi();
                    }

                }

                return false;
            }
        });

        listone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                showSettingsAlert(id[position]);
            }
        });

        datelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog(DATE_PICKER_ID);
                openDialog();

            }
        });

    }
    public void openDialog() {

        Log.i(TAG, "Open openDialog()");

        final TextView tvShowNumbers;
        final TimePicker simpleTimePicker;
        final DatePicker simpleDatePicker;

        dialog = new Dialog(ListPakegesActivity.this);
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
                            Toast.makeText(ListPakegesActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
                        } else {
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("date", DateUtils.convertDateToString(date));
                            editor.apply();
                        }

                        date1.setText(DateUtils.convertDateToString(date));
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(ListPakegesActivity.this, " Please Select Current Year ", Toast.LENGTH_SHORT).show();
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

                }
            });
        }

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

                }
            });
        }

        dialog.show();

    }

    public void showSettingsAlert(final String id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListPakegesActivity.this);


        alertDialog.setTitle("Confirm package");

        alertDialog.setMessage("Are you sure you want to select this package ?");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (date != null && !date.isEmpty() && !date.equals("null")) {

                    long tenBefore = System.currentTimeMillis() + PRE_BOOKING_DELAY_TIME;
                    if (selectedCalendar.getTimeInMillis() < tenBefore) {
                        Toast.makeText(ListPakegesActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
                    } else {
                        advancebooking(id);
                    }
                } else {
                    callapi2(id);
                }

            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
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
                Toast.makeText(ListPakegesActivity.this, getString(R.string.error_time_before), Toast.LENGTH_LONG).show();
            } else {
                Log.e("Test Test", date);
                Log.e("Test Test DAT", String.valueOf(selectedCalendar.getTime()));

                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("date", date);
                editor.apply();
            }
            date1.setText(DateUtils.convertDateToString(selectedCalendar.getTime()));
            date = DateUtils.convertDateToString(selectedCalendar.getTime());
        }
    };

    void advancebooking(final String pack_id) {

        progstart(ListPakegesActivity.this, "Loading...", "Loading...");
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        userid = prefs.getString("userid", "");

        final String[] latlong = locationAddress1.split(",");
        final String[] latlong1 = locationAddress2.split(",");

        StringRequest request = new StringRequest(Request.Method.POST, advancebooking, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("advance", "List package response:" + new Gson().toJson(response));


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
                        ;

                        //Toast.makeText(ListPakegesActivity.this,submian.getString("message") , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ListPakegesActivity.this, AdvanceBookingThanksActivity.class);
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
                Toast.makeText(ListPakegesActivity.this, "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                Log.d("asdf   ","Bearer " + logtokan);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("start_date_time", date);
                params.put("user_id", userid);
                params.put("end_point", dest);
                params.put("start_point", Sorce);
                params.put("end_longitude", latlong1[1]);
                params.put("end_latitude", latlong1[0]);
                params.put("start_latitude", latlong[0]);
                params.put("start_longitude", latlong[1]);
                params.put("car_type", carpakeges);
                params.put("fueltype", feultype);
                params.put("package_id", pack_id);

                Log.e(TAG,"Rental param :"+new Gson().toJson(params));


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
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

    private class GeocoderHandler1 extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress2 = bundle.getString("address");
                    break;
                default:
                    locationAddress2 = null;
            }
            //  Toast.makeText(Drivar_map.this, locationAddress.toString(), Toast.LENGTH_LONG).show();

        }
    }

    String logtokan;

    void postRequest(String postBody) throws IOException {


        Intent i = new Intent(ListPakegesActivity.this, NewCustomerWaitingActivity.class);
        startActivity(i);

        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("inRental", "1");
        editor.putString("isridestart", "yes");
        editor.apply();
        ;

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
                    Log.e("exeption", e1.toString());
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                Log.e("ListPackages", " ListPackages -->>> onResponse: " + response);

                try {
                    JSONObject mainobject = new JSONObject(String.valueOf(response));
                    JSONObject succ = mainobject.getJSONObject("success");
                    String status = succ.getString("status");
                    if (status.equalsIgnoreCase("empty_records")) {

                        Toast.makeText(ListPakegesActivity.this, getResources().getString(R.string.tripnotaveleble), Toast.LENGTH_LONG).show();
                        progstop();
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Jo kljzdhckjhs", e.toString());
                }

            }
        });
    }


    void callapi2(final String pack_id) {

        progstart(ListPakegesActivity.this, "Loading...", "Loading...");

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        userid = prefs.getString("userid", "");

        String[] latlong = locationAddress1.split(",");
        String[] latlong1 = locationAddress2.split(",");


        try {
            JSONObject params = new JSONObject();
            params.put("user_id", userid);
            params.put("start_point", Sorce);
            params.put("start_lat", latlong[0]);
            params.put("start_lng", latlong[1]);
            params.put("end_point", dest);
            params.put("end_lat", latlong1[0]);
            params.put("end_lng", latlong1[1]);
            params.put("cartype", carpakeges);
            params.put("fueltype", feultype);
            params.put("package_id", pack_id);
            postRequest(String.valueOf(params));
            Log.e("Loginpppa", String.valueOf(params));
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    void callapi() {
        //Progressbar starting
        progstart(ListPakegesActivity.this, "Loading...", "Loading...");

        //Getting Login Token from the Local Storage
        final String logtokan;
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");

        // FOR API CLLING
        StringRequest request = new StringRequest(Request.Method.POST, getpackageslist, new Response.Listener<String>() {
            // For API Response
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject main = new JSONObject(response);
                    JSONObject succ = main.getJSONObject("success");
                    if (!succ.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(ListPakegesActivity.this, succ.getString("status"), Toast.LENGTH_LONG).show();
                        progstop();
                        return;
                    }
                    JSONArray packages = succ.getJSONArray("packages");

                    id = new String[packages.length()];
                    name = new String[packages.length()];
                    kilometer = new String[packages.length()];
                    hours = new String[packages.length()];
                    tax1 = new String[packages.length()];
                    tax2 = new String[packages.length()];
                    price = new String[packages.length()];
                    condition = new String[packages.length()];
                    for (int i = 0; i < packages.length(); i++) {
                        id[i] = packages.getJSONObject(i).getString("id");
                        name[i] = packages.getJSONObject(i).getString("name");
                        kilometer[i] = packages.getJSONObject(i).getString("kilometer");
                        hours[i] = packages.getJSONObject(i).getString("hours");
                        tax1[i] = packages.getJSONObject(i).getString("tax1");
                        tax2[i] = packages.getJSONObject(i).getString("tax2");
                        price[i] = packages.getJSONObject(i).getString("price");
                        condition[i] = packages.getJSONObject(i).getString("condition");
                    }
                    PakegesAdeptor adeptor = new PakegesAdeptor(ListPakegesActivity.this, id, name, kilometer, hours, tax1, tax2, price, condition);
                    listone.setAdapter(adeptor);

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                // For Stopping Progress Bar
                progstop();
            }
            // For API Response ErrorListener
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                // For Stopping Progress Bar
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
                params.put("cartype", carpakeges);
                params.put("fueltype", feultype);

                return params;
            }
        };
        // Create Queue for calling API
        RequestQueue queue = Volley.newRequestQueue(ListPakegesActivity.this);
        // Add Request in queue
        queue.add(request);
    }

    public HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> carpakeges = new ArrayList<String>();
//        carpakeges.add(getResources().getString(R.string.micromini));
        carpakeges.add(getResources().getString(R.string.sedan));
        carpakeges.add(getResources().getString(R.string.suv));

        fultype = new ArrayList<String>();

        fultype.add(getResources().getString(R.string.cng));
        fultype.add(getResources().getString(R.string.diesel));

        expandableListDetail.put(getResources().getString(R.string.selectcar), carpakeges);
        expandableListDetail.put(getResources().getString(R.string.selectfuel), fultype);

        return expandableListDetail;
    }
}
