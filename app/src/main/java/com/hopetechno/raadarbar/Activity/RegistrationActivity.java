package com.hopetechno.raadarbar.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CEditText;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.Common;
import com.hopetechno.raadarbar.Utils.MyApplication;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class RegistrationActivity extends AppCompatActivity implements AppConstant {
    CTextView relogin;
    CEditText fname, lname, emailedit, pass, cpass, monumber, mname;
    String fireregId = "";
    CButton singubutton;
    SharedPreferences sharedpreferences;
    String MobileNumber,refer_id ="0";


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        MyApplication.setCurrentActivity(this);

        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        fireregId = prefs.getString("regId", "");
        MobileNumber = getIntent().getStringExtra("mobile");

        if (!TextUtils.isEmpty(fireregId))
            Log.e("Firebase Reg Id: ", fireregId);


        relogin = (CTextView) findViewById(R.id.relogin);
        lname = (CEditText) findViewById(R.id.lname);
        emailedit = (CEditText) findViewById(R.id.emailedit);
        pass = (CEditText) findViewById(R.id.pass);
        cpass = (CEditText) findViewById(R.id.cpass);
        monumber = (CEditText) findViewById(R.id.monumber);
        fname = (CEditText) findViewById(R.id.fname);
        mname = (CEditText) findViewById(R.id.mname);
        singubutton = (CButton) findViewById(R.id.singubutton);

        singubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailedit.getText().toString().trim().matches(emailPattern)) {
                    if (fname.getText().toString().trim().equalsIgnoreCase("") ||
                            lname.getText().toString().trim().equalsIgnoreCase("") ||
                            emailedit.getText().toString().trim().equalsIgnoreCase("")
                            || pass.getText().toString().trim().equalsIgnoreCase("")/*||cpass.getText().toString().trim().equalsIgnoreCase("")
                    ||monumber.getText().toString().trim().equalsIgnoreCase("")*/) {

                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.allrequired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {
                        if (pass.getText().length() < 6) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Please enter atlist 6 digit password", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
//                            if(pass.getText().toString().equalsIgnoreCase(cpass.getText().toString())){
//                                if(fname.getText().toString().contains(" ")) {
                            try {
                                apicallingpois();
//                                insertdata();
                            } catch (Exception e) {
                                Log.e("Exception", e.toString());
                            }
//                                }else{
//                                    Toast toast = Toast.makeText(getApplicationContext(),"Please enter Full Name", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                }
//                            }else{
//                                Toast toast = Toast.makeText(getApplicationContext(),"Password and Confirm password is not match", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//
//                            }


                            sharedpreferences = getSharedPreferences("REGISTER", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("fname", fname.getText().toString().trim());
                            editor.putString("mname", mname.getText().toString().trim());
                            editor.putString("lname", lname.getText().toString().trim());
                            editor.putString("emailedit", emailedit.getText().toString().trim());
                            editor.putString("pass", pass.getText().toString().trim());
                            editor.putString("mobile", "0");
                            editor.commit();

//                            Intent i = new Intent(RegistrationActivity.this, PhoneNumberVerify.class);
                           /* i.putExtra("fname",fname.getText().toString().trim());
                            i.putExtra("mname",mname.getText().toString().trim());
                            i.putExtra("lname",lname.getText().toString().trim());
                            i.putExtra("emailedit",emailedit.getText().toString().trim());
                            i.putExtra("pass",pass.getText().toString().trim());*/
//                            startActivity(i);


                        }

                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, getString(R.string.emailvalid), Toast.LENGTH_LONG).show();
                }

            }
        });

        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, PhoneNumberVerifyActivity.class);
                startActivity(i);
            }
        });
    }

//    private void insertdata() {
//
//        StringRequest request = new StringRequest(Request.Method.POST, "fhefdh", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> map = new HashMap<String, String>();
//                map.put("email","jnsdsds");
//                map.put("password","jnsdsds");
//                return map;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
//        queue.add(request);
//    }

    void apicallingpois() {
        mname.setText(" ");
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        refer_id = prefs.getString("refer_id", "0");
        progstart(RegistrationActivity.this, "Loading...", "Loading...");

        StringRequest postRequest = new StringRequest(Request.Method.POST, register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            try {
                                JSONObject success = jsonObject.getJSONObject("error");
                                if (success.has("email")) {
                                    JSONArray Token = success.getJSONArray("email");
                                    Toast.makeText(RegistrationActivity.this, Token.get(0).toString(), Toast.LENGTH_LONG).show();
                                }
                                if (success.has("phone")) {
                                    JSONArray Token1 = success.getJSONArray("phone");
                                    Toast.makeText(RegistrationActivity.this, Token1.get(0).toString(), Toast.LENGTH_LONG).show();
                                }
                                progstop();
                                return;
                            } catch (Exception e) {
                                Log.e("reg1 Exception", e.toString());
                            }

                            JSONObject success = jsonObject.getJSONObject("success");
                            String Token = success.getString("token");
                            String role = success.getString("role");
                            JSONObject user = success.getJSONObject("user");
                            String first_name, last_name, email, email_verified_at, image, license, phone, address, pincode, city, state, birthdate, created_at, updated_at, id, photo_proof;

                            id = user.getString("id");
                            first_name = user.getString("first_name");
                            last_name = user.getString("last_name");
                            email = user.getString("email");
                            email_verified_at = user.getString("email_verified_at");
                            image = user.getString("image");
                            license = user.getString("license");
                            photo_proof = user.getString("photo_proof");
                            phone = user.getString("phone");
                            SharedPreferences sharedpreferences;
                            sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("tokan", Token);
                            editor.putString("userid", id);
                            editor.putString("role", role);
                            editor.putString("first_name", first_name);
                            editor.putString("last_name", last_name);
                            editor.putString("email", email);
                            editor.putString("email_verified_at", email_verified_at);
                            editor.putString("image", image);
                            editor.putString("license", license);
                            editor.putString("phone", phone);
                            editor.putBoolean("hasLoggedIn", true);
                            editor.putString("photo_proof", photo_proof);
                            editor.apply();

                            Toast.makeText(RegistrationActivity.this, getString(R.string.succreg), Toast.LENGTH_LONG).show();

                            Intent i = new Intent(RegistrationActivity.this, CustHomePageActivity.class);
                            startActivity(i);
                            finish();
                            progstop();
                        } catch (Exception e) {
                            Log.e("reg Exception", e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("reg Error.Response", error.getMessage());

                        progstop();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", fname.getText().toString().trim() + mname.getText().toString() + lname.getText().toString().trim());// " " + mname.getText().toString().trim() +
                params.put("last_name", mname.getText().toString() + lname.getText().toString().trim());//mname.getText().toString().trim() + " " +
                params.put("phone", MobileNumber);
                params.put("email", emailedit.getText().toString().trim());
                params.put("password", pass.getText().toString().trim());
                params.put("c_password", pass.getText().toString().trim());

                SharedPreferences prefsFireBase = getSharedPreferences("FireBaseToken", MODE_PRIVATE);
                if(fireregId!= null && !fireregId.isEmpty())
                    params.put("fireregId", fireregId);
                else
                    params.put("fireregId", prefsFireBase.getString("regId", REGISERTER));

                params.put("refer_id", refer_id);

                Log.e("first_name", fname.getText().toString().trim() + " " + mname.getText().toString() + " " + lname.getText().toString().trim());
                Log.e("last_name", mname.getText().toString() + " " + lname.getText().toString().trim());
                Log.e("phone", MobileNumber);
                Log.e("email", emailedit.getText().toString().trim());
                Log.e("password", pass.getText().toString().trim());
                Log.e("c_password", pass.getText().toString().trim());
                Log.e("fireregId", fireregId);
                Log.e("refer_id", refer_id);
                Log.e("reg_params", new Gson().toJson(params));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjReq);
        queue.add(postRequest);
    }
}
