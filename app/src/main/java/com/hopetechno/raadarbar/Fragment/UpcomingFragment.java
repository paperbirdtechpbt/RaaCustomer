package com.hopetechno.raadarbar.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hopetechno.raadarbar.Adapter.RideHistryUpdateAdapter;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class UpcomingFragment extends Fragment implements AppConstant {
    ListView listview;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_upcoming, container, false);
        listview = RootView.findViewById(R.id.listview);
        mContext = getActivity();
        hederget();

        return RootView;
    }

    void hederget() {

        progstart(mContext, "Loading...", "Loading...");

        final String logtokan;
        SharedPreferences prefs = mContext.getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");

        StringRequest request = new StringRequest(Request.Method.GET, ridehistory_upcoming, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("upcoming response ", response);
                    JSONObject mainobject = new JSONObject(response);
                    JSONObject succ = mainobject.getJSONObject("success");
                    String status = succ.getString("status");
                    Log.e("status", status);
                    if (status.equalsIgnoreCase("empty_records")) {
                        progstop();
                        Toast.makeText(mContext, getResources().getString(R.string.tripnotaveleble), Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray trips = succ.getJSONArray("trips");
                    String[] id, start_date, end_date, start_time, end_time, start_point, end_point, car_type, fueltype, incity, roundtrip, start_longitude, start_latitude, end_longitude, end_latitude;
                    Log.e("trips", String.valueOf(trips));
                    id = new String[trips.length()];
                    start_date = new String[trips.length()];
                    end_date = new String[trips.length()];
                    start_time = new String[trips.length()];
                    end_time = new String[trips.length()];
                    start_point = new String[trips.length()];
                    end_point = new String[trips.length()];
                    car_type = new String[trips.length()];
                    fueltype = new String[trips.length()];
                    incity = new String[trips.length()];
                    roundtrip = new String[trips.length()];
                    start_longitude = new String[trips.length()];
                    start_latitude = new String[trips.length()];
                    end_longitude = new String[trips.length()];
                    end_latitude = new String[trips.length()];
                    for (int i = 0; i < trips.length(); i++) {

                        id[i] = trips.getJSONObject(i).getString("id");
                        start_date[i] = trips.getJSONObject(i).getString("start_date");
                        end_date[i] = trips.getJSONObject(i).getString("end_date");
                        start_time[i] = trips.getJSONObject(i).getString("start_time");
                        end_time[i] = trips.getJSONObject(i).getString("end_time");
                        start_point[i] = trips.getJSONObject(i).getString("start_point");
                        end_point[i] = trips.getJSONObject(i).getString("end_point");
                        car_type[i] = trips.getJSONObject(i).getString("car_type");
                        fueltype[i] = trips.getJSONObject(i).getString("fueltype");
                        incity[i] = trips.getJSONObject(i).getString("incity");
                        roundtrip[i] = trips.getJSONObject(i).getString("roundtrip");
                        start_longitude[i] = trips.getJSONObject(i).getString("start_longitude");
                        start_latitude[i] = trips.getJSONObject(i).getString("start_latitude");
                        end_longitude[i] = trips.getJSONObject(i).getString("end_longitude");
                        end_latitude[i] = trips.getJSONObject(i).getString("end_latitude");

                    }

                    RideHistryUpdateAdapter adeptor = new RideHistryUpdateAdapter(mContext,start_longitude,start_latitude,end_longitude,end_latitude, id, start_date, end_date, start_time, end_time, start_point, end_point, car_type, fueltype, incity, roundtrip);
                    listview.setAdapter(adeptor);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Jo kljzdhckjhs", e.toString());
                }
                progstop();
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


        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
