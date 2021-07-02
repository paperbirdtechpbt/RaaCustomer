package com.hopetechno.raadarbar.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Other.VolleyMultipartRequest;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class UpdateAdvancebknSelectedPakegeShow extends AppCompatActivity implements AppConstant {
    String packegename, cartype, kilometer, hours, price, condition, googledata, userid, photo_proof,
            estimatedprice, car_rate_per_kl, car_rate_per_hr, distanceText, special_ratestring, date,
            end_address, start_address, end_lat, end_lng, start_lat, start_lng, fueltype, logtokan, incity, triptype, selectedcarid,package_id;
    CTextView pakegenametext, pricetext, kmtext, hrtext, basefare, jarny, conditionone;
    CButton booknow, backbutton;
    String end_date, end_time,start_date,start_time,id;
    SharedPreferences prefs,sharedPreferences;

    public String TAG ="UpdateAdvanceRide";

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateAdvancebknSelectedPakegeShow.this, EditAdvancedTripActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_selected_pakege_show);

        MyApplication.setCurrentActivity(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prefs = getSharedPreferences("UpdateData", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);


        id = prefs.getString("update_id", "");
        cartype = prefs.getString("update_car_type", cartype);
        fueltype = prefs.getString("update_fueltype", "");
        kilometer = prefs.getString("kilometer", kilometer);
        hours = prefs.getString("hours", hours);
        price = prefs.getString("price", price);
        condition = prefs.getString("condition", condition);
        estimatedprice = prefs.getString("estimatedprice", estimatedprice);
        car_rate_per_kl = prefs.getString("car_rate_per_kl", car_rate_per_kl);
        car_rate_per_hr = prefs.getString("car_rate_per_hr", car_rate_per_hr);
        distanceText = prefs.getString("update_distanceText", distanceText);
        incity = prefs.getString("update_incity", "");
        triptype = prefs.getString("update_roundtrip", "");
        end_address = prefs.getString("update_epoint", end_address);
        start_address = prefs.getString("update_spoint", start_address);
//        end_lat = prefs.getString("update_elat", end_lat);
//        end_lng = prefs.getString("update_elang", end_lng);
//        start_lat = prefs.getString("update_slat", start_lat);
//        start_lng = prefs.getString("update_slang", start_lng);
        end_date = prefs.getString("update_edate", "");
        end_time = prefs.getString("update_etime", "");
        start_date = prefs.getString("update_sdate", "");
        start_time = prefs.getString("update_stime", "");

        end_lat = prefs.getString("update_end_latitude", end_lat);
        end_lng = prefs.getString("update_end_longitude", end_lng);
        start_lat = prefs.getString("update_start_latitude", "110");
        start_lng = prefs.getString("update_start_longitude", "111");
//        start_lng = prefs.getString("package_id", start_lng);

        Log.e(TAG," start lat : "+prefs.getString("update_start_latitude", "110")+" start long"+prefs.getString("update_start_longitude", "111"));

//        editor.putString("update_start_longitude", start_longitude[position]);
//        editor.putString("update_start_latitude", start_latitude[position]);
//        editor.putString("update_end_longitude", end_longitude[position]);
//        editor.putString("update_end_latitude", end_latitude[position]);

//        special_ratestring = prefs.getString("special_ratestring", special_ratestring);

        userid = sharedPreferences.getString("userid", userid);
//        googledata = prefs.getString("jsonData", googledata);
//        photo_proof = sharedPreferences.getString("photo_proof", "");
       logtokan = sharedPreferences.getString("tokan", "");

        Log.e("fueltype", fueltype);

        // pakegenametext = findViewById(R.id.pakegenametext);
        pricetext = findViewById(R.id.pricetext);
//        kmtext = findViewById(R.id.kmtext);
//        hrtext = findViewById(R.id.hrtext);
//        basefare = findViewById(R.id.basefare);
        jarny = findViewById(R.id.jarny);
        booknow = findViewById(R.id.booknow);
        conditionone = findViewById(R.id.conditionone);
        backbutton = findViewById(R.id.backbutton);
        //pakegenametext = findViewById(R.id.pakegenametext);

        //pakegenametext.setText(packegename);
        pricetext.setText("₹  " + estimatedprice);
//        kmtext.setText(kilometer);
//        hrtext.setText(hours);
//        basefare.setText(price);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(AdvancebknSelectedPakegeShow.this, CustHomePage.class);
                startActivity(i);*/
                finish();
            }
        });
        /** if any extra hours added then it will charge Rs. 200 per Hour */

        try {
            if (incity.equalsIgnoreCase("0") && triptype.equalsIgnoreCase("0")) {
                jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + "</b> " + getResources().getString(R.string.jarnyone)));

            } else {
                Log.e("distanceText",
                        "incity    " + incity + "   triptype    " + distanceText);
                String[] dist = distanceText.split(" ");

                float newtotalkm = Float.valueOf(dist[0]);
                newtotalkm = newtotalkm * 2;
                Log.e("newtotalkm", dist[0]);
                Log.e("newtotalkm", String.valueOf(newtotalkm));
                //
                jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + " x 2 = " + String.valueOf(newtotalkm) + " km</b> " + getResources().getString(R.string.jarnyone)));
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        conditionone.setText(Html.fromHtml(condition));
        Log.e("condition", condition);

        booknow.setText("Update Ride");
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ConfirmationAdvanceBooking();
                UpdateRide();
            }
        });
    }

    private void UpdateRide() {
        final String logtokan;
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        Log.e("asdf", logtokan);
        progstart(UpdateAdvancebknSelectedPakegeShow.this, "Loading...", "Loading...");

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
                        Intent i = new Intent(UpdateAdvancebknSelectedPakegeShow.this, RideHistoryActivity.class);
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
                Toast.makeText(UpdateAdvancebknSelectedPakegeShow.this, "Please try again", Toast.LENGTH_LONG).show();
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
                params.put("end_point", end_address);
                params.put("start_point", start_address);
                params.put("start_time", start_time);
                params.put("end_date", end_date);
                params.put("end_time", end_time);

//                params.put("start_date_time", date);
//                params.put("user_id", userid);
//                params.put("end_point", end_address);
//                params.put("start_point", start_address);
                params.put("end_longitude", end_lng);
                params.put("end_latitude", end_lat);
                params.put("start_latitude", start_lat);
                params.put("start_longitude", start_lng);
//                params.put("car_type", cartype);
//                params.put("fueltype", fueltype);
//                params.put("incity", incity);
//                params.put("roundtrip", triptype);
//                params.put("end_date", end_date);
//                params.put("end_time", end_time);
//                params.put("package_id", package_id);

                Log.e(TAG," updateride : "+new Gson().toJson(params));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request); }

    Dialog dialog;
    ImageView select_docu;

    public void showPopUp() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.customdocument);

        LinearLayout docu_layout = dialog.findViewById(R.id.docu_layout);
        select_docu = dialog.findViewById(R.id.select_docu);
        docu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //displaying selected image to imageview
                select_docu.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);


                progstart(UpdateAdvancebknSelectedPakegeShow.this, "Uploading...", "Uploading your Image Please wait...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {


        //getting the tag from the edittext
        //  final String tags = editTextTags.getText().toString().trim();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadproof,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("response", String.valueOf(response));
                        try {
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("photo_proof", "yes");
                            editor.apply();
                            ;

                            progstop();
                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.e("JSONObject", obj.toString());
                            JSONObject sub_object = obj.getJSONObject("success");
                            if (sub_object.getString("status").equalsIgnoreCase("success")) {

                                dialog.cancel();
                                callapi();
                            } else {
                                Toast.makeText(UpdateAdvancebknSelectedPakegeShow.this, "Image not uploaded please try again", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Exception", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progstop();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("error", error.toString());
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("tags", tags);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("photo_proof", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));

                return params;
            }
        };


        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    void postRequest(String postBody) throws IOException {

        Intent i = new Intent(UpdateAdvancebknSelectedPakegeShow.this, NewCustomerWaitingActivity.class);
        startActivity(i);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("Authorization", "Bearer " + logtokan);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .addHeader("Authorization", "Bearer " + logtokan)
                .url(confirmride)
                .post(body)
                .build();
        Log.e("postUrl1", logtokan);
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

                Log.e("UpdateAdvanceBo", " UpdateAdvancebknSelectedPakegeShow -->>> onResponse: " + response);

                Log.e("response", "onResponse: " + response);

                try {
                    Log.e("response response ", String.valueOf(response));
                    JSONObject mainobject = new JSONObject(String.valueOf(response));
                    JSONObject succ = mainobject.getJSONObject("success");
                    String status = succ.getString("status");
                    Log.e("status one one ", status);
                    if (status.equalsIgnoreCase("empty_records")) {

                        Toast.makeText(UpdateAdvancebknSelectedPakegeShow.this, getResources().getString(R.string.tripnotaveleble), Toast.LENGTH_LONG).show();

                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Jo kljzdhckjhs", e.toString());
                }

            }
        });
    }

    void callapi() {
        try {
            JSONObject loginParams = new JSONObject();
            loginParams.put("user_id", userid);
            loginParams.put("end_point", end_address);
            loginParams.put("start_point", start_address);
            loginParams.put("end_lat", end_lat);
            loginParams.put("end_lng", end_lng);
            loginParams.put("start_lat", start_lat);
            loginParams.put("start_lng", start_lng);
            loginParams.put("cartype", cartype);
            loginParams.put("fueltype", fueltype);
            loginParams.put("incity", incity);
            loginParams.put("triptype", triptype);
            loginParams.put("car_id", selectedcarid);
            loginParams.put("estimatedprice", estimatedprice);

            postRequest(String.valueOf(loginParams));
            Log.e("UpdateAdvancebook", "UpdateAdvancebookingSelectPackageShow --->param  --->> "+String.valueOf(loginParams));

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }
}
