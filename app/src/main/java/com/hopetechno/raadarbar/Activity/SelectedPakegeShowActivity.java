package com.hopetechno.raadarbar.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Dialog.InfoWindowDialog;
import com.hopetechno.raadarbar.Other.VolleyMultipartRequest;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class SelectedPakegeShowActivity extends AppCompatActivity implements AppConstant {
    String packegename, cartype = "sedan", kilometer, hours, price, condition, googledata, userid, photo_proof,
            estimatedprice, car_rate_per_kl, car_rate_per_hr, distanceText,durationText, special_ratestring, date,
            end_address, start_address, end_lat, end_lng, start_lat, start_lng, fueltype, logtokan, incity, triptype, selectedcarid;
    TextView pakegenametext, conditionone1, pricetext, jarny, conditionone, conditionone3, textwltamnt;
    Button booknow, backbutton;
    String wallet_amount, distancevalue, durationvalue;
//    CTextView basefare,hrtext,kmtext;

    RadioGroup radioGroupTripType;
    RadioGroup radioGroupFuelType;

    RadioButton radioButtonDisel, rbtnCng, rbtnrStrip, rbtnrRtrip;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SelectedPakegeShowActivity.this, CustHomePageActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    String TAG = "SelectedPackagesShow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_pakege_show);

        MyApplication.setCurrentActivity(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        packegename = prefs.getString("packegename", packegename);
        cartype = prefs.getString("cartype", cartype);
        fueltype = prefs.getString("fueltype", "");
        kilometer = prefs.getString("kilometer", kilometer);
        hours = prefs.getString("hours", hours);
        price = prefs.getString("price", price);
        condition = prefs.getString("condition", condition);
        estimatedprice = prefs.getString("estimatedprice", estimatedprice);
        car_rate_per_kl = prefs.getString("car_rate_per_kl", car_rate_per_kl);
        car_rate_per_hr = prefs.getString("car_rate_per_hr", car_rate_per_hr);
        distanceText = prefs.getString("distanceText", distanceText);
        durationText = prefs.getString("durationText", distanceText);
        special_ratestring = prefs.getString("special_ratestring", special_ratestring);
        date = prefs.getString("date", "");
        userid = prefs.getString("userid", userid);
        end_address = prefs.getString("end_address", end_address);
        start_address = prefs.getString("start_address", start_address);
        end_lat = prefs.getString("end_lat", end_lat);
        end_lng = prefs.getString("end_lng", end_lng);
        googledata = prefs.getString("jsonData", googledata);
        photo_proof = prefs.getString("photo_proof", "");
        start_lat = prefs.getString("start_lat", start_lat);
        start_lng = prefs.getString("start_lng", start_lng);
        logtokan = prefs.getString("tokan", "");
        incity = prefs.getString("incity", "");
        triptype = prefs.getString("triptype", "");
        selectedcarid = prefs.getString("selectedcarid", "");
        wallet_amount = prefs.getString("wallet_amount", "");
        distancevalue = prefs.getString("distancevalue", "");
        durationvalue = prefs.getString("durationvalue", "");

        Log.e(TAG, "fueltype" + fueltype);
        Log.i(TAG, " Car Type  : " + cartype);
        Log.i(TAG, " Car Type  : " + incity);
        Log.i(TAG, " distanceText  : " + distanceText);
        Log.i(TAG, " durationText  : " + durationText);
        conditionone1 = findViewById(R.id.conditionone1);

        radioGroupTripType = findViewById(R.id.selectjourny);
        radioGroupFuelType = findViewById(R.id.radioGroupFuelType);
        radioButtonDisel = findViewById(R.id.rbtnDiesel);
        rbtnCng = findViewById(R.id.rbtnCng);
        rbtnrStrip = findViewById(R.id.rsingletrip);
        rbtnrRtrip = findViewById(R.id.rreturntrip);

        if (incity.equals("1")) {
            rbtnCng.setChecked(false);
            radioButtonDisel.setChecked(true);
        }

        if (cartype.equals(SUV)) {
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
                getRideNowPackages();
            }
        });

        rbtnCng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fueltype = "cng";
                radioButtonDisel.setChecked(false);
                rbtnCng.setChecked(true);
                Log.e("Radio", "  rbtnCng  ==>>> " + fueltype);
                getRideNowPackages();
            }
        });

        rbtnrStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triptype = "0";

                Log.e("Radio", "  rbtnrsingletrip  ==>>> ");
                rbtnrStrip.setChecked(true);
                rbtnrRtrip.setChecked(false);
                getRideNowPackages();
            }
        });

        rbtnrRtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Radio", "  rbtnrReturntrip  ==>>> ");
                triptype = "1";
                rbtnrStrip.setChecked(false);
                rbtnrRtrip.setChecked(true);


                getRideNowPackages();
            }
        });

        Log.i(TAG, " Car Type  : " + cartype);


// This will get the radiobutton in the radiogroup that is checked
//        RadioButton checkedRadioButton = (RadioButton) radioGroupTripType.findViewById(radioGroupTripType.getCheckedRadioButtonId());

//        radioGroupTripType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
//
//                if (checkedRadioButton.getText().equals("Single trip")) {
//                    triptype = "0";
//                } else {
//                    triptype = "1";
//                }
//                getRideNowPackages();
//            }
//        });
//        radioGroupFuelType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
//
//                if (checkedRadioButton.getText().equals("CNG")) {
//                    fueltype = "cng";
//                    radioButtonDisel.setChecked(false);
//                    rbtnCng.setChecked(true);
//                } else {
//                    fueltype = "diesel";
//                    radioButtonDisel.setChecked(true);
//                    rbtnCng.setChecked(false);
//                }
//                getRideNowPackages();
//            }
//        });


        // pakegenametext = findViewById(R.id.pakegenametext);
        pricetext = findViewById(R.id.pricetext);
//        kmtext = findViewById(R.id.kmtext);
//        hrtext = findViewById(R.id.hrtext);
//        basefare = findViewById(R.id.basefare);
        jarny = findViewById(R.id.jarny);
        booknow = findViewById(R.id.booknow);
        conditionone = findViewById(R.id.conditionone);
//        conditionone3 = findViewById(R.id.conditionone3);
        backbutton = findViewById(R.id.backbutton);
        textwltamnt = findViewById(R.id.textwltamnt);
        textwltamnt.setVisibility(View.GONE);


//        if (estimatedprice != null && !estimatedprice.isEmpty()) {
//            int price2 = Integer.parseInt(estimatedprice) + 20;
//            if (start_address.toLowerCase().contains(KALUPUR) || end_address.toLowerCase().contains(KALUPUR)) {
//                if (Double.valueOf(estimatedprice) < 130)
//                    pricetext.setText(ESTIMATED +RUPEE + PRICE);
//                else
//                    pricetext.setText(ESTIMATED +PRICE + estimatedprice +DASH + price2);
//            } else if (start_address.toLowerCase().contains(AIRPORT))
//                pricetext.setText(ESTIMATED+ RUPEE + estimatedprice + DASH + price2 + PARKING_CHARGE);
//            else
//                pricetext.setText(ESTIMATED + RUPEE + estimatedprice + DASH + price2);
//        }

        if ( estimatedprice != null && !estimatedprice.isEmpty()) {
            int price2 = Integer.parseInt(estimatedprice) + 10;
            if (start_address!= null && start_address.toLowerCase().contains(AIRPORT)) {
                pricetext.setText(ESTIMATED + RUPEE  + price2 + PARKING_CHARGE);
            } else if (start_address!= null && start_address.toLowerCase().contains(KALUPUR) || end_address.toLowerCase().contains(KALUPUR)) {
                if (Double.valueOf(estimatedprice) < 197)
                    pricetext.setText(ESTIMATED + RUPEE + PRICE);
                else
                    pricetext.setText(ESTIMATED + RUPEE + price2);
            } else
                pricetext.setText(ESTIMATED + RUPEE         + price2);
        }


        if (wallet_amount != null && wallet_amount.trim().length() > 0) {

            if (wallet_amount.equalsIgnoreCase("0")) {
                textwltamnt.setVisibility(View.GONE);
            } else {
                textwltamnt.setVisibility(View.VISIBLE);
                textwltamnt.setText(YOUR_WALLET + wallet_amount);
            }
        } else {
            textwltamnt.setVisibility(View.GONE);
        }


//        kmtext.setText(kilometer);
//        hrtext.setText(hours);
//        basefare.setText(price);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectedPakegeShowActivity.this, CustHomePageActivity.class);
                startActivity(i);
                finish();
            }
        });
        /** if any extra hours added then it will charge Rs. 200 per Hour */

        try {
            if (incity.equalsIgnoreCase("0") && triptype.equalsIgnoreCase("0")) {
                jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + "</b> "));

            } else {
                Log.e("distanceText",
                        "incity    " + incity + "   triptype    " + distanceText);
                String[] dist = distanceText.split(" ");

                float newtotalkm = Float.valueOf(dist[0]);
                newtotalkm = newtotalkm * 2;
                Log.e("newtotalkm", dist[0]);
                Log.e("newtotalkm", String.valueOf(newtotalkm));
                //
                jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + " x 2 = " + String.valueOf(newtotalkm) + " km</b> "));
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }


        if (condition != null) {
            final String[] x = condition.split("\r\n*");
//            String y= condition.substring(211,condition.length()-1);


            conditionone.setText(Html.fromHtml(x[0] + x[1] + x[2]));


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

                    InfoWindowDialog cdd = new InfoWindowDialog(SelectedPakegeShowActivity.this, finalTearms);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    cdd.show();
                }
            });
        }

//            conditionone3.setText(Html.fromHtml(y));
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //estimatedprice="99999";

                if (date.equalsIgnoreCase("")) {
                    int es = Integer.parseInt(estimatedprice);
                    Log.e("es", String.valueOf(es));
//                    if (es < 10000) {
                    callapi();

                    Log.e("es   in  if", String.valueOf(es));
//                    } else {
//
//                        Log.e("es   in  else", String.valueOf(es));
//                        //  photo_proof = "";
//                        if (photo_proof != null && !photo_proof.isEmpty() && !photo_proof.equals("null")) {
//                            Log.e("es   in  else  if", String.valueOf(es) + "   " + photo_proof);
//                            callapi();
//                        } else {
//                            Log.e("es   in  else  else", String.valueOf(es));
//                            showPopUp();
//                        }
//
//                    }

                } else {
                    Toast.makeText(SelectedPakegeShowActivity.this, "Your advance booking is Successful", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

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


                progstart(SelectedPakegeShowActivity.this, "Uploading...", "Uploading your Image Please wait...");
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
                                Toast.makeText(SelectedPakegeShowActivity.this, "Image not uploaded please try again", Toast.LENGTH_LONG).show();
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

        Intent i = new Intent(SelectedPakegeShowActivity.this, NewCustomerWaitingActivity.class);
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

//                Log.e("TAG", response.body().string());

                Log.e("SelectedPackageShow", " SelectedPackageShow -->>> onResponse: " + response);

                try {
                    Log.e("response response ", String.valueOf(response));
                    JSONObject mainobject = new JSONObject(String.valueOf(response));
                    JSONObject succ = mainobject.getJSONObject("success");
                    String status = succ.getString("status");
                    Log.e("status one one ", status);
                    if (status.equalsIgnoreCase("empty_records")) {

                        Toast.makeText(SelectedPakegeShowActivity.this, getResources().getString(R.string.tripnotaveleble), Toast.LENGTH_LONG).show();

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
            loginParams.put("totalhr_value", durationvalue);
            loginParams.put("totalkm_value", distancevalue);


            if (estimatedprice != null && !estimatedprice.isEmpty()) {
                int price2 = Integer.parseInt(estimatedprice) + 20;
                if (start_address.toLowerCase().contains(AIRPORT)) {
                    loginParams.put("estimatedprice", String.valueOf(Integer.parseInt(estimatedprice)+90));
                } else if (start_address.toLowerCase().contains(KALUPUR) || end_address.toLowerCase().contains(KALUPUR)) {
                    if (Double.valueOf(estimatedprice) < 197)
                        loginParams.put("estimatedprice", "197");
                    else
                        loginParams.put("estimatedprice", estimatedprice);
                } else
                    loginParams.put("estimatedprice", estimatedprice);
            }

            postRequest(String.valueOf(loginParams));
            Log.e("SelectedPakageShow", "   confirmride  --->param  --->> " + new Gson().toJson(loginParams));

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }

    void getRideNowPackages() {

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");

        Log.i("logtokan", logtokan);
        progstart(this, "Loading...", "Loading...");

        SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

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
                        editor.putString("incity", incity);
                        editor.putString("triptype", triptype);
                        editor.putString("wallet_amount", wallet_amount);
                        editor.remove("date");
                        editor.apply();

                        try {
                            if (incity.equalsIgnoreCase("0") && triptype.equalsIgnoreCase("0")) {
                                jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + "</b> "));

                            } else {
                                Log.e("distanceText",
                                        "incity    " + incity + "   triptype    " + distanceText);
                                String[] dist = distanceText.split(" ");

                                float newtotalkm = Float.valueOf(dist[0]);
                                newtotalkm = newtotalkm * 2;
                                Log.e("newtotalkm", dist[0]);
                                Log.e("newtotalkm", String.valueOf(newtotalkm));
                                //
                                jarny.setText(Html.fromHtml(getResources().getString(R.string.jarny) + "  <b>" + distanceText + " x 2 = " + String.valueOf(newtotalkm) + " km</b> "));
                            }
                        } catch (Exception e) {
                            Log.e("distanceText", "" + e.getMessage() + " Line : " + e.getStackTrace()[0].getLineNumber());
                        }


                        if (mainobject.has("estimatedprice") && !mainobject.getString("estimatedprice").isEmpty()) {
                            int price2 = Integer.parseInt(mainobject.getString("estimatedprice")) + 20;
                            pricetext.setText(RUPEE_1 + mainobject.getString("estimatedprice") + " - " + price2);
                        } else {
                            pricetext.setText(RUPEE_1 + mainobject.getString("estimatedprice"));
                        }

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
                Toast.makeText(SelectedPakegeShowActivity.this, "Please try again", Toast.LENGTH_LONG).show();
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


//                    if (cartype.equals("suv")) {
//                        fueltype = "diesel";
//                    } else {
//                        fueltype = "cng";
//                    }


                SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                params.put("start_lat", sharedpreferences.getString("Mstart_lat", ""));
                params.put("start_lng", sharedpreferences.getString("Mstart_lng", ""));
                params.put("end_lat", sharedpreferences.getString("Mend_lat", ""));
                params.put("end_lng", sharedpreferences.getString("Mend_lng", ""));
                params.put("totalkm", distancevalue);
                params.put("totalhr", durationvalue);
                params.put("cartype", cartype);
                params.put("userid", userid);
                params.put("incity", incity);
                params.put("fueltype", fueltype);
                params.put("triptype", triptype);
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
