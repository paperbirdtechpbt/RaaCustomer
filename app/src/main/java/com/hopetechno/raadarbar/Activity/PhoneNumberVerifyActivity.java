package com.hopetechno.raadarbar.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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


public class PhoneNumberVerifyActivity extends AppCompatActivity implements AppConstant {

    CEditText ed_mob;
    CButton otp_continue;
    ImageView backbutton;
    String fname, mname, lname, email, password, mobileno;
    SharedPreferences sharedpreferences;
    private String TAG = "mypermis";
    private final int MULTIPERMISSION = 20;

    String mob;

    public static final int MULTIPLE_PERMISSIONS = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    public static final int WRITE_PERMISSION_REQUEST_CODE = 6;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verify);

        MyApplication.setCurrentActivity(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        backbutton = toolbar.findViewById(R.id.backbutton1);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed_mob = (CEditText) findViewById(R.id.ed_mobile_number);
        otp_continue = findViewById(R.id.otp_continue);
        otp_continue.setEnabled(false);

//        checkpermission();

        otp_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mob = ed_mob.getText().toString().trim();

                if (mob.length() == 10) {

                    sharedpreferences = getSharedPreferences("REGISTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("mobile", mob);
                    editor.commit();

                    progstart(PhoneNumberVerifyActivity.this, "Loading...", "Loading...");
                    call_api(mob);
//                    otp_continue.setEnabled(false);
//                    Intent i = new Intent(PhoneNumberVerify.this, OtpVerificationActivity.class);
//                    i.putExtra("mobile", mob);
//                    startActivity(i);

                } else {
                    Toast.makeText(PhoneNumberVerifyActivity.this, "Please check the number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



//    private void checkpermission() {
//        int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.RECEIVE_SMS);
//        int permissionCheckwrite = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_SMS);
//        int permissionphonestate = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.SEND_SMS);
//
//
//        // we already asked for permisson & Permission granted, call camera intent
//        if (permissionCheckCamera == PackageManager.PERMISSION_GRANTED && permissionCheckwrite == PackageManager.PERMISSION_GRANTED && permissionphonestate == PackageManager.PERMISSION_GRANTED) {
//        } //asking permission for the first tim e
//        else if (permissionCheckCamera != PackageManager.PERMISSION_GRANTED && permissionCheckwrite != PackageManager.PERMISSION_GRANTED && permissionphonestate != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS},
//                    MULTIPLE_PERMISSIONS);
//
//        } else {
//            // Permission denied, so request permission
//
//            // if camera request is denied
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.RECEIVE_SMS)) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("These permissions are mandatory for the application. Please allow access");
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//
//                        // Show permission request popup
//                        ActivityCompat.requestPermissions(PhoneNumberVerifyActivity.this,
//                                new String[]{Manifest.permission.RECEIVE_SMS},
//                                CAMERA_PERMISSION_REQUEST_CODE);
//                    }
//                });
//                builder.show();
//
//            }/// / if storage request is denied
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_SMS)) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("These permissions are mandatory for the application. Please allow access");
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//
//                        // Show permission request popup
//                        ActivityCompat.requestPermissions(PhoneNumberVerifyActivity.this,
//                                new String[]{Manifest.permission.READ_SMS},
//                                WRITE_PERMISSION_REQUEST_CODE);
//                    }
//                });
//                builder.show();
//
//            }
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.SEND_SMS)) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("These permissions are mandatory for the application. Please allow access");
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//
//                        // Show permission request popup
//                        ActivityCompat.requestPermissions(PhoneNumberVerifyActivity.this,
//                                new String[]{Manifest.permission.SEND_SMS},
//                                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//                    }
//                });
//                builder.show();
//
//            }
//        }
//    }

    private void call_api(final String mob) {

        Log.d("asdf", "response Mobile api Response --->> " + enterphone);

        StringRequest postRequest = new StringRequest(Request.Method.POST, enterphone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asdf", "response Mobile api Response --->> " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                otp_continue.setEnabled(false);
                                Intent i = new Intent(PhoneNumberVerifyActivity.this, OtpVerificationActivity.class);
                                i.putExtra("mobile", mob);
                                startActivity(i);
                            } else {
                                Toast.makeText(PhoneNumberVerifyActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            progstop();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PhoneNumberVerifyActivity.this,"Something Wrong !", Toast.LENGTH_SHORT).show();
                            progstop();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PhoneNumberVerifyActivity.this, "Something Wrong !", Toast.LENGTH_SHORT).show();
                        progstop();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", mob);
                params.put("app", "3");
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.RECEIVE_SMS)) {
                    // check whether camera permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    }
                }
                break;
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_SMS)) {
                    // check whether camera permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    }
                }
                break;
            case WRITE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.SEND_SMS)) {
                    // check whether camera permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    }
                }
                break;

            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && permissions[2].equals(Manifest.permission.RECORD_AUDIO)) {
                    // check whether All permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    }
                }
                break;
            default:
                break;
        }
    }

    private void callnextActivity() {


        if (mob.length() == 10) {

            sharedpreferences = getSharedPreferences("REGISTER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("mobile", mob);
            editor.commit();


            otp_continue.setEnabled(false);
            Intent i = new Intent(PhoneNumberVerifyActivity.this, OtpVerificationActivity.class);
            i.putExtra("mobile", mob);
            startActivity(i);
        } else {
            Toast.makeText(PhoneNumberVerifyActivity.this, "Please check the number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        otp_continue.setEnabled(true);
    }


}
