package com.hopetechno.raadarbar.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hopetechno.raadarbar.Adapter.FaveAdapter;
import com.hopetechno.raadarbar.Adapter.GooglePlaceAdapter;
import com.hopetechno.raadarbar.Fonts.CEditText;
import com.hopetechno.raadarbar.Modal.GooglePlaces;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.DatabaseHelper;
import com.hopetechno.raadarbar.Utils.MyApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DestinationSearchActivity extends Activity {

    private static final String TAG = DestinationSearchActivity.class.getSimpleName();
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static String API_KEY;
    ListView listviewSearchPlaces, listviewFav;
    String[] favarray;
    ArrayList<String> listFavPlaces;
    ArrayList<GooglePlaces.PredictionsBean> listGooglePlaces;
    GooglePlaceAdapter googlePlaceAdapter;
    FaveAdapter faveAdapter;
    CEditText editTextSearchPlace;
    int edit = 0;

    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_search);

        MyApplication.setCurrentActivity(this);

        editTextSearchPlace = (CEditText) findViewById(R.id.edittext_search_place);
        listviewFav = (ListView) findViewById(R.id.listview_favourite);
        listviewSearchPlaces = (ListView) findViewById(R.id.listview_search_places);

        //init fav listview
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        String favplace = prefs.getString("favplace", "");
        favarray = favplace.split("<mrrhope>");
        listFavPlaces = new ArrayList<String>(Arrays.asList(favarray));
        if (!listFavPlaces.isEmpty()) {
            for (int i = 0; i < listFavPlaces.size(); i++) {
                if (listFavPlaces.get(i).isEmpty()) {
                    listFavPlaces.remove(i);
                }
            }
        }


        Intent i = getIntent();
        edit = i.getIntExtra("edit", 0);

        faveAdapter = new FaveAdapter(listFavPlaces, DestinationSearchActivity.this);
        listviewFav.setAdapter(faveAdapter);

        //init fav search listview
        listGooglePlaces = new ArrayList<>();
        googlePlaceAdapter = new GooglePlaceAdapter(listGooglePlaces, DestinationSearchActivity.this);
        listviewSearchPlaces.setAdapter(googlePlaceAdapter);
        API_KEY = getResources().getString(R.string.google_maps_key);

        editTextSearchPlace.setHint(getResources().getString(R.string.enter_dest));

        //set Type listner
        editTextSearchPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 2) {
                    listviewFav.setVisibility(View.GONE);
                    try {
                        db = new DatabaseHelper(DestinationSearchActivity.this);
                        GooglePlaces googlePlaces = db.getAllNotes(charSequence.toString());
                        if (googlePlaces.getPredictions() != null) {
                            listGooglePlaces.clear();
                            listGooglePlaces.addAll(googlePlaces.getPredictions());
                            googlePlaceAdapter.notifyDataSetChanged();
                        } else
                            callGooglePlaceApi(charSequence.toString());
                    }catch (Exception e){
                        callGooglePlaceApi(charSequence.toString());
                    }
                } else {
                    listviewFav.setVisibility(View.VISIBLE);
                    if (listGooglePlaces != null) {
                        if (!listGooglePlaces.isEmpty())
                            listGooglePlaces.clear();
                        googlePlaceAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //On favourite Place click
        listviewFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("favriteiconchangedest", "1");
                editor.apply();

                editTextSearchPlace.setText(favarray[position]);



                if (edit == 1) {
                    Intent i = new Intent(DestinationSearchActivity.this, EditAdvancedTripActivity.class);
                    i.putExtra("placeto", favarray[position]);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else {

                    SharedPreferences prefs1 = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = prefs.edit();
                    editor2.putString("end_lat", "");
                    editor2.putString("end_lng", "");
                    editor2.putString("Mend_lat", "");
                    editor2.putString("Mend_lng", "");
                    editor2.apply();

                    Intent i = new Intent(DestinationSearchActivity.this, CustHomePageActivity.class);
                    i.putExtra("placeto", favarray[position]);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        //On Search Place click
        listviewSearchPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("favriteiconchangedest", "");
                editor.apply();

                GooglePlaces.PredictionsBean selectedAddress = (GooglePlaces.PredictionsBean) adapterView.getItemAtPosition(position);
                //Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                if (edit == 1) {

                    try {

                        db = new DatabaseHelper(DestinationSearchActivity.this);
                        db.insertNote(selectedAddress.getStructured_formatting().getMain_text(), selectedAddress.getDescription());
                    }catch (Exception e){

                    }

                    Intent i = new Intent(DestinationSearchActivity.this, EditAdvancedTripActivity.class);
                    i.putExtra("placeto", selectedAddress.getDescription());
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else {
                    try {
                        db = new DatabaseHelper(DestinationSearchActivity.this);
                        db.insertNote(selectedAddress.getStructured_formatting().getMain_text(), selectedAddress.getDescription());
                    }catch (Exception e){

                    }
                    Intent i = new Intent(DestinationSearchActivity.this, CustHomePageActivity.class);
                    i.putExtra("placeto", selectedAddress.getDescription());
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {

        if (edit == 1) {
            Intent i = new Intent(DestinationSearchActivity.this, EditAdvancedTripActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(DestinationSearchActivity.this, CustHomePageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        super.onBackPressed();
    }

    private void callGooglePlaceApi(String input) {
        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
        try {
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, sb.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e(TAG,"Api CAlled Place -->> ");

                Gson gson = new Gson();
                GooglePlaces googlePlaces = gson.fromJson(response, GooglePlaces.class);
                listGooglePlaces.clear();
                listGooglePlaces.addAll(googlePlaces.getPredictions());
                googlePlaceAdapter.notifyDataSetChanged();

//                if(googlePlaces.getPredictions() != null && googlePlaces.getPredictions().size()>0){
//                    for(GooglePlaces.PredictionsBean predictionsBean  :googlePlaces.getPredictions()) {
//                        db = new DatabaseHelper(DestinationSearchActivity.this);
//                        db.insertNote(predictionsBean.getStructured_formatting().getMain_text(), predictionsBean.getDescription());
//                    }
//                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);

            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("start_otp", otp);
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(DestinationSearchActivity.this);
        queue.add(request);
    }
}