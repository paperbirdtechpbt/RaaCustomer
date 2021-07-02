package com.hopetechno.raadarbar.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class RideDetailActivity extends AppCompatActivity implements AppConstant {

    Context mContext;
    String id = "", roundtrip;
    CTextView startdate, starttime, startpoint, endotp, startotp, endpoint, triptype, totalfaretext, totalfaretext123, enddate, endtime;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RideDetailActivity.this, RideHistoryActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);
        mContext = RideDetailActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        MyApplication.setCurrentActivity(this);

        Intent i = getIntent();
        id = i.getStringExtra("id");


        startdate = findViewById(R.id.startdate);
        starttime = findViewById(R.id.starttime);
        startpoint = findViewById(R.id.startpoint);
        endpoint = findViewById(R.id.endpoint);
        triptype = findViewById(R.id.triptype);
        totalfaretext = findViewById(R.id.totalfare);
        totalfaretext123 = findViewById(R.id.totalfaretext);
        enddate = findViewById(R.id.enddate);
        endtime = findViewById(R.id.endtime);
        startotp = findViewById(R.id.startotp);
        endotp = findViewById(R.id.endotp);

        Log.e(":id:  ", id);
        GetDetail();

    }

    void GetDetail() {

        progstart(mContext, "Loading...", "Loading...");

        final String logtokan;
        SharedPreferences prefs = mContext.getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        Log.e("hihihihi", logtokan);
        String Url = ridehistory_detail + "/" + id;
        Log.e("URLL", Url);
        StringRequest request = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Your Array Response", response);

                try {
                    JSONObject mainobject = new JSONObject(response);
                    JSONObject succ = mainobject.getJSONObject("success");
                    String status = succ.getString("status");
                    Log.e("status", status);
                    if (status.equalsIgnoreCase("empty_records")) {

                        Toast.makeText(mContext, getResources().getString(R.string.tripnotaveleble), Toast.LENGTH_LONG).show();
                        progstop();
                        return;
                    }
                    JSONObject trips = succ.getJSONObject("trips");
                    String bookingdate, booking_time, id1, start_date, end_date,start_otp,end_otp, start_point, end_point, start_latitude, start_km, start_time, end_longitude, end_latitude, end_km, end_time, ac_trip, trip_rate_per_km, trip_rate_per_hr, extra_kl, extra_hr, extra_per_kl_charge, extra_per_hr_charge, extra_kl_charge, extra_hr_charge, fare,
                            taxandparking, tax1, tax2, tax_total_price, totalfare, estimated_fare, payment_mode, trip_status;

                    bookingdate = trips.getString("advance_booking_date");
                    booking_time = trips.getString("advance_booking_time");
                    start_point = trips.getString("start_point");
                    end_point = trips.getString("end_point");
                    start_otp = trips.getString("start_otp");
                    end_otp = trips.getString("end_otp");
                    start_latitude = trips.getString("start_latitude");
                    start_km = trips.getString("start_km");
                    start_date = trips.getString("start_date");
                    start_time = trips.getString("start_time");
                    end_longitude = trips.getString("end_longitude");
                    end_latitude = trips.getString("end_latitude");
                    end_km = trips.getString("end_km");
                    end_date = trips.getString("end_date");
                    end_time = trips.getString("end_time");
                    ac_trip = trips.getString("ac_trip");
                    trip_rate_per_km = trips.getString("trip_rate_per_km");
                    trip_rate_per_hr = trips.getString("trip_rate_per_hr");
                    extra_kl = trips.getString("extra_kl");
                    extra_hr = trips.getString("extra_hr");
                    extra_per_kl_charge = trips.getString("extra_per_kl_charge");
                    extra_per_hr_charge = trips.getString("extra_per_hr_charge");
                    extra_kl_charge = trips.getString("extra_kl_charge");
                    extra_hr_charge = trips.getString("extra_hr_charge");
                    fare = trips.getString("fare");
                    taxandparking = trips.getString("taxandparking");
                    id1 = trips.getString("id");
                    payment_mode = trips.getString("payment_mode");
                    trip_status = trips.getString("trip_status");
                    tax1 = trips.getString("tax1");
                    tax2 = trips.getString("tax2");
                    tax_total_price = trips.getString("tax_total_price");
                    totalfare = trips.getString("totalfare");
                    estimated_fare = trips.getString("estimated_fare");
                    roundtrip = trips.getString("roundtrip");


                    enddate.setText(end_date);
                    endtime.setText(end_time);

                    startdate.setText(start_date);
                    starttime.setText(start_time);
                    startpoint.setText(start_point);
                    endpoint.setText(end_point);
                    endotp.setText(end_otp);
                    startotp.setText(start_otp);

                    if (roundtrip.equalsIgnoreCase("0")) {
                        triptype.setText("Single Trip");
                    } else if (roundtrip.equalsIgnoreCase("1")) {
                        triptype.setText("Round Trip");
                    }

                    if (totalfare != null && !totalfare.isEmpty() && !totalfare.equals("null")) {
                        totalfaretext.setText(totalfare);
                    } else {
                        totalfaretext.setVisibility(View.GONE);
                        totalfaretext123.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Jo kljzdhckjhs", e.toString());
                }

                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progstop();
                Log.e("error is ", "" + error);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
