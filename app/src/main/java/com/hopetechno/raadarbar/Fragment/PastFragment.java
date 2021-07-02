package com.hopetechno.raadarbar.Fragment;

import android.app.ProgressDialog;
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
import com.hopetechno.raadarbar.Adapter.RideHistryAdeptor;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PastFragment extends Fragment implements AppConstant {
    ListView listview;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_past, container, false);

        listview = RootView.findViewById(R.id.listview);
        mContext = getActivity();
        hederget();
        return RootView;

    }

    void hederget() {

        final ProgressDialog progress = new ProgressDialog(mContext);
        progress.setMessage("Loading...");
        progress.setTitle("Loading...");
        progress.setCancelable(false);
        progress.show();
        // progstart(mContext,"Loading...","Loading...");

        final String logtokan;
        SharedPreferences prefs = mContext.getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        Log.e("hihihihi", ridehistory_past);
        Log.e("hihihihi", logtokan);
        StringRequest request = new StringRequest(Request.Method.GET, ridehistory_past, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Your Array Response", response);

                Log.d("past response ",response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    JSONObject succ = mainobject.getJSONObject("success");
                    String status = succ.getString("status");
                    Log.e("status", status);
                    if (status.equalsIgnoreCase("empty_records")) {
                        progress.cancel();
                        Toast.makeText(mContext, getResources().getString(R.string.tripnotaveleble), Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray trips = succ.getJSONArray("trips");
                    String[] id, start_date, end_date, start_point, end_point, pdflink;
                    id = new String[trips.length()];
                    start_date = new String[trips.length()];
                    end_date = new String[trips.length()];
                    start_point = new String[trips.length()];
                    end_point = new String[trips.length()];
                    pdflink = new String[trips.length()];

                    for (int i = 0; i < trips.length(); i++) {
                        id[i] = trips.getJSONObject(i).getString("id");
                        start_date[i] = trips.getJSONObject(i).getString("start_date");
                        end_date[i] = trips.getJSONObject(i).getString("end_date");
                        start_point[i] = trips.getJSONObject(i).getString("start_point");
                        end_point[i] = trips.getJSONObject(i).getString("end_point");
                        pdflink[i] = trips.getJSONObject(i).getString("download_url");
                    }
                    RideHistryAdeptor adeptor = new RideHistryAdeptor(mContext, id, start_date, end_date, start_point, end_point, pdflink);
                    listview.setAdapter(adeptor);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Jo kljzdhckjhs", e.toString());
                }

                progress.cancel();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.cancel();
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