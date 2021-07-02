package com.hopetechno.raadarbar.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hopetechno.raadarbar.Activity.EditAdvancedTripActivity;
import com.hopetechno.raadarbar.Activity.RideDetailActivity;
import com.hopetechno.raadarbar.Activity.RideHistoryActivity;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class RideHistryUpdateAdapter extends BaseAdapter implements AppConstant {
    String[] id, start_date, end_date, start_time, end_time, start_point, end_point, car_type, fueltype, incity, roundtrip, start_longitude, start_latitude, end_longitude, end_latitude;
    static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Context mContext;
    String filename_final;
    private static LayoutInflater inflater;

    public RideHistryUpdateAdapter(Context mContext, String[] start_longitude, String[] start_latitude, String[] end_longitude, String[] end_latitude, String[] id, String[] start_date, String[] end_date, String[] start_time, String[] end_time, String[] start_point, String[] end_point
            , String[] car_type, String[] fueltype, String[] incity, String[] roundtrip) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.start_point = start_point;
        this.end_point = end_point;
        this.car_type = car_type;
        this.fueltype = fueltype;
        this.incity = incity;
        this.roundtrip = roundtrip;
        this.mContext = mContext;
        this.start_longitude=start_longitude;
        this.start_latitude=start_latitude;
        this.end_longitude=end_longitude;
        this.end_latitude=end_latitude;

        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return id.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class Holder {
        CTextView datetime, from, carnumber, price, to;
        ImageView img;
        CButton clickfordetail, delete, edit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_ride_update, null);

        holder.datetime = (CTextView) rowView.findViewById(R.id.datetime);
        holder.from = (CTextView) rowView.findViewById(R.id.from);
        holder.carnumber = (CTextView) rowView.findViewById(R.id.carnumber);
        holder.price = (CTextView) rowView.findViewById(R.id.price);
        holder.to = (CTextView) rowView.findViewById(R.id.to);
        holder.clickfordetail = rowView.findViewById(R.id.clickfordetail);
        holder.delete = rowView.findViewById(R.id.delete);
        holder.edit = rowView.findViewById(R.id.editdetail);
        // holder.img=(ImageView) rowView.findViewById(R.id.carimg);

        holder.datetime.setText(start_date[position]);
        holder.from.setText(start_point[position]);
        holder.to.setText(end_point[position]);

        holder.price.setVisibility(View.GONE);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(mContext);


                alertDialog1.setTitle("Delete this Advance Trip");

                alertDialog1.setMessage("Are you sure you want to Delete this Advance Trip ?");


                alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        apicallingpois(id[position]);
                        dialog.cancel();
                    }
                });


                alertDialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog1.show();

            }
        });
        holder.carnumber.setVisibility(View.GONE);

        holder.clickfordetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("RideHistory : "," Detail Trip : Id : "+id[position]);

                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, RideDetailActivity.class);
                i.putExtra("id", id[position]);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (incity[position].equalsIgnoreCase("null")) {
//                    Toast.makeText(mContext, "Please Contact Email", Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Contact Admin");

                    String str2 = "Please Contact Admin. <br />  <br /> 9727379793<br /> 07940321076";
                    alertDialog.setMessage(Html.fromHtml(str2));
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();

                } else {
                    SharedPreferences sharedpreferences;
                    sharedpreferences = mContext.getSharedPreferences("UpdateData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("update_id", id[position]);
                    editor.putString("update_sdate", start_date[position]);
                    editor.putString("update_edate", end_date[position]);
                    editor.putString("update_spoint", start_point[position]);
                    editor.putString("update_epoint", end_point[position]);
                    editor.putString("update_stime", start_time[position]);
                    editor.putString("update_etime", end_time[position]);
                    editor.putString("update_car_type", car_type[position]);
                    editor.putString("update_fueltype", fueltype[position]);
                    editor.putString("update_incity", incity[position]);
                    editor.putString("update_roundtrip", roundtrip[position]);
                    editor.putString("update_start_longitude", start_longitude[position]);
                    editor.putString("update_start_latitude", start_latitude[position]);
                    editor.putString("update_end_longitude", end_longitude[position]);
                    editor.putString("update_end_latitude", end_latitude[position]);
                    editor.apply();

                    Intent i = new Intent(mContext, EditAdvancedTripActivity.class);
                    mContext.startActivity(i);
                }
            }
        });


        return rowView;
    }


    void apicallingpois(final String id) {

        progstart(mContext, "Loading...", "Loading...");

        final String logtokan;
        SharedPreferences prefs = mContext.getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");

        StringRequest postRequest = new StringRequest(Request.Method.POST, advancebookingdelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        Intent i = new Intent(mContext, RideHistoryActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(i);
                        progstop();
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

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("trip_id", id);
                Log.e("trip_id", id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);
    }

}
