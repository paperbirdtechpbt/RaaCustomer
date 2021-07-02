package com.hopetechno.raadarbar.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CEditText;
import com.hopetechno.raadarbar.Other.VolleyMultipartRequest;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class ProfileActivity extends AppCompatActivity implements AppConstant {
    ImageView profile_image;
    TextView email, phone;
    CButton submit;
    boolean isnewupload = false, isnewtext = false;
    CEditText driver_edit;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this, CustHomePageActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profile_image = findViewById(R.id.profile_image);
        submit = findViewById(R.id.submit);
        driver_edit = findViewById(R.id.driver_edit);
        MyApplication.setCurrentActivity(this);

        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String image_pref = sharedpreferences.getString("image", "");
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        driver_edit.setText(sharedpreferences.getString("first_name", ""));

        email.setText(sharedpreferences.getString("email", ""));
        phone.setText(sharedpreferences.getString("phone", ""));

        Log.e("image_pref", image_pref);
        UrlImageViewHelper.setUrlDrawable(profile_image, image + "users/thumbnail/" + image_pref, R.drawable.raalogo_white);


        driver_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isnewupload = true;
                return false;
            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isnewupload = true;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnewupload) {
                    uploadBitmap(bitmap);
                } else {
                    Toast.makeText(ProfileActivity.this, "You not made any change", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                //displaying selected image to imageview
                profile_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    Bitmap bitmap;


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        progstart(ProfileActivity.this, "Uploading...", "Uploading your Image Please wait...");

        //getting the tag from the edittext
        //  final String tags = editTextTags.getText().toString().trim();
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        final String logtokan = prefs.getString("tokan", "");
        //our custom volley request

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, profile,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("response", String.valueOf(response));
                        String parsed;
                        try {
                            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            parsed = new String(response.data);
                        }
                        Log.e("response  parsed ", parsed);
                        try {

                            JSONObject main = new JSONObject(parsed);
                            JSONObject submain = main.getJSONObject("success");
                            String status = submain.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(ProfileActivity.this, submain.getString("message"), Toast.LENGTH_LONG).show();

                                SharedPreferences sharedpreferences;
                                sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                if (submain.has("file_name")) {
                                    editor.putString("image", submain.getString("file_name"));
                                    editor.apply();
                                }
                                editor.putString("first_name", driver_edit.getText().toString().trim());
                                editor.apply();
                            } else {
                                Toast.makeText(ProfileActivity.this, submain.getString("message"), Toast.LENGTH_LONG).show();
                            }

                            progstop();


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


            /** If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             *
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + logtokan);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", driver_edit.getText().toString().trim());
                return params;
            }

            /* * Here we are passing image by renaming it with a unique name
             **/
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (bitmap != null && !bitmap.equals("null")) {
                    long imagename = System.currentTimeMillis();
                    params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                }

                return params;
            }
        };


        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}