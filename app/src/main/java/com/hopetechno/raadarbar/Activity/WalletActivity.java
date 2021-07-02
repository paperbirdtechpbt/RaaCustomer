package com.hopetechno.raadarbar.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class WalletActivity extends AppCompatActivity implements AppConstant {
    CTextView textrupee;
    private String logtokan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        MyApplication.setCurrentActivity(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView() {
        textrupee =(CTextView) findViewById(R.id.textrupee);


        CallApi();
    }

    private void CallApi() {
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");

        StringRequest request = new StringRequest(Request.Method.POST, getwalletdetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("custhome", "Custhome res : " + new Gson().toJson(response.toString()));

                try {
                    JSONObject jobject = new JSONObject(response);
                    String total_amount = jobject.getString("total_amount");
                    textrupee.setText(Html.fromHtml("" + total_amount + " " + getString(R.string.rs)));
                } catch (Exception e) {
                    Log.e("Exception  ", "Ridenow class" + e.toString());
                }
                //Common.image+'cars/primary/thumbnail/'+carimage;

                progstop();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progstop();
                Toast.makeText(WalletActivity.this, "Please try again", Toast.LENGTH_LONG).show();
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
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WalletActivity.this, CustHomePageActivity.class));
        finish();
    }
}
