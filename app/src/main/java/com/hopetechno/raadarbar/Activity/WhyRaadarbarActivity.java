package com.hopetechno.raadarbar.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class WhyRaadarbarActivity extends AppCompatActivity implements AppConstant {

    WebView webView;
    Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WhyRaadarbarActivity.this, CustHomePageActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);


        MyApplication.setCurrentActivity(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        getwhyraadarbar();
    }

    String logtokan;
    void getwhyraadarbar() {

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        logtokan = prefs.getString("tokan", "");
        String role = prefs.getString("role", "");
        progstart(WhyRaadarbarActivity.this, "Loading...", "Loading...");

        Log.e("role", role);
        Log.e("URL", whyraadarbar);
        StringRequest postRequest = new StringRequest(Request.Method.GET, whyraadarbar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject main = new JSONObject(response);
                            JSONObject submain = main.getJSONObject("success");
                            JSONObject customer_data = submain.getJSONObject("data");
                            String velue = customer_data.getString("value");
                            Log.e("velue", velue);
//                            webView.loadData(velue, "text/html", "UTF-8");
                            webView.loadDataWithBaseURL(null, velue, "text/html", "utf-8", null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progstop();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        progstop();
                        Toast.makeText(WhyRaadarbarActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                Log.e("Authorization", "Bearer " + logtokan);
                return params;
            }
            /*@Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("msg", msg.getText().toString().trim());
                //params.put("password", pass.getText().toString().trim());

                return params;
            }*/
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);
    }
}
