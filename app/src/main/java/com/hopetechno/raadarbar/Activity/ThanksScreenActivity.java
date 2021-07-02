package com.hopetechno.raadarbar.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CEditText;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class ThanksScreenActivity extends AppCompatActivity implements AppConstant {

    RatingBar ratingBar;
    CEditText message;
    TextView head;
    CButton submit, skip;
    String logtokan;
    String customer_id;
    String driver_id = "";

    @Override
    public void onBackPressed() {
        Toast.makeText(ThanksScreenActivity.this, getResources().getString(R.string.thanksstring), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanksscreen);
        MyApplication.setCurrentActivity(this);

        head = findViewById(R.id.head);
        ratingBar = findViewById(R.id.ratingBar);
        submit = findViewById(R.id.submit);
        message = findViewById(R.id.message);
        skip = findViewById(R.id.skip);


        head.setText("Give Driver Rating");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = String.valueOf(ratingBar.getRating());
                String mess = message.getText().toString().trim();
                //Toast.makeText(mContext, rating + "   " + mess, Toast.LENGTH_LONG).show();
                callAPI(rating, mess);

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ThanksScreenActivity.this, CustHomePageActivity.class);
                startActivity(i);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

    }

    void callAPI(final String rating, final String mess) {

        progstart(ThanksScreenActivity.this, "Loading...", "Loading...");

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        customer_id = prefs.getString("userid", "");
        driver_id = prefs.getString("Driver_id", "");

        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.rating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("thanksResponse ", "start Ride onr onr onr " + response);

                    JSONObject main = new JSONObject(response);
                    JSONObject success = main.getJSONObject("success");
                    String stetus = success.getString("status");
                    if(stetus.equalsIgnoreCase("success")){

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                                Intent i = new Intent(ThanksScreenActivity.this, CustHomePageActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                progstop();
//                            }
//                        }, 5000);

                    }else{
                        Toast.makeText(ThanksScreenActivity.this, "SomethingWrong !", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ThanksScreenActivity.this, "Something Wrong !", Toast.LENGTH_SHORT).show();
                    Log.e("Jo kljzdhckjhs", e.toString());
                    progstop();
                }
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
                Log.e("Authorization", "Bearer " + logtokan);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rate", rating);
                params.put("comment", mess);
                params.put("user_id", driver_id);
                params.put("from_user_id", customer_id);

                Log.e("Customer", "Customer");
                Log.e("rate", rating);
                Log.e("comment", mess);
                Log.e("user_id", driver_id);
                Log.e("from_user_id", customer_id);

                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(ThanksScreenActivity.this);
        queue.add(request);
    }
}
